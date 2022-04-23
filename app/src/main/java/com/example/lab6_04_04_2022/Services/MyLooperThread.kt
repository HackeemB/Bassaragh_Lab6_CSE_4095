package com.example.lab6_04_04_2022.Services

import android.os.Looper
import android.os.Handler
import android.os.Message
import android.util.Log


const val TAG: String = "HAB"

class MyLooperThread(): Thread("Looper Thread") {
    fun getHandler():Handler? {
        return mHandler
    }
    var mHandler: Handler? = null
    override fun run() {

        Looper.prepare()
        mHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    1 -> Log.i("LOOPER", "Val 1")
                    2 -> Log.i("LOOPER", "Val 2")
                    else -> {
                        Log.i("LOOPER", "ELSE")
                    }
                }
                looper.thread.interrupt()
            }
        }
        Looper.loop()
    }
}