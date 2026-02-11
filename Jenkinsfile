pipeline {
  agent any

  environment {
    // Ensure Maven offline mode isn't enabled in Jenkins
    MAVEN_OPTS = "-Xmx1024m"
  }

  parameters {
    string(name: 'CUCUMBER_TAGS', defaultValue: '@e2e', description: 'Cucumber tags to run')
    string(name: 'MVN_ADDITIONAL_ARGS', defaultValue: '', description: 'Extra maven args')
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Run tests') {
      steps {
        sh 'chmod +x ./ci/run_tests.sh || true'
        sh "./ci/run_tests.sh '${params.CUCUMBER_TAGS}' '${params.MVN_ADDITIONAL_ARGS}'"
      }
    }

    stage('Archive results') {
      steps {
        junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
        archiveArtifacts artifacts: 'test-output/**, target/*.exec', fingerprint: true
      }
    }
  }

  post {
    always {
      echo 'Pipeline finished — see archived artifacts and JUnit results.'
    }
    failure {
      echo 'Build failed — check test reports and screenshots in artifacts.'
    }
  }
}
