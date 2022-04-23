package com.example.lab6_04_04_2022

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.widget.Button
import android.widget.Toast
import com.example.lab6_04_04_2022.Services.MediaServices
import com.example.lab6_04_04_2022.model.RadioStations

/*-----------------------------------*/
import android.media.MediaDrm
import android.media.MediaPlayer
import android.net.Uri
import android.view.SurfaceHolder
import android.view.View
import android.view.WindowManager
import android.widget.MediaController
import android.widget.SeekBar
import android.widget.VideoView
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.lab6_04_04_2022.media.VideoController
import java.net.URI

lateinit var mediaServices: MediaServices
private var bound: Boolean = false
private var message: Message = Message()
//var radioOn: Boolean = false


class MainActivity : AppCompatActivity(), MediaPlayer.OnPreparedListener, MediaPlayer.OnDrmInfoListener,
    SurfaceHolder.Callback, SeekBar.OnSeekBarChangeListener {

    lateinit var videoView: VideoView // pass this
    lateinit var videoToggle: Button  // pass this
    lateinit var volumeToggle: Button  // pass this
    lateinit var radioButton: Button
    lateinit var mediaController: MediaController
    lateinit var mediaPlayer: MediaPlayer
    lateinit var videoController: VideoController
    var videoOn: Boolean = false
    var videoVolumeOn: Boolean = false
    var firstTimeOn: Boolean = true

    val videoStr = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
        //"https://manifest.googlevideo.com/api/manifest/hls_playlist/expire/1649895850/ei/ShVXYt3qDom58wSPuKnoAQ/ip/137.99.236.44/id/DDU-rZs-Ic4.3/itag/96/source/yt_live_broadcast/requiressl/yes/ratebypass/yes/live/1/sgoap/gir%3Dyes%3Bitag%3D140/sgovp/gir%3Dyes%3Bitag%3D137/hls_chunk_host/rr2---sn-jvooxqouf3-cqaz.googlevideo.com/playlist_duration/30/manifest_duration/30/spc/4ocVC1ZALQguP-a74YvsBC9DzK_D/vprv/1/playlist_type/DVR/initcwndbps/8200/mh/PU/mm/44/mn/sn-jvooxqouf3-cqaz/ms/lva/mv/m/mvi/2/pl/16/dover/11/pacing/0/keepalive/yes/fexp/24001373,24007246/mt/1649873800/sparams/expire,ei,ip,id,itag,source,requiressl,ratebypass,live,sgoap,sgovp,playlist_duration,manifest_duration,spc,vprv,playlist_type/sig/AOq0QJ8wRQIgPgxHeOEKFSjbPV5pCwM1iCRyO5sF9WvD-qwV5-zQXRkCIQDIjiBZPdpc7xnp98zjpD5Cz7unJtnO6MV9K_c_IThZGg%3D%3D/lsparams/hls_chunk_host,initcwndbps,mh,mm,mn,ms,mv,mvi,pl/lsig/AG3C_xAwRQIgc5M7PfIdVtJ9Phe_YuvYSBTTWuFzHt2kG2y5a6ocNhUCIQDwPZbCYRqv1T7Uqva2NolZjhDfXLEgbEh5ld7zFuXRdg%3D%3D/playlist/index.m3u8"


    lateinit var radioToggle: Button
    val radioStations: RadioStations = RadioStations()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)


        radioToggle = findViewById(R.id.radio_toggle_button)

        // Video code starts here
        videoView = findViewById(R.id.videoView)
        videoToggle = findViewById(R.id.video_toggle_button)
        volumeToggle = findViewById(R.id.volume_off)

        videoController = VideoController(baseContext, videoView)
//        videoController = VideoController()

        mediaServices = MediaServices()
        mediaPlayer = MediaPlayer()
        videoController.kickOffVideo()


        videoView.holder.addCallback(this)


        volumeToggle.setOnClickListener {
                videoController.adjustVolume(mediaPlayer)
        }

        videoController.setUpListeners()


        mediaPlayer.setOnPreparedListener(this)


        videoToggle.setOnClickListener {
            videoController.startVideo(mediaPlayer)

        }

        // Video code ends here

        mediaServices = MediaServices()


        radioToggle.setOnClickListener{
//            var value = mediaServices.getInt()
            message.what = 1
            mediaServices.radioToggle()

            mediaServices.SelectMedia(1)
            mediaServices.sendMessagge(message,1)
//            Toast.makeText(applicationContext,"Hello: $value", Toast.LENGTH_SHORT).show()
        }
    }



    override fun onStart() {
        super.onStart()

        val intent: Intent = Intent(this,MediaServices::class.java)
        intent.putExtra("Greeting", "Hello world!")
//        startService(intent)
        bindService(intent, connection, BIND_AUTO_CREATE)

    }

    override fun onStop() {
        super.onStop()

        unbindService(connection)
        bound = false
    }
    private val connection: ServiceConnection = object: ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            val binder: MediaServices.LocalBinder = service as MediaServices.LocalBinder
            mediaServices = binder.getInstance()
            bound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            bound = false
        }

    }

    override fun onPrepared(mediaPlayer: MediaPlayer) {
        mediaPlayer.start()
    }

    override fun onDrmInfo(mediaPlayer: MediaPlayer, drmInfo: MediaPlayer.DrmInfo?) {
        mediaPlayer.apply {
            val key = drmInfo?.supportedSchemes?.get(0)
            key?.let {
                prepareDrm(key)
                val keyRequest = getKeyRequest(
                    null, null, null,
                    MediaDrm.KEY_TYPE_STREAMING, null
                )
                provideKeyResponse(null, keyRequest.data)
            }
        }
    }

    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        mediaPlayer.apply {
            setOnDrmInfoListener(this@MainActivity)
            //                 Uri.parse())
            setDataSource(applicationContext, Uri.parse(videoStr))
            setDisplay(surfaceHolder)
            //prepareAsync()
        }
    }

    override fun onProgressChanged(p0: SeekBar?, p1: Int, fromUser: Boolean) {
        if(fromUser) {
            //mediaPlayer.seekTo(progress* SECOND)
        }
    }


    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        // Nothing
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        // Nothing
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
        // Nothing
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
        // Nothing
    }
}