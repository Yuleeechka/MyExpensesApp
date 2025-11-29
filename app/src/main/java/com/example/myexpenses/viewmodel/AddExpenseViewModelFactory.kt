package com.example.myexpenses.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myexpenses.data.CategoryRepository
import com.example.myexpenses.data.ExpenseRepository

class AddExpenseViewModelFactory(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddExpenseViewModel(expenseRepository, categoryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
