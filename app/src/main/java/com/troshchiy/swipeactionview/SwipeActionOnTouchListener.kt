package com.troshchiy.swipeactionview

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View


class SwipeActionOnTouchListener : View.OnTouchListener {

    private var daowViewX = 0f
    private var downX = 0f
    private var deltaX = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent) = when (event.action) {
        MotionEvent.ACTION_DOWN -> {
            actionDown(v, event)
            true
        }
        MotionEvent.ACTION_MOVE -> {
            actionMove(event)
            true
        }
        MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
            actionUp(event)
            true
        }
        else -> false
    }

    private fun actionDown(v: View, event: MotionEvent) {
        daowViewX = v.x
        downX = event.rawX
    }

    private fun actionMove(event: MotionEvent) {
    }

    private fun actionUp(event: MotionEvent) {
    }
}