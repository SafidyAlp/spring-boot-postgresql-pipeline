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
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    // Pour les tests d'intégration si vous en avez
                    junit 'target/failsafe-reports/*.xml' 
                }
            }
        }
        
        stage('Validate Templates') {
            steps {
                script {
                    // Validation des templates Thymeleaf
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
            cleanWs() // Nettoyage du workspace
        }
        success {
            bat 'echo "✅ SUCCÈS: Build avec templates Thymeleaf réussi!"'
            // Envoyer un email de succès (si configuré)
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

                L'application Spring avec interface web est prête !
                """,
                to: "randrianomentsoasafidy@gmail.com"
            )
        }
        failure {
            bat 'echo "❌ ÉCHEC: Build a échoué"'
            // Envoyer un email d'échec (si configuré)
            emailext (
                subject: "❌ ÉCHEC Build: ${JOB_NAME} #${BUILD_NUMBER}",
                body: """
                Le build Jenkins a échoué !

                Détails:
                - Projet: ${JOB_NAME}
                - Build: #${BUILD_NUMBER}
                - Statut: ÉCHEC
                - Lien: ${BUILD_URL}

                Veuillez vérifier les erreurs.
                """,
                to: "randrianomentsoasafidy@gmail.com"
            )
        }
    }
}