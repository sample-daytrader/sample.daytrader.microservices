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
            mavenBuild('daytrader-portfoliosapp')
        }
    }
    stage('Build with Kaniko') {
      environment {
        PATH = "/busybox:/kaniko:$PATH"
      }
      steps {
        kanikoBuild('kaniko',
                    'daytrader-portfoliosapp',
                    'daytrader-portfolios',
                    'baserepodev.devrepo.malibu-pctn.com/104017-malibu-artifacts/malibu-app-images/daytrader',
                    'daytrader-example-portfoliosapp',
                    'latest',
                    '4.0.0', 
                    3443, 
                    '--build-arg DATABASE_DRIVER=org.apache.derby.jdbc.EmbeddedDriver --build-arg DATABASE_URL="jdbc:derby:tradesdb;create=true"')
      }
    }
  }
}
