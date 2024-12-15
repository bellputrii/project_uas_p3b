package com.bell.gorasa

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_data)

        // Mengambil referensi ke widget
        val nameFoodTextView: TextView = findViewById(R.id.tv_dtl_name_food)
        val priceFoodTextView: TextView = findViewById(R.id.tv_dtl_price_food)
        val descriptionFoodTextView: TextView = findViewById(R.id.tv_dtl_description_food)
        val backButton: AppCompatButton = findViewById(R.id.btn_back)
        val foodImageView: ImageView = findViewById(R.id.img_food_preview)

        // Mengambil data dari intent
        val foodName = intent.getStringExtra("food_name")
        val foodPrice = intent.getStringExtra("food_price")
        val foodDescription = intent.getStringExtra("food_description")
        val foodImageResId = intent.getIntExtra("food_image", R.drawable.food) // Default image

        // Menampilkan data pada views
        nameFoodTextView.text = foodName
        priceFoodTextView.text = foodPrice
        descriptionFoodTextView.text = foodDescription
        foodImageView.setImageResource(foodImageResId)

        // Menambahkan action ketika tombol kembali ditekan
        backButton.setOnClickListener {
            finish()  // Kembali ke activity sebelumnya
        }
    }
}
