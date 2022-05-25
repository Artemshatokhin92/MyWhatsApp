package com.shatokhin.mywhatsapp

import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.shatokhin.mywhatsapp.databinding.ActivityMainBinding
import com.shatokhin.mywhatsapp.ui.main.SectionsPagerAdapter
import com.shatokhin.mywhatsapp.ui.viewmodel.ViewModelListChats
import com.shatokhin.mywhatsapp.ui.viewmodel.ViewModelListChatsFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // создание макета из XML с помощью binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // создание viewPager для размещения фрагментов
        val pagerAdapter = SectionsPagerAdapter( supportFragmentManager, lifecycle )
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = pagerAdapter

        // создание TabLayout (view с вкладками) который привязывается к viewPager
        val tabLayout: TabLayout = binding.tabs
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when(position){
                0 -> tab.text = "ЧАТЫ"
                1 -> tab.text = "СТАТУС"
                2 -> tab.text = "ЗВОНКИ"
            }
        }.attach()

        // кнопка для создания нового сообщения (логика не реализована!)
        val fab: FloatingActionButton = binding.fab
        fab.setOnClickListener { view ->

        }

    }
}