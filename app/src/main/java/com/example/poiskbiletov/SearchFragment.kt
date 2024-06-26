package com.example.poiskbiletov

import Adapters.TicketAdapter
import Adapters.TicketOffersAdapter
import TicketOffer
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.poiskbiletov.databinding.FragmentSearchBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale


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
        fetchOffers()

        binding.imageButton1.setOnClickListener {
            val editTextValue = binding.EditText2.text.toString()
            val textViewValue = binding.tvDeparture.text.toString()

            // Поменять местами тексты
            binding.EditText2.setText(textViewValue)
            binding.tvDeparture.setText(editTextValue)
        }

        val calendar = Calendar.getInstance()
        val dataFormat = SimpleDateFormat("dd MMM, E", Locale.getDefault())

        val dataText1 = view.findViewById<TextView>(R.id.data_Text1)

        binding.dataText1.text = dataFormat.format(calendar.time)
        binding.dataOtp.setOnClickListener {

            val datePickerDialog = DatePickerDialog(
                requireActivity(),{_, year, monthOfYear, dayOfMonth ->
                    // Установка выбранной даты в календарь
                    calendar.set(year, monthOfYear, dayOfMonth)
                    // Обновление текста в TextView
                    dataText1.text = dataFormat.format(calendar.time)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
            // Показать диалоговое окно выбора даты
            datePickerDialog.show()
        }

        binding.vseBileti.setOnClickListener {

            val fragment = SearchTicketsFragment()
            val activity = requireActivity()

            if (activity != null) {
                // Получаем менеджер фрагментов
                val fragmentManager = activity.supportFragmentManager
                // Начинаем транзакцию
                val fragmentTransaction = fragmentManager.beginTransaction()
                // Заменяем текущий фрагмент на SearchTicketsFragment
                fragmentTransaction.replace(R.id.fragment_container, fragment)
                // Добавляем транзакцию в back stack, чтобы пользователь мог вернуться назад
                fragmentTransaction.addToBackStack(null)
                // Завершаем транзакцию
                fragmentTransaction.commit()
            } else {
                // Обработка случая, когда активность равна null
                Log.e("FragmentTransaction", "Activity is null")
            }

        }

    }

    private fun fetchOffers() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://run.mocky.io/v3/38b5205d-1a3d-4c2f-9d77-2f9d1ef01a4a")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { responseBody ->
                    val jsonString = responseBody.string()
                    val offers = parseJson(jsonString)

                    activity?.runOnUiThread {
                        updateUII(offers)
                    }
                }
            }
        })
    }
    private fun updateUII(offers: List<TicketOffer>) {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_view_offers1)
        recyclerView?.adapter = TicketOffersAdapter(offers)
    }


    private fun parseJson(response: String): List<TicketOffer> {
        val offers = mutableListOf<TicketOffer>()
        val jsonObject = JSONObject(response)
        val jsonArray = jsonObject.getJSONArray("tickets_offers")

        for (i in 0 until jsonArray.length()) {
            val offerObject = jsonArray.getJSONObject(i)
            val timeRangeArray = offerObject.getJSONArray("time_range")
            val timeRangeList = mutableListOf<String>()

            for (j in 0 until timeRangeArray.length()) {
                timeRangeList.add(timeRangeArray.getString(j))
            }

            val offer = TicketOffer(
                offerObject.getInt("id"),
                offerObject.getString("title"),
                timeRangeList,
                offerObject.getJSONObject("price").getDouble("value")
            )
            offers.add(offer)
        }

        return offers
    }


}


















