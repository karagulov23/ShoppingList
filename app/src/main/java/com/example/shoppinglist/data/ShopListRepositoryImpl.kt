package com.example.shoppinglist.data

import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.shopListRepository
import kotlin.io.path.fileVisitor

object ShopListRepositoryImpl: shopListRepository {
    private val shopList = mutableListOf<ShopItem>()
    private var autoIncrement = 0

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID){
            shopItem.id = autoIncrement++
        }
        shopList.add(shopItem)
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldElement = getShopItem(shopItem.id)
        shopList.remove(oldElement)
        addShopItem(shopItem)
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopList.find {
            it.id == shopItemId
        } ?: throw RuntimeException("Элемент с айди $shopItemId не найден")
    }

    override fun removeShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
    }

    override fun getShopList(): List<ShopItem> {
        return shopList.toList()
    }
}