node {
  catchError {
    def nexusRepo = 'https://www.silverpeas.org/nexus/content/repositories/snapshots/'
    docker.image('silverpeas/silverbuild')
        .inside('-u root -v $HOME/.m2/settings.xml:/root/.m2/settings.xml -v $HOME/.m2/settings-security.xml:/root/.m2/settings-security.xml -v $HOME/.gitconfig:/root/.gitconfig -v $HOME/.ssh:/root/.ssh -v $HOME/.gnupg:/root/.gnupg') {
      stage('Preparation') {
        checkout scm
      }
      stage('Build') {
        sh "mvn clean install -Pdeployment -Djava.awt.headless=true -Dcontext=ci"
      }
      stage('Deployment') {
        // deployment to ensure dependencies on this snapshot version of Silverpeas Core for other
        // projects to build downstream. By doing so, we keep clean the local maven repository for
        // reproducibility reason
        sh "mvn deploy -DaltDeploymentRepository=silverpeas::default::${nexusRepo} -Pdeployment -Djava.awt.headless=true -Dmaven.test.skip=true"
      }
    }
  }
  step([$class                  : 'Mailer',
        notifyEveryUnstableBuild: true,
        recipients              : "miguel.moquillon@silverpeas.org, yohann.chastagnier@silverpeas.org, nicolas.eysseric@silverpeas.org",
        sendToIndividuals       : true])
}