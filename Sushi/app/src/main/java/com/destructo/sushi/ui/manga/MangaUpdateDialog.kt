package com.destructo.sushi.ui.manga

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.destructo.sushi.R
import com.destructo.sushi.databinding.MangaStatusUpdateBottomSheetBinding
import com.destructo.sushi.util.getColorFromAttr
import com.destructo.sushi.util.toEditable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import timber.log.Timber

class MangaUpdateDialog :BottomSheetDialogFragment(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: MangaStatusUpdateBottomSheetBinding
    private lateinit var mangaStatusSpinner: Spinner
    private lateinit var chapterIncButton: MaterialButton
    private lateinit var chapterDecButton: MaterialButton
    private lateinit var chapterText: EditText
    private lateinit var volumeIncButton: MaterialButton
    private lateinit var volumeDecButton: MaterialButton
    private lateinit var volumeText: EditText
    private lateinit var mangaScoreSeekBar: SeekBar
    private lateinit var scoreText: TextView

    private lateinit var updateButton: Button
    private lateinit var removeButton: Button

    private var mangaStatus:String?=null
    private var mangaChapters:String?=null
    private var mangaVolumes:String?=null
    private var mangaScore:Int?=0

    private lateinit var listener: MangaUpdateListener

    companion object{
        fun newInstance(status:String?, chapters:String?, volumes:String?, score:Int): MangaUpdateDialog {
            val mangaUpdateDialog = MangaUpdateDialog()
            val bundle = Bundle()
            bundle.putString("status",status)
            bundle.putString("chapters",chapters)
            bundle.putString("volumes",volumes)
            bundle.putInt("score",score)
            mangaUpdateDialog.arguments = bundle
            return mangaUpdateDialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mangaScore = arguments?.getInt("score")
        mangaStatus = arguments?.getString("status")
        mangaChapters = arguments?.getString("chapters")
        mangaVolumes = arguments?.getString("volumes")


        listener = parentFragment as MangaUpdateListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MangaStatusUpdateBottomSheetBinding.inflate(inflater, container, false)

        mangaStatusSpinner = binding.mangaStatusSpinner
        chapterIncButton = binding.incrementChapterButton
        chapterDecButton = binding.decrementChapterButton
        chapterText = binding.chapterCount
        volumeIncButton = binding.incrementVolumeButton
        volumeDecButton = binding.decrementVolumeButton
        volumeText = binding.volumeCount
        mangaScoreSeekBar = binding.mangaScoreSeekBar
        scoreText = binding.mangaScoreText
        updateButton = binding.updateMangaStatusButton
        removeButton = binding.removeMangaButton

        context?.let {
            ArrayAdapter.createFromResource(
                it, R.array.user_manga_status, android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                mangaStatusSpinner.adapter = adapter
            }
        }

        chapterIncButton.setOnClickListener {
            var chapterCount = 0

            if(chapterText.text.isNotEmpty()){
                chapterCount = chapterText.text.toString().toInt()
            }
            if(chapterCount >= 0){
                chapterText.text = (chapterCount + 1).toString().toEditable()
            }
        }

        chapterDecButton.setOnClickListener {
            var chapterCount = 0

            if(chapterText.text.isNotEmpty()){
                chapterCount = chapterText.text.toString().toInt()
            }

            if(chapterCount > 0){
                chapterText.text = (chapterCount - 1).toString().toEditable()
            }
        }

        volumeIncButton.setOnClickListener {
            var volumeCount = 0

            if(volumeText.text.isNotEmpty()){
                volumeCount = volumeText.text.toString().toInt()
            }
            if(volumeCount >= 0){
                volumeText.text = (volumeCount + 1).toString().toEditable()
            }
        }

        volumeDecButton.setOnClickListener {
            var volumeCount = 0

            if(volumeText.text.isNotEmpty()){
                volumeCount = volumeText.text.toString().toInt()
            }

            if(volumeCount > 0){
                volumeText.text = (volumeCount - 1).toString().toEditable()
            }
        }

        mangaScoreSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                updateSeekBarProgress(progress, scoreText)

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

        removeButton.setOnClickListener {
            val dialog = AlertDialog.Builder(context, R.style.SushiAlertDialog)
                .setTitle("Remove manga from list")
                .setMessage("Are you sure you want to remove this manga from your list?")
                .setPositiveButton(R.string.yes
                ) { _, _ ->
                    listener.onUpdateClick("",0,0,0,true)
                    dismiss()
                }
                .setNegativeButton(R.string.no
                ) { _, _ -> dismiss() }
                .create()

            dialog.setOnShowListener {
                val view = dialog.window
                view?.setBackgroundDrawable(context?.let { it1 -> ContextCompat.getDrawable(it1,R.drawable.drawable_alert_dialog_bg) })
                context?.let {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(it.getColorFromAttr(R.attr.textColorPrimary))
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(it.getColorFromAttr(R.attr.textColorSecondary))
                }
            }

            dialog.show()

        }

        updateButton.setOnClickListener {

            val status = mangaStatusSpinner.selectedItem.toString()
            var chapter = 0
            if(chapterText.text.toString().isNotEmpty()){
                chapter = chapterText.text.toString().toInt()
            }
            var volume = 0
            if(volumeText.text.toString().isNotEmpty()){
                volume = volumeText.text.toString().toInt()

            }
            val score = mangaScoreSeekBar.progress
            listener.onUpdateClick(status, chapter, volume, score, false)
            dismiss()

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setSpinnerSelected(mangaStatus,mangaStatusSpinner)
        setChaptersRead(mangaChapters, chapterText)
        setVolumeRead(mangaVolumes, volumeText)
        setScoreProgress(mangaScore, scoreText, mangaScoreSeekBar)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        Timber.e("Do nothing")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        Timber.e("Do nothing")
    }


    @Suppress("UNCHECKED_CAST")
    private fun setSpinnerSelected(item: String?, spinner: Spinner){
        val adapter = spinner.adapter as ArrayAdapter<String>
        val array = resources.getStringArray(R.array.user_manga_status)
        when(item?.trim()){
            getString(R.string.reading)->{spinner.setSelection(adapter.getPosition(array[0]))}
            getString(R.string.completed)->{spinner.setSelection(adapter.getPosition(array[1]))}
            getString(R.string.plan_to_read)->{spinner.setSelection(adapter.getPosition(array[2]))}
            getString(R.string.on_hold)->{spinner.setSelection(adapter.getPosition(array[3]))}
            getString(R.string.dropped)->{spinner.setSelection(adapter.getPosition(array[4]))}
        }

    }

    private fun setChaptersRead(chaptersRead: String?, chapterText: TextView) {
        chapterText.text = chaptersRead
    }

    private fun setVolumeRead(volumeRead: String?, volumeText: TextView) {
        volumeText.text = volumeRead
    }

    private fun setScoreProgress(mangaScore: Int?, textView: TextView, seekBar: SeekBar) {
        seekBar.progress = mangaScore?:0
        updateSeekBarProgress(mangaScore?:0, textView)
    }

    private fun updateSeekBarProgress(progress: Int, textView: TextView){

        when(progress){
            0->{textView.text = getString(R.string.nothing)}
            1->{textView.text = getString(R.string.score_one)}
            2->{textView.text = getString(R.string.score_two)}
            3->{textView.text = getString(R.string.score_three)}
            4->{textView.text = getString(R.string.score_four)}
            5->{textView.text = getString(R.string.score_five)}
            6->{textView.text = getString(R.string.score_six)}
            7->{textView.text = getString(R.string.score_seven)}
            8->{textView.text = getString(R.string.score_eight)}
            9->{textView.text = getString(R.string.score_nine)}
            10->{textView.text = getString(R.string.score_ten)}
        }

    }

}

interface MangaUpdateListener{
    fun onUpdateClick(status:String, chapters:Int,volume:Int, score:Int, remove:Boolean)
}

