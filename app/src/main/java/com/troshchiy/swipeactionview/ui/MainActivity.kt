package com.troshchiy.swipeactionview.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.troshchiy.swipeactionview.R
import com.troshchiy.swipeactionview.extensions.toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
    }

    private fun setupViews() {
        oneSideSwipeActionViewProgress.onAccept = {
            oneSideSwipeActionViewProgress.setText("Done")
            oneSideSwipeActionViewProgress.showProgress()
        }

        twoSidesSwipeActionViewProgress.onAccept = {
            twoSidesSwipeActionViewProgress.showProgress()
        }

        oneSideSwipeActionView.onAccept = { toast("Accept") }

        twoSidesSwipeActionView.onAccept = { toast("Accept") }
        twoSidesSwipeActionView.onReject = { toast("Reject") }
    }
}