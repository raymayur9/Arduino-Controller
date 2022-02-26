package com.example.arduinocontroller

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.LayoutInflater
import android.view.OnReceiveContentListener
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(
    private val list: ArrayList<BluetoothDevice>,
    private val listener: BluetoothDeviceClicked
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>(){

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView : TextView = view.findViewById(R.id.title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        val viewHolder = ItemViewHolder(adapterLayout)
        adapterLayout.setOnClickListener{
            listener.onDeviceClicked(list[viewHolder.adapterPosition].address)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val device = list[position]
        val address: String = device.address
        holder.textView.text = address
    }

    override fun getItemCount() = list.size
}

interface BluetoothDeviceClicked {
    fun onDeviceClicked(address: String)
}