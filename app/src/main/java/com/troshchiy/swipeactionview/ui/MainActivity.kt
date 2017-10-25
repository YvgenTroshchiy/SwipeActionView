package com.troshchiy.swipeactionview.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.troshchiy.swipeactionview.R
import com.troshchiy.swipeactionview.extensions.toast


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val swipeActionView = findViewById<SwipeActionView>(R.id.swipeActionView)

        swipeActionView.onAccept = { toast("Accept") }
        swipeActionView.onReject = { toast("Reject") }
    }
}