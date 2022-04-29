package com.example.botcontroller

import android.Manifest
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.app.ActivityCompat
import com.example.botcontroller.databinding.ActivityControlBinding
import java.io.IOException
import java.util.*

class ControlActivity : AppCompatActivity() {

    private lateinit var binding: ActivityControlBinding
//    var ledOn = false

    companion object {
        var mMyUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var mBluetoothSocket: BluetoothSocket? = null
        lateinit var mProgress: ProgressDialog
        lateinit var mBluetoothAdapter: BluetoothAdapter
        var mIsConnected: Boolean = false
        lateinit var mAddress: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControlBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAddress = intent.getStringExtra(MainActivity.EXTRA_ADDRESS)!!

        ConnectToDevice(this).execute()
//  --------------------------------------- LED --------------------------------------------------
//        if (mIsConnected)
//            Toast.makeText(this, "Connection : Success!", Toast.LENGTH_LONG).show()
//        else
//            Toast.makeText(this, "Connection : Failed!", Toast.LENGTH_LONG).show()
//        ledOn = getState()
//        if (ledOn)
//        {
//            binding.imageView.setImageResource(R.drawable.ledon)
//            binding.ledSwitch.setText(R.string.off)
//        }
//        else
//        {
//            binding.imageView.setImageResource(R.drawable.ledoff)
//            binding.ledSwitch.setText(R.string.on)
//        }
//        binding.ledSwitch.setOnClickListener{
//            if (ledOn)
//            {
//                sendCommand("b")
//                ledOn = false
//                binding.imageView.setImageResource(R.drawable.ledoff)
//                binding.ledSwitch.setText(R.string.on)
//            }
//            else
//            {
//                sendCommand("a")
//                ledOn = true
//                binding.imageView.setImageResource(R.drawable.ledon)
//                binding.ledSwitch.setText(R.string.off)
//            }
//        }

//        binding.ledSwitch.setOnTouchListener (View.OnTouchListener { view, motionEvent ->
//            when (motionEvent.action){
//                MotionEvent.ACTION_UP -> {
//                    sendCommand("b")
//                    ledOn = false
//                    binding.imageView.setImageResource(R.drawable.ledoff)
//                    binding.ledSwitch.setText(R.string.on)
//                }
//                MotionEvent.ACTION_DOWN -> {
//                    sendCommand("a")
//                    ledOn = true
//                    binding.imageView.setImageResource(R.drawable.ledon)
//                    binding.ledSwitch.setText(R.string.off)
//                }
//            }
//            return@OnTouchListener true
//        })

//  ----------------------------------------------------------------------------------------------

        binding.northButton.setOnTouchListener (View.OnTouchListener { view, motionEvent ->
            when (motionEvent.action){
                MotionEvent.ACTION_DOWN -> {
                    sendCommand("n1")
                    binding.northButton.setBackgroundResource(R.color.dark_bg)
                }
                MotionEvent.ACTION_UP -> {
                    sendCommand("n0")
                    binding.northButton.setBackgroundResource(R.color.light_bg)
                }
            }
            return@OnTouchListener true
        })

        binding.southButton.setOnTouchListener (View.OnTouchListener { view, motionEvent ->
            when (motionEvent.action){
                MotionEvent.ACTION_DOWN -> {
                    sendCommand("s1")
                    binding.southButton.setBackgroundResource(R.color.dark_bg)
                }
                MotionEvent.ACTION_UP -> {
                    sendCommand("s0")
                    binding.southButton.setBackgroundResource(R.color.light_bg)
                }
            }
            return@OnTouchListener true
        })

        binding.westButton.setOnTouchListener (View.OnTouchListener { view, motionEvent ->
            when (motionEvent.action){
                MotionEvent.ACTION_DOWN -> {
                    sendCommand("w1")
                    binding.westButton.setBackgroundResource(R.color.dark_bg)
                }
                MotionEvent.ACTION_UP -> {
                    sendCommand("w0")
                    binding.westButton.setBackgroundResource(R.color.light_bg)
                }
            }
            return@OnTouchListener true
        })

        binding.eastButton.setOnTouchListener (View.OnTouchListener { view, motionEvent ->
            when (motionEvent.action){
                MotionEvent.ACTION_DOWN -> {
                    sendCommand("e1")
                    binding.eastButton.setBackgroundResource(R.color.dark_bg)
                }
                MotionEvent.ACTION_UP -> {
                    sendCommand("e0")
                    binding.eastButton.setBackgroundResource(R.color.light_bg)
                }
            }
            return@OnTouchListener true
        })

        binding.northWestButton.setOnTouchListener (View.OnTouchListener { view, motionEvent ->
            when (motionEvent.action){
                MotionEvent.ACTION_DOWN -> {
                    sendCommand("nw1")
                    binding.northWestButton.setBackgroundResource(R.color.dark_bg)
                }
                MotionEvent.ACTION_UP -> {
                    sendCommand("nw0")
                    binding.northWestButton.setBackgroundResource(R.color.light_bg)
                }
            }
            return@OnTouchListener true
        })

        binding.northEastButton.setOnTouchListener (View.OnTouchListener { view, motionEvent ->
            when (motionEvent.action){
                MotionEvent.ACTION_DOWN -> {
                    sendCommand("ne1")
                    binding.northEastButton.setBackgroundResource(R.color.dark_bg)
                }
                MotionEvent.ACTION_UP -> {
                    sendCommand("ne0")
                    binding.northEastButton.setBackgroundResource(R.color.light_bg)
                }
            }
            return@OnTouchListener true
        })

        binding.southWestButton.setOnTouchListener (View.OnTouchListener { view, motionEvent ->
            when (motionEvent.action){
                MotionEvent.ACTION_DOWN -> {
                    sendCommand("sw1")
                    binding.southWestButton.setBackgroundResource(R.color.dark_bg)
                }
                MotionEvent.ACTION_UP -> {
                    sendCommand("sw0")
                    binding.southWestButton.setBackgroundResource(R.color.light_bg)
                }
            }
            return@OnTouchListener true
        })

        binding.southEastButton.setOnTouchListener (View.OnTouchListener { view, motionEvent ->
            when (motionEvent.action){
                MotionEvent.ACTION_DOWN -> {
                    sendCommand("se1")
                    binding.southEastButton.setBackgroundResource(R.color.dark_bg)
                }
                MotionEvent.ACTION_UP -> {
                    sendCommand("se0")
                    binding.southEastButton.setBackgroundResource(R.color.light_bg)
                }
            }
            return@OnTouchListener true
        })


        binding.antiClockwiseButton.setOnTouchListener (View.OnTouchListener { view, motionEvent ->
            when (motionEvent.action){
                MotionEvent.ACTION_DOWN -> {
                    sendCommand("c1")
                    binding.antiClockwiseButton.setBackgroundResource(R.color.dark_bg)
                }
                MotionEvent.ACTION_UP -> {
                    sendCommand("c0")
                    binding.antiClockwiseButton.setBackgroundResource(R.color.light_bg)
                }
            }
            return@OnTouchListener true
        })

        binding.clockwiseButton.setOnTouchListener (View.OnTouchListener { view, motionEvent ->
            when (motionEvent.action){
                MotionEvent.ACTION_DOWN -> {
                    sendCommand("c1")
                    binding.clockwiseButton.setBackgroundResource(R.color.dark_bg)
                }
                MotionEvent.ACTION_UP -> {
                    sendCommand("c0")
                    binding.clockwiseButton.setBackgroundResource(R.color.light_bg)
                }
            }
            return@OnTouchListener true
        })
        binding.disconnect.setOnClickListener{ disconnect() }
    }

    // TODO: get the current state of the LED
//    private fun getState() : Boolean {
//        val buffer: ByteArray = ByteArray(1024)
//        mBluetoothSocket!!.inputStream.read(buffer)
//        return false
//    }

    private fun sendCommand(input: String)
    {
        if (mBluetoothSocket != null)
        {
            try {
                mBluetoothSocket!!.outputStream.write(input.toByteArray())
            }
            catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun disconnect()
    {
        if (mBluetoothSocket != null)
        {
            try {
                mBluetoothSocket!!.close()
                mBluetoothSocket = null
                mIsConnected = false
            }
            catch (e: IOException) {
                e.printStackTrace()
            }
        }
        finish()
    }

    private class ConnectToDevice(c: Context) : AsyncTask<Void, Void, String>() {
        private var connectSuccess: Boolean = true
        private var context: Context = c

        override fun onPreExecute() {
            super.onPreExecute()
            mProgress = ProgressDialog.show(context, "Connecting...", "please wait")
        }

        override fun doInBackground(vararg p0: Void?): String? {
            try {
                if (mBluetoothSocket == null || !mIsConnected)
                {
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                    val device: BluetoothDevice = mBluetoothAdapter.getRemoteDevice(mAddress)
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.

                    }
                    mBluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(mMyUUID)
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    mBluetoothSocket!!.connect()
                }
            }
            catch (e: IOException) {
                connectSuccess = false
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!connectSuccess)
                Log.i("data", "Couldn't connect!")
            else
                mIsConnected = true
            mProgress.dismiss()
        }
    }
}