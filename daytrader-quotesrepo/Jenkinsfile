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
            mavenBuild('daytrader-quotesapp')
        }
    }
    stage('Build with Kaniko') {
      environment {
        PATH = "/busybox:/kaniko:$PATH"
      }
      steps {
        kanikoBuild('kaniko',
                    'daytrader-quotesapp',
                    'daytrader-quotes',
                    'baserepodev.devrepo.malibu-pctn.com/104017-malibu-artifacts/malibu-app-images/daytrader',
                    'daytrader-example-quotesapp',
                    'latest',
                    '4.0.0', 
                    4443, 
                    '--build-arg DATABASE_DRIVER=org.apache.derby.jdbc.EmbeddedDriver --build-arg DATABASE_URL="jdbc:derby:tradesdb;create=true"')
      }
    }
  }
}
