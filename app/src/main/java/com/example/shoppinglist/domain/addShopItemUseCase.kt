package com.example.shoppinglist.domain

class addShopItemUseCase(private val shopListRepository: shopListRepository) {
    fun addShopItem(shopItem:ShopItem){
           shopListRepository.addShopItem(shopItem)
    }
}