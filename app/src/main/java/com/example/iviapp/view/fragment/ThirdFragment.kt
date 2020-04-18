package com.example.iviapp.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.iviapp.R
import com.example.iviapp.view.activity.MainActivity
import com.example.iviapp.model.account.CurrentUser
import com.example.iviapp.view_model.ProfileViewModel
import com.example.iviapp.view_model.ViewModelProviderFactory

class ThirdFragment : Fragment() {
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: ViewGroup = inflater
            .inflate(
                R.layout.profile_page,
                container, false
            ) as ViewGroup
        val name: TextView = rootView.findViewById(R.id.name)
        val logoutBtn: Button = rootView.findViewById(R.id.logout)
        name.text = CurrentUser.user.userName

        val viewModelProviderFactory = ViewModelProviderFactory(activity as Context)
        profileViewModel = ViewModelProvider(this, viewModelProviderFactory)
            .get(ProfileViewModel::class.java)

        logoutBtn.setOnClickListener {
            profileViewModel.logout(rootView)
            val intent = Intent(rootView.context, MainActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        return rootView
    }

}
