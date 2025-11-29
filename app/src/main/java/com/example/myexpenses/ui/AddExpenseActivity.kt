package com.example.myexpenses.ui

import android.R
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myexpenses.data.CategoryRepository
import com.example.myexpenses.data.ExpenseDatabase
import com.example.myexpenses.data.ExpenseRepository
import com.example.myexpenses.databinding.ActivityAddExpenseBinding
import com.example.myexpenses.viewmodel.AddExpenseViewModel
import com.example.myexpenses.viewmodel.AddExpenseViewModelFactory

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddExpenseBinding

    private val viewModel: AddExpenseViewModel by viewModels {
        AddExpenseViewModelFactory(
            ExpenseRepository(ExpenseDatabase.Companion.getDatabase(this).expenseDao()),
            CategoryRepository(ExpenseDatabase.Companion.getDatabase(this).categoryDao())
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- загрузка категорий через ViewModel ---
        viewModel.categories.observe(this) { list ->
            val names = list.map { it.name }
            val adapter = ArrayAdapter(
                this,
                R.layout.simple_spinner_item,
                names
            )
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            binding.spinnerCategory.adapter = adapter
        }

        // --- кнопка добавления категории ---
        binding.buttonAddCategory.setOnClickListener {
            showAddCategoryDialog()
        }

        // --- кнопка сохранения расхода ---
        binding.saveButton.setOnClickListener {
            val description = binding.editDescription.text.toString().take(100).trim()
            val amount = binding.editAmount.text.toString().toDoubleOrNull()
            val position = binding.spinnerCategory.selectedItemPosition

            if (description.isBlank() || amount == null) {
                Toast.makeText(this, "Заполните оба поля корректно", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val categories = viewModel.categories.value ?: return@setOnClickListener
            val categoryId = categories[position].id

            viewModel.addExpense(description, amount, categoryId)

            Toast.makeText(this, "Сохранено!", Toast.LENGTH_SHORT).show()
            finish()
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
                    viewModel.addCategory(name)
                } else {
                    Toast.makeText(this, "Введите название", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}