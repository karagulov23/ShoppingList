package orlo.karagulov.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import orlo.karagulov.shoppinglist.R
import orlo.karagulov.shoppinglist.domain.ShopItem
import orlo.karagulov.shoppinglist.domain.ShopItem.Companion.UNDEFINED_ID

class ShopItemActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = UNDEFINED_ID
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
        launchRightMode()
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_UPDATE) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_UPDATE) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Param shopItem id is absent $mode")
            }
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, -1)
        }
    }

    private fun launchRightMode() {
        val fragment = when (screenMode) {
            MODE_ADD -> ShopItemFragment.newInstanceAddItem()
            MODE_UPDATE -> ShopItemFragment.newInstanceUpdateItem(shopItemId)
            else -> throw RuntimeException("Unknown screen mode $screenMode")
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.shopItemContainer, fragment)
            .commit()
    }
    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_id"
        private const val MODE_UPDATE = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""
        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }
        fun newIntentUpdateItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_UPDATE)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }

    }
    override fun onEditingFinished() {
        Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()
        finish()
    }

}