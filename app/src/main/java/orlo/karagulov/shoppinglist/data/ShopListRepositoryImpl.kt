package orlo.karagulov.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import orlo.karagulov.shoppinglist.domain.ShopItem
import orlo.karagulov.shoppinglist.domain.ShopListRepository
import java.lang.RuntimeException
import kotlin.random.Random

object ShopListRepositoryImpl : ShopListRepository {

    private val shopList = sortedSetOf<ShopItem>(Comparator<ShopItem> { p0, p1 -> p0.id.compareTo(p1.id) })
    private val shopListLiveData = MutableLiveData<List<ShopItem>>()
    private var autoIncrementId = 0

    init {
        for (i in 0 until 13) {
            val item = ShopItem("Name $i", i, Random.nextBoolean())
            addShopItem(item)
        }
    }
    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementId++
        }
        shopList.add(shopItem)
        updateLiveData()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateLiveData()
    }

    override fun updateShopItem(shopItem: ShopItem) {
        val oldElement = getShopItemById(shopItem.id)
        shopList.remove(oldElement)
        addShopItem(shopItem)
    }

    override fun getShopItemById(shopItemId: Int): ShopItem {
        return shopList.find { it.id == shopItemId }
            ?: throw RuntimeException("Element with id $shopItemId not found")
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopListLiveData
    }

    private fun updateLiveData(){
        shopListLiveData.value = shopList.toList()
    }

}