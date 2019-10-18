node {
  catchError {
    docker.image('silverpeas/silverbuild')
        .inside('-v $HOME/.m2/settings.xml:/root/.m2/settings.xml -v $HOME/.m2/settings-security.xml:/root/.m2/settings-security.xml -v $HOME/.gitconfig:/root/.gitconfig -v $HOME/.ssh:/root/.ssh -v $HOME/.gnupg:/root/.gnupg') {
      stage('Checkout') {
        checkout scm
      }
      stage('Check Parent POM Version') {
        echo 'Check the version of the parent POM is up to date'
        def pom = readMavenPom()
        copyArtifacts projectName: 'Silverpeas_Project_Definition_AutoDeploy', flatten: true
        def parentPom = readYaml file: 'build.yaml'
        if (parentPom.version != pom.parent.version) {
          error("The declared version of the parent POM isn't up to date with the last version that is ${parentPom.version}")
        }
      }
      stage('Set Silverpeas Version') {
        echo 'Set the version of the dependencies on Silverpeas to the last build version'
        copyArtifacts projectName: 'Silverpeas_Master_AutoDeploy', flatten: true
        def lastSilverpeasBuild = readYaml file: 'build.yaml'
        def lastSilverpeasVersion = lastSilverpeasBuild.version
        sh """
sed -i -e "s/<silverpeas.version>[0-9a-zA-Z.-]\\+/<silverpeas.version>${lastSilverpeasVersion}/g" pom.xml
"""
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