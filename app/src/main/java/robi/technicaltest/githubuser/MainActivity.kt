package robi.technicaltest.githubuser

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import robi.technicaltest.baseapplication.BaseActivity
import robi.technicaltest.githubuser.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    private lateinit var navController : NavController
    private lateinit var graph: NavGraph
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var isSearchVisible = false

    override fun initBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { false }
        setSupportActionBar(binding.appBar.topAppBar)
        navigation()
        setUpToolbar()

        binding.root.setOnTouchListener { _, _ ->
            if (isSearchVisible) {
                toggleSearchBox(false)
            }
            false
        }

        binding.appBar.inputSearchBar.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                val searchText = binding.appBar.inputSearchBar.text.toString().trim()
                if (searchText.isNotEmpty()) {
                    performSearch(searchText)
                }
                toggleSearchBox(false)
                true
            } else {
                false
            }
        }
    }

    private fun setUpToolbar() {
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.listUserFragment)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        setTitle(getString(R.string.app_name))
    }

    private fun navigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        graph = inflater.inflate(R.navigation.nav_graph)
        graph.setStartDestination(R.id.listUserFragment)
        navController = navHostFragment.navController
        navController.setGraph(graph, intent.extras)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.listUserFragment) {
                setTitle(getString(R.string.app_name))
            } else {
                binding.appBar.topAppBar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_left_line)
                binding.appBar.topAppBar.setNavigationOnClickListener {
                    navController.navigateUp() // Handle back press
                }
            }
        }
    }

    fun setTitle(title: String) {
        binding.appBar.topAppBar.title = title
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_search_bar -> {
                Log.d(TAG, "Search Clicked")
                toggleSearchBox(true)
                binding.appBar.searchBar.visibility = View.VISIBLE
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    fun toggleSearchBox(show: Boolean) {
        isSearchVisible = show
        binding.appBar.searchBar.apply {
            visibility = if (show) View.VISIBLE else View.GONE
        }

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (show) {
            binding.appBar.inputSearchBar.requestFocus()
            imm.showSoftInput(binding.appBar.inputSearchBar, InputMethodManager.SHOW_IMPLICIT)
        } else {
            imm.hideSoftInputFromWindow(binding.appBar.inputSearchBar.windowToken, 0)
        }
    }

    private fun performSearch(query: String) {
        val bundle = Bundle().apply {
            putString("Query", query)
        }
        navController.navigate(R.id.searchFragment, bundle)
    }
}