def call(Map pipelineParams) {

    pipeline {
        agent any
        stages {
            stage('checkout git') {
                steps {
                    git changelog: false, url: pipelineParams.repoUrl
                }
            }
        }
    }
}
