package com.example.shoppinglist.domain

class removeShopItemUseCase(private val shopListRepository: shopListRepository) {
    fun removeShopItem(shopItem: ShopItem) {
        shopListRepository.removeShopItem(shopItem)
    }
}