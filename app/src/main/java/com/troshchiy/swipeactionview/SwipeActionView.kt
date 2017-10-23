package com.troshchiy.swipeactionview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.activity_main.view.*


public class SwipeActionView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    init {
        init()
    }

    private fun init() {
        if (isInEditMode) return

        View.inflate(context, R.layout.swipe_action_view, this)

        swipeActionView.setOnTouchListener(SwipeActionOnTouchListener())
    }
}