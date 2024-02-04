package com.hgm.custompaging

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hgm.custompaging.ui.theme.CustomPagingTheme

class MainActivity : ComponentActivity() {

      override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                  CustomPagingTheme {
                        val viewModel = viewModel<MainViewModel>()
                        val state = viewModel.state
                        val listState = rememberLazyListState()
                        val isReachedBottom by remember {
                              derivedStateOf {
                                    // 如果列表第一个元素下标加上现在屏幕上所有可见元素的数量等于整个列表的数量，说明数据到底了
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
                                    items(state.posts) {
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
