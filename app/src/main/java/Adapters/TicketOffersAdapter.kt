package Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.poiskbiletov.R

import TicketOffer

class TicketOffersAdapter(private var ticketOffersList: List<TicketOffer>) : RecyclerView.Adapter<TicketOffersAdapter.TicketOfferViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketOfferViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ragment_offers1, parent, false)
        return TicketOfferViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketOfferViewHolder, position: Int) {
        val currentTicketOffer = ticketOffersList[position]
        holder.bind(currentTicketOffer)
    }

    override fun getItemCount(): Int = ticketOffersList.size

    class TicketOfferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.Name_avia)
        private val timeRange: TextView = itemView.findViewById(R.id.time)
        private val price: TextView = itemView.findViewById(R.id.price)
        private val image: ImageView = itemView.findViewById(R.id.image1)

        fun bind(ticketOffer: TicketOffer) {
            title.text = ticketOffer.title
            timeRange.text = ticketOffer.timeRange.joinToString(", ")  // Преобразование списка времени в строку
            price.text = "от ${String.format("%,d", ticketOffer.price.toInt())}₽" // Разделение между разрядами
            // Установите изображение по id объекта
            image.setImageResource(getImageResource(ticketOffer.id))
        }

        private fun getImageResource(id: Int): Int {
            return when (id) {
                1 -> R.drawable.rectangle
                10 -> R.drawable.rectangle1
                30 -> R.drawable.rectangle2
                else -> R.drawable.rectangle
            }
        }
    }
}
