pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials')
        DOCKER_IMAGE_BACKEND  = "nagaramnagalla014/retail-backend"
        DOCKER_IMAGE_FRONTEND = "nagaramnagalla014/retail-frontend"
        IMAGE_TAG = "${BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/nagaramnagalla014-ctrl/Online-Retail-Management-Platform.git'
            }
        }

        stage('Build Backend') {
            steps {
                dir('backend') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Test Backend') {
            steps {
                dir('backend') {
                    sh 'mvn test'
                }
            }
            post {
                always {
                    junit 'backend/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    sh 'npm install'
                    sh 'npm run build:prod'
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh "docker build -f Dockerfile.backend -t ${DOCKER_IMAGE_BACKEND}:${IMAGE_TAG} ."
                sh "docker build -f Dockerfile.frontend -t ${DOCKER_IMAGE_FRONTEND}:${IMAGE_TAG} ."
                sh "docker tag ${DOCKER_IMAGE_BACKEND}:${IMAGE_TAG} ${DOCKER_IMAGE_BACKEND}:latest"
                sh "docker tag ${DOCKER_IMAGE_FRONTEND}:${IMAGE_TAG} ${DOCKER_IMAGE_FRONTEND}:latest"
            }
        }

        stage('Docker Push') {
            steps {
                sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
                sh "docker push ${DOCKER_IMAGE_BACKEND}:${IMAGE_TAG}"
                sh "docker push ${DOCKER_IMAGE_BACKEND}:latest"
                sh "docker push ${DOCKER_IMAGE_FRONTEND}:${IMAGE_TAG}"
                sh "docker push ${DOCKER_IMAGE_FRONTEND}:latest"
            }
        }

        stage('Deploy') {
            steps {
                sh 'docker-compose down || true'
                sh "IMAGE_TAG=${IMAGE_TAG} docker-compose up -d"
            }
        }
    }

    post {
        success {
            echo "Deployment successful! Backend: ${DOCKER_IMAGE_BACKEND}:${IMAGE_TAG}, Frontend: ${DOCKER_IMAGE_FRONTEND}:${IMAGE_TAG}"
        }
        failure {
            echo "Pipeline failed. Check the logs above."
        }
        always {
            sh 'docker logout'
        }
    }
}
