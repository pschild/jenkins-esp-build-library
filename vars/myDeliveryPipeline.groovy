def call(Map pipelineParams) {
    pipeline {
        agent any

        /*parameters {
            choice(name: 'ESP', choices: loadESPDefinitions(), description: 'Choose Target ESP')
        }*/

        environment {
            //FIRMWARE_NAME="${sh(script:'echo ${pipelineParams.repoUrl} | grep -P "([^/]+$)" -o | sed "s/.git//g"', returnStdout: true).trim()}"
            FIRMWARE_NAME=sh(
                script: """
                    #!/bin/bash
                    echo "xx" + ${pipelineParams.repoUrl} + "xx"
                """,
                returnStdout: true
            ).trim()
            FIRMWARE_VERSION="v${BUILD_NUMBER}-${sh(script:'git rev-parse HEAD', returnStdout: true).trim().take(7)}"
            //CHIPID="${sh(script:'echo $ESP | cut -d"|" -f1', returnStdout: true).trim()}"
            //PIOENV="${sh(script:'echo $ESP | cut -d"|" -f2', returnStdout: true).trim()}"
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
            /*stage('Build Binary') {
                steps {
                    withCredentials([usernamePassword(credentialsId: '4ba76353-3bab-4d0d-9364-9f9e9909495f', passwordVariable: 'WIFI_PASS', usernameVariable: 'WIFI_SSID')]) {
                        sh '''
                            pio run -t clean -e ${PIOENV}
                            pio run -e ${PIOENV} -v
                        '''
                    }
                }
            }
            stage('Copy Binary') {
                steps {
                    sh '''
                        FILENAME=.pio/build/${PIOENV}/firmware.bin
                        TARGETNAME=/var/binfiles/${CHIPID}/${FIRMWARE_NAME}-${FIRMWARE_VERSION}.bin
                        mkdir -p /var/binfiles/${CHIPID}
                        mv ${FILENAME} ${TARGETNAME}
                    '''
                }
            }*/
        }
    }
}
