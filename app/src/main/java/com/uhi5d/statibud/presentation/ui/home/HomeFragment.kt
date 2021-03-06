package com.uhi5d.statibud.presentation.ui.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.uhi5d.statibud.R
import com.uhi5d.statibud.application.ToastHelper
import com.uhi5d.statibud.databinding.FragmentHomeBinding
import com.uhi5d.statibud.domain.model.recenttracks.RecentTracksItem
import com.uhi5d.statibud.domain.model.recommendations.RecommendationsTrack
import com.uhi5d.statibud.presentation.ui.detailed.track.toDetailedTrackFragmentModel
import com.uhi5d.statibud.util.CustomItemDecoration
import com.uhi5d.statibud.util.DataState
import com.uhi5d.statibud.util.hide
import com.uhi5d.statibud.util.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@InternalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment(),
RecommendationsAdapter.OnItemClickListener,
RecentTracksAdapter.OnItemClickListener{
    private var TAG = "Home Fragment"

    @Inject
    lateinit var toastHelper: ToastHelper

    private var _binding: FragmentHomeBinding? = null
    val binding: FragmentHomeBinding
        get() = _binding!!


    private val viewModel: HomeViewModel by viewModels()
    private lateinit var recommendationsAdapter: RecommendationsAdapter
    private lateinit var recentTracksAdapter: RecentTracksAdapter

    private val recentTracksAdapterCollapsedItemSize = 5
    private var recentTracksAdapterMaxItemSize = 0
    private var isCollapsed = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recommendationsAdapter = RecommendationsAdapter(requireContext(),this)
        recentTracksAdapter = RecentTracksAdapter(requireContext(),this)
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.token.observe(viewLifecycleOwner){
            if (it.length > 10){
                viewModel.getUserDisplayName(it)
                viewModel.getRecentTracks(it)
                viewModel.getRecommendations(it)
            }
        }

        viewModel.user.observe(viewLifecycleOwner){
            if (it != null){
                with(binding){
                    buttonProfile.setOnClickListener(goProfile)
                    tvHeader.text = String.format(getString(R.string.welcome_header),it.displayName)
                    success.show()
                    shimmer.hide()
                }
            }
        }

        with(binding.recyclerRecentHome){
            adapter = recentTracksAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(CustomItemDecoration(10))
        }
        with(binding.recyclerRecommendation){
            adapter = recommendationsAdapter
            layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)
            addItemDecoration(CustomItemDecoration(10))
        }

        viewModel.recommendations.observe(viewLifecycleOwner){ state ->
            when(state){
                is DataState.Success -> {
                    state.data.tracks?.let {
                        recommendationsAdapter.setTrackList(it) }
                }
                is DataState.Fail -> {
                    toastHelper.errorMessage(requireContext(),state.e.message!!)
                }
            }
        }

        viewModel.recentTracks.observe(viewLifecycleOwner){ state ->
            when(state){
                is DataState.Success -> {
                   if(state.data.items!!.isNotEmpty()){
                        binding.buttonRecyclerExpand.setOnClickListener(recentTrackButtonClick)
                        recentTracksAdapter.setRecentTracksList(state.data.items)
                        recentTracksAdapterMaxItemSize = state.data.items.size}
                }
                is DataState.Fail -> toastHelper.errorMessage(requireContext(),state.e.message!!)
            }
        }

    }

    private val goProfile = View.OnClickListener {
        val action = HomeFragmentDirections.actionHomeFragmentToProfileFragment()
        findNavController().navigate(action)
    }

    override fun onItemClicked(recommendationsTrack: RecommendationsTrack) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailedTrackFragment(
            recommendationsTrack.toDetailedTrackFragmentModel()
        )
        findNavController().navigate(action)
    }

    override fun onItemClicked(recentTrack: RecentTracksItem) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailedTrackFragment(
            recentTrack.toDetailedTrackFragmentModel())
        findNavController().navigate(action)
    }

    private val recentTrackButtonClick = View.OnClickListener {
        isCollapsed = if(isCollapsed){
            recentTracksAdapter.setAdapterSize(recentTracksAdapterMaxItemSize)
            binding.buttonRecyclerExpand.text = getString(R.string.show_less)
            false
        }else{
            recentTracksAdapter.setAdapterSize(recentTracksAdapterCollapsedItemSize)
            binding.buttonRecyclerExpand.text = getString(R.string.show_more)
            true
        }
    }


}