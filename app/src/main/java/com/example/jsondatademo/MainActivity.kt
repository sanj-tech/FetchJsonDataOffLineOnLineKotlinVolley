package com.example.jsondatademo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.jsondatademo.Utility.ConnectivityUtil
import com.example.jsondatademo.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException

class MainActivity : AppCompatActivity() {
    val subCategoryDetailsList = mutableListOf<SubCategoryDetails>()
    private val adapter = SubCategoryAdapter(mutableListOf())

    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.rv)
        recyclerView.adapter = adapter

        //fetchDataFromApi() and populate recyclerview
        if (ConnectivityUtil.isNetworkConnected(this)) {
            Toast.makeText(this, "Online mode - Displaying data from Server", Toast.LENGTH_LONG).show()
            fetchDataFromApi()
        } else {

            // Fetch data from Room and populate RecyclerView
            displayDataFromRoom()
            Toast.makeText(this, "Offline mode - Displaying data from Room", Toast.LENGTH_LONG).show()

        }

    }

    private fun displayDataFromRoom() {
        val db = MyDatabase.getInstance(this)
        val subCategoriesOffline = db.subCategoryDetailsDao().getAllSubCategories()

        if (subCategoriesOffline.isNotEmpty()) {
            // Update subCategoryDetailsList with offline data from Room
            subCategoryDetailsList.clear()
            subCategoryDetailsList.addAll(subCategoriesOffline)
            adapter.updateData(subCategoriesOffline)

            // Data from Room is not empty
            val names = subCategoriesOffline.joinToString(separator = "\n") { it.name }
            runOnUiThread {

                Toast.makeText(this@MainActivity, "Names from Room:\n$names", Toast.LENGTH_LONG).show()
            }

        } else {

            // Data from Room is empty or not available
            runOnUiThread {
                Toast.makeText(
                    this@MainActivity,
                    "No data available in Room",
                    Toast.LENGTH_LONG
                ).show()
            }


        }
    }


    fun fetchDataFromApi() {

        val url =
            "http://vasundharaapps.com/artwork_apps/api/AdvertiseNewApplications/17/com.hd.camera.apps.high.quality"
        val requestQueue = Volley.newRequestQueue(this)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                try {
                    if (response.has("app_center")) {
                        val appCenterArray = response.getJSONArray("app_center")
                        for (i in 0 until appCenterArray.length()) {
                            val appCenterObject = appCenterArray.getJSONObject(i)
                            if (appCenterObject.has("sub_category")) {
                                val subCategoryArray =
                                    appCenterObject.getJSONArray("sub_category")
                                for (j in 0 until subCategoryArray.length()) {
                                    val subCategory = subCategoryArray.getJSONObject(j)
                                    val id = subCategory.getInt("id")
                                    val subName = subCategory.getString("name")
                                    val bannerImage = subCategory.getString("banner_image")

                                    if (bannerImage.isNotEmpty() && bannerImage != "null") {
                                        //this is for logcat
                                        // Process the subcategory name as needed
                                        Log.d("SUBCATEGORY_DETAILS", "Name: $subName")

                                        val subCategoryDetails =
                                            SubCategoryDetails(id, subName, bannerImage)
//this is for Room Database
                                        val db =
                                            MyDatabase.getInstance(applicationContext) // Get instance of your Room database
                                        GlobalScope.launch(Dispatchers.IO) {
                                            db.subCategoryDetailsDao()
                                                .insertAll(listOf(subCategoryDetails))
                                        }

                                        subCategoryDetailsList.add(subCategoryDetails)
                                    }
                                }
                            }
                        }
                        adapter.updateData(subCategoryDetailsList)

                    } else {
                        Log.e("API_ERROR", "No 'app_center' found in the response")
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },

            { error ->
                Log.e("API_ERROR", "Error fetching data: ${error.message}")
            })

        requestQueue.add(jsonObjectRequest)

    }


}












