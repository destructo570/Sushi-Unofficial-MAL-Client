package com.destructo.sushi.ui.anime.animeSongs

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.destructo.sushi.LIST_SPACE_HEIGHT
import com.destructo.sushi.R
import com.destructo.sushi.adapter.AnimeSongAdapter
import com.destructo.sushi.databinding.FragmentAllAnimeOpeningSongsBinding
import com.destructo.sushi.listener.MalUrlListener
import com.destructo.sushi.util.ListItemVerticalDecor
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLEncoder

@AndroidEntryPoint
class OpeningSongsFragment : Fragment() {

    private lateinit var binding: FragmentAllAnimeOpeningSongsBinding
    private val animeSongsViewModel: AnimeSongsViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )
    private var animeIdArg: Int = 0
    private lateinit var openingSongsRecyclerView: RecyclerView
    private lateinit var openingSongsAdapter: AnimeSongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAllAnimeOpeningSongsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner }

        openingSongsRecyclerView = binding.openingSongsRecycler
        openingSongsRecyclerView.layoutManager = LinearLayoutManager(context)
        openingSongsRecyclerView.addItemDecoration(ListItemVerticalDecor(LIST_SPACE_HEIGHT))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        openingSongsAdapter = AnimeSongAdapter(MalUrlListener {

            it?.let { song->
                val options = arrayOf(
                    getString(R.string.youtube),
                    getString(R.string.spotify),
                    getString(R.string.google_search),
                    getString(R.string.copy_title)
                )
                context?.let { it1 ->
                    val alertDialog = AlertDialog.Builder(it1, R.style.SushiAlertDialog)
                    .setTitle(song)
                    .setItems(options
                    ) { _, p1 ->
                        when (p1) {
                            0 -> {
                                openYoutube(song)
                            }
                            1 -> {
                                openSpotify(song)
                            }
                            2 -> {
                                openGoogle(song)
                            }
                            3 -> {
                                copyToClipBoard(song)
                            }
                        }
                    }
                    .create()
                    alertDialog.window?.setBackgroundDrawable(ContextCompat.getDrawable(it1,R.drawable.drawable_alert_dialog_bg))
                    alertDialog.show()
                }
            }


        })

        openingSongsRecyclerView.adapter = openingSongsAdapter
        animeSongsViewModel.openingSongs.observe(viewLifecycleOwner) { songs ->
            songs?.let { openingSongsAdapter.submitList(it) }
        }

    }

    private fun openYoutube(song: String) {
        val url = "${getString(R.string.youtube_search_url)}${URLEncoder.encode(song, "utf-8")}"
        val builder = CustomTabsIntent.Builder()
        val customTabIntent = builder.build()
        customTabIntent.launchUrl(requireContext(), Uri.parse(url))
    }

    private fun openSpotify(song: String) {
        val url = "${getString(R.string.spotify_search_url)}${URLEncoder.encode(song, "utf-8")}"
        val builder = CustomTabsIntent.Builder()
        val customTabIntent = builder.build()
        customTabIntent.launchUrl(requireContext(), Uri.parse(url))
    }

    private fun openGoogle(song: String) {
        val url = "${getString(R.string.google_search_url)}${URLEncoder.encode(song, "utf-8")}"
        val builder = CustomTabsIntent.Builder()
        val customTabIntent = builder.build()
        customTabIntent.launchUrl(requireContext(), Uri.parse(url))
    }

    private fun copyToClipBoard(song: String) {
        val clipboard =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", song)
        clipboard.setPrimaryClip(clipData)

        Toast.makeText(context,
            "${getString(R.string.copied_to_clipboard)}\n$song",
            Toast.LENGTH_SHORT).show()
    }



}