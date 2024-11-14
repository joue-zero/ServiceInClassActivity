package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        findViewById<Button>(R.id.startButton).setOnClickListener {
//
//        }
//
//        findViewById<Button>(R.id.stopButton).setOnClickListener {
//
//        }
//    }

    lateinit var timerTextView: TextView
    var timerBinder : TimerService.TimerBinder? = null
    val serviceConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            timerBinder = service as TimerService.TimerBinder
            timerBinder!!.setHandler(timerHandler)
            isconnected=true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            timerBinder = null
            isconnected=false
        }

    }
    var isconnected=false
//    val timerHandler = Handler(Looper.getMainLooper()){
//        timerTextView.text=it.obj.toString()
//        true
//    }

    val timerHandler= Handler(Looper.getMainLooper()){
        timerTextView.text=it.what.toString()
        true
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerTextView=findViewById(R.id.textView)
        bindService(
            Intent(this, TimerService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE
        )

        val button = findViewById<Button>(R.id.startButton)

        button.setOnClickListener {
            if(isconnected && button.text=="Start"){
                timerBinder?.start(100)
                button.text="Pause"

            }
            else if(isconnected && button.text=="Pause"){
                timerBinder?.pause()
                button.text="Start"
            }
        }

        findViewById<Button>(R.id.stopButton).setOnClickListener {
            timerBinder?.stop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }
}