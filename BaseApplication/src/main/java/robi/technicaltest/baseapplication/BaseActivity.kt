package robi.technicaltest.baseapplication

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    private var _binding: VB? = null
    val binding get() = _binding!!
    val TAG = this::class.java.simpleName

    abstract fun initBinding(): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = initBinding()
        setContentView(binding.root)
    }

    private fun isDarkMode(): Boolean {
        return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            onOrientationChanged(Configuration.ORIENTATION_LANDSCAPE)
        } else {
            onOrientationChanged(Configuration.ORIENTATION_PORTRAIT)
        }
    }

    open fun onOrientationChanged(orientation: Int) {}
}