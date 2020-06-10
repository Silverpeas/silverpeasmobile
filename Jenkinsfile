node {
  catchError {
    def pom
    docker.image('silverpeas/silverbuild:6.1')
        .inside('-v $HOME/.m2/settings.xml:/root/.m2/settings.xml -v $HOME/.m2/settings-security.xml:/root/.m2/settings-security.xml -v $HOME/.gitconfig:/root/.gitconfig -v $HOME/.ssh:/root/.ssh -v $HOME/.gnupg:/root/.gnupg') {
      stage('Checkout') {
        checkout scm
      }
      stage('Check Parent POM Version') {
        echo 'Check the version of the parent POM is up to date'
        pom = readMavenPom()
        copyArtifacts projectName: 'Silverpeas_Project_Definition_AutoDeploy', flatten: true
        def parentPom = readYaml file: 'build.yaml'
        if (parentPom.version != pom.parent.version) {
          error("The declared version of the parent POM isn't up to date with the last version that is ${parentPom.version}")
        }
      }
      stage('Check Silverpeas Version') {
        echo 'Check the version of the dependencies on Silverpeas is up to date'
        copyArtifacts projectName: 'Silverpeas_Master_AutoDeploy', flatten: true
        def lastSilverpeasBuild = readYaml file: 'build.yaml'
        def lastSilverpeasVersion = lastSilverpeasBuild.version
        if (pom.properties['silverpeas.version'] != lastSilverpeasVersion) {
          echo "WARNING: your current dependencies on Silverpeas aren't up to date with the last version ${lastSilverpeasVersion}"
          echo "=> update the dependencies version only for the current build."
          sh """
sed -i -e "s/<silverpeas.version>[0-9a-zA-Z.-]\\+/<silverpeas.version>${lastSilverpeasVersion}/g" pom.xml
"""
        }
      }
      stage('Build') {
        echo 'Build the Silverpeas Mobile project'
        sh "mvn clean install -Pdeployment -Djava.awt.headless=true -Dcontext=ci"
      }
    }
  }
  step([$class                  : 'Mailer',
        notifyEveryUnstableBuild: true,
        recipients              : "miguel.moquillon@silverpeas.org, yohann.chastagnier@silverpeas.org, sebastien.vuillet@silverpeas.org, nicolas.eysseric@silverpeas.org",
        sendToIndividuals       : true])
}