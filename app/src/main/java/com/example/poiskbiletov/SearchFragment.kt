package com.example.poiskbiletov

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.poiskbiletov.databinding.FragmentSearchBinding
import com.example.poiskbiletov.databinding.FragmentTicketsBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStreamReader


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TicketOffersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerViewOffers1
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        // Инициализация адаптера
        adapter = TicketOffersAdapter(emptyList())
        recyclerView.adapter = adapter
        fetchDataFromServer()

    }

        private fun fetchDataFromServer() {
            val apiClient = ApiClients(requireContext())
            GlobalScope.launch(Dispatchers.Main) {
                val response = apiClient.getOffersFromLocall()
                response?.let {
                    processResponse(it)
                }
            }
        }

        private fun processResponse(response: String) {
            try {
                val jsonObject = JSONObject(response)
                val ticketsArray = jsonObject.getJSONArray("tickets_offers")

                val offers = mutableListOf<TicketOffer>()
                for (i in 0 until ticketsArray.length()) {
                    val offerObject = ticketsArray.getJSONObject(i)
                    val id = offerObject.getInt("id")
                    val title = offerObject.getString("title")
                    val timeRangeArray = offerObject.getJSONArray("time_range")
                    val timeRangeList = mutableListOf<String>()
                    for (j in 0 until timeRangeArray.length()) {
                        val time = timeRangeArray.getString(j)
                        timeRangeList.add(time)
                    }
                    val priceObject = offerObject.getJSONObject("price")
                    val priceValue = priceObject.getDouble("value")

                    val price = Price(priceValue)
                    val ticketOffer = TicketOffer(id, title, timeRangeList, price)
                    offers.add(ticketOffer)
                }

                // Обновление списка в адаптере
                adapter.updateTicketOffers(offers)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


















