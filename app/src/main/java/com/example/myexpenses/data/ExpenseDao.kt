package com.example.myexpenses.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY id DESC")
    fun getAll(): LiveData<List<Expense>>

    @Insert
    suspend fun insert(expense: Expense)
}