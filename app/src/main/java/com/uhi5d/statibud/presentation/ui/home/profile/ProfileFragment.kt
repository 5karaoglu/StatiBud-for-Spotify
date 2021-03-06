package com.uhi5d.statibud.presentation.ui.home.profile

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.squareup.picasso.Picasso
import com.uhi5d.statibud.application.ToastHelper
import com.uhi5d.statibud.databinding.FragmentProfileBinding
import com.uhi5d.statibud.domain.model.currentuser.CurrentUser
import com.uhi5d.statibud.domain.model.devices.Devices
import com.uhi5d.statibud.util.DataState
import com.uhi5d.statibud.util.showIf
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject


@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private val TAG = "Profile Fragment"
    private val viewModel : ProfileViewModel by viewModels()

    @Inject lateinit var toastHelper: ToastHelper

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.token.observe(viewLifecycleOwner){
            if (it.length > 10){
                viewModel.getProfileInfo(it)
                viewModel.getAvailableDevices(it)
            }
        }

        viewModel.profileInfo.observe(viewLifecycleOwner){ state ->
            binding.successScreen.showIf {_ -> state is DataState.Success }
            binding.shimmer.showIf {_ -> state is  DataState.Loading }
            when(state){
                is DataState.Success -> {setUser(state.data)
                initCollapsingToolbar(state.data)}
                is DataState.Fail -> toastHelper.errorMessage(requireContext(),state.e.message!!)
            }
        }

        viewModel.devices.observe(viewLifecycleOwner){
            binding.devicesLayout.showIf {_ -> it is DataState.Success }
            binding.shimmer.showIf {_ -> it is  DataState.Loading }
            when(it){
                is DataState.Success -> {generateAvailableDevices(it.data)}
                is DataState.Fail -> toastHelper.errorMessage(requireContext(),it.e.message!!)
            }
        }

    }

    private fun generateAvailableDevices(devices: Devices) {
        val adapter = AvailableDeviceAdapter(requireContext())
        adapter.setDevices(devices)
        val layoutManager = LinearLayoutManager(requireContext())
        with(binding) {
            recyclerDevices.layoutManager = layoutManager
            recyclerDevices.adapter = adapter
        }
    }
    private fun initCollapsingToolbar(user: CurrentUser){
        detailedToolbarProfile.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        //collapsing toolbar design section
        var isShow = true
        var scrollRange = -1
        appBarDetailedArtist.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener {
                appBarLayout, verticalOffset ->
            if (scrollRange == -1){
                scrollRange = appBarLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0){
                collapsingToolbarArtist.title = user.displayName
                isShow = true
            } else if (isShow){
                collapsingToolbarArtist.title = " "
                isShow = false
            }})
        /////////////////////////////////////////////////////////////////////////////////////
    }
    private fun setUser(user: CurrentUser){
        with(user){
            Picasso.get()
                .load(images?.get(0)?.url)
                .noFade()
                .into(ivProfilePhoto)

            tvParallaxHeaderProfile.text = displayName
            tvId.text = id
            tvEmail.text = email
            tvFollowers.text = followers?.total.toString()
            tvCountry.text = country
            tvAccountStatus.text = product
        }

        fabProfile.setOnClickListener {
            try {
                val uri = Uri.parse("http://open.spotify.com/user/${id}")
                val intent = Intent(Intent.ACTION_VIEW,uri)
                startActivity(intent)
            }catch (ex: ActivityNotFoundException){
                toastHelper.errorMessage(requireContext(),ex.message!!)
            }
        }
    }

}