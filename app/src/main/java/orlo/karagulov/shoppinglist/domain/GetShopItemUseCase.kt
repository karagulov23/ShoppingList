package orlo.karagulov.shoppinglist.domain

class GetShopItemUseCase(private val shopListRepository: ShopListRepository) {
    fun getShopItemById(shopItemId: Int): ShopItem {
        return shopListRepository.getShopItemById(shopItemId)
    }
}