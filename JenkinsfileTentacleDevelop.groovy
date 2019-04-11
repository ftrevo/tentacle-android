#!groovy
node('master') {

    println "\u2756 ${env.JOB_NAME} \u2756"

    stage('Checkout') {
        deleteDir()
        checkout scm
    }

    def utils = load('JenkinsUtils.groovy')
    
    stage('cp Arquivos') {
        utils.cpArquivos()
    }
    
    stage('clean') {
         utils.gradleClean()
    }

    /*stage('Version') {
        utils.upgradeVersionName()
    }*/

    stage('Build APK') {
        utils.buildApk('Debug')
    }

    /*stage('Release') {
        utils.releaseAndroidAPKToCrashlytics('Debug')*/
    }

    stage('Inform Build OK') {
        utils.sendSucessNotify('Job success')
    }
}
