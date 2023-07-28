package anmol.bansal.anmolgetswipe.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import anmol.bansal.anmolgetswipe.databinding.ActivityMainBinding
import anmol.bansal.anmolgetswipe.utils.CommonUtils

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(binding.navHostFragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}