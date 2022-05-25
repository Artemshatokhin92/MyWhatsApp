package com.shatokhin.mywhatsapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shatokhin.mywhatsapp.ChatActivity
import com.shatokhin.mywhatsapp.databinding.FragmentMainBinding
import com.shatokhin.mywhatsapp.domain.models.ChatData
import com.shatokhin.mywhatsapp.ui.rvadapters.RVAdapterChats
import com.shatokhin.mywhatsapp.ui.viewmodel.ViewModelListChats
import com.shatokhin.mywhatsapp.ui.viewmodel.ViewModelListChatsFactory
import com.shatokhin.mywhatsapp.utilites.EXTRA_NAME_OPEN_CHAT
import com.shatokhin.mywhatsapp.utilites.TAG_FOR_LOGCAT

class ListChatsFragment : Fragment() {

    // viewModel - модель представления, которая служит прослойкой между View и Model
    private val viewModelListChats: ViewModelListChats by viewModels { ViewModelListChatsFactory() }
    private var countPageInServer = 0
    private var countPageLoading = 0
    private lateinit var rvAdapterChatData: RVAdapterChats

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycleViewChatData()
        refreshListChats()
    }

    private fun refreshListChats() {
        viewModelListChats.refreshListChats()
        countPageInServer = viewModelListChats.getCountPageChatsFromServer()
        viewModelListChats.getPageChats(1)
        countPageLoading = 1
        binding.swiperefresh.isRefreshing = false
    }

    fun loadNextPageChats() {
        val nextPage = countPageLoading + 1
        if (nextPage > countPageInServer) return
        viewModelListChats.getPageChats(nextPage)
        countPageLoading = nextPage
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycleViewChatData(){
        val clickListener = object: RVAdapterChats.ClickListenerChatData{
            override fun openChat(chatData: ChatData) {
                val intent = Intent(activity, ChatActivity::class.java)
                intent.putExtra(EXTRA_NAME_OPEN_CHAT, chatData.name)
                startActivity(intent)
            }
        }

        rvAdapterChatData = RVAdapterChats(clickListener)
        binding.rvChats.adapter = rvAdapterChatData
        val layoutForRvChats = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvChats.layoutManager = layoutForRvChats
        viewModelListChats.listChatData.observe(viewLifecycleOwner){ listChatData ->
            Log.d(TAG_FOR_LOGCAT, "Fragment ${listChatData.size}")
            rvAdapterChatData.submitList(listChatData)
        }

        binding.rvChats.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var lastPositionLoading = 0
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visiblePosition = layoutManager.findLastVisibleItemPosition()
                val positionLoading = rvAdapterChatData.getSizeListChats() - 2

                if (visiblePosition != lastPositionLoading) {
                    lastPositionLoading = visiblePosition

                    if (lastPositionLoading == positionLoading ) {
                        Log.d(TAG_FOR_LOGCAT, "подгружаем страницу: ${countPageLoading + 1}")
                        loadNextPageChats()
                    }
                }
            }
        })

        binding.swiperefresh.setOnRefreshListener {
            rvAdapterChatData.submitList(null)
            refreshListChats()
        }

    }
}