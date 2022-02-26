package com.example.arduinocontroller

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.arduinocontroller.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), BluetoothDeviceClicked {

    private lateinit var binding: ActivityMainBinding
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private lateinit var mPairedDevices: Set<BluetoothDevice>
//    private val REQUEST_ENABLE_BLUETOOTH = 1

    companion object{
        const val EXTRA_ADDRESS: String = "Device Address"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter == null)
        {
            Toast.makeText(this, "This device doesn't support Bluetooth!", Toast.LENGTH_SHORT).show()
            return
        }
        if (!mBluetoothAdapter!!.isEnabled) {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
            startForResult.launch(enableBluetoothIntent)
        }
        pairedDeviceList()
        binding.refresh.setOnClickListener{ pairedDeviceList() }
    }

    private fun pairedDeviceList() {
        if (ActivityCompat.checkSelfPermission(
                this,
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
            if (!mBluetoothAdapter!!.isEnabled) {
                val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
                startForResult.launch(enableBluetoothIntent)
            }
        }
        mPairedDevices = mBluetoothAdapter!!.bondedDevices
        val list: ArrayList<BluetoothDevice> = ArrayList()

        if (mPairedDevices.isNotEmpty())
        {
            for (device in mPairedDevices)
            {
                list.add(device)
                Log.i("device", device.toString())
            }
        }
        else
        {
            Toast.makeText(this, "No paired Bluetooth devices found!", Toast.LENGTH_SHORT).show()
        }

        val adapter = ItemAdapter(list, this)
        binding.deviceList.adapter = adapter

    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK)
            {
                if (mBluetoothAdapter!!.isEnabled)
                    Toast.makeText(this, "Bluetooth has been enabled!", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this, "Bluetooth has been disabled!", Toast.LENGTH_SHORT).show()
            }
            else if (it.resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Bluetooth enabling has been canceled!", Toast.LENGTH_SHORT).show()
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_ENABLE_BLUETOOTH)
//        {
//            if (resultCode == Activity.RESULT_OK)
//            {
//                if (mBluetoothAdapter!!.isEnabled)
//                    Toast.makeText(this, "Bluetooth has been enabled!", Toast.LENGTH_SHORT).show()
//                else
//                    Toast.makeText(this, "Bluetooth has been disabled!", Toast.LENGTH_SHORT).show()
//            }
//            else if (resultCode == Activity.RESULT_CANCELED)
//                Toast.makeText(this, "Bluetooth enabling has been canceled!", Toast.LENGTH_LONG).show()
//        }
//    }

    override fun onDeviceClicked(address: String) {
        val intent = Intent(this, ControlActivity::class.java)
        intent.putExtra(EXTRA_ADDRESS, address)
        startActivity(intent)
    }
}