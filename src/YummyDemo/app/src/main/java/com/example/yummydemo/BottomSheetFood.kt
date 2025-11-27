package com.example.yummydemo

// Import các thư viện cần thiết
import android.app.DatePickerDialog          // Hộp thoại chọn ngày
import android.os.Bundle                    // Lưu và nhận dữ liệu giữa các màn hình
import android.view.LayoutInflater           // Dùng để “thổi phồng” (inflate) layout XML thành View
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment // Thư viện Material Design để tạo Bottom Sheet
import java.util.Calendar                    // Lấy thời gian hiện tại (ngày/tháng/năm)

// Lớp BottomSheetFood kế thừa BottomSheetDialogFragment
// => Dùng để hiển thị bảng trượt từ dưới lên, thường dùng hiển thị chi tiết hoặc tùy chọn nhanh
class BottomSheetFood : BottomSheetDialogFragment() {

    // Khai báo biến food kiểu Food để chứa dữ liệu món ăn được truyền vào
    private lateinit var food: Food

    // Hàm onCreate() chạy đầu tiên khi fragment được tạo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Lấy dữ liệu được truyền qua khi tạo fragment (từ newInstance)
        arguments?.let {
            // Gán dữ liệu vào biến food
            food = Food(
                it.getString("name") ?: "",   // Lấy tên món, nếu null thì dùng chuỗi rỗng
                it.getString("desc") ?: "",   // Lấy mô tả món
                it.getInt("imageResId")       // Lấy hình ảnh món
            )
        }
    }

    // Hàm onCreateView() – tạo và trả về giao diện hiển thị của Bottom Sheet
    override fun onCreateView(
        inflater: LayoutInflater,            // Dùng để đọc layout XML
        container: ViewGroup?,               // View cha chứa fragment
        savedInstanceState: Bundle?          // Dữ liệu lưu trạng thái (nếu có)
    ): View {
        // Gắn layout XML (bottom_sheet_food.xml) vào fragment
        val view = inflater.inflate(R.layout.bottom_sheet_food, container, false)

        // Kết nối các thành phần trong layout với code (findViewById)
        val name = view.findViewById<TextView>(R.id.foodDetailName)   // Tên món
        val desc = view.findViewById<TextView>(R.id.foodDetailDesc)   // Mô tả món
        val image = view.findViewById<ImageView>(R.id.foodDetailImage) // Hình món
        val btnAdd = view.findViewById<Button>(R.id.btnAddCart)       // Nút “Thêm vào giỏ hàng”
        val btnPickDate = view.findViewById<Button>(R.id.btnPickDate) // Nút “Chọn ngày giao”

        // Hiển thị dữ liệu món ăn lên giao diện
        name.text = food.name
        desc.text = food.description
        image.setImageResource(food.imageResId)

        // Khi người dùng nhấn nút “Thêm vào giỏ hàng”
        btnAdd.setOnClickListener {
            dismiss()   // Đóng Bottom Sheet lại
        }

        // Khi người dùng nhấn nút “Chọn ngày giao”
        btnPickDate.setOnClickListener {
            val calendar = Calendar.getInstance()  // Lấy ngày hiện tại
            // Tạo hộp thoại DatePickerDialog để chọn ngày
            val datePicker = DatePickerDialog(
                requireContext(),                   // Ngữ cảnh hiện tại (context của fragment)
                { _, year, month, day ->            // Khi người dùng chọn xong ngày
                    // Cập nhật text của nút thành “Ngày giao: dd/MM/yyyy”
                    btnPickDate.text = "Ngày giao: $day/${month + 1}/$year"
                },
                // Thiết lập ngày mặc định trong lịch là ngày hôm nay
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()   // Hiển thị hộp thoại lịch
        }

        // Trả về View hiển thị cho Bottom Sheet
        return view
    }

    // Companion object – giống như "hàm tạo nhanh" của Fragment
    // Giúp tạo một instance mới của BottomSheetFood và truyền dữ liệu món ăn vào
    companion object {
        fun newInstance(food: Food): BottomSheetFood {
            val fragment = BottomSheetFood()   // Tạo đối tượng BottomSheetFood mới
            val args = Bundle()                // Dùng Bundle để đóng gói dữ liệu gửi qua fragment
            args.putString("name", food.name)  // Gửi tên món
            args.putString("desc", food.description) // Gửi mô tả
            args.putInt("imageResId", food.imageResId) // Gửi hình ảnh
            fragment.arguments = args          // Gắn bundle này vào fragment
            return fragment                    // Trả về fragment kèm dữ liệu
        }
    }
}
