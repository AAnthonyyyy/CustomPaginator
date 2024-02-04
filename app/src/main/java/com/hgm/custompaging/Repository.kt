package com.hgm.custompaging

import kotlinx.coroutines.delay

/**
 * @author：HGM
 * @created：2024/2/2 0002
 * @description：
 **/
class Repository {

      private val remoteDataSource = (1..100).map {
            Post(
                  title = "Item $it",
                  desc = "Description $it"
            )
      }

      suspend fun getPosts(page: Int, pageSize: Int): Result<List<Post>> {
            // 模拟网络延迟
            delay(2000L)

            // 对数据分割模拟分页
            val startingIndex = page * pageSize
            return if (startingIndex + pageSize <= remoteDataSource.size) {
                  Result.success(
                        remoteDataSource.slice(startingIndex until startingIndex + pageSize)
                  )
            } else Result.success(emptyList())
      }
}