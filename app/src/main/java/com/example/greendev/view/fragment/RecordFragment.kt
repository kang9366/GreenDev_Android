package com.example.greendev.view.fragment

import android.os.Bundle
import android.view.View
import com.example.greendev.BindingFragment
import com.example.greendev.R
import com.example.greendev.adapter.RecordRecyclerViewAdapter
import com.example.greendev.databinding.FragmentRecordBinding
import com.example.greendev.model.RecordData
import com.example.greendev.view.activity.MainActivity

class RecordFragment : BindingFragment<FragmentRecordBinding>(R.layout.fragment_record, false) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val item = ArrayList<RecordData>()
        //recyclerview test
        for (i in 0..10) item.add(RecordData("2023-07-15", "다다익선 캠페인", "스타벅스"))

        val adapter = RecordRecyclerViewAdapter(item)
        binding?.campaignRecyclerView?.adapter = adapter
        binding?.backButton?.let { returnToPreviousFragment(it) }
        binding?.applyButton?.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.apply {
                replace(R.id.frameLayout, CertificationFragment())
                addToBackStack(null)
                commit()
            }
        }
    }
}