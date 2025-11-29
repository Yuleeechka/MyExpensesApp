package com.example.myexpenses.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
public interface CategoryDao {

    @Insert
    suspend fun insert(category: Category)

    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<Category>
}