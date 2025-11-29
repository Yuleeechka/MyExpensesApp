package com.example.myexpenses.data

class CategoryRepository(private val categoryDao: CategoryDao) {

    fun getAllCategories() = categoryDao.getAllCategories()

    suspend fun insert(category: Category) {
        categoryDao.insert(category)
    }
}
