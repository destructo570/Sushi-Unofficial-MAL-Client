package com.destructo.sushi.ui.anime

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import com.destructo.sushi.R
import com.destructo.sushi.databinding.AnimeStatusUpdateBottomSheetBinding
import com.destructo.sushi.util.toEditable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AnimeUpdateDialog: BottomSheetDialogFragment(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: AnimeStatusUpdateBottomSheetBinding
    private lateinit var animeStatusSpinner:Spinner
    private lateinit var episodeIncButton:MaterialButton
    private lateinit var episodeDecButton:MaterialButton
    private lateinit var episodeText:EditText
    private lateinit var animeScoreSeekBar: SeekBar
    private lateinit var scoreText: TextView

    private var animeStatus:String?=null
    private var animeEpisodes:String?=null
    private var animeScore:Int?=0

    private lateinit var updateButton:Button
    private lateinit var removeButton: Button
    private lateinit var listener: AnimeUpdateListener

    companion object{
        fun newInstance(animeId: Int?, status:String?, episodes:String?, score:Int): AnimeUpdateDialog{
            val animeUpdateDialog = AnimeUpdateDialog()
            val bundle = Bundle()
            bundle.putString("status",status)
            bundle.putString("episodes",episodes)
            bundle.putInt("score",score)
            animeUpdateDialog.arguments = bundle
            return animeUpdateDialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        animeScore = arguments?.getInt("score")
        animeStatus = arguments?.getString("status")
        animeEpisodes = arguments?.getString("episodes")

        listener = parentFragment as AnimeUpdateListener

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AnimeStatusUpdateBottomSheetBinding.inflate(inflater, container, false)

        animeStatusSpinner = binding.animeStatusSpinner
        episodeIncButton = binding.incrementEpisodeButton
        episodeDecButton = binding.decrementEpisodeButton
        episodeText = binding.episodeCount
        animeScoreSeekBar = binding.animeScoreSeekBar
        scoreText = binding.animeScoreText
        updateButton = binding.updateAnimeStatusButton
        removeButton = binding.removeAnimeButton

        context?.let {
            ArrayAdapter.createFromResource(
                it, R.array.user_anime_status, android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                animeStatusSpinner.adapter = adapter
            }
        }

        episodeIncButton.setOnClickListener {view->
            val episodeCount:Int

             if(episodeText.text.isNotEmpty()){
                 episodeCount = episodeText.text.toString().toInt()
                 if(episodeCount >= 0){
                     episodeText.text = (episodeCount + 1).toString().toEditable()
                 }
             }

        }

        episodeDecButton.setOnClickListener {view->
            val episodeCount:Int

            if(episodeText.text.isNotEmpty()){
                episodeCount = episodeText.text.toString().toInt()
                if(episodeCount > 0){
                    episodeText.text = (episodeCount - 1).toString().toEditable()
                }
            }
        }

        animeScoreSeekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                updateSeekBarProgress(progress, scoreText)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

        removeButton.setOnClickListener {
           val dialog = AlertDialog.Builder(context)
                .setTitle("Remove anime from list")
                .setMessage("Are you sure you want to remove this anime from your list?")
                .setPositiveButton(R.string.yes
                ) { p0, p1 ->
                    listener.onUpdateClick(null,"",0,0,true)
                    dismiss()
                }
               .setNegativeButton(R.string.no
                ) { p0, p1 -> dismiss() }
               .create()

            dialog.setOnShowListener {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context?.let { it1 ->
                    AppCompatResources.getColorStateList(
                        it1,
                        R.color.textColorSecondary
                    )
                })

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context?.let { it1 ->
                    AppCompatResources.getColorStateList(
                        it1,
                        R.color.textColorPrimary
                    )
                })

            }

            dialog.show()

        }

        updateButton.setOnClickListener {
            val status = animeStatusSpinner.selectedItem.toString()
            var episode = 0
            if(episodeText.text.toString().isNotEmpty()){
                episode = episodeText.text.toString().toInt()
            }
            val score = animeScoreSeekBar.progress
            listener.onUpdateClick(null,status, episode, score, false)
            dismiss()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setSpinnerSelected(animeStatus, animeStatusSpinner)
        setEpisodesWatched(animeEpisodes, episodeText)
        setScoreProgress(animeScore, scoreText, animeScoreSeekBar)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {}

    override fun onNothingSelected(p0: AdapterView<*>?) {}

    @Suppress("UNCHECKED_CAST")
    private fun setSpinnerSelected(item: String?, spinner: Spinner){
        val adapter = spinner.adapter as ArrayAdapter<String>
        val array = resources.getStringArray(R.array.user_anime_status)
        when(item?.trim()){
            getString(R.string.user_anime_status_watching)->{spinner.setSelection(adapter.getPosition(array[0]))}
            getString(R.string.user_anime_status_completed)->{spinner.setSelection(adapter.getPosition(array[1]))}
            getString(R.string.user_anime_status_plan_to_watch)->{spinner.setSelection(adapter.getPosition(array[2]))}
            getString(R.string.user_anime_status_on_hold)->{spinner.setSelection(adapter.getPosition(array[3]))}
            getString(R.string.user_anime_status_dropped)->{spinner.setSelection(adapter.getPosition(array[4]))}
        }

    }

    private fun setEpisodesWatched(episodesWatched: String?, episodeText: TextView) {
        episodeText.text = episodesWatched
    }

    private fun setScoreProgress(animeScore: Int?, textView: TextView, seekBar: SeekBar) {
        seekBar.progress = animeScore?:0
        updateSeekBarProgress(animeScore?:0, textView)
    }

    private fun updateSeekBarProgress(progress: Int, textView: TextView){

        when(progress){
            0->{textView.text = getString(R.string.user_score_null)}
            1->{textView.text = getString(R.string.user_score_one)}
            2->{textView.text = getString(R.string.user_score_two)}
            3->{textView.text = getString(R.string.user_score_three)}
            4->{textView.text = getString(R.string.user_score_four)}
            5->{textView.text = getString(R.string.user_score_five)}
            6->{textView.text = getString(R.string.user_score_six)}
            7->{textView.text = getString(R.string.user_score_seven)}
            8->{textView.text = getString(R.string.user_score_eight)}
            9->{textView.text = getString(R.string.user_score_nine)}
            10->{textView.text = getString(R.string.user_score_ten)}
        }
    }
    }

interface AnimeUpdateListener{
    fun onUpdateClick(animeId:Int?=null, status:String, episodes:Int, score:Int, remove:Boolean)
}

