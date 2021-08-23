package com.uc3m.searchyourrecipe.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uc3m.searchyourrecipe.repository.EdamamRepository

class SearchViewModelFactory(private  val repository: EdamamRepository): ViewModelProvider.Factory {

    override fun <T: ViewModel?> create(modelClass: Class<T>): T{

        return SearchViewModel(repository) as T
    }
}