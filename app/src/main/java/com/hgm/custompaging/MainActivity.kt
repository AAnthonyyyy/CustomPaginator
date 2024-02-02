package com.hgm.custompaging

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hgm.custompaging.ui.theme.CustomPagingTheme
import kotlinx.coroutines.flow.collect

class MainActivity : ComponentActivity() {
      private fun Column(modifier: Modifier) {

      }

      override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                  CustomPagingTheme {
                        val viewModel = viewModel<MainViewModel>()
                        val state = viewModel.state
                        val listState = rememberLazyListState()
                        val isReachedBottom by remember {
                              derivedStateOf {
                                    listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.size == listState.layoutInfo.totalItemsCount
                              }
                        }

                        LaunchedEffect(Unit) {
                              snapshotFlow { isReachedBottom }
                                    .collect { isReached ->
                                          if (isReached) {
                                                viewModel.loadNextItem()
                                          }
                                    }
                        }

                        Surface(
                              modifier = Modifier.fillMaxSize(),
                              color = MaterialTheme.colorScheme.background
                        ) {
                              LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    state = listState
                              ) {
                                    items(state.items) {
                                          Column(
                                                modifier = Modifier
                                                      .fillMaxWidth()
                                                      .padding(16.dp)
                                          ) {
                                                Text(
                                                      text = it.title,
                                                      fontSize = 20.sp,
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(it.desc)
                                          }
                                    }
                                    item {
                                          if (state.isLoading) {
                                                Row(
                                                      modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(8.dp),
                                                      horizontalArrangement = Arrangement.Center
                                                ) {
                                                      CircularProgressIndicator()
                                                }
                                          }
                                    }
                              }
                        }
                  }
            }
      }
}
