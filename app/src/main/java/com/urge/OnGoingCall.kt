package com.urge

import android.os.Build
import android.telecom.Call
import android.telecom.VideoProfile
import androidx.annotation.RequiresApi
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

object OngoingCall {
    val state: BehaviorSubject<Int> = BehaviorSubject.create()

    private val callback = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        object : Call.Callback() {
            override fun onStateChanged(call: Call, newState: Int) {
                Timber.d(call.toString())
                state.onNext(newState)
            }
        }
    } else {
        TODO("VERSION.SDK_INT < M")
    }

    var call: Call? = null
        @RequiresApi(Build.VERSION_CODES.M)
        set(value) {
            field?.unregisterCallback(callback)
            value?.let {
                it.registerCallback(callback)
                state.onNext(it.state)
            }
            field = value
        }

    @RequiresApi(Build.VERSION_CODES.M)
    fun answer() {
        call!!.answer(VideoProfile.STATE_AUDIO_ONLY)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun hangup() {
        call!!.disconnect()
    }
}