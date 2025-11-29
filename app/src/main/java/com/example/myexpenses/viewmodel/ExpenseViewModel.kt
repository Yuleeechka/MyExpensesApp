package com.example.myexpenses.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myexpenses.data.Expense
import com.example.myexpenses.data.ExpenseRepository
import kotlinx.coroutines.launch

class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {

    val allExpenses = repository.allExpenses

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            repository.insert(expense)
        }
    }
}
