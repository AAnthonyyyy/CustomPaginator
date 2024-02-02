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

      override suspend fun loadNextItem() {
            if(isMakingRequest) {
                  return
            }
            isMakingRequest = true
            onLoadUpdated(true)
            val result = onRequest(currentKey)
            isMakingRequest = false
            val items = result.getOrElse {
                  onError(it)
                  onLoadUpdated(false)
                  return
            }
            currentKey = getNextKey(items)
            onSuccess(items, currentKey)
            onLoadUpdated(false)
      }

      override fun reset() {
            currentKey = initialKey
      }
}