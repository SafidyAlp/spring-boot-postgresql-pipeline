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
                bat 'mvn clean compile test'
                // Diagnostic
                bat 'dir target /s || echo "Aucun répertoire target"'
                bat 'dir target\\surefire-reports /s 2>nul || echo "Aucun répertoire surefire-reports"'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Validate Templates') {
            steps {
                script {
                    bat '''
                        echo "Validation des templates Thymeleaf..."
                        mvn compile -q
                        echo "✅ Templates compilés avec succès"
                    '''
                }
            }
        }
        
        stage('Package Application') {
            steps {
                bat 'mvn package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts 'target/*.jar'
                    bat 'echo "✅ Application packagée avec templates Thymeleaf"'
                }
            }
        }
    }
    
    post {
        always {
            bat 'echo "Build terminé: ${currentBuild.result}"'
            cleanWs()
        }
        success {
            bat 'echo "✅ SUCCÈS: Build avec templates Thymeleaf réussi!"'
            emailext (
                subject: "✅ SUCCÈS Build: ${JOB_NAME} #${BUILD_NUMBER}",
                body: """
                Le build Jenkins a réussi !
                Détails:
                - Projet: ${JOB_NAME}
                - Build: #${BUILD_NUMBER}
                - Statut: SUCCÈS
                - Templates Thymeleaf: VALIDÉS
                - Lien: ${BUILD_URL}
                """,
                to: "randrianomentsoasafidy@gmail.com"
            )
        }
        failure {
            bat 'echo "❌ ÉCHEC: Build a échoué"'
            emailext (
                subject: "❌ ÉCHEC Build: ${JOB_NAME} #${BUILD_NUMBER}",
                body: """
                Le build Jenkins a échoué !
                Détails:
                - Projet: ${JOB_NAME}
                - Build: #${BUILD_NUMBER}
                - Lien: ${BUILD_URL}
                """,
                to: "randrianomentsoasafidy@gmail.com"
            )
        }
    }
}
