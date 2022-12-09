package com.example.shoppinglist.domain

class getShopListUseCase(private val shopListRepository: shopListRepository) {
    fun getShopList(): List<ShopItem> {
        return shopListRepository.getShopList()
    }
}