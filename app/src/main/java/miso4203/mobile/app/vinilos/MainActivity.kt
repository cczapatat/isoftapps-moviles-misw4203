package miso4203.mobile.app.vinilos

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.sidesheet.SideSheetBehavior
import miso4203.mobile.app.vinilos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var isCollector = false
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        isCollector = bundle?.getBoolean("is_collector", false) ?: false

        val navView: BottomNavigationView = binding.navView

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment

        navController = navHostFragment.navController

        /* val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_album, R.id.navigation_artist, R.id.navigation_collector
            )
        ) */
        // setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val standardSideSheetBehavior = SideSheetBehavior.from(binding.standardSideSheet)

        binding.headerNav.openMenuBtn.setOnClickListener {
            standardSideSheetBehavior.expand()
        }

        binding.lateralMenu.btnCloseMenu.setOnClickListener {
            standardSideSheetBehavior.hide()
        }

        binding.lateralMenu.btnExit.setOnClickListener {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}