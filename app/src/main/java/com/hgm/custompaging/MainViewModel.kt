package com.hgm.custompaging

import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * @author：HGM
 * @created：2024/2/2 0002
 * @description：
 **/
class MainViewModel : ViewModel() {

      var state by mutableStateOf(UIState())

      private val repository = Repository()

      private val paginator = DefaultPaginator(
            initialKey = state.page,
            onLoadUpdated = { isLoading ->
                  state = state.copy(
                        isLoading = isLoading
                  )
            },
            onRequest = { nextPage ->
                  repository.getItems(nextPage, 20)
            },
            getNextKey = {
                  state.page + 1
            },
            onError = { e ->
                  state = state.copy(error = e?.localizedMessage)

            },
            onSuccess = { newItems, newKey ->
                  state = state.copy(
                        items = state.items + newItems,
                        page = newKey,
                        endReached = newItems.isEmpty()
                  )
            }
      )

      init {
            loadNextItem()
      }


      fun loadNextItem() {
            viewModelScope.launch {
                  paginator.loadNextItem()
            }
      }

}

data class UIState(
      val isLoading: Boolean = false,
      val items: List<Item> = emptyList(),
      val error: String? = null,
      val endReached: Boolean = false,
      val page: Int = 0
)