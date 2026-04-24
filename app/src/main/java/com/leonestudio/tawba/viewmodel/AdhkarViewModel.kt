package com.leonestudio.tawba.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.leonestudio.tawba.data.Dhikr
import com.leonestudio.tawba.data.DhikrDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdhkarViewModel(private val dao: DhikrDao) : ViewModel() {

    val adhkar: StateFlow<List<Dhikr>> = dao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val totalCount: StateFlow<Int> = dao.totalCount()
        .map { it ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    fun incrementCount(dhikr: Dhikr) {
        viewModelScope.launch {
            val newCount = (dhikr.currentCount + 1).coerceAtMost(dhikr.targetCount)
            dao.updateCount(dhikr.id, newCount)
        }
    }

    fun resetCount(dhikr: Dhikr) {
        viewModelScope.launch {
            dao.updateCount(dhikr.id, 0)
        }
    }

    fun resetAll() {
        viewModelScope.launch { dao.resetAllCounts() }
    }

    class Factory(private val dao: DhikrDao) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AdhkarViewModel(dao) as T
        }
    }
}
