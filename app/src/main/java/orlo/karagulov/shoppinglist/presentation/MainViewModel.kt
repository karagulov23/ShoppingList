package orlo.karagulov.shoppinglist.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import orlo.karagulov.shoppinglist.data.ShopListRepositoryImpl
import orlo.karagulov.shoppinglist.domain.DeleteShopItemUseCase
import orlo.karagulov.shoppinglist.domain.GetShopListUseCase
import orlo.karagulov.shoppinglist.domain.ShopItem
import orlo.karagulov.shoppinglist.domain.UpdateShopItemUseCase

class MainViewModel: ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopListUseCase = DeleteShopItemUseCase(repository)
    private val updateShopItemUseCase = UpdateShopItemUseCase(repository)

    val shopList = getShopListUseCase.getShopList()


    fun deleteShopItem(shopItem: ShopItem) {
        deleteShopListUseCase.deleteShopItem(shopItem)
    }

    fun updateEnableState(shopItem: ShopItem) {
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        updateShopItemUseCase.updateShopItem(newItem)
    }

}