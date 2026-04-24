package com.leonestudio.tawba.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.leonestudio.tawba.data.SadaqaDao
import com.leonestudio.tawba.data.SadaqaEntry
import com.leonestudio.tawba.util.DateUtils
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SadaqaViewModel(private val dao: SadaqaDao) : ViewModel() {

    val entries: StateFlow<List<SadaqaEntry>> = dao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val totalAmount: StateFlow<Double> = dao.totalAmount()
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    val thisMonthAmount: StateFlow<Double> = dao.totalAmountSince(DateUtils.startOfMonth())
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    val entryCount: StateFlow<Int> = dao.entryCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    fun addSadaqa(amount: Double, currency: String, note: String, category: String) {
        viewModelScope.launch {
            dao.insert(
                SadaqaEntry(
                    amount = amount,
                    currency = currency,
                    note = note,
                    category = category
                )
            )
        }
    }

    fun deleteEntry(entry: SadaqaEntry) {
        viewModelScope.launch { dao.delete(entry) }
    }

    class Factory(private val dao: SadaqaDao) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SadaqaViewModel(dao) as T
        }
    }
}
