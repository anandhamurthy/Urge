package com.urge

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.telecom.Call
import android.telecom.InCallService

class CallService : InCallService() {

    override fun onCallAdded(call: Call) {
        OngoingCall.call = call
        startService(Intent(this, BackgroundService::class.java))
        AnswerActivity.start(this, call)

    }

    override fun onCallRemoved(call: Call) {
        OngoingCall.call = null
    }

}