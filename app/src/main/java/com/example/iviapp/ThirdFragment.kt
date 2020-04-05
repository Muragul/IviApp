package com.example.iviapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.profile_page.*
import org.w3c.dom.Text

class ThirdFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView: ViewGroup = inflater
            .inflate(R.layout.profile_page,
                container, false) as ViewGroup
        

        return rootView


    }

}
