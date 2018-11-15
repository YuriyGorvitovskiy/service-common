void setBuildStatus(String message, String state) {
  step([
      $class: "GitHubCommitStatusSetter",
      reposSource: [$class: "ManuallyEnteredRepositorySource", url: "https://github.com/YuriyGorvitovskiy/service-common"],
      contextSource: [$class: "ManuallyEnteredCommitContextSource", context: "jenkins/build-status"],
      errorHandlers: [[$class: "ChangingBuildStatusErrorHandler", result: "UNSTABLE"]],
      statusResultSource: [ $class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]] ]
  ]);
}

pipeline {
    agent any
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('Build Server') {
            steps {
                setBuildStatus("Build in progress...", "PENDING")

                sh './gradlew clean build publishToMavenLocal'
            }
        }
    }
    post {
        success {
            setBuildStatus("Build succeeded!", "SUCCESS")
        }
        failure {
            setBuildStatus("Build failed!", "FAILURE")
        }
    }
}
