package com.example.yummydemo

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

// Màn hình hiển thị chi tiết món ăn khi người dùng bấm vào 1 món trong danh sách
class FoodDetailActivity : AppCompatActivity() {

    private var quantity = 1   // Biến lưu số lượng món (mặc định = 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_detail) // Gắn layout XML cho màn hình chi tiết món

        // Nhận dữ liệu món ăn được truyền từ MainActivity qua (dạng Serializable)
        @Suppress("DEPRECATION")
        val food = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("food", Food::class.java)
        } else {
            intent.getSerializableExtra("food") as? Food
        }

        // Kết nối các view trong layout với code
        val imageView = findViewById<ImageView>(R.id.foodImage)      // Ảnh món ăn
        val nameView = findViewById<TextView>(R.id.foodName)         // Tên món
        val descView = findViewById<TextView>(R.id.foodDesc)         // Mô tả món
        val btnBack = findViewById<ImageButton>(R.id.btnBack)        // Nút quay lại
        val btnMinus = findViewById<Button>(R.id.btnMinus)           // Nút “–” giảm số lượng
        val btnPlus = findViewById<Button>(R.id.btnPlus)             // Nút “+” tăng số lượng
        val txtQuantity = findViewById<TextView>(R.id.txtQuantity)   // Hiển thị số lượng hiện tại
        val btnAdd = findViewById<Button>(R.id.btnAddCart)           // Nút “Thêm vào giỏ hàng”

        // Hiển thị dữ liệu món ăn lên giao diện
        imageView.setImageResource(food?.imageResId ?: 0)
        nameView.text = food?.name
        descView.text = food?.description

        // Xử lý sự kiện nút quay lại → trở về màn hình trước
        btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        // Khi người dùng nhấn nút “–”
        btnMinus.setOnClickListener {
            if (quantity > 1) {              // Chỉ giảm nếu số lượng > 1 (không âm)
                quantity--                   // Giảm 1 đơn vị
                txtQuantity.text = quantity.toString() // Cập nhật giao diện
            }
        }

        // Khi người dùng nhấn nút “+”
        btnPlus.setOnClickListener {
            quantity++                       // Tăng 1 đơn vị
            txtQuantity.text = quantity.toString() // Hiển thị lại số lượng mới
        }

        // Khi nhấn nút “Thêm vào giỏ hàng”
        btnAdd.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java) // Tạo Intent để mở màn hình giỏ hàng
            intent.putExtra("food", food)                       // Gửi món ăn được chọn sang
            startActivity(intent)                               // Mở CartActivity
        }
    }
}
