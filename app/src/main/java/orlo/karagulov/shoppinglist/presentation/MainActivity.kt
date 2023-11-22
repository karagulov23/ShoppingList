package orlo.karagulov.shoppinglist.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import orlo.karagulov.shoppinglist.R

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter
    private var shopItemContainerLand: FragmentContainerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        shopItemContainerLand = findViewById(R.id.shopItemContainerLand)
        isOnePaneMode()
        setupRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this) {
            shopListAdapter.submitList(it)
        }
        val btnAddItem = findViewById<FloatingActionButton>(R.id.fbaAdd)

        btnAddItem.setOnClickListener {
            if (isOnePaneMode()) {
                val intent = ShopItemActivity.newIntentAddItem(this)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceAddItem())
            }
        }
    }

    private fun isOnePaneMode(): Boolean {
        return shopItemContainerLand == null
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.shopItemContainerLand, fragment)
            .addToBackStack(null)
            .commit()
    }
    private fun setupRecyclerView() {
        val rvShopList = findViewById<RecyclerView>(R.id.rvShopList)
        shopListAdapter = ShopListAdapter()
        rvShopList.adapter = shopListAdapter

        shopListAdapter.onShopItemLongClickListener = {
            if (isOnePaneMode()) {
                val intent = ShopItemActivity.newIntentUpdateItem(this, it.id)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceUpdateItem(it.id))
            }
        }

        shopListAdapter.onShopItemClickListener = {
            viewModel.updateEnableState(it)
        }

        setupSwipeListener(rvShopList)

    }
    private fun setupSwipeListener(recyclerView: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = shopListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
    override fun onEditingFinished() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }

}