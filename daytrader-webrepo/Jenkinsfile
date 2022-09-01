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
            mavenBuild('daytrader-webapp')
        }
    }
    stage('Build with Kaniko') {
      environment {
        PATH = "/busybox:/kaniko:$PATH"
      }
      steps {
        kanikoBuild('kaniko',
                    'daytrader-webapp',
                    'daytrader-web',
                    'baserepodev.devrepo.malibu-pctn.com/104017-malibu-artifacts/malibu-app-images/daytrader',
                    'daytrader-example-webapp',
                    'latest',
                    '4.0.0', 
                    5443)
      }
    }
    //stage('Deploy Trigger') {
    //  steps {  
    //    spinnakerTrigger('maven',
    //                    'malibu-apps',
    //                    'waitPipeline')
    //  }
    //}
    //stage('Deploy Status') {
    //  input{
    //    id 'Spinnaker-deploy'
    //    message 'Waiting for spinnaker'
    //    ok 'succeeded'
    //    parameters {
    //      string(defaultValue: 'pending', description: '', name: 'STATUS', trim: false)
    //    }
    //  }
    //  steps {
    //    echo "Deploying ${STATUS}."
    //  }
    //}
  }
}
