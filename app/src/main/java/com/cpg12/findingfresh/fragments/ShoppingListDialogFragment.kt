package com.cpg12.findingfresh.fragments

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.cpg12.findingfresh.R
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class ShoppingListDialogFragment: DialogFragment() {

    var listItemName = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.dialog_nutrition, container, false)
        listItemName = arguments?.getString("TEXT").toString()
        //println(listItemName)
        //fetchJSON()

        val caloriesTV = view.findViewById<TextView>(R.id.caloriesTV)
        val servingTV = view.findViewById<TextView>(R.id.servingTV)
        val totalFatTV = view.findViewById<TextView>(R.id.totalFatTV)
        val cholesterolTV = view.findViewById<TextView>(R.id.cholesterolTV)
        val sodiumTV = view.findViewById<TextView>(R.id.sodiumTV)
        val carbohydratesTV = view.findViewById<TextView>(R.id.carbohydratesTV)
        val proteinTV = view.findViewById<TextView>(R.id.proteinTV)
        val potassiumTV = view.findViewById<TextView>(R.id.potassiumTV)

        val nutritionixIV = view.findViewById<ImageView>(R.id.nutritionixIV)

        nutritionixIV.setOnClickListener {
            val url = "https://www.nutritionix.com/business/api"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }






        val client = OkHttpClient()
        val mediaType = "application/x-www-form-urlencoded".toMediaTypeOrNull()
        val body = RequestBody.create(mediaType, "query=${listItemName}")

        val request = Request.Builder()
            .url("https://trackapi.nutritionix.com/v2/natural/nutrients")
            .method("POST", body)
            .header("User-Agent", "OkHttp Headers.java")
            .addHeader("x-app-id", "a875d480")
            .addHeader("x-app-key", "02427e40a05205e9ae38fd4dae1ab4a3")
            .addHeader("content", "application/json")
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .build()


        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }

            override fun onResponse(call: Call, response: Response) {
                val bodys = response.body?.string()
                println(bodys)

                val response = JSONObject(bodys)
                val Jarray: JSONArray = response.getJSONArray("foods")
                for (i in 0 until response.length()) {
                    val jsonobject: JSONObject = Jarray.getJSONObject(i)

                    val foodName = jsonobject.getString("food_name")
                    val foodQty = jsonobject.getString("serving_qty").toString()
                    val foodUnit = jsonobject.getString("serving_unit").toString()
                    val foodCalories = jsonobject.getString("nf_calories").toString()
                    val foodFats = jsonobject.getString("nf_total_fat").toString()
                    val foodCarbohydrates = jsonobject.getString("nf_total_carbohydrate").toString()
                    val foodProteins = jsonobject.getString("nf_protein").toString()
                    val foodSodium = jsonobject.getString("nf_sodium").toString()
                    val foodPotassium = jsonobject.getString("nf_potassium").toString()
                    val foodCholesterol = jsonobject.getString("nf_cholesterol").toString()
                    println(foodName)

                    lifecycleScope.launch(Main){
                        caloriesTV.text = "Calories: ${foodCalories}"
                        servingTV.text = "Amount Per Serving (${foodQty} ${foodUnit} of ${foodName})"
                        totalFatTV.text = "Total Fat: ${foodFats}g"
                        cholesterolTV.text = "Cholesterol: ${foodCholesterol}mg"
                        sodiumTV.text = "Sodium: ${foodSodium}mg"
                        potassiumTV.text = "Potassium: ${foodPotassium}mg"
                        carbohydratesTV.text = "Total Carbohydrates: ${foodCarbohydrates}g"
                        proteinTV.text = "Protein: ${foodProteins}g"
                    }
                }
            }
        })

















        return view
    }

    /*private fun fetchJSON() {
        val client = OkHttpClient()
        val mediaType = "application/x-www-form-urlencoded".toMediaTypeOrNull()
        val body = RequestBody.create(mediaType, "query=${listItemName}")

        val request = Request.Builder()
            .url("https://trackapi.nutritionix.com/v2/natural/nutrients")
            .method("POST", body)
            .header("User-Agent", "OkHttp Headers.java")
            .addHeader("x-app-id", "a875d480")
            .addHeader("x-app-key", "02427e40a05205e9ae38fd4dae1ab4a3")
            .addHeader("content", "application/json")
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .build()

        lifecycleScope.launch(IO){
            val response = client.newCall(request).execute()
            val bodys = response.body
            if (bodys != null) {
                println(bodys.string())
            }

        try{
            val food = JSONObject(bodys?.string()).getJSONObject("foods")
            val foodName = food.getString("food_name").toString()
            val foodQty = food.getString("serving_qty").toString()
            val foodUnit = food.getString("serving_unit").toString()
            val foodCalories = food.getString("nf_calories").toString()
            val foodFats = food.getString("nf_total_fat").toString()
            val foodCarbohydrates = food.getString("nf_total_carbohydrates").toString()
            val foodProteins = food.getString("nf_protein").toString()
            val foodSodium = food.getString("nf_sodium").toString()
            val foodPotassium = food.getString("nf_potassium").toString()
            val foodCholesterol = food.getString("nf_cholesterol").toString()

        } catch (e: JSONException){
            e.printStackTrace()
        }
        }
    }*/

}