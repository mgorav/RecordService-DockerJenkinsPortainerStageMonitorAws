pipeline {
    // Java8
    agent { label "" }

    stages {
        stage('build') {
            steps {
                script {

                    git branch: 'master', credentialsId: '21b2ff64-0488-4867-828f-6b40133f863e', url: 'git@github.com:NDE-Europe/gaurav-exercise.git'
                }
            }

        }
        stage('Integration tests') {
            // Run the maven build
            steps {
                script {
                    def jdk8 = tool 'jdk 1.8.0_91'

                    env.JAVA_HOME = "${jdk8}"

                    echo "jdk installation path is: ${jdk8}"

                    sh "${jdk8}/bin/java -version"

                    sh '$JAVA_HOME/bin/java -version'

                    def mvnHome = tool 'maven 3.5.0'
                    if (isUnix()) {
                        sh "'${mvnHome}/bin/mvn'  clean install"
                    } else {
                        bat(/"${mvnHome}\bin\mvn" clean install/)
                    }

                }
            }
        }
    }

}