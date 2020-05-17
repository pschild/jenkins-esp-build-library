def call(Map pipelineParams) {
    
    //def espChoices = new EspChoiceBuilder(libraryResource("esp-config.json")).build()
    def newChoices = new EspChoiceBuilder(this).build()
    
    def opts = []
    opts.push(parameters(newChoices))
    properties(opts)
    
    pipeline {
        agent any

        /*parameters {
            choice(name: 'ESP', choices: espChoices, description: 'Choose Target ESP')
        }*/

        environment {
            FIRMWARE_NAME=resolveFirmwareName(pipelineParams.repoUrl)
            FIRMWARE_VERSION="v${BUILD_NUMBER}-${sh(script:'git rev-parse HEAD', returnStdout: true).trim().take(7)}"
            CHIPID="${sh(script:'echo $ESP | cut -d"|" -f1', returnStdout: true).trim()}"
            PIOENV="${sh(script:'echo $ESP | cut -d"|" -f2', returnStdout: true).trim()}"
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
                    print getUniqueEnvironments(env.getEnvironment().CHIPS_CHOSEN)
                }
            }
            stage('Build Binary') {
                steps {
                    withCredentials([usernamePassword(credentialsId: '4ba76353-3bab-4d0d-9364-9f9e9909495f', passwordVariable: 'WIFI_PASS', usernameVariable: 'WIFI_SSID')]) {
                        /*sh '''
                            pio run -t clean -e ${PIOENV}
                            pio run -e ${PIOENV}
                        '''*/
                        sh '''
                            pio run -t clean -e esp12e esp01_1m nodemcuv2
                        '''
                    }
                }
            }
            /*stage('Copy Binary') {
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

def resolveFirmwareName(String repoUrl) {
    urlParts = repoUrl.split('/') as List
    repoName = urlParts.last()
    repoNameWithExtension = repoName.replaceFirst(/\.git$/, "")
    return repoNameWithExtension
}

def getUniqueEnvironments(buildTargets) {
    return (buildTargets.split(",").collect {
        def parts = it.split("\\|")
        return parts[1]
    }).unique()
}
