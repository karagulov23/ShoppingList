package orlo.karagulov.shoppinglist.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import orlo.karagulov.shoppinglist.R
import orlo.karagulov.shoppinglist.domain.ShopItem.Companion.UNDEFINED_ID

class ShopItemFragment : Fragment() {

    private lateinit var shopItemViewModel: ShopItemViewModel
    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var btnSave: Button

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = UNDEFINED_ID
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnEditingFinishedListener) {
            onEditingFinishedListener = context
        } else {
            throw RuntimeException("Activity must implement OnEditingFinishedListener interface")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        shopItemViewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        initViews(view)
        addTextChangeListeners()
        launchRightMode()
        observeViewModel()
    }

    private fun observeViewModel() {
        shopItemViewModel.errorInputCount.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_count)
            } else {
                null
            }
            tilCount.error = message
        }
        shopItemViewModel.errorInputName.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            tilName.error = message
        }
        shopItemViewModel.finish.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinished()
        }
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_ADD -> launchAddMode()
            MODE_UPDATE -> launchUpdateMode()
        }
    }

    private fun launchAddMode() {
        btnSave.setOnClickListener {
            shopItemViewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    private fun launchUpdateMode() {
        shopItemViewModel.getShopItem(shopItemId)
        shopItemViewModel.shopItem.observe(viewLifecycleOwner) {
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        }
        btnSave.setOnClickListener {
            shopItemViewModel.updateShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    private fun initViews(view: View) {
        tilName = view.findViewById(R.id.tilName)
        tilCount = view.findViewById(R.id.tilCount)
        etName = view.findViewById(R.id.etName)
        etCount = view.findViewById(R.id.etCount)
        btnSave = view.findViewById(R.id.btnSave)
    }

    private fun addTextChangeListeners() {
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                shopItemViewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                shopItemViewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_UPDATE) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_UPDATE) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw RuntimeException("Param shopItem id is absent $mode")
            }
            shopItemId = args.getInt(SHOP_ITEM_ID, UNDEFINED_ID)
        }
    }
    companion object {
        private const val SCREEN_MODE = "extra_mode"
        private const val SHOP_ITEM_ID = "extra_id"
        private const val MODE_UPDATE = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem(): ShopItemFragment {
            val args = Bundle().apply {
                putString(SCREEN_MODE, MODE_ADD)
            }
            return ShopItemFragment().apply {
                arguments = args
            }
        }
        fun newInstanceUpdateItem(shopItemId: Int): ShopItemFragment {
            val args = Bundle().apply {
                putString(SCREEN_MODE, MODE_UPDATE)
                putInt(SHOP_ITEM_ID, shopItemId)
            }
            return ShopItemFragment().apply {
                arguments = args
            }
        }

    }

    interface OnEditingFinishedListener{
        fun onEditingFinished()
    }

}