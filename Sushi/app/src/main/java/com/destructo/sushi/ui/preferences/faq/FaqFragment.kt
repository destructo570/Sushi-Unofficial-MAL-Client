package com.destructo.sushi.ui.preferences.faq

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.addCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentFaqBinding
import com.destructo.sushi.views.FaqListView

class FaqFragment : Fragment() {

    private lateinit var binding: FragmentFaqBinding
    private lateinit var linearLayout: LinearLayout
    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFaqBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner }

        toolbar = binding.toolbar
        linearLayout = binding.faqLayout

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        handleBackPress()
        setupToolbar()

        val questions = resources.getStringArray(R.array.questions)
        val answers = resources.getStringArray(R.array.answers)

        for ((index, question) in questions.withIndex()){
            val faq = context?.let { FaqListView(it) }
            faq?.setValues(question, answers[index])
            linearLayout.addView(faq)
        }
    }

    private fun setupToolbar(){
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun handleBackPress(){
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }
    }
}