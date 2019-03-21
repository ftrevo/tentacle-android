package br.com.concrete.tentacle.data.services

import br.com.concrete.tentacle.utils.LogWrapper
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService

class MyJobService : JobService() {

    override fun onStartJob(jobParameters: JobParameters): Boolean {
        LogWrapper.log(TAG, "Performing long running task in scheduled job")
        // TODO(developer): add long running task here.
        return false
    }

    override fun onStopJob(jobParameters: JobParameters): Boolean {
        return false
    }

    companion object {

        private const val TAG = "MyJobService"
    }
}