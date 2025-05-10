pipeline {
  agent any

  tools {
    nodejs 'Node 22'
    maven 'Maven 3'
  }

  environment {
    DEPLOY_DIR = '/srv/demo'
    BACKUP_DIR = '/srv/demo/backup'
    TIMESTAMP = "${new Date().format('yyyyMMdd_HHmmss')}"
  }

  stages {

    stage('Checkout') {
      steps {
        git url: 'git@github.com:igonzalezco/tfg-teamapp.git', credentialsId: 'github-ssh'
      }
    }

    stage('Build Backend') {
      steps {
        script {
          sh "mkdir -p ${BACKUP_DIR}/${TIMESTAMP}"
          sh "[ -f ${DEPLOY_DIR}/teamapp-server-1.0-SNAPSHOT.jar ] && cp ${DEPLOY_DIR}/app.jar ${BACKUP_DIR}/${TIMESTAMP}/teamapp-server-1.0-SNAPSHOT.jar || echo 'No previous teamapp-server-1.0-SNAPSHOT.jar to backup'"
          sh 'mvn clean package -DskipTests  -Dspring.profiles.active=demo'
          sh "cp app-server/target/*.jar ${DEPLOY_DIR}/teamapp-server-1.0-SNAPSHOT.jar"
        }
      }
    }

    stage('Build Frontend') {
      steps {
        dir('teamapp-web') {
          script {
            sh "mkdir -p ${BACKUP_DIR}/${TIMESTAMP}"
            sh "[ -d ${DEPLOY_DIR}/dist ] && cp -r ${DEPLOY_DIR}/dist ${BACKUP_DIR}/${TIMESTAMP}/dist || echo 'No previous dist to backup'"
            sh 'npm install'
            sh 'npm run build:demo'
            sh "cp -r dist ${DEPLOY_DIR}/"
          }
        }
      }
    }

    stage('Run Backend') {
      steps {
        sh "nohup java -jar ${DEPLOY_DIR}/teamapp-server-1.0-SNAPSHOT.jar > ${DEPLOY_DIR}/backend.log 2>&1 &"
      }
    }
  }

  post {
    success {
      echo "Build completado. Backup en /srv/demo/backup/${env.TIMESTAMP}"
    }
    failure {
      echo 'Error en la pipeline. Puedes restaurar desde /srv/demo/backup/'
    }
  }
}