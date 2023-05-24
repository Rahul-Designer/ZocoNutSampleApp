package com.example.zoconutsampleapp

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.zoconutsampleapp.data.Contact
import org.w3c.dom.Text

class ContactAdapter(private val context: Context, private val arrContact: ArrayList<Contact>) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var profileImage : ImageView
        var name : TextView
        var phoneNumber : TextView
        var city : TextView
        var country : TextView

        init {
            profileImage = itemView.findViewById(R.id.profile_img)
            name = itemView.findViewById(R.id.name)
            phoneNumber = itemView.findViewById(R.id.phone_number)
            city = itemView.findViewById(R.id.city)
            country = itemView.findViewById(R.id.country)
        }
        fun bind(info: Contact) {
            name.text = info.name
            phoneNumber.text = info.phoneNumber
            city.text = info.city
            country.text = info.country
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.contact_recyclerview_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pos = arrContact[position]
        Glide.with(context).load(pos.profileImage).into(holder.profileImage)
        holder.bind(pos)
    }

    override fun getItemCount(): Int {
        return arrContact.size
    }
}