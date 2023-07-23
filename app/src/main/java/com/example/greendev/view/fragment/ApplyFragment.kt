package com.example.greendev.view.fragment

import android.os.Bundle
import android.view.View
import com.example.greendev.BindingFragment
import com.example.greendev.R
import com.example.greendev.databinding.FragmentApplyBinding

class ApplyFragment : BindingFragment<FragmentApplyBinding>(R.layout.fragment_apply, false) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.backButton?.let { returnToPreviousFragment(it) }
    }
}