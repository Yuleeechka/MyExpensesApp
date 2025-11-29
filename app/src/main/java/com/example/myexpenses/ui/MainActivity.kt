package com.example.myexpenses.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myexpenses.data.ExpenseDatabase
import com.example.myexpenses.data.ExpenseRepository
import com.example.myexpenses.databinding.ActivityMainBinding
import com.example.myexpenses.viewmodel.ExpenseViewModel
import com.example.myexpenses.viewmodel.ExpenseViewModelFactory
import com.example.myexpenses.viewmodel.RateViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ExpenseAdapter

    // Создаём ViewModel для курса
    private val rateViewModel: RateViewModel by viewModels()

    // <-- ViewModel создаётся через делегат
    private val viewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory(
            ExpenseRepository(
                ExpenseDatabase.Companion.getDatabase(this).expenseDao()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ExpenseAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        // Наблюдаем за курсом доллара
        rateViewModel.usdToByn.observe(this) { rate ->
            if (rate != null) {
                binding.usdRateText.text = "1 USD = %.2f BYN".format(rate)
            } else {
                binding.usdRateText.text = "Ошибка загрузки курса"
            }
        }

// Загружаем курс
        rateViewModel.loadRate()


        // ---- Наблюдаем за LiveData из ViewModel ----
        viewModel.allExpenses.observe(this) { expenses ->
            adapter.submitList(expenses)
            val total = expenses.sumOf { it.amount }
            binding.totalText.text = "Итого: %.2f".format(total)
        }

        // ---- Переход на экран добавления ----
        binding.addButton.setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }

    }
}