package com.troshchiy.swipeactionview.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.troshchiy.swipeactionview.R
import com.troshchiy.swipeactionview.extensions.toast
import com.troshchiy.swipeactionview.ui.widget.TwoSidesSwipeActionView


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val twoSidesSwipeActionView = findViewById<TwoSidesSwipeActionView>(R.id.swipeActionView)
        twoSidesSwipeActionView.onAccept = { toast("Accept") }
        twoSidesSwipeActionView.onReject = { toast("Reject") }
    }
}