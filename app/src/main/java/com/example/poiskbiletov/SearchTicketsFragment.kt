package com.example.poiskbiletov

import Adapters.TicketAdapter
import Arrival
import Departure
import HandLuggage
import Luggage

import Price
import Ticket
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.poiskbiletov.databinding.FragmentSearchTicketsBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import org.json.JSONObject


class SearchTicketsFragment : Fragment() {
     private lateinit var binding: FragmentSearchTicketsBinding
     private lateinit var recyclerView: RecyclerView
     private lateinit var adapter: TicketAdapter

    private val client = OkHttpClient()
    private val mainHandler = Handler(Looper.getMainLooper())




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchTicketsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerViewOffers3
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        // Инициализация адаптера
        adapter = TicketAdapter(emptyList())
        recyclerView.adapter = adapter
        fetchTickets()

    }

    private fun fetchTickets() {
        val request = Request.Builder()
            .url("https://run.mocky.io/v3/c0464573-5a13-45c9-89f8-717436748b69")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { responseBody ->
                    val jsonString = responseBody.string()
                    val tickets = parseTicketsJson(jsonString)

                    activity?.runOnUiThread {
                        updateTicketsUI(tickets)
                    }
                }
            }
        })
    }
    private fun updateTicketsUI(tickets: List<Ticket>) {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_view_offers3)
        val adapter = TicketAdapter(tickets)
        recyclerView?.adapter = adapter
    }


    private fun parseTicketsJson(response: String): List<Ticket> {
        val tickets = mutableListOf<Ticket>()
        val jsonObject = JSONObject(response)
        val jsonArray = jsonObject.getJSONArray("tickets")

        for (i in 0 until jsonArray.length()) {
            val ticketObject = jsonArray.getJSONObject(i)
            val ticket = Ticket(
                id = ticketObject.getInt("id"),
                badge = ticketObject.optString("badge"),
                price = Price(ticketObject.getJSONObject("price").getDouble("value")),
                provider_name = ticketObject.getString("provider_name"),
                company = ticketObject.getString("company"),
                departure = Departure(
                    town = ticketObject.getJSONObject("departure").getString("town"),
                    date = ticketObject.getJSONObject("departure").getString("date"),
                    airport = ticketObject.getJSONObject("departure").getString("airport")
                ),
                arrival = Arrival(
                    town = ticketObject.getJSONObject("arrival").getString("town"),
                    date = ticketObject.getJSONObject("arrival").getString("date"),
                    airport = ticketObject.getJSONObject("arrival").getString("airport")
                ),
                has_transfer = ticketObject.getBoolean("has_transfer"),
                has_visa_transfer = ticketObject.getBoolean("has_visa_transfer"),
                luggage = Luggage(
                    has_luggage = ticketObject.getJSONObject("luggage").getBoolean("has_luggage"),
                    price = ticketObject.getJSONObject("luggage").optJSONObject("price")?.let {
                        Price(it.getDouble("value"))
                    }
                ),
                hand_luggage = HandLuggage(
                    has_hand_luggage = ticketObject.getJSONObject("hand_luggage").getBoolean("has_hand_luggage"),
                    size = ticketObject.getJSONObject("hand_luggage").optString("size")
                ),
                is_returnable = ticketObject.getBoolean("is_returnable"),
                is_exchangable = ticketObject.getBoolean("is_exchangable")
            )
            tickets.add(ticket)
        }

        return tickets
    }




}