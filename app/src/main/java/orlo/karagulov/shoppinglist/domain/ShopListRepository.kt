package orlo.karagulov.shoppinglist.domain

import androidx.lifecycle.LiveData

interface ShopListRepository {

    fun addShopItem(shopItem: ShopItem)
    fun deleteShopItem(shopItem: ShopItem)
    fun updateShopItem(shopItem: ShopItem)
    fun getShopItemById(shopItemId: Int): ShopItem
    fun getShopList(): LiveData<List<ShopItem>>

}