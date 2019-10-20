package com.urge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.telecom.Call
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_answer.*
import java.util.concurrent.TimeUnit
import android.provider.ContactsContract.PhoneLookup
import android.net.Uri.withAppendedPath
import android.content.ContentResolver
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri


class AnswerActivity : AppCompatActivity() {

    private val disposables = CompositeDisposable()

    private lateinit var number: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStart() {
        super.onStart()
        startService(Intent(this, BackgroundService::class.java))
        answer_attend.setOnClickListener {
            OngoingCall.answer()
        }

        answer_end.setOnClickListener {
            OngoingCall.hangup()
        }

        OngoingCall.state
                .subscribe(::updateUi)
                .addTo(disposables)

        OngoingCall.state
                .filter { it == Call.STATE_DISCONNECTED }
                .delay(1, TimeUnit.SECONDS)
                .firstElement()
                .subscribe { finish() }
                .addTo(disposables)
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi(state: Int) {
        answer_name.text = "Urge"

        answer_attend.isVisible = state == Call.STATE_RINGING
        answer_end.isVisible = state in listOf(
                Call.STATE_DIALING,
                Call.STATE_RINGING,
                Call.STATE_ACTIVE
        )
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }

    companion object {
        fun start(context: Context, call: Call) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Intent(context, AnswerActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .setData(call.details.handle)
                        .let(context::startActivity)
            }
        }
    }

    fun getContactName(context: Context, phoneNumber: String): String? {
        val cr = context.contentResolver
        val uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber))
        val cursor =
            cr.query(uri, arrayOf(PhoneLookup.DISPLAY_NAME), null, null, null) ?: return null
        var contactName: String? = null
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME))
        }

        if (cursor != null && !cursor.isClosed) {
            cursor.close()
        }

        return contactName
    }

}

