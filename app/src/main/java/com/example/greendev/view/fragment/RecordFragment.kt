package com.example.greendev.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.greendev.R
import com.example.greendev.adapter.RecordRecyclerViewAdapter
import com.example.greendev.databinding.FragmentRecordBinding
import com.example.greendev.model.RecordData

class RecordFragment : Fragment() {
    private lateinit var binding: FragmentRecordBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecordBinding.bind(view)

        val item = ArrayList<RecordData>()
        item.add(RecordData("2023-07-15", "다다익선 캠페인", "스타벅스"))
        item.add(RecordData("2023-07-15", "다다익선 캠페인", "스타벅스"))
        item.add(RecordData("2023-07-15", "다다익선 캠페인", "스타벅스"))
        item.add(RecordData("2023-07-15", "다다익선 캠페인", "스타벅스"))
        item.add(RecordData("2023-07-15", "다다익선 캠페인", "스타벅스"))
        item.add(RecordData("2023-07-15", "다다익선 캠페인", "스타벅스"))
        item.add(RecordData("2023-07-15", "다다익선 캠페인", "스타벅스"))
        item.add(RecordData("2023-07-15", "다다익선 캠페인", "스타벅스"))

        val adapter = RecordRecyclerViewAdapter(item)
        binding.campaignRecyclerView.adapter = adapter
        binding.backButton.setOnClickListener {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.remove(this)
                ?.commit()
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record, container, false)
    }
}