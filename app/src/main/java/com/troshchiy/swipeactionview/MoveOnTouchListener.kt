package com.troshchiy.swipeactionview

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.MotionEvent.INVALID_POINTER_ID
import android.view.View


class MoveOnTouchListener(private val move: (Float) -> Unit) : View.OnTouchListener {

    // The ‘active pointer’ is the one currently moving our object.
    private var activePointerId = INVALID_POINTER_ID

    private var lastTouchX = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent) = when (event.actionMasked) {
        MotionEvent.ACTION_DOWN -> {
            actionDown(event)
            true
        }
        MotionEvent.ACTION_MOVE -> {
            actionMove(v, event)
            true
        }
        MotionEvent.ACTION_POINTER_UP -> {
            actionPointerUp(event)
            true
        }
        MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
            actionUp()
            true
        }
        else -> false
    }

    private fun actionDown(event: MotionEvent) {
        // Save the ID of this pointer (for dragging)
        activePointerId = event.actionIndex

        // Remember where we started (for dragging)
        lastTouchX = event.getX(activePointerId)
    }

    private fun actionMove(v: View, event: MotionEvent) {
        // Find the index of the active pointer and fetch its position
        val pointerIndex = event.findPointerIndex(activePointerId)
        val x = event.getX(pointerIndex)
        val dx = x - lastTouchX

        move(v.x + dx)
    }

    private fun actionPointerUp(event: MotionEvent) {
        val pointerIndex = event.actionIndex
        val pointerId = event.getPointerId(pointerIndex)

        if (pointerId == activePointerId) {
            // This was our active pointer going up. Choose a new active pointer and adjust accordingly.
            val newPointerIndex = if (pointerIndex == 0) 1 else 0
            lastTouchX = event.getX(newPointerIndex)
            activePointerId = event.getPointerId(newPointerIndex)
        }
    }

    private fun actionUp() {
        activePointerId = INVALID_POINTER_ID
    }
}