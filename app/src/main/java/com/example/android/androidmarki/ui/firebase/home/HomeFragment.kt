package com.example.android.androidmarki.ui.firebase.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.android.androidmarki.R
import com.example.android.androidmarki.ui.base.BaseActivity
import com.example.android.androidmarki.ui.base.BaseFragment
import com.example.android.androidmarki.ui.home.second.HomeSecondFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_home_second.*


class HomeFragment : BaseFragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        homeViewModel =
//            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home_second, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        (activity as BaseActivity ).setSupportActionBar(view.findViewById(R.id. firebase_toolbar))
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        (activity as BaseActivity ). setupActionBarWithNavController(navController, appBarConfiguration)
        (activity as BaseActivity ).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as BaseActivity ).supportActionBar?.setDisplayShowHomeEnabled(true)

//        view.findViewById<Toolbar>(R.id.firebase_toolbar)
//            .setupWithNavController(navController, appBarConfiguration)
        pager.adapter = ScreenSlidePagerAdapter(requireActivity() as BaseActivity)
        TabLayoutMediator(tab_layout, pager) { tab, position ->
            tab.text = "OBJECT ${(position + 1)}"
        }.attach()

        //        val badge: BadgeDrawable = tablayout.getTabAt(0)!!.orCreateBadge
//        badge.isVisible = true
// Optionally show a number.
// Optionally show a number.
//        badge.number = 99
    }



    private inner class ScreenSlidePagerAdapter(fa: BaseActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): BaseFragment = HomeSecondFragment()
    }

    companion object{
        private const val NUM_PAGES = 5

    }

}
