package com.destructo.sushi.ui.manga.mangaDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentMangaReviewBottomSheetBinding
import com.destructo.sushi.model.jikan.manga.ReviewEntity
import com.destructo.sushi.model.jikan.manga.Scores
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import timber.log.Timber

private const val MANGA_REVIEW = "manga_review_object"

class MangaReviewBottomSheetFragment : BottomSheetDialogFragment() {

    private var review: ReviewEntity? = null
    private lateinit var binding: FragmentMangaReviewBottomSheetBinding
    private lateinit var scoreChipGroup: ChipGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            review = it.getParcelable(MANGA_REVIEW)
            Timber.e("Review: ${review.toString()}")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMangaReviewBottomSheetBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        scoreChipGroup =  binding.scoreChipGroup

        review?.let {
            it.reviewer?.scores?.let { scores -> setScoreChips(scores) }
            binding.review = it
        }

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(review: ReviewEntity) =
            MangaReviewBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(MANGA_REVIEW, review)
                }
            }
    }


    private fun setScoreChips(reviewScores: Scores){
        reviewScores.art?.let {createChip(getString(R.string.art) + " " + it )}
        reviewScores.character?.let {createChip(getString(R.string.character) + " " + it)}
        reviewScores.enjoyment?.let {createChip(getString(R.string.enjoyment) + " " + it)}
        reviewScores.overall?.let {createChip(getString(R.string.overall) + " " + it)}
        reviewScores.story?.let {createChip(getString(R.string.story) + " " + it)}
    }

    private fun createChip(string: String){
        val chip = Chip(context, null, R.attr.customChipStyle)
        chip.text = string
        scoreChipGroup.addView(chip)
    }
}