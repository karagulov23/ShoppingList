package com.example.shoppinglist.domain

import java.sql.RowId

interface shopListRepository {
    fun addShopItem(shopItem: ShopItem)
    fun editShopItem(shopItem: ShopItem)
    fun getShopItem(shopItemId: Int) : ShopItem
    fun removeShopItem(shopItem: ShopItem)
    fun getShopList(): List<ShopItem>
}