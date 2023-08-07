package com.example.shimon.ciscord

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.shimon.ciscord.databinding.FragmentGroupChatBinding


class GroupChatFragment : Fragment() {
    lateinit var binding:FragmentGroupChatBinding
    private lateinit var group : Group

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGroupChatBinding.inflate(layoutInflater,container,false)

        group = if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){

            requireArguments().getParcelable<Group>("key",Group::class.java)!!
        }
        else{
            requireArguments().getParcelable<Group>("key")!!
        }

        Log.d("group","$group")


        return binding.root
    }


}