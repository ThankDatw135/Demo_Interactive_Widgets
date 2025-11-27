package com.example.yummydemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

// Màn hình chính: hiển thị danh sách món ăn và xử lý tương tác chạm + vuốt
class MainActivity : AppCompatActivity() {

    private lateinit var adapter: FoodAdapter      // Adapter để hiển thị danh sách món ăn
    private lateinit var recyclerView: RecyclerView // RecyclerView hiển thị danh sách dạng cuộn

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Gắn layout XML cho giao diện chính

        // Kết nối RecyclerView trong layout với code
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this) // Sắp xếp item theo cột dọc

        // 🔹 Tạo danh sách món ăn mẫu
        val foods = mutableListOf(
            Food("Pizza", "Bánh pizza phô mai thơm ngon", R.drawable.pizza),
            Food("Burger", "Hamburger bò Mỹ đặc biệt", R.drawable.burger),
            Food("Sushi", "Sushi cá hồi tươi", R.drawable.sushi),
            Food("Bún bò Huế", "Đậm đà hương vị miền Trung", R.drawable.bunbo),
            Food("Cơm tấm", "Món ăn truyền thống Việt Nam", R.drawable.comtam)
        )

        // 🔹 Gán adapter cho RecyclerView và xử lý sự kiện khi người dùng bấm vào món ăn
        adapter = FoodAdapter(foods) { food ->
            // Khi người dùng chạm vào một món, chuyển sang màn hình chi tiết món ăn
            val intent = Intent(this, FoodDetailActivity::class.java)
            intent.putExtra("food", food) // Gửi đối tượng Food sang Activity khác
            startActivity(intent)
        }

        recyclerView.adapter = adapter // Gắn adapter vào RecyclerView để hiển thị dữ liệu

        // 🔹 Tạo tính năng vuốt để xóa món ăn (Dismissible Widget)
        val swipeHelper = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            // Không xử lý kéo thả (drag & drop), nên trả về false
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            // Khi người dùng vuốt item sang trái hoặc phải
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.bindingAdapterPosition  // Lấy vị trí món bị vuốt
                val removed = adapter.deleteItem(pos)        // Xóa món khỏi danh sách

                // Hiển thị Snackbar để thông báo và cho phép hoàn tác
                Snackbar.make(recyclerView, "Đã xóa: ${removed.name}", Snackbar.LENGTH_LONG)
                    .setAction("Hoàn tác") { // Khi người dùng bấm "Hoàn tác"
                        foods.add(pos, removed)             // Thêm lại món vừa xóa
                        adapter.notifyItemInserted(pos)     // Cập nhật lại giao diện
                    }.show()
            }
        }

        // Gắn tính năng vuốt (swipeHelper) vào RecyclerView
        ItemTouchHelper(swipeHelper).attachToRecyclerView(recyclerView)
    }
}
