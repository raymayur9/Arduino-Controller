package com.example.arduinocontroller

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
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import com.example.arduinocontroller.databinding.ActivityControlBinding
import java.io.IOException
import java.util.*

class ControlActivity : AppCompatActivity() {

    private lateinit var binding: ActivityControlBinding
    var ledOn = false

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

//        if (mIsConnected)
//            Toast.makeText(this, "Connection : Success!", Toast.LENGTH_LONG).show()
//        else
//            Toast.makeText(this, "Connection : Failed!", Toast.LENGTH_LONG).show()
        ledOn = getState()
        if (ledOn)
        {
            binding.imageView.setImageResource(R.drawable.ledon)
            binding.ledSwitch.setText(R.string.off)
        }
        else
        {
            binding.imageView.setImageResource(R.drawable.ledoff)
            binding.ledSwitch.setText(R.string.on)
        }
        binding.ledSwitch.setOnClickListener{
            if (ledOn)
            {
                sendCommand("b")
                ledOn = false
                binding.imageView.setImageResource(R.drawable.ledoff)
                binding.ledSwitch.setText(R.string.on)
            }
            else
            {
                sendCommand("a")
                ledOn = true
                binding.imageView.setImageResource(R.drawable.ledon)
                binding.ledSwitch.setText(R.string.off)
            }
        }
        binding.disconnect.setOnClickListener{ disconnect() }
    }

    // TODO: get the current state of the LED
    private fun getState() : Boolean {
//        val buffer: ByteArray = ByteArray(1024)
//        mBluetoothSocket!!.inputStream.read(buffer)
        return false
    }

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