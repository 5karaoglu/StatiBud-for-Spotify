package com.uhi5d.statibud.presentation.ui.mostlistened

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.uhi5d.statibud.R
import com.uhi5d.statibud.databinding.FragmentMostListenedBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MostListenedFragment : Fragment() {

    private var _binding: FragmentMostListenedBinding? = null
    private val binding get() =  _binding!!

    private lateinit var mlFragmentAdapter: MlFragmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentMostListenedBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mlFragmentAdapter = MlFragmentAdapter(this)
        with(binding){
            viewPager2.adapter = mlFragmentAdapter
            TabLayoutMediator(tabLayout,viewPager2){tab, position ->
                when(position){
                    0 -> tab.text = getString(R.string.top_fragment_tracks)
                    1 -> tab.text = getString(R.string.top_fragment_artists)
                }
            }.attach()
        }
    }


}