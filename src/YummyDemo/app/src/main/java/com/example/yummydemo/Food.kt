package com.example.yummydemo

import java.io.Serializable   // Import giao diện Serializable để cho phép truyền dữ liệu giữa các màn hình (Intent)

// Lớp dữ liệu (data class) biểu diễn một món ăn
// Dùng để lưu trữ thông tin cơ bản của từng món trong danh sách
data class Food(
    val name: String,         // Tên món ăn (ví dụ: "Pizza", "Burger")
    val description: String,  // Mô tả chi tiết về món ăn
    val imageResId: Int       // ID của hình ảnh món (liên kết tới file ảnh trong thư mục res/drawable)
) : Serializable              // Kế thừa Serializable để có thể “đóng gói” dữ liệu này gửi qua Intent
