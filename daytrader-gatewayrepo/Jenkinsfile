@Library('DaytraderLib')_
def podTemplateYaml = libraryResource 'maven-kaniko.yaml'

pipeline {
  agent {
    kubernetes {
      label 'maven-kaniko-pod'
      yaml podTemplateYaml
    }
  }
  //libraries {
  //  lib('DaytraderLib')
  //}
  stages {
    stage('build maven') {
        steps {
            mavenBuild('daytrader-gatewayapp')
        }
    }
        stage('Build with Kaniko') {
      environment {
        PATH = "/busybox:/kaniko:$PATH"
      }
      steps {
        kanikoBuild('kaniko',
                    'daytrader-gatewayapp',
                    'daytrader-gateway',
                    'baserepodev.devrepo.malibu-pctn.com/104017-malibu-artifacts/malibu-app-images/daytrader',
                    'daytrader-example-gatewayapp',
                    'latest',
                    '4.0.0', 
                    2443)
      }
    }
  }
}
