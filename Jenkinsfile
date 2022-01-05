import java.util.regex.Matcher

pipeline {
  environment {
    lockFilePath = null
    version = null
    silverpeas = 'Master'
  }
  agent {
    docker {
      image 'silverpeas/silverbuild'
      args '-v $HOME/.m2:/home/silverbuild/.m2 -v $HOME/.gitconfig:/home/silverbuild/.gitconfig -v $HOME/.ssh:/home/silverbuild/.ssh -v $HOME/.gnupg:/home/silverbuild/.gnupg'
    }
  }
  stages {
    stage('Waiting for core running build if any') {
      steps {
        script {
          version = computeSnapshotVersion()
          lockFilePath = createLockFile(version, 'mobile')
          waitForDependencyRunningBuildIfAny(version, 'core')
        }
      }
    }
    stage('Waiting for components running build if any') {
      steps {
        script {
          waitForDependencyRunningBuildIfAny(version, 'components')
        }
      }
    }
    stage('Build') {
      steps {
        script {
          checkParentPOMVersion(version)
          def silverpeasVersion = getSilverpeasLastBuildVersion()
          boolean coreDependencyExists = existsDependency(version, 'core')
          boolean componentDependencyExists = existsDependency(version, 'components')
          if (coreDependencyExists || componentDependencyExists) {
            silverpeasVersion = version
          }
          sh """
sed -i -e "s/<silverpeas.version>[\\\\\\${}0-9a-zA-Z.-]\\\\+/<silverpeas.version>${silverpeasVersion}/g" pom.xml
mvn -U versions:set -DgenerateBackupPoms=false -DnewVersion=${version}
mvn clean install -Pdeployment -Djava.awt.headless=true -Dcontext=ci
"""
          deleteLockFile(lockFilePath)
        }
      }
    }
  }
  post {
    always {
      deleteLockFile(lockFilePath)
      step([$class                  : 'Mailer',
            notifyEveryUnstableBuild: true,
            recipients              : "miguel.moquillon@silverpeas.org, yohann.chastagnier@silverpeas.org, sebastien.vuillet@silverpeas.org, nicolas.eysseric@silverpeas.org",
            sendToIndividuals       : true])
    }
  }
}

def computeSnapshotVersion() {
  def pom = readMavenPom()
  final String version = pom.version
  final String defaultVersion = env.BRANCH_NAME == 'master' ? version :
      env.BRANCH_NAME.toLowerCase().replaceAll('[# -]', '')
  Matcher m = env.CHANGE_TITLE =~ /^(Bug #?\d+|Feature #?\d+).*$/
  String snapshot = m.matches()
      ? m.group(1).toLowerCase().replaceAll(' #?', '')
      : ''
  if (snapshot.isEmpty()) {
    m = env.CHANGE_TITLE =~ /^\[([^\[\]]+)].*$/
    snapshot = m.matches()
        ? m.group(1).toLowerCase().replaceAll('[/><|:&?!;,*%$=}{#~\'"\\\\°)(\\[\\]]', '').trim().replaceAll('[ @]', '-')
        : ''
  }
  return snapshot.isEmpty() ? defaultVersion : "${pom.properties['next.release']}-${snapshot}"
}

def getSilverpeasLastBuildVersion() {
  copyArtifacts projectName: "Silverpeas_${silverpeas}_AutoDeploy", flatten: true
  def lastBuild = readYaml file: 'build.yaml'
  return lastBuild.version
}

def existsDependency(version, projectName) {
  def exitCode = sh script: "test -d \$HOME/.m2/repository/org/silverpeas/${projectName}/${version}", returnStatus: true
  return exitCode == 0
}

static def createLockFilePath(version, projectName) {
  final String lockFilePath = "\$HOME/.m2/${version}_${projectName}_build.lock"
  return lockFilePath
}

def createLockFile(version, projectName) {
  final String lockFilePath = createLockFilePath(version, projectName)
  sh "touch ${lockFilePath}"
  return lockFilePath
}

def deleteLockFile(lockFilePath) {
  if (isLockFileExisting(lockFilePath)) {
    sh "rm -f ${lockFilePath}"
  }
}

def isLockFileExisting(lockFilePath) {
  if (lockFilePath?.trim()?.length() > 0) {
    def exitCode = sh script: "test -e ${lockFilePath}", returnStatus: true
    return exitCode == 0
  }
  return false
}

def waitForDependencyRunningBuildIfAny(version, projectName) {
  final String dependencyLockFilePath = createLockFilePath(version, projectName)
  timeout(time: 3, unit: 'HOURS') {
    waitUntil {
      return !isLockFileExisting(dependencyLockFilePath)
    }
  }
  if (isLockFileExisting(dependencyLockFilePath)) {
    error "After timeout dependency lock file ${dependencyLockFilePath} is yet existing!!!!"
  }
}

def checkParentPOMVersion(version) {
  def pom = readMavenPom()
  int idx = pom.parent.version.indexOf('-SNAPSHOT')
  if (idx > 0) {
    String[] snapshot = version.split('-')
    String parentVersion = pom.parent.version.substring(0, idx) + '-' + snapshot[0]
    echo "Update parent POM to ${parentVersion}"
    sh """
mvn -U versions:update-parent -DgenerateBackupPoms=false -DparentVersion="[${parentVersion}]"
"""
  }
}