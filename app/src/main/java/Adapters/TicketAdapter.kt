package Adapters

import Ticket
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.poiskbiletov.R

class TicketAdapter(private var ticketoff: List<Ticket>) : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val priceTextView: TextView = itemView.findViewById(R.id.price_value)
        private val travelTimeTextView: TextView = itemView.findViewById(R.id.travel_time)
        private val hasTransferTextView: TextView = itemView.findViewById(R.id.has_transfer)
        private val departureDateTextView: TextView = itemView.findViewById(R.id.departure_date)
        private val departureAirportTextView: TextView = itemView.findViewById(R.id.departure_airport)
        private val arrivalDateTextView: TextView = itemView.findViewById(R.id.arrival_date)
        private val arrivalAirportTextView: TextView = itemView.findViewById(R.id.arrival_airport)
        private val image:ImageView = itemView.findViewById(R.id.image2)

        fun bind(ticket: Ticket) {
            priceTextView.text = "${ticket.price.value}₽"
            departureDateTextView.text = formatDate(ticket.departure.date)
            arrivalDateTextView.text = formatDate(ticket.arrival.date)
            departureAirportTextView.text = ticket.departure.airport
            arrivalAirportTextView.text = ticket.arrival.airport
            travelTimeTextView.text = calculateFlightDuration(ticket.departure.date,ticket.arrival.date)
            hasTransferTextView.text = if (ticket.has_transfer) "" else "/без пересадок"
            image.setImageResource(R.drawable.rectangle2)
        }



        private fun formatDate(dateTime: String): String {
            return dateTime.substring(11, 16) // Extracts time in format HH:mm
        }

        private fun calculateFlightDuration(departureDateTime: String, arrivalDateTime: String): String {
            val departureHour = departureDateTime.substring(11, 13).toInt()
            val departureMinute = departureDateTime.substring(14, 16).toInt()
            val arrivalHour = arrivalDateTime.substring(11, 13).toInt()
            val arrivalMinute = arrivalDateTime.substring(14, 16).toInt()

            var hours = arrivalHour - departureHour
            var minutes = arrivalMinute - departureMinute

            if (minutes < 0) {
                hours -= 1
                minutes += 60
            }

            return "${hours}ч ${minutes}мин"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.ragment_offers3, parent, false)
        return TicketViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = ticketoff[position]
        holder.bind(ticket)

    }

    override fun getItemCount(): Int = ticketoff.size



}

