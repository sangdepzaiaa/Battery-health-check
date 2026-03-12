package com.example.myapplication.ui.settings

import com.example.myapplication.R
import com.example.myapplication.base.BaseFragment
import com.example.myapplication.databinding.FragmentSettingsBinding

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(
    inflater = FragmentSettingsBinding::inflate
){

    override fun initView() {
        super.initView()
        binding.layoutToolBar.txvTitle.text = getString(R.string.settings)
    }
}