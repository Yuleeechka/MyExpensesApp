package com.example.myexpenses

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myexpenses.data.Expense
import com.example.myexpenses.data.ExpenseDatabase
import com.example.myexpenses.databinding.ActivityAddExpenseBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddExpenseBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dao = ExpenseDatabase.getDatabase(this).expenseDao()

        binding.saveButton.setOnClickListener {
            val description = binding.editDescription.text.toString().take(100).trim()
            val amount = binding.editAmount.text.toString().toDoubleOrNull()

            if (description.isBlank() || amount == null) {
                Toast.makeText(this, "Заполните оба поля корректно", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val now = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
            val dateTimeString = now.format(formatter)

            val date = dateTimeString

            lifecycleScope.launch {
                dao.insert(Expense(description = description, amount = amount, date = date))
                finish()
            }
        }
    }
}