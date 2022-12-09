package com.example.shoppinglist.domain

class editShopItemUseCase(private val shopListRepository: shopListRepository) {
    fun editShopItem(shopItem: ShopItem){
        shopListRepository.editShopItem(shopItem)
    }
}