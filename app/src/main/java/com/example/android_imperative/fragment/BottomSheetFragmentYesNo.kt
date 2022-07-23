package com.example.android_imperative.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android_imperative.R
import com.example.android_imperative.databinding.BottomSheetYesNoBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragmentYesNo(private val title: String) : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetYesNoBinding
    var clickYes:(() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetYesNoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tvTitle.text = title
        binding.btnYes.setOnClickListener {
            clickYes?.invoke()
        }
        binding.btnNo.setOnClickListener {
            this.dismiss()
        }
    }

}