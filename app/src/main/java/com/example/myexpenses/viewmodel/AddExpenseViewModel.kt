package com.example.myexpenses.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import androidx.lifecycle.asLiveData
import com.example.myexpenses.data.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddExpenseViewModel(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    val categories: LiveData<List<Category>> = categoryRepository.getAllCategories().asLiveData()

    @RequiresApi(Build.VERSION_CODES.O)
    fun addExpense(description: String, amount: Double, categoryId: Int) {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        val date = now.format(formatter)

        val expense = Expense(
            description = description,
            amount = amount,
            date = date,
            categoryId = categoryId
        )

        viewModelScope.launch {
            expenseRepository.insert(expense)
        }
    }

    fun addCategory(name: String) {
        viewModelScope.launch {
            categoryRepository.insert(Category(name = name))
        }
    }
}
