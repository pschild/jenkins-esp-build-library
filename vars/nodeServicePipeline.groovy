def call(Map pipelineParams) {

    pipeline {
        agent any

        // properties([pipelineTriggers([pollSCM('H/5 * * * *')])])

        stages {
            stage('Checkout') {
                steps {
                    git changelog: false, url: pipelineParams.repoUrl/*, poll: true*/
                }
            }
            stage('Debug') {
                steps {
                    print pipelineParams
                    sh 'printenv'
                }
            }
            /*stage('Install') {
                steps {
                    sh 'npm install'
                }
            }
            stage('Test') {
                steps {
                    sh 'npm test'
                }
            }*/
            /*stage('Build and push docker image') {
                steps {
                    sh '''
                        docker build -t foo .
                        docker tag foo:latest localhost:5000/esp-update-server
                        docker push localhost:5000/esp-update-server
                        docker rmi foo:latest
                    '''
                }
            }*/
        }
    }
}
