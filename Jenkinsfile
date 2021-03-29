def getGradleTasks(branch) {
    if (branch == 'master') {
        return 'clean final'
    } else {
        return 'clean devSnapshot'
    }
}

pipeline {
    agent {
        node {
            label 'amzlnx2'
        }
    }

    environment {
        GRAALVM_HOME = tool 'graalvm-20.1.0.r11'
        JAVA_HOME = "${env.GRAALVM_HOME}"
        PATH = "${env.GRAALVM_HOME}/bin:$PATH"
    }

    stages {
        stage('Install Native Image') {
            steps {
               sh 'gu install native-image'
            }
        }

        stage('Build Lambda Function') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'jenkins-github-userpass',
                                                  passwordVariable: 'GRGIT_PASS',
                                                  usernameVariable: 'GRGIT_USER')]) {
                    script {
                        def gradleTasks = getGradleTasks("${env.BRANCH_NAME}")
                        sh  "./gradlew ${gradleTasks}"
                    }
                }
            }
            post {
                always {
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }

        stage('deploy zip to S3') {
            steps {
                s3Upload(bucket: 'chewy-artifacts-lambda', path: 'csbb/htmlpreview/', workingDir: 'build/distributions/lambda', includePathPattern: '**/*.zip')
            }
        }
    }
}
