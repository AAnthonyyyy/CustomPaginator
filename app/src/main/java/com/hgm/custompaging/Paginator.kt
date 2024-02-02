package com.hgm.custompaging

/**
 * @author：HGM
 * @created：2024/2/2 0002
 * @description：
 **/
interface Paginator<Key, Item> {

      suspend fun loadNextItem()

      fun reset()
}