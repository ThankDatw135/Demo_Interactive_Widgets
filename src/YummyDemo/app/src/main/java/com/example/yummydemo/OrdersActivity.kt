package com.example.yummydemo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

// Màn hình hiển thị kết quả đặt hàng (xác nhận đơn)
class OrdersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders) // Gắn layout XML cho giao diện xác nhận đơn

        // 📨 Nhận dữ liệu được gửi từ CartActivity qua Intent
        val name = intent.getStringExtra("name")  // Tên người nhận
        val date = intent.getStringExtra("date")  // Ngày giao hàng
        val type = intent.getStringExtra("type")  // Hình thức giao hàng

        // Kết nối các view trong layout XML
        val txtSummary = findViewById<TextView>(R.id.txtSummary)  // Text hiển thị tóm tắt đơn hàng
        val btnBackCart = findViewById<Button>(R.id.btnBackCart)  // Nút quay lại giỏ hàng
        val btnHome = findViewById<Button>(R.id.btnHome)          // Nút quay về trang chính

        // 🧾 Hiển thị thông tin đơn hàng ra màn hình
        txtSummary.text = """
            ✅ Đơn hàng của bạn:
            👤 Người nhận: $name
            📅 Ngày giao: $date
            🚚 Hình thức: $type
        """.trimIndent()  // trimIndent giúp bỏ các khoảng trắng đầu dòng

        // 🔙 Khi người dùng bấm “Quay lại giỏ hàng”
        btnBackCart.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Quay về màn hình trước (CartActivity)
        }

        // 🏠 Khi người dùng bấm “Quay lại trang chính”
        btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java) // Tạo Intent về trang chính
            // FLAG_CLEAR_TOP: Xóa các Activity cũ khỏi stack
            // FLAG_NEW_TASK: Mở MainActivity như một tác vụ mới
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish() // Kết thúc Activity hiện tại để tránh quay lại
        }
    }
}
