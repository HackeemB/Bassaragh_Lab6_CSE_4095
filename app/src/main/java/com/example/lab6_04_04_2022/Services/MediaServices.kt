package com.example.lab6_04_04_2022.Services
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.*
import android.os.health.ServiceHealthStats
import android.widget.Button
import com.example.lab6_04_04_2022.R
import com.example.lab6_04_04_2022.mediaServices
//import com.example.k2022_03_08_rv
import com.example.lab6_04_04_2022.model.RadioStations
import com.example.lab6_04_04_2022.model.MyMediaPlayer

var value: Int = 0
var radioOn: Boolean = false

class MediaServices: Service() {

    private lateinit var serviceLooper: Looper
    private lateinit var serviceHandler: ServiceHandler


    val myMediaPlayerAudio: MyMediaPlayer = MyMediaPlayer()
    var whatMedia: Int = 0


    private inner class ServiceHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            try {
                Thread.sleep(5000)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
            stopSelf(msg.arg1)
        }
    }

    override fun onCreate() {
        super.onCreate()

        HandlerThread("Service Args", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()

            serviceLooper = looper
            serviceHandler = ServiceHandler(serviceLooper)
        }
    }

    fun sendMessagge(message: Message, what: Int) {
        serviceHandler?.obtainMessage().also { msg ->
            msg.arg1 = what
            serviceHandler?.sendMessage(msg)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        serviceHandler?.obtainMessage().also { msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)

        }
        return START_STICKY
    }

    fun SelectMedia(svcs: Int) {
        whatMedia = svcs
    }

    var binder = LocalBinder()

    override fun onBind(sevice: Intent?): IBinder? {
        return binder
    }

    fun getInt(): Int {
        value = value.plus(1)
        return value
    }

    fun radioToggle() {


//        val link: String = "http://stream.whus.org:8000/whusfm" //http://evcast.mediacp.eu:1750/;;
        val link: String    = "http://streaming.live365.com/b76353_128mp3"
        if (radioOn) {
            myMediaPlayerAudio.pause()
        }
        else {
            myMediaPlayerAudio.setVolume(0.1F,0.1F)
            myMediaPlayerAudio.setUpRadio(link)
            myMediaPlayerAudio.prepareAndPlayStation(link)
        }
        radioOn = !radioOn
    }

    inner class LocalBinder : Binder() {
        fun getInstance() : MediaServices = this@MediaServices
    }
}