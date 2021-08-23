package com.example.trazeapp


import android.os.Bundle

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import androidx.navigation.NavController

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController

import com.example.trazeapp.viewmodel.MainActivityViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.activity_main_container) as NavHostFragment
        val navController = navHostFragment.findNavController()

        val inflater = navController.navInflater
        val graph = inflater.inflate(R.navigation.navigation_graph)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.dashboard_bottom_navigation)
        bottomNavigation.setupWithNavController(navController)
        // Ibalin ni sa viewmodel
        graph.startDestination = if (mainViewModel.hasUserAuthenticated())
            R.id.mainFragment
        else
            R.id.mainScreenFragment

        navController.graph = graph

//        lifecycleScope.launch {
//            mainViewModel.hasUserState().collect { hasUser ->
//                val direction: NavDirections = if (hasUser) {
//                    NavigationGraphDirections.actionGlobalMainFragment()
//                } else {
//
//                    NavigationGraphDirections.actionGlobalMainScreenFragment()
//                }
//
////                 like kung si user kay wala pa naka navigate diha? then i salpak dayon siya padulong didto?
////                 gets na nako
//                if (navController.currentDestination?.id != graph.getAction(direction.actionId)?.destinationId) {
//                    navController.navigate(direction)
//
//                }
//
//            }
//        }


        mainViewModel.bottomNavigationVisibility.observe(this, { visibility ->
            bottomNavigation.visibility = visibility
        })
        setUpNavigationBottom(navController)

    }

    private fun setUpNavigationBottom(navController: NavController) {

        navController.addOnDestinationChangedListener {
                _, destination,
                _,
            ->
            when (destination.id) {
                R.id.loginFragment,
                R.id.registerFragment,
                R.id.mainScreenFragment,
                R.id.otpVerificationFragment,
                -> mainViewModel.hideBottomNavigation()
                R.id.mainFragment,
                R.id.qrCodeFragment,
                R.id.patientFragment,
                -> mainViewModel.showBottomNavigation()

            }
        }


    }

}

//    fun NavController.safeNavigate(
//        @IdRes currentDestinationId: Int,
//        @IdRes id: Int,
//        args: Bundle? = null
//    ) {
//        if (currentDestinationId == currentDestination?.id) {
//            navigate(id, args)
//        }
//    }
//}




