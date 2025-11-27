package com.example.yummydemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adapter là cầu nối giữa dữ liệu (foodList) và RecyclerView (giao diện danh sách)
// Nó giúp hiển thị từng món ăn (Food) lên màn hình
class FoodAdapter(
    private val foodList: MutableList<Food>,         // Danh sách món ăn sẽ hiển thị
    private val onItemClick: (Food) -> Unit          // Hàm callback xử lý khi người dùng bấm vào 1 món
) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() { // Kế thừa Adapter của RecyclerView

    // ViewHolder đại diện cho 1 item (một dòng món ăn trong danh sách)
    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Khai báo các view con trong layout item_food.xml
        val foodName: TextView = itemView.findViewById(R.id.foodName)   // Tên món ăn
        val foodDesc: TextView = itemView.findViewById(R.id.foodDesc)   // Mô tả món ăn

        // init: Khối lệnh khởi tạo – chạy khi ViewHolder được tạo
        init {
            // Lắng nghe sự kiện khi người dùng bấm vào một item trong danh sách
            itemView.setOnClickListener {
                val pos = bindingAdapterPosition           // Lấy vị trí item hiện tại
                // Kiểm tra xem vị trí hợp lệ không (tránh lỗi khi bấm lúc đang xóa item)
                if (pos != RecyclerView.NO_POSITION) {
                    // Gọi hàm callback onItemClick và truyền đối tượng món ăn tương ứng
                    onItemClick(foodList[pos])
                }
            }
        }
    }

    // onCreateViewHolder: tạo mới 1 ViewHolder (một item) khi cần hiển thị
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        // Inflate layout item_food.xml để tạo giao diện cho 1 item món ăn
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food, parent, false)
        return FoodViewHolder(view) // Trả về ViewHolder mới
    }

    // onBindViewHolder: gán dữ liệu vào từng item (tên + mô tả món)
    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = foodList[position]           // Lấy món ăn ở vị trí tương ứng
        holder.foodName.text = food.name        // Gán tên món vào TextView
        holder.foodDesc.text = food.description // Gán mô tả món vào TextView
    }

    // getItemCount: trả về số lượng món ăn trong danh sách
    override fun getItemCount(): Int = foodList.size

    // Hàm xóa 1 món khỏi danh sách (khi người dùng vuốt để xóa)
    fun deleteItem(position: Int): Food {
        val removed = foodList.removeAt(position)  // Xóa món tại vị trí đó khỏi danh sách
        notifyItemRemoved(position)                // Cập nhật lại RecyclerView để hiển thị thay đổi
        return removed                             // Trả về món vừa bị xóa (để có thể hoàn tác)
    }
}
