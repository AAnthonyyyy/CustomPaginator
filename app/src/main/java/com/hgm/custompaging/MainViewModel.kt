package com.hgm.custompaging

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                  repository.getPosts(nextPage, 20)
            },
            getNextKey = {
                  state.page + 1
            },
            onError = { e ->
                  state = state.copy(error = e?.localizedMessage)

            },
            onSuccess = { newItems, newKey ->
                  state = state.copy(
                        posts = state.posts + newItems,
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
                  paginator.load()
            }
      }
}

data class UIState(
      val isLoading: Boolean = false,
      val posts: List<Post> = emptyList(),
      val error: String? = null,
      val endReached: Boolean = false,
      val page: Int = 0
)