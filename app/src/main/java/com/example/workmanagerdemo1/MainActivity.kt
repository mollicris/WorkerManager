package com.example.workmanagerdemo1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.work.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    companion object{
        const val KEY_COUNT_VALUE = "key_count"
    }

    private lateinit var textview1 : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.button)
        textview1 = findViewById(R.id.textview)
        button.setOnClickListener {
            //setOneTimeWorkRequest()
            setPeriodicWorkRequest()
        }
    }
    private fun setOneTimeWorkRequest(){
        val workManager = WorkManager.getInstance(applicationContext)

        val data : Data = Data.Builder()
            .putInt(KEY_COUNT_VALUE,125)
            .build()
        //seejecutara solo cuando pase algun evento que este cargando el celular
        /*val contraints = Constraints.Builder()
            .setRequiresCharging(true)
            .build()*/
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val uploadRequest = OneTimeWorkRequest
            .Builder(UploadWorker::class.java)
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        val filteringRequest = OneTimeWorkRequest.Builder(FilteringWorker::class.java)
            .build()

        val compressingRequest = OneTimeWorkRequest.Builder(CompressingWorker::class.java)
            .build()

        val downloadingWorker = OneTimeWorkRequest.Builder(DownloadingWorker::class.java)
            .build()

        val parallWorks = mutableListOf<OneTimeWorkRequest>()
        parallWorks.add(downloadingWorker)
        parallWorks.add(filteringRequest)
        //workManager.enqueue(uploadRequest)
        workManager
            .beginWith(parallWorks)
            .then(compressingRequest)
            .then(uploadRequest)
            .enqueue()



        //Esto sirve para escuchar
        workManager.getWorkInfoByIdLiveData(uploadRequest.id)
            .observe(this, Observer {
            textview1.text = it.state.name
                if (it.state.isFinished){
                    val data = it.outputData
                    val message:String?=data.getString(UploadWorker.KEY_WORKER)
                    Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
                }
            })

    }

    private fun setPeriodicWorkRequest(){
        val periodicWorkRequest = PeriodicWorkRequest.
        Builder(DownloadingWorker::class.java,16,TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(applicationContext).enqueue(periodicWorkRequest)
    }
}