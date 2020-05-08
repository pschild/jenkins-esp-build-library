def call(Map pipelineParams) {
    stage('checkout git') {
        steps {
            git changelog: false, url: pipelineParams.repoUrl
        }
    }
}
