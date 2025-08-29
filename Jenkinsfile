pipeline {
    agent any
    
    tools {
        // Utilisez le nom exact de votre JDK 22 configuré dans Jenkins
        jdk 'JDK22'  // Remplacez par le nom exact configuré dans Jenkins
        maven 'M3'   // Remplacez par le nom exact de Maven dans Jenkins
    }
    
    environment {
        // Variables d'environnement pour PostgreSQL
        DB_URL = 'jdbc:postgresql://localhost:5432/MAMI'
        DB_USERNAME = credentials('postgres-credentials')?.username
        DB_PASSWORD = credentials('postgres-credentials')?.password
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
                    bat 'dir && java -version && mvn --version'
                }
            }
        }
        
        stage('Build Application') {
            steps {
                echo 'Construction de l application Spring Boot avec JDK 22...'
                bat 'mvn clean compile'
            }
        }
        
        stage('Run Tests') {
            steps {
                echo 'Exécution des tests avec PostgreSQL...'
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
                    echo "Application packagée avec succès avec JDK 22"
                }
            }
        }
    }
    
    post {
        always {
            echo "Pipeline ${currentBuild.fullDisplayName} terminé"
            cleanWs() 
        }
        success {
            echo '✅ Pipeline exécuté avec succès avec JDK 22!'
        }
        failure {
            echo '❌ Échec du pipeline. Vérifiez les logs.'
        }
    }
}