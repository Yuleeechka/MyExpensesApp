package com.example.myexpenses.ui

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myexpenses.data.Expense
import com.example.myexpenses.databinding.ItemExpenseBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.LocalDate
import java.util.Locale


class ExpenseAdapter : ListAdapter<Expense, ExpenseAdapter.ExpenseViewHolder>(DiffCallback) {

    inner class ExpenseViewHolder(private val binding: ItemExpenseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(expense: Expense) {
            binding.textDescription.text = expense.description
            binding.textAmount.text = "%.2f".format(expense.amount)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                binding.textDate.text = formatExpenseDate(expense.date)
            } else {
                binding.textDate.text = expense.date // fallback
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ItemExpenseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExpenseViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }



    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Expense>() {
            override fun areItemsTheSame(old: Expense, new: Expense) = old.id == new.id
            override fun areContentsTheSame(old: Expense, new: Expense) = old == new
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatExpenseDate(dateString: String): String {
        val formatterInput = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        val dateTime = try {
            LocalDateTime.parse(dateString, formatterInput)
        } catch (e: Exception) {
            return dateString
        }

        val now = LocalDateTime.now()
        val today = now.toLocalDate()
        val yesterday = today.minusDays(1)
        val expenseDate = dateTime.toLocalDate()

        return when {
            expenseDate.isEqual(today) ->
                "Сегодня ${dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
            expenseDate.isEqual(yesterday) ->
                "Вчера ${dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
            expenseDate.year == today.year ->
                dateTime.format(DateTimeFormatter.ofPattern("d MMMM", Locale("ru")))
            else ->
                dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        }
    }
}
