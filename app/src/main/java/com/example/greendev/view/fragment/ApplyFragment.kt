package com.example.greendev.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.greendev.BindingFragment
import com.example.greendev.R
import com.example.greendev.databinding.FragmentApplyBinding

class ApplyFragment(campaignId: Int) : BindingFragment<FragmentApplyBinding>(R.layout.fragment_apply, false) {
    private var campaignId: Int? = null

    init {
        this.campaignId = campaignId
        Log.d("testtt", "id is : $campaignId")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.backButton?.let { returnToPreviousFragment(it) }
    }
}