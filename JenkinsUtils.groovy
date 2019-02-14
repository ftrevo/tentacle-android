import groovy.json.JsonOutput

class Constants {
    final static String FALLBACK = 'Required plain-text summary of the attachment.'
    final static String COLOR_JOB_SUCCESS = '#36a64f'
    final static String COLOR_JOB_FAIL = '#ff0000'
    final static String PRETEXT = ''
    final static String JOB_NAME = "JOB_NAME"
    final static String JOB_LINK = "JOB_URL"
    final static String AUTHOR_ICON = 'https://avatars0.githubusercontent.com/u/10986514?v=3&s=400'
    final static String APPLICATION_NAME = 'Job executado'
    final static String APPLICATION_LINK = "BUILD_URL"
    final static String TITLE_JOB_FAIL_SLACK = 'BUILD DEV FAIL'
    final static String IMAGE_FAIL_URL = 'http://i.giphy.com/Nweu3IeBIZIvm.gif'
    final static String IMAGE_SUCCESS_URL = 'http://i.giphy.com/IO5cB3RVPeklW.gif'
    final static String TITLE_JOB_SUCCESS_SLACK = 'BUILD DEV SUCCESS'
    final static String TS = ''
}

class Attachment {
    def fallback = Constants.FALLBACK
    def color = "#ff0000"
    def author_name = "JOB_NAME"
    def author_link = "JOB_URL"
    def author_icon = Constants.AUTHOR_ICON
    def title = Constants.APPLICATION_NAME
    def title_link = "BUILD_URL"
    def footer = 'Status build success or failed'
    def footer_icon = 'Icon URL of the success or failed'
    def text = "Stage Build"
    def ts = Constants.TS

    Attachment(String color, String jobName, String jobUrl, String buildUrl, String statusSuccessFailed, String stage, String iconSuccessFailed) {
        this.color = color
        this.author_name = jobName
        this.author_link = jobUrl
        this.title_link = buildUrl
        this.footer = statusSuccessFailed
        this.text = stage
        this.footer_icon = iconSuccessFailed
    }
}

def notifySlack(String color, String jobName, String jobUrl, String buildUrl, String messengerStatus, String stage, String imageJenkins) {
    def slackURL = 'https://hooks.slack.com/services/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'
    def payload = JsonOutput.toJson([attachments: [new Attachment(color, jobName, jobUrl, buildUrl, messengerStatus, stage, imageJenkins)]])
    sh "curl -X POST --data-urlencode \'payload=${payload}\' ${slackURL}"
}

def sendFailedNotify(String messageBuild , Throwable err) {

    notifySlack(Constants.COLOR_JOB_FAIL,
        "${env.JOB_NAME}",
        "${env.JOB_URL}",
        "${env.BUILD_URL}",
        "Failed",
        "${messageBuild} :broken_heart:",
        Constants.IMAGE_FAIL_URL)

    println err.getMessage()
    currentBuild.result = "FAILURE"
    throw new Exception(messageBuild)
}

def sendSucessNotify(String messageBuild) {
    if (currentBuild.result == "FAILURE") {
        echo 'Build Failed' 
    } else if (currentBuild.result == "UNSTABLE") {
        echo 'Build Unstable'
    } else {
    notifySlack(Constants.COLOR_JOB_SUCCESS,
        "${env.JOB_NAME}",
        "${env.JOB_URL}",
        "${env.BUILD_URL}",
        "Success",
        "${messageBuild} :heart:",
        Constants.IMAGE_SUCCESS_URL)
    }
}

def archivesProject(String pathArchive) {
    archiveArtifacts "${pathArchive}"
}

def archiveXML(String pathXML) {
     junit "$pathXML/*.xml"
}

def publishHtmlProject(String pathPublish, String namePublish) {
    publishHTML([allowMissing: true, 
        alwaysLinkToLastBuild: true, 
        keepAll: true, 
        reportDir: "${pathPublish}", 
        reportFiles: 'index.html', 
        reportName: "${namePublish}", 
        reportTitles: ''])
}

def upgradeVersionName() {
    sh '''
    BUILD_VERSION=$(cat tools/version/version_code.txt)
    VERSION_NEW="$BUILD_VERSION"."$BUILD_NUMBER"
    echo $VERSION_NEW | tr -d "\n" > tools/version/version_name.txt
    cat tools/version/version_name.txt
    '''
}

def testsKtlint() {
    try {
        sh """
        java -version
        ./gradlew wrapper
        ./gradlew ktlint
        """
        publishHtmlProject('app/build/reports/ktlint/', 'Ktlint')
    } catch (err) {
        publishHtmlProject('app/build/reports/ktlint/', 'Ktlint')
        sendFailedNotify('ktlint Tests', err)
    }
}

def lintDebug() {
    try {
        // padrao nome de JOB: xx-xx-xx/xx-xx-xx-xx-xx/PR-<ID>
        def bitbucketID = env.JOB_NAME.split('-')[7]
        sh "./gradlew lintDebug"
        sh "./gradlew addCommentsForPR -DpullRequestId=${bitbucketID}"
        publishHtmlProject('app/build/reports/lint/', 'Lint Debug')
    } catch(err) {
        publishHtmlProject('app/build/reports/lint/', 'Lint Debug')
        sendFailedNotify('Lint Debug', err)
    }
}

def buildApk(String buildType) {
    try {
        def lowercaseBuidtype = buildType.toLowerCase()
        sh "./gradlew :app:assemble${buildType} --stacktrace --info --profile"
        archivesProject("app/build/outputs/apk/${lowercaseBuidtype}/app-${lowercaseBuidtype}.apk")
    } catch(err) {
        sendFailedNotify('Build APK', err)
    }
}

def unitTests(String buildType) {
    try {
        sh "./gradlew test${buildType}UnitTest"
         archiveXML("app/build/test-results/test${buildType}UnitTest")
        publishHtmlProject("app/build/reports/tests/test${buildType}UnitTest", 'Local Tests')
    } catch (err) {
        publishHtmlProject("app/build/reports/tests/test${buildType}UnitTest", 'Local Tests')
        sendFailedNotify('Local Tests', err)
    }
}

def killEmulator() {
    sh '''
    /Users/macmini-recife/homebrew/bin/adb devices -l |grep emulator-5554
    if [[ $? -eq 0 ]]; then
        PROCESS=$(ps -ef |grep -v grep |grep Nexus_5X_API_28 |awk '{print $2}')
        sudo kill -9 ${PROCESS}

        sleep 5
    fi
    '''
}

def instrumentTests() {
    try {
        sh '''
        /Library/Java/AndroidSDK/emulator/emulator -avd Nexus_5X_API_28 -netdelay none -netspeed full &
        sleep 120

        ./gradlew uninstallAll
        ./gradlew connectedDebugAndroidTest
        '''
        killEmulator()
        publishHtmlProject('app/build/reports/androidTests/connected', 'Instrument Tests')
        archivesProject("app/build/reports/androidTests/connected/index.html")

    }
    catch(err) {
        killEmulator()
        publishHtmlProject('app/build/reports/androidTests/connected', 'Instrument Tests')
        archivesProject("app/build/reports/androidTests/connected/index.html")
        sendFailedNotify('Instrument Tests', err)
    }   
}

def reportJacoco() { 
    try {
        sh "./gradlew jacocoReport"
        publishHtmlProject('app/build/reports/jacoco/jacocoReport/html/', 'Code Coverage')
    }
    catch(err) {
        publishHtmlProject('app/build/reports/jacoco/jacocoReport/html/', 'Code Coverage')
        sendFailedNotify('Report Jacoco', err)
    }
}

def releaseAndroidAPKToCrashlytics(String buildType) {
    try {
        sh "./gradlew :app:crashlyticsUploadDistribution${buildType}"
    } catch(err) {
        sendFailedNotify('Release Crashlytics', err)
    }
}

def gradleClean() {
    sh "./gradlew clean"
}

return this
