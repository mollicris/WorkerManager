package com.example.workmanagerdemo1

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.text.SimpleDateFormat
import java.util.*

class DownloadingWorker(context : Context, params: WorkerParameters)  : Worker(context,params){

    override fun doWork(): Result {
        try {
            for (i:Int in 0..3000){
                Log.i("MyTAG","Downloading $i")
            }

            val time = SimpleDateFormat("dd:MM/yyyy hh:mm:ss")
            val currentDate = time.format(Date())
            Log.i("MyTAG","Completed $currentDate")
            return  Result.success()
        }catch (e:java.lang.Exception){
            return Result.failure()
        }
    }
}