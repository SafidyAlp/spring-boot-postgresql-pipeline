pipeline {
    agent any
    
    tools {
        // Utilisez les noms EXACTS tels que configurés dans Jenkins
        // Allez dans Manage Jenkins > Global Tool Configuration pour voir les noms exacts
        jdk 'JDK11'  // Notez la majuscule - vérifiez le nom exact dans Jenkins
        maven 'M3'   // Notez la majuscule - vérifiez le nom exact dans Jenkins
    }
    
    environment {
        // Variables d'environnement corrigées
        DB_URL = 'jdbc:postgresql://localhost:5432/testdb'
        // Utilisation correcte de la fonction credentials()
        DB_USERNAME = credentials('postgres-credentials').username
        DB_PASSWORD = credentials('postgres-credentials').password
    }
    
    stages {
        stage('Checkout Code') {
            steps {
                git(
                    branch: 'main',
                    url: 'https://github.com/SafidyAlp/spring-boot-postgresql-pipeline.git',
                    credentialsId: 'git-credentials'
                )
                script {
                    echo "Code récupéré depuis GitHub avec succès"
                    bat 'dir' // Lister les fichiers pour vérification
                }
            }
        }
        
        stage('Build Application') {
            steps {
                echo 'Construction de l application Spring Boot...'
                bat 'mvn clean compile'
            }
        }
        
        stage('Run Tests') {
            steps {
                echo 'Exécution des tests...'
                // Configuration des variables d'environnement pour les tests
                bat """
                set SPRING_DATASOURCE_URL=%DB_URL%
                set SPRING_DATASOURCE_USERNAME=%DB_USERNAME%
                set SPRING_DATASOURCE_PASSWORD=%DB_PASSWORD%
                set SPRING_JPA_HIBERNATE_DDL_AUTO=create-drop
                mvn test
                """
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    archiveArtifacts artifacts: 'target/surefire-reports/**/*', allowEmptyArchive: true
                }
            }
        }
        
        stage('Package Application') {
            steps {
                echo 'Création du package JAR...'
                bat 'mvn package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts 'target/*.jar'
                    echo "Application packagée avec succès"
                }
            }
        }
    }
    
    post {
        always {
            echo "Pipeline ${currentBuild.fullDisplayName} terminé"
            cleanWs() // Nettoyage de l'espace de travail
        }
        success {
            echo '✅ Pipeline exécuté avec succès!'
        }
        failure {
            echo '❌ Échec du pipeline. Vérifiez les logs.'
        }
    }
}