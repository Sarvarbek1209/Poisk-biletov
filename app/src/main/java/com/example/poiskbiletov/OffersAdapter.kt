package com.example.poiskbiletov

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OffersAdapter(private var offersList: List<offer>) : RecyclerView.Adapter<OffersAdapter.OfferViewHolder>() {

    fun updateOffers(newOffers: List<offer>) {
        offersList = newOffers
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ragment_offers, parent, false)
        return OfferViewHolder(view)
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        val currentOffer = offersList[position]
        holder.bind(currentOffer)
    }

    override fun getItemCount(): Int = offersList.size

    class OfferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.title)
        private val town: TextView = itemView.findViewById(R.id.city)
        private val price: TextView = itemView.findViewById(R.id.price)
        private val image: ImageView = itemView.findViewById(R.id.image)

        fun bind(offer: offer) {
            title.text = offer.title
            town.text = offer.city
            price.text = "от ${String.format("%,d", offer.price.toInt())}₽" // Разделение между разрядами
            // Установите изображение по id объекта
            image.setImageResource(getImageResource(offer.id))
        }

        private fun getImageResource(id: Int): Int {
            return when (id) {
                1 -> R.drawable.ic_image
                2 -> R.drawable.ic_image1
                3 -> R.drawable.ic_image2
                else -> R.drawable.ic_image
            }
        }
    }
}
