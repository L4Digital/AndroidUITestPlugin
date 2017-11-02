node("OpenSTF") {
    stage("Checkout") {
        git branch: 'master', credentialsId: 'Gitlab', url: 'git@gitlab.l4digital.com:org/l4digital/tools/android-test-helper-gradle-plugin.git'
        checkout scm
    }

    stage("Gradle - Assemble") {
        sh "./gradlew clean assemble"
    }

    stage("Archive") {
        archiveArtifacts artifacts: 'build/libs/*.jar', onlyIfSuccessful: true
    }
}