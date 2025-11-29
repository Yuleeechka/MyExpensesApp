package com.example.myexpenses

import android.R
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myexpenses.data.Expense
import com.example.myexpenses.data.ExpenseDatabase
import com.example.myexpenses.databinding.ActivityAddExpenseBinding
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.example.myexpenses.data.Category
import com.example.myexpenses.data.CategoryDao


class AddExpenseActivity : AppCompatActivity() {


    private lateinit var binding: ActivityAddExpenseBinding
    private lateinit var db: ExpenseDatabase
    private lateinit var categoryDao: CategoryDao
    private lateinit var categories: MutableList<Category>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = ExpenseDatabase.getDatabase(this)
        categoryDao = db.categoryDao()

        loadCategories()

        val dao = ExpenseDatabase.getDatabase(this).expenseDao()

        binding.buttonAddCategory.setOnClickListener {
            showAddCategoryDialog()
        }


        binding.saveButton.setOnClickListener {
            val description = binding.editDescription.text.toString().take(100).trim()
            val amount = binding.editAmount.text.toString().toDoubleOrNull()
            val selectedCategoryPosition = binding.spinnerCategory.selectedItemPosition
            val categoryId = categories[selectedCategoryPosition].id

            if (description.isBlank() || amount == null) {
                Toast.makeText(this, "Заполните оба поля корректно", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val now = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
            val dateTimeString = now.format(formatter)

            val date = dateTimeString

            val expense = Expense(
                description = description,
                amount = amount,
                date = date,
                categoryId = categoryId   // ← вот оно!
            )

            lifecycleScope.launch {
                dao.insert(Expense(description = description, amount = amount, date = date, categoryId = categoryId))
                finish()
            }
        }
    }
    private fun loadCategories() {
        lifecycleScope.launch {
            categories = categoryDao.getAllCategories().toMutableList()

            val names = categories.map { it.name }

            val adapter = ArrayAdapter(
                this@AddExpenseActivity,
                R.layout.simple_spinner_item,
                names
            )

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerCategory.adapter = adapter
        }
    }
    private fun showAddCategoryDialog() {
        val editText = EditText(this)
        editText.hint = "Название категории"
        editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES


        AlertDialog.Builder(this)
            .setTitle("Новая категория")
            .setView(editText)
            .setPositiveButton("Добавить") { _, _ ->
                val name = editText.text.toString().trim()

                if (name.isNotBlank()) {
                    lifecycleScope.launch {
                        val category = Category(name = name)
                        categoryDao.insert(category)
                        loadCategories() // обновляем спиннер
                    }
                }  else {
            Toast.makeText(this, "Введите название", Toast.LENGTH_SHORT).show()
        }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }


}
