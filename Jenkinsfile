pipeline {
  agent any
  environment {
    DOCKER_HUB = 'nmy5220'
    K8S_NAMESPACE = 'ecommerce'
  }
  stages {
    stage('Checkout') {
      steps {
        git url: "https://github.com/skalsduq52/Yeop-Commerce.git"
      }
    }

    stage('Detect Changes') {
      steps {
        script {
          def changedFiles = sh(
            script: "git diff --name-only origin/main...HEAD",
            returnStdout: true
          ).trim().split("\n")

          def servicesToBuild = []
          changedFiles.each { file ->
            if (file.startsWith('User/')) {
              servicesToBuild << 'user-manage'
            }
            if (file.startsWith('Product/')) {
              servicesToBuild << 'product-manage'
            }
          }

          env.SERVICES = servicesToBuild.unique().join(' ')
          echo "Services to Build: ${env.SERVICES}"
        }
      }
    }

    stage('Build and Push Docker Images') {
      when {
        expression { return env.SERVICES }
      }
      steps {
        script {
          env.SERVICES.split(' ').each { service ->
            dir(service == 'user-manage' ? 'User' : 'Product') {
              def imageName = "${DOCKER_HUB}/${service}:latest"
              withCredentials([usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                sh """
                    docker build -t ${imageName} .
                    docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD}
                    docker push ${imageName}
                """
              }
            }
          }
        }
      }
    }

    stage('Deploy to Kubernetes') {
      when {
        expression { return env.SERVICES }
      }
      steps {
        script {
          env.SERVICES.split(' ').each { service ->
            def deployName = (service == 'user-manage') ? 'user-management' : 'product-management'
            sh """
              kubectl set image deployment/${deployName}-deployment \
              ${deployName}=${DOCKER_HUB}/${service}:latest \
              --namespace=${K8S_NAMESPACE}
            """
          }
        }
      }
    }
  }
}
