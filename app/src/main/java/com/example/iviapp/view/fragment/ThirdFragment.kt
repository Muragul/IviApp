package com.example.iviapp.view.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.iviapp.R
import com.example.iviapp.view.activity.MainActivity
import com.example.iviapp.model.account.CurrentUser
import com.example.iviapp.view_model.ProfileViewModel
import com.example.iviapp.view_model.ViewModelProviderFactory

class ThirdFragment : Fragment() {
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var progressBar: ProgressBar

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
        progressBar = rootView.findViewById(R.id.progressBar)

        val viewModelProviderFactory = ViewModelProviderFactory(activity as Context)
        profileViewModel = ViewModelProvider(this, viewModelProviderFactory)
            .get(ProfileViewModel::class.java)
        profileViewModel.liveData.observe(this, Observer { result ->
            when (result) {
                is ProfileViewModel.State.ShowLoading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is ProfileViewModel.State.HideLoading -> {
                    progressBar.visibility = View.GONE
                }
                is ProfileViewModel.State.Result -> {
                    if (result.isSuccess) {
                        val savedUser: SharedPreferences = rootView.context.getSharedPreferences(
                            "current_user",
                            Context.MODE_PRIVATE
                        )
                        savedUser.edit().remove("current_user").apply()
                        val intent = Intent(rootView.context, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                }
            }
        })

        logoutBtn.setOnClickListener {
            profileViewModel.logout(rootView)
        }
        return rootView
    }

}