package com.hgm.custompaging

/**
 * @author：HGM
 * @created：2024/2/2 0002
 * @description：自定义分页器
 **/
class DefaultPaginator<Key, Item>(
      private val initialKey: Key,
      private inline val onLoadUpdated: (Boolean) -> Unit,
      private inline val onRequest: suspend (nextKey: Key) -> Result<List<Item>>,
      private inline val onSuccess: suspend (items: List<Item>, newKey: Key) -> Unit,
      private inline val onError: suspend (Throwable?) -> Unit,
      private inline val getNextKey: suspend (List<Item>) -> Key
) : Paginator<Key, Item> {

      private var currentKey = initialKey
      private var isMakingRequest = false

      // 加载下一页
      override suspend fun load() {
            // 判断当前是否正在请求中
            if(isMakingRequest) {
                  return
            }

            // 开始请求，更改为加载状态
            isMakingRequest = true
            onLoadUpdated(true)
            val result = onRequest(currentKey)
            isMakingRequest = false
            val items = result.getOrElse {
                  // 请求出现错误走这里
                  onError(it)
                  onLoadUpdated(false)
                  return
            }

            // 更新Key，返回请求结果出去
            currentKey = getNextKey(items)
            onSuccess(items, currentKey)
            onLoadUpdated(false)
      }

      // 重置
      override fun reset() {
            currentKey = initialKey
      }
}