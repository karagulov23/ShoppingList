package orlo.karagulov.shoppinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import orlo.karagulov.shoppinglist.data.ShopListRepositoryImpl
import orlo.karagulov.shoppinglist.domain.AddShopItemUseCase
import orlo.karagulov.shoppinglist.domain.GetShopItemUseCase
import orlo.karagulov.shoppinglist.domain.ShopItem
import orlo.karagulov.shoppinglist.domain.UpdateShopItemUseCase

class ShopItemViewModel: ViewModel() {


    private val repository = ShopListRepositoryImpl
    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val updateShopItemUseCase = UpdateShopItemUseCase(repository)

    private val _shopItem = MutableLiveData<ShopItem>()
    private val _errorInputName = MutableLiveData<Boolean>()
    private val _errorInoutCount = MutableLiveData<Boolean>()
    private val _finish = MutableLiveData<Unit>()
    val finish: LiveData<Unit>
        get() = _finish
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName
    val errorInputCount: LiveData<Boolean>
        get() = _errorInoutCount
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    fun getShopItem(shopItemId: Int){
        val item = getShopItemUseCase.getShopItemById(shopItemId)
        _shopItem.value = item
    }
    fun addShopItem(inputName: String?, inputCount: String?){
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            val shopItem = ShopItem(name = name, count = count,true)
            addShopItemUseCase.addShopItemUseCase(shopItem)
            _finish.value = Unit
        }
    }
    fun updateShopItem(inputName: String?, inputCount: String?){
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            _shopItem.value?.let {
                val item = it.copy(name = name, count = count)
                updateShopItemUseCase.updateShopItem(item)
                _finish.value = Unit
            }
        }
    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }
    private fun parseCount(inputCount: String?) : Int {
        return try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }
    private fun validateInput(name: String, count: Int): Boolean {
        var result = true
        if (name.isBlank()){
            _errorInputName.value = true
            result = false
        }
        if (count <= 0) {
            _errorInoutCount.value = true
            result = false
        }
        return result
    }
    fun resetErrorInputName(){
        _errorInputName.value = false
    }
    fun resetErrorInputCount(){
        _errorInputName.value = false
    }

}