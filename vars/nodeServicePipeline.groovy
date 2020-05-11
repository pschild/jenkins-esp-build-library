def call(Map pipelineParams) {

    pipeline {
        agent any
        
        triggers {
            pollSCM('H/5 * * * *')
        }
        
        environment {
            REPO_NAME=resolveRepoName(pipelineParams.repoUrl)
        }

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
                        docker build -t foo .
                        docker tag foo:latest localhost:5000/${REPO_NAME}
                        docker push localhost:5000/${REPO_NAME}
                        docker rmi foo:latest
                    '''
                }
            }
        }
    }
}

def resolveRepoName(String repoUrl) {
    urlParts = repoUrl.split('/') as List
    repoName = urlParts.last()
    repoNameWithExtension = repoName.replaceFirst(/\.git$/, "")
    return repoNameWithExtension
}
