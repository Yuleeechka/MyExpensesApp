package com.example.myexpenses

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myexpenses.data.ExpenseDatabase
import com.example.myexpenses.databinding.ActivityMainBinding
import com.example.myexpenses.ui.ExpenseAdapter
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ExpenseAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        val dao = ExpenseDatabase.getDatabase(this).expenseDao()
        dao.getAll().observe(this) { expenses ->
            adapter.submitList(expenses)
            val total = expenses.sumOf { it.amount }
            binding.totalText.text = "Итого: %.2f".format(total)
        }

        binding.addButton.setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }
    }
}