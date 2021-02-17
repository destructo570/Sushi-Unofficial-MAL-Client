package com.destructo.sushi.ui.anime

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.destructo.sushi.R
import com.destructo.sushi.databinding.AnimeStatusUpdateBottomSheetBinding
import com.destructo.sushi.model.params.DateParam
import com.destructo.sushi.util.getColorFromAttr
import com.destructo.sushi.util.toEditable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AnimeUpdateDialog : BottomSheetDialogFragment(),
    AdapterView.OnItemSelectedListener
{
    private lateinit var binding: AnimeStatusUpdateBottomSheetBinding
    private lateinit var animeStatusSpinner: Spinner
    private lateinit var episodeIncButton: MaterialButton
    private lateinit var episodeDecButton: MaterialButton
    private lateinit var episodeText: EditText
    private lateinit var animeScoreSeekBar: SeekBar
    private lateinit var scoreText: TextView
    private lateinit var animeStartText: TextView
    private lateinit var animeFinishText: TextView
    private lateinit var startTodayCheckbox: CheckBox
    private lateinit var finishTodayCheckbox: CheckBox
    private lateinit var startDateListener: OnDateSetListener
    private lateinit var finishDateListener: OnDateSetListener

    private var animeStatus: String? = null
    private var animeEpisodes: String? = null
    private var animeScore: Int? = 0
    private var startDate: String? = null
    private var finishDate: String? = null

    private lateinit var updateButton: Button
    private lateinit var removeButton: Button
    private lateinit var listener: AnimeUpdateListener

    companion object {
        fun newInstance(
            animeId: Int?, status: String?, episodes: String?, score: Int,
            startDate: String?, finishDate: String?
        ): AnimeUpdateDialog {
            val animeUpdateDialog = AnimeUpdateDialog()
            val bundle = Bundle()
            bundle.putString("status", status)
            bundle.putString("episodes", episodes)
            bundle.putInt("score", score)
            bundle.putString("startDate", startDate)
            bundle.putString("finishDate", finishDate)

            animeUpdateDialog.arguments = bundle
            return animeUpdateDialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        animeScore = arguments?.getInt("score")
        animeStatus = arguments?.getString("status")
        animeEpisodes = arguments?.getString("episodes")
        startDate = arguments?.getString("startDate")
        finishDate = arguments?.getString("finishDate")

        listener = parentFragment as AnimeUpdateListener

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AnimeStatusUpdateBottomSheetBinding.inflate(inflater, container, false)

        animeStatusSpinner = binding.animeStatusSpinner
        episodeIncButton = binding.incrementEpisodeButton
        episodeDecButton = binding.decrementEpisodeButton
        episodeText = binding.episodeCount
        animeScoreSeekBar = binding.animeScoreSeekBar
        scoreText = binding.animeScoreText
        updateButton = binding.updateAnimeStatusButton
        removeButton = binding.removeAnimeButton
        animeStartText = binding.animeStartDateTxt
        animeFinishText = binding.animeFinishDateTxt
        startTodayCheckbox = binding.todayCheckbox1
        finishTodayCheckbox = binding.todayCheckbox2

        context?.let {
            ArrayAdapter.createFromResource(
                it, R.array.user_anime_status, android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                animeStatusSpinner.adapter = adapter
            }
        }


        startTodayCheckbox.setOnCheckedChangeListener { _, state ->
            if (state) {
                animeStartText.text = DateParam.getTodayFormatted()
            }else{
                setStartDate(startDate)
            }
        }

        finishTodayCheckbox.setOnCheckedChangeListener { _, state ->
            if (state) {
                animeFinishText.text = DateParam.getTodayFormatted()
            }else{
                setFinishDate(finishDate)
            }
        }

        animeStartText.setOnClickListener {
            context?.let {
                if (animeStartText.text == "Pick a start date"){
                    createDatePicker(null, startDateListener)
                }else{
                    createDatePicker(DateParam
                        .fromFormattedToDateParam(animeStartText.text.toString()), startDateListener)
                }
            }
        }

        animeFinishText.setOnClickListener {
            context?.let {
                if (animeFinishText.text == "Pick a start date"){
                    createDatePicker(null, finishDateListener)
                }else{
                    createDatePicker(DateParam
                        .fromFormattedToDateParam(animeFinishText.text.toString()), finishDateListener)
                }
            }
        }

        episodeIncButton.setOnClickListener {
            val episodeCount: Int

            if (episodeText.text.isNotEmpty()) {
                episodeCount = episodeText.text.toString().toInt()
                if (episodeCount >= 0) {
                    episodeText.text = (episodeCount + 1).toString().toEditable()
                }
            }

        }

        episodeDecButton.setOnClickListener {
            val episodeCount: Int

            if (episodeText.text.isNotEmpty()) {
                episodeCount = episodeText.text.toString().toInt()
                if (episodeCount > 0) {
                    episodeText.text = (episodeCount - 1).toString().toEditable()
                }
            }
        }

        animeScoreSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                updateSeekBarProgress(progress, scoreText)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

        removeButton.setOnClickListener {
            context?.let { it1 ->
                val dialog = AlertDialog.Builder(context, R.style.SushiAlertDialog)
                    .setTitle(getString(R.string.remove_anime_title))
                    .setMessage(getString(R.string.remove_anime_confirmation))
                    .setPositiveButton(R.string.yes) { _, _ ->
                        listener.onUpdateClick(
                            null, "", 0, 0,
                            true, "", ""
                        )
                        dismiss()
                    }
                    .setNegativeButton(R.string.no) { _, _ -> dismiss() }
                    .create()
                dialog.window?.setBackgroundDrawable(ContextCompat.getDrawable(it1,R.drawable.drawable_alert_dialog_bg))

                dialog.setOnShowListener {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            .setTextColor(it1.getColorFromAttr(R.attr.textColorPrimary))
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                            .setTextColor(it1.getColorFromAttr(R.attr.textColorSecondary))
                }
                dialog.show()
            }


        }

        updateButton.setOnClickListener {
            val status = animeStatusSpinner.selectedItem.toString()
            var episode = 0
            var start = startDate
            var finish = finishDate
            if (episodeText.text.toString().isNotEmpty()) {
                episode = episodeText.text.toString().toInt()
            }
            val score = animeScoreSeekBar.progress
            if (animeStartText.text != getString(R.string.pick_a_start_date)){
                start = DateParam.fromFormattedToMalFormat(animeStartText.text.toString())
            }
            if (animeFinishText.text != getString(R.string.pick_a_finish_date)){
                finish = DateParam.fromFormattedToMalFormat(animeFinishText.text.toString())
            }

            listener.onUpdateClick(
                null, status, episode, score,
                false,start, finish
            )
            dismiss()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        startDateListener =
            OnDateSetListener { _, year, month, dayOfMonth ->
                animeStartText.text = DateParam
                    .getFormattedDate("$year-${month+1}-$dayOfMonth") }

        finishDateListener =
            OnDateSetListener { _, year, month, dayOfMonth ->
                animeFinishText.text = DateParam
                    .getFormattedDate("$year-${month+1}-$dayOfMonth") }

        setSpinnerSelected(animeStatus, animeStatusSpinner)
        setEpisodesWatched(animeEpisodes, episodeText)
        setScoreProgress(animeScore, scoreText, animeScoreSeekBar)
        setStartDate(startDate)
        setFinishDate(finishDate)

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {}

    override fun onNothingSelected(p0: AdapterView<*>?) {}

    @Suppress("UNCHECKED_CAST")
    private fun setSpinnerSelected(item: String?, spinner: Spinner) {
        val adapter = spinner.adapter as ArrayAdapter<String>
        val array = resources.getStringArray(R.array.user_anime_status)
        when (item?.trim()) {
            getString(R.string.watching) -> {
                spinner.setSelection(adapter.getPosition(array[0]))
            }
            getString(R.string.completed) -> {
                spinner.setSelection(adapter.getPosition(array[1]))
            }
            getString(R.string.plan_to_watch) -> {
                spinner.setSelection(adapter.getPosition(array[2]))
            }
            getString(R.string.on_hold) -> {
                spinner.setSelection(adapter.getPosition(array[3]))
            }
            getString(R.string.dropped) -> {
                spinner.setSelection(adapter.getPosition(array[4]))
            }
        }

    }

    private fun setStartDate(date: String?){
        if (date != null){
            animeStartText.text = DateParam.getFormattedDate(date)
        }else { animeStartText.text = getString(R.string.pick_a_start_date) }
    }

    private fun setFinishDate(date: String?){
        if (date != null){
            animeFinishText.text = DateParam.getFormattedDate(date)
        }else { animeFinishText.text = getString(R.string.pick_a_finish_date) }
    }

    private fun setEpisodesWatched(episodesWatched: String?, episodeText: TextView) {
        episodeText.text = episodesWatched
    }

    private fun setScoreProgress(animeScore: Int?, textView: TextView, seekBar: SeekBar) {
        seekBar.progress = animeScore ?: 0
        updateSeekBarProgress(animeScore ?: 0, textView)
    }

    private fun updateSeekBarProgress(progress: Int, textView: TextView) {

        when (progress) {
            0 -> {
                textView.text = getString(R.string.nothing)
            }
            1 -> {
                textView.text = getString(R.string.score_one)
            }
            2 -> {
                textView.text = getString(R.string.score_two)
            }
            3 -> {
                textView.text = getString(R.string.score_three)
            }
            4 -> {
                textView.text = getString(R.string.score_four)
            }
            5 -> {
                textView.text = getString(R.string.score_five)
            }
            6 -> {
                textView.text = getString(R.string.score_six)
            }
            7 -> {
                textView.text = getString(R.string.score_seven)
            }
            8 -> {
                textView.text = getString(R.string.score_eight)
            }
            9 -> {
                textView.text = getString(R.string.score_nine)
            }
            10 -> {
                textView.text = getString(R.string.score_ten)
            }
        }
    }

    private fun createDatePicker(dateParam: DateParam?, listener: OnDateSetListener){
        context?.let {

            val minDate = Calendar.getInstance()
            minDate.set(1990, 12, 1)

            if (dateParam != null){
                val datePicker = DatePickerDialog(it, R.style.SushiDatePicker,
                    listener,
                    dateParam.year.toInt(),
                    dateParam.month.toInt().minus(1),
                    dateParam.day.toInt())
                datePicker.datePicker.minDate = minDate.timeInMillis
                datePicker.datePicker.maxDate = System.currentTimeMillis()
                datePicker.show()
            }else {
                val datePicker = DatePickerDialog(it, R.style.SushiDatePicker,
                    listener,
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
                datePicker.datePicker.maxDate = System.currentTimeMillis()
                datePicker.show()
            }


        }
    }



}

interface AnimeUpdateListener {
    fun onUpdateClick(
        animeId: Int? = null, status: String, episodes: Int,
        score: Int, remove: Boolean, startDate: String?, finishDate: String?
    )
}


