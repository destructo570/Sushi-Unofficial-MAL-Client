package com.destructo.sushi.ui.anime.animeDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.destructo.sushi.R
import com.destructo.sushi.databinding.FragmentAnimeReviewBottomSheetBinding
import com.destructo.sushi.model.jikan.common.Review
import com.destructo.sushi.model.jikan.common.ReviewScores
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

private const val ANIME_REVIEW = "anime_review_object"

class AnimeReviewBottomSheetFragment : BottomSheetDialogFragment() {

    private var review: Review? = null
    private lateinit var binding: FragmentAnimeReviewBottomSheetBinding
    private lateinit var scoreChipGroup:ChipGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            review = it.getParcelable(ANIME_REVIEW)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAnimeReviewBottomSheetBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        scoreChipGroup =  binding.scoreChipGroup

        review?.let {
            it.reviewer?.reviewScores?.let { scores -> setScoreChips(scores) }
            binding.review = it
        }

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(review: Review) =
            AnimeReviewBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ANIME_REVIEW, review)
                }
            }
    }

    private fun setScoreChips(reviewScores: ReviewScores){
        reviewScores.art?.let {createChip(getString(R.string.art) + " " + it )}
        reviewScores.animation?.let {createChip(getString(R.string.animation) + " " + it)}
        reviewScores.character?.let {createChip(getString(R.string.character) + " " + it)}
        reviewScores.enjoyment?.let {createChip(getString(R.string.enjoyment) + " " + it)}
        reviewScores.overall?.let {createChip(getString(R.string.overall) + " " + it)}
        reviewScores.sound?.let {createChip(getString(R.string.sound) + " " + it)}
        reviewScores.story?.let {createChip(getString(R.string.story) + " " + it)}
    }

    private fun createChip(string: String){
        val chip = Chip(context, null, R.attr.customChipStyle)
        chip.text = string
        scoreChipGroup.addView(chip)
    }
}