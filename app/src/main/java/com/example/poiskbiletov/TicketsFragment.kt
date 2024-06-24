package com.example.poiskbiletov


import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.poiskbiletov.databinding.FragmentTicketsBinding
import org.json.JSONObject



class TicketsFragment : Fragment() {


    private lateinit var binding: FragmentTicketsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OffersAdapter

    private val PREFS_NAME = "MyPrefs"
    private val KEY_DEPARTURE = "departure"
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTicketsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerViewOffers
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = OffersAdapter(emptyList()) // Пока данные не загружены, используем пустой список
        recyclerView.adapter = adapter

        val apiClient = ApiClient(requireContext())
        val response = apiClient.getOffersFromLocal()

        response?.let {
            val offers = parseJson(it)
            adapter.updateOffers(offers)
        }
        binding.Text1.setOnClickListener {
            showCustomDialog()

        }

        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val lastDeparture = sharedPreferences.getString(KEY_DEPARTURE,  "Откуда - Москва")
        binding.tvDeparture.setText(lastDeparture)

        binding.tvDeparture.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            val regex = Regex("[а-яА-ЯёЁ ]*")
            if (source.matches(regex)){
                null
            }else{
                "sff"
            }
        })

        // Установите OnClickListener для TextView
        binding.tvDeparture.setOnClickListener {
            showInputDialog()
        }
        // Сохранение введенного значения при изменении текста
        binding.tvDeparture.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val departure = binding.tvDeparture.text.toString()
                saveDeparture(departure)
            }
        }



    }
    private fun showInputDialog(){
        val textView = EditText(requireContext())
        textView.filters = arrayOf(CyrillicInputFilter())

    }
    private fun saveDeparture(departure: String) {
        with(sharedPreferences.edit()) {
            putString(KEY_DEPARTURE, departure)
            apply()
        }
    }



    private fun parseJson(response: String): List<offer> {
        val offers = mutableListOf<offer>()
        val jsonObject = JSONObject(response)
        val jsonArray = jsonObject.getJSONArray("offers")

        for (i in 0 until jsonArray.length()) {
            val offerObject = jsonArray.getJSONObject(i)
            val offer = offer(
                offerObject.getInt("id"),
                offerObject.getString("title"),
                offerObject.getString("town"),
                offerObject.getJSONObject("price").getDouble("value")
            )
            offers.add(offer)
        }

        return offers
    }

     private fun showCustomDialog(){
         val dialog = Dialog(requireContext())
         dialog.setContentView(R.layout.fragment_suggestions_dialog)
         dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

         val button_close = dialog.findViewById<ImageButton>(R.id.button_close)
         button_close.setOnClickListener {
             dialog.dismiss()
         }

         val phuket = dialog.findViewById<TextView>(R.id.Phuket)
         val popular3 = dialog.findViewById<TextView>(R.id.popular3)
         phuket.setOnClickListener {
             popular3.setText(phuket.text,)
             phuket.visibility=View.GONE
         }

         val sochi = dialog.findViewById<TextView>(R.id.Sochi)
         val popular2 = dialog.findViewById<TextView>(R.id.popular2)
         sochi.setOnClickListener {
             popular2.setText(sochi.text,)
             sochi.visibility=View.GONE
         }


         val stambul = dialog.findViewById<TextView>(R.id.Stambul)
         val popular1 = dialog.findViewById<TextView>(R.id.popular1)
         stambul.setOnClickListener {
             popular1.setText(stambul.text,)
             stambul.visibility=View.GONE
         }

         val textViewSuggestion1 = dialog.findViewById<LinearLayout>(R.id.textViewSuggestion1)
         textViewSuggestion1.setOnClickListener {
             dialog.dismiss() // Закрыть диалог при нажатии на TextView
             Log.d("SuggestionsDialogFragment", "TextView clicked")
         }
         val textViewSuggestion2 = dialog.findViewById<LinearLayout>(R.id.textViewSuggestion2)
         textViewSuggestion2.setOnClickListener {
              val editText = dialog.findViewById<EditText>(R.id.Edit_text2)
               editText.setText("Куда угодно")

         }
         val editText = dialog.findViewById<EditText>(R.id.Edit_text2)
             editText.addTextChangedListener{text ->
                 if (!text.isNullOrEmpty()){
                     val fragment = SearchFragment()
                     parentFragmentManager.beginTransaction()
                         .replace(R.id.fragment_container, fragment) //
                         .addToBackStack(null)
                         .commit()

                     // Закройте диалоговое окно
                     dialog.dismiss()


                 }
             }




         val textViewSuggestion3 = dialog.findViewById<LinearLayout>(R.id.textViewSuggestion3)
         textViewSuggestion3.setOnClickListener {
             dialog.dismiss() // Закрыть диалог при нажатии на TextView
             Log.d("SuggestionsDialogFragment", "TextView clicked")
         }

         val textViewSuggestion4 = dialog.findViewById<LinearLayout>(R.id.textViewSuggestion4)
         textViewSuggestion4.setOnClickListener {
             dialog.dismiss() // Закрыть диалог при нажатии на TextView
             Log.d("SuggestionsDialogFragment", "TextView clicked")
         }

         dialog.show()

     }

}


