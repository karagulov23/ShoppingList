package orlo.karagulov.shoppinglist.domain

class AddShopItemUseCase(private val shopListRepository: ShopListRepository) {
    fun addShopItemUseCase(shopItem: ShopItem){
        shopListRepository.addShopItem(shopItem)
    }
}