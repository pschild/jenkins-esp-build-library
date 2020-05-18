def call(Map pipelineParams) {
    def newChoices = new EspChoiceBuilder(this).build()
    
    def opts = []
    opts.push(parameters(newChoices))
    properties(opts)
    
    pipeline {
        agent any

        environment {
            FIRMWARE_NAME=resolveFirmwareName(pipelineParams.repoUrl)
            FIRMWARE_VERSION="v${BUILD_NUMBER}-${sh(script:'git rev-parse HEAD', returnStdout: true).trim().take(7)}"
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
                    print buildPioEnvCommand(env.getEnvironment().CHIPS_CHOSEN)
                }
            }
            stage('Build Binary') {
                steps {
                    withCredentials([usernamePassword(credentialsId: '4ba76353-3bab-4d0d-9364-9f9e9909495f', passwordVariable: 'WIFI_PASS', usernameVariable: 'WIFI_SSID')]) {
                        sh "pio run -t clean ${buildPioEnvCommand(env.getEnvironment().CHIPS_CHOSEN)}"
                        sh "pio run ${buildPioEnvCommand(env.getEnvironment().CHIPS_CHOSEN)}"
                    }
                }
            }
            stage('Copy Binary') {
                steps {
                    script {
                        def targets = env.getEnvironment().CHIPS_CHOSEN.split(",")
                        // use for instead of groovy's .each!
                        for (int i = 0; i < targets.size(); i++) {
                            def parts = targets[i].split("\\|")
                            def chipId = parts[0]
                            def env = parts[1]
                            
                            def sourceFile = ".pio/build/${env}/firmware.bin"
                            def targetFile = "/var/binfiles/${chipId}/${FIRMWARE_NAME}-${FIRMWARE_VERSION}.bin"
                            sh "mkdir -p /var/binfiles/${chipId}"
                            sh "mv ${sourceFile} ${targetFile}"
                        }
                    }
                    /*sh '''
                        FILENAME=.pio/build/${PIOENV}/firmware.bin
                        TARGETNAME=/var/binfiles/${CHIPID}/${FIRMWARE_NAME}-${FIRMWARE_VERSION}.bin
                        mkdir -p /var/binfiles/${CHIPID}
                        mv ${FILENAME} ${TARGETNAME}
                    '''*/
                }
            }
        }
    }
}

def resolveFirmwareName(String repoUrl) {
    urlParts = repoUrl.split('/') as List
    repoName = urlParts.last()
    repoNameWithExtension = repoName.replaceFirst(/\.git$/, "")
    return repoNameWithExtension
}

def buildPioEnvCommand(buildTargets) {
    def uniqueEnvs = (buildTargets.split(",").collect {
        def parts = it.split("\\|")
        return parts[1]
    }).unique()
    return (uniqueEnvs.collect { "-e " + it }).join(" ")
}
