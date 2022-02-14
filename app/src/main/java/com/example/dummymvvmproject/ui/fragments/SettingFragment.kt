package com.example.dummymvvmproject.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dummymvvmproject.R
import com.example.dummymvvmproject.databinding.AlarmFragmentBinding
import com.example.dummymvvmproject.databinding.SettingFragmentBinding

class SettingFragment : Fragment(R.layout.setting_fragment) {

    private lateinit var binding: SettingFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SettingFragmentBinding.bind(view)


    }

}