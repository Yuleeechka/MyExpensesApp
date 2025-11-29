package com.example.myexpenses.data

class ExpenseRepository(private val expenseDao: ExpenseDao) {

    val allExpenses = expenseDao.getAll()

    suspend fun insert(expense: Expense) {
        expenseDao.insert(expense)
    }
}
