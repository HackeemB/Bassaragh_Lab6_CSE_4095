package com.example.lab6_04_04_2022.media

import android.content.Context
import android.media.MediaDrm
import android.media.MediaPlayer
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import android.view.SurfaceHolder
import android.view.View
import android.widget.*
import android.widget.SeekBar.*
import androidx.appcompat.app.AppCompatActivity

class VideoController(myVideoContext: Context, videoView: VideoView) :  MediaPlayer.OnPreparedListener, MediaPlayer.OnDrmInfoListener,
SurfaceHolder.Callback, OnSeekBarChangeListener {


    private var myGlobalVideoContext = myVideoContext
    private var myVideoView = videoView
//    private var myVideoToggle = videoToggle
//    private var myVolumeToggle = volumeToggle

    lateinit var mediaController: MediaController
    lateinit var mediaPlayer: MediaPlayer

    var videoOn: Boolean = false
    var videoVolumeOn: Boolean = false
    var firstTimeOn: Boolean = true

    val videoStr = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"

    fun kickOffVideo() {

        mediaController = MediaController(myGlobalVideoContext)
        mediaController.setAnchorView(myVideoView)
        myVideoView.setMediaController(mediaController)
//        myVideoView.holder.addCallback(this)

    }

    fun setUpListeners() {
        mediaController.setPrevNextListeners({
            object: View.OnClickListener {
                override fun onClick(p0: View?) {
                    Toast.makeText(myGlobalVideoContext, "Previous", Toast.LENGTH_SHORT).show()
                }
            }
        }, {
            object: View.OnClickListener {
                override fun onClick(p0: View?) {
                    Toast.makeText(myGlobalVideoContext, "Next", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    fun adjustVolume(mediaPlayer: MediaPlayer) {
        if (videoVolumeOn) {
            mediaPlayer.setVolume(1.0F, 1.0F)

        }
        else {
            mediaPlayer.setVolume(0.0F, 0.0F)
        }
        videoVolumeOn = !videoVolumeOn
    }

    fun startVideo(mediaPlayer: MediaPlayer) {
        if (videoOn) {
            mediaPlayer.pause()
            mediaPlayer.stop()
        } else {
            if (firstTimeOn) {
                mediaPlayer.prepareAsync()
                firstTimeOn = !firstTimeOn
            } else {
                mediaPlayer.reset()
                mediaPlayer.setDataSource(myGlobalVideoContext,Uri.parse(videoStr))
                mediaPlayer.prepareAsync()
            }
        }
        videoOn = ! videoOn
    }









//    override fun newArray(size: Int): Array<VideoController?> {
//            return arrayOfNulls(size)
//    }

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
            setOnDrmInfoListener(this@VideoController)
            //                 Uri.parse())
            setDataSource(myGlobalVideoContext, Uri.parse(videoStr))
            setDisplay(surfaceHolder)
            //prepareAsync()
        }
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        TODO("Not yet implemented")
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        TODO("Not yet implemented")
    }

    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
        TODO("Not yet implemented")
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
        TODO("Not yet implemented")
    }
}


