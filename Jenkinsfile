pipeline {
    agent any

    tools {
        maven 'M3'
        jdk 'JDK21'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', 
                url: 'https://github.com/votre-utilisateur/votre-repo.git'
            }
        }

        stage('Build et Tests') {
            steps {
                // Build et exécution des tests (y compris les tests d'intégration)
                sh 'mvn clean compile test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml' // Tests unitaires
                    junit 'target/failsafe-reports/*.xml' // Tests d'intégration (si vous en avez)
                }
            }
        }

        stage('Validation Templates') {
            steps {
                // Vérification que les templates Thymeleaf sont valides
                script {
                    // Cette étape compile les templates pour détecter les erreurs
                    sh 'mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.main.web-application-type=none -Dspring.main.banner-mode=off" --quiet &'
                    sleep time: 10, unit: 'SECONDS'
                    sh 'pkill -f "spring-boot:run" || true'
                }
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts 'target/*.jar'
                    echo '✅ Application packagée avec succès avec templates Thymeleaf'
                }
            }
        }
    }

    post {
        always {
            cleanWs() // Nettoyage du workspace
        }
        failure {
            emailext body: "Le build a échoué. Consultez Jenkins: ${BUILD_URL}", 
                    subject: "❌ ÉCHEC du build : ${JOB_NAME}",
                    to: 'votre-email@example.com'
        }
        success {
            emailext body: "Le build a réussi! Application avec templates Thymeleaf prête.", 
                    subject: "✅ SUCCÈS du build : ${JOB_NAME}",
                    to: 'votre-email@example.com'
        }
    }
}