package com.shatokhin.mywhatsapp.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

// Адаптер для viewPager (служит для переключения фрагментов)
class SectionsPagerAdapter(fr: FragmentManager, lf: Lifecycle) :
    FragmentStateAdapter(fr, lf) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> ListChatsFragment()
            1 -> StatusFragment()
            else -> CallsFragment()
        }

    }

}