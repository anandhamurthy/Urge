package com.urge

import android.Manifest.permission.CALL_PHONE
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.telecom.TelecomManager
import android.telecom.TelecomManager.ACTION_CHANGE_DEFAULT_DIALER
import android.telecom.TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.core.net.toUri
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialing_pad.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        dialer_number.setText(intent?.data?.schemeSpecificPart)

    }

    override fun onStart() {
        super.onStart()
        startService(Intent(this, BackgroundService::class.java))
        offerReplacingDefaultDialer()

        dialer_number.setOnEditorActionListener { _, _, _ ->
            makeCall()
            true
        }
    }

    private fun makeCall() {
        if (checkSelfPermission(this, CALL_PHONE) == PERMISSION_GRANTED) {
            val uri = "tel:${dialer_number.text}".toUri()
            startActivity(Intent(Intent.ACTION_CALL, uri))
        } else {
            requestPermissions(this, arrayOf(CALL_PHONE), REQUEST_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSION && PERMISSION_GRANTED in grantResults) {
            makeCall()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun offerReplacingDefaultDialer() {
        if (getSystemService(TelecomManager::class.java).defaultDialerPackage != packageName) {
            Intent(ACTION_CHANGE_DEFAULT_DIALER)
                .putExtra(EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName)
                .let(::startActivity)
        }
    }

    companion object {
        const val REQUEST_PERMISSION = 0
    }

    fun one (view: View) {
        onButtonClick(btn_one,dialer_number,"1")
    }
    fun two (view: View) {
        onButtonClick(btn_two,dialer_number,"2")
    }
    fun three (view: View) {
        onButtonClick(btn_three,dialer_number,"3")
    }
    fun four (view: View) {
        onButtonClick(btn_four,dialer_number,"4")
    }
    fun five (view: View) {
        onButtonClick(btn_five,dialer_number,"5")
    }
    fun six (view: View) {
        onButtonClick(btn_six,dialer_number,"6")
    }
    fun seven (view: View) {
        onButtonClick(btn_seven,dialer_number,"7")
    }
    fun eight (view: View) {
        onButtonClick(btn_eight,dialer_number,"8")
    }
    fun nine (view: View) {
        onButtonClick(btn_nine,dialer_number,"9")
    }
    fun star (view: View) {
        onButtonClick(btn_star,dialer_number,"*")
    }
    fun zero (view: View) {
        onButtonClick(btn_zero,dialer_number,"0")
    }

    fun hash (view: View) {
        onButtonClick(btn_hash,dialer_number,"#")
    }

    fun clear (view: View) {
        dialer_number.setText("")
    }

    fun call (view: View) {
        if(dialer_number.text.length<=3){
            Toast.makeText(this, "Cannot Dial", Toast.LENGTH_SHORT).show()
        }else{
            makeCall()
        }
    }

    fun onButtonClick(btn: TextView, edt: EditText, inp: String){
        var cahe = dialer_number.text.toString()
        edt.setText(cahe+inp)
    }
}
