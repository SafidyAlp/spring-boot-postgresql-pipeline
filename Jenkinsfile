pipeline {
    agent any
    
    tools {
        jdk 'JDK22'
        maven 'M3'
    }
    
    stages {
        stage('Checkout') {
            steps {
                git(
                    branch: 'main',
                    url: 'https://github.com/SafidyAlp/spring-boot-postgresql-pipeline.git',
                    credentialsId: 'git-credentials'
                )
                bat 'echo "Checkout réussi" && dir'
            }
        }
        
        stage('Build and Test') {
            steps {
                // Méthode SANS variables d'environnement complexes
                bat 'mvn clean test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
    }
    
    post {
        always {
            echo "Build terminé: ${currentBuild.result}"
        }
    }
}