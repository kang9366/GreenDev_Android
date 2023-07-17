package com.example.greendev

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.greendev.databinding.FragmentSearchBinding
import java.util.Locale


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: RecyclerViewAdapter

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        val item = ArrayList<CampaignData>()
        val search_item = ArrayList<CampaignData>()

        //recycerview test
        item.add(CampaignData("데보션 캠페인", "SK"))
        item.add(CampaignData("다다익선 캠페인", "스타벅스"))
        item.add(CampaignData("다다익선 캠페인", "스타벅스"))
        item.add(CampaignData("다다익선 캠페인", "스타벅스"))
        item.add(CampaignData("다다익선 캠페인", "스타벅스"))
        item.add(CampaignData("다다익선 캠페인", "스타벅스"))

        adapter = RecyclerViewAdapter(item, R.layout.campaign_item_layout)

        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("test", "before")
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("test", "on")
            }

            override fun afterTextChanged(p0: Editable?) {
                Log.d("test", "after")
                val searchText: String = binding.searchView.text.toString()
                search_item.clear()
                if(searchText == "") {
                    adapter.setItem(item)
                }else {
                    for(i in 0 until item.size) {
                        if (item[i].name.toLowerCase().contains(searchText.lowercase(Locale.getDefault()))
                            || item[i].company.toLowerCase().contains(searchText.lowercase(Locale.getDefault()))) {
                            search_item.add(item[i])
                        }
                        adapter.setItem(search_item)
                    }
                }
            }
        })


        adapter.setOnItemClickListener(object : RecyclerViewAdapter.OnItemClickListener {
            @SuppressLint("ResourceType")
            override fun onItemClick(v: View, data: CampaignData, pos: Int) {
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.apply {
                    replace(R.id.frameLayout, ApplyFragment())
                    commit()
                }
            }
        })

        binding.recyclerView.adapter = adapter
        binding.layout.setOnTouchListener(OnTouchListener { _, _ ->
            hideKeyboard()
            false
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    private fun hideKeyboard() {
        if (activity != null && requireActivity().currentFocus != null) {
            val inputManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                requireActivity().currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}