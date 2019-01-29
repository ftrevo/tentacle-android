#!groovy
node('vv-macmini-recife') {

    println "\u2756 ${env.JOB_NAME} \u2756"

    stage('Checkout') {
        deleteDir()
        checkout scm
    }

    def utils = load('JenkinsUtils.groovy')

    stage('clean') {
         utils.gradleClean()
    }

    stage('Version') {
        utils.upgradeVersionName()
    }

    stage('Sintaxe Code') {
        utils.testsKtlint()
    }

    stage('Lint Debug') {
        utils.lintDebug()
    }

    stage('Build APK') {
        utils.buildApk('Homolog')
    }

     stage('Unit Tests') {
         utils.unitTests('Homolog')
     }

    stage('Instruments Tests') {
        utils.instrumentTests()
    }

    stage('Jacoco Report') {
        utils.reportJacoco()
    }

    stage('Inform Build OK') {
        utils.sendSucessNotify('Job success')
    }
}
