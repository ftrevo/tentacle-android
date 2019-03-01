#!groovy
node('master') {

    println "\u2756 ${env.JOB_NAME} \u2756"

    stage('Checkout') {
        deleteDir()
        checkout scm
    }

    def utils = load('JenkinsUtils.groovy')

    stage('clean') {
         utils.gradleClean()
    }

    /*stage('Version') {
        utils.upgradeVersionName()
    }*/

    stage('Build APK') {
        utils.buildApk('Release')
    }

    stage('Release') {
        utils.releaseAndroidAPKToCrashlytics('Release')
    }

    stage('Inform Build OK') {
        utils.sendSucessNotify('Job success')
    }
}
