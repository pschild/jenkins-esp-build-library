def call(Map pipelineParams) {

    pipeline {
        agent any

        stages {
            stage('Checkout') {
                steps {
                    git changelog: false, url: pipelineParams.repoUrl
                }
            }
            stage('Debug') {
                steps {
                    print pipelineParams
                    sh 'printenv'
                }
            }
            stage('Install') {
                steps {
                    sh 'npm install'
                }
            }
            stage('Test') {
                steps {
                    sh 'npm test'
                }
            }
            stage('Build and push docker image') {
                steps {
                    sh '''
                        docker x
                        docker y
                    '''
                }
            }
        }
    }
}
