package com.example.workmanagerdemo1

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class CompressingWorker(context : Context, params: WorkerParameters)  : Worker(context,params){

    override fun doWork(): Result {
        try {
            for (i:Int in 0..300){
                Log.i("MyTAG","Compressing $i")
            }
            return  Result.success()
        }catch (e:java.lang.Exception){
            return Result.failure()
        }
    }
}