package com.example.yummydemo

// Import các thư viện cần thiết
import android.app.DatePickerDialog           // Hộp thoại chọn ngày giao hàng
import android.content.Intent                 // Dùng để chuyển màn hình (Activity)
import android.os.Bundle
import android.widget.*                       // Gồm EditText, Button, ImageButton, RadioGroup...
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*                            // Dùng để lấy thời gian hiện tại (ngày/tháng/năm)

// Màn hình giỏ hàng (CartActivity) – nơi người dùng nhập thông tin đặt món
class CartActivity : AppCompatActivity() {

    // Danh sách món trong giỏ hàng (mutableList có thể thêm/xóa được)
    private val cartItems = mutableListOf<Food>()

    // Khai báo Adapter để hiển thị danh sách món ăn trong RecyclerView
    private lateinit var adapter: FoodAdapter

    // Khai báo các view trong layout
    private lateinit var nameInput: EditText          // Ô nhập tên người nhận
    private lateinit var deliveryType: RadioGroup     // Nhóm nút chọn hình thức giao hàng
    private lateinit var btnPickDate: Button          // Nút chọn ngày giao hàng
    private lateinit var btnSubmit: Button            // Nút "Đặt hàng"
    private lateinit var btnBack: ImageButton         // Nút quay lại
    private var selectedDate: String = ""             // Biến lưu ngày giao hàng đã chọn

    // Hàm onCreate() chạy khi màn hình được khởi tạo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)        // Gắn layout XML cho màn hình giỏ hàng

        // Kết nối (findViewById) với các thành phần giao diện trong layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerCart)
        nameInput = findViewById(R.id.inputName)
        deliveryType = findViewById(R.id.radioDeliveryType)
        btnPickDate = findViewById(R.id.btnPickDate)
        btnSubmit = findViewById(R.id.btnSubmit)
        btnBack = findViewById(R.id.btnBackCart)

        // Nhận món ăn được truyền từ FoodDetailActivity sang (qua Intent)
        val newItem = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("food", Food::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("food") as? Food
        }

        // Nếu có món ăn được truyền qua, thêm vào danh sách giỏ hàng
        newItem?.let { cartItems.add(it) }

        // Thiết lập RecyclerView để hiển thị danh sách món
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = FoodAdapter(cartItems) { }           // Adapter hiển thị từng item món
        recyclerView.adapter = adapter

        // Khi bấm nút quay lại → trở về màn hình trước đó
        btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        // Khi người dùng bấm nút “Chọn ngày giao hàng”
        btnPickDate.setOnClickListener {
            val cal = Calendar.getInstance()           // Lấy ngày hiện tại làm mặc định
            DatePickerDialog(
                this,                                  // Ngữ cảnh hiện tại (Activity)
                { _, y, m, d ->                        // Khi người dùng chọn xong ngày
                    selectedDate = "$d/${m + 1}/$y"    // Lưu ngày được chọn
                    btnPickDate.text = "📅 Ngày giao: $selectedDate" // Cập nhật text nút
                },
                cal.get(Calendar.YEAR),                // Năm hiện tại
                cal.get(Calendar.MONTH),               // Tháng hiện tại
                cal.get(Calendar.DAY_OF_MONTH)         // Ngày hiện tại
            ).show()                                   // Hiển thị hộp thoại lịch
        }

        // Khi người dùng nhấn nút “Đặt hàng”
        btnSubmit.setOnClickListener {
            // Lấy tên người nhận từ ô nhập
            val name = nameInput.text.toString().trim()

            // Kiểm tra nếu tên bị bỏ trống → hiện thông báo Toast
            if (name.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên người nhận", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Kiểm tra nếu chưa chọn ngày giao → thông báo lỗi
            if (selectedDate.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn ngày giao", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Kiểm tra người dùng chọn hình thức giao hàng nào
            val orderType = when (deliveryType.checkedRadioButtonId) {
                R.id.radioDelivery -> "Giao hàng"
                R.id.radioPickup -> "Tự lấy"
                else -> "Không rõ"
            }

            // Tạo Intent để chuyển sang màn hình OrdersActivity
            val intent = Intent(this, OrdersActivity::class.java)
            // Gửi dữ liệu người dùng nhập qua màn hình mới
            intent.putExtra("name", name)
            intent.putExtra("date", selectedDate)
            intent.putExtra("type", orderType)
            // Chạy màn hình OrdersActivity để hiển thị kết quả
            startActivity(intent)
        }
    }
}
