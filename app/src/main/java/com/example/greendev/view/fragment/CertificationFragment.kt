package com.example.greendev.view.fragment

import android.os.Bundle
import android.view.View
import com.example.greendev.BindingFragment
import com.example.greendev.R
import com.example.greendev.databinding.FragmentCertificationBinding

class CertificationFragment : BindingFragment<FragmentCertificationBinding>(R.layout.fragment_certification, false) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.backButton?.let { returnToPreviousFragment(it) }
    }
}