package com.troshchiy.swipeactionview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import com.troshchiy.swipeactionview.App.Companion.APP
import kotlinx.android.synthetic.main.swipe_action_view.view.*


class SwipeActionView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private val TAG = SwipeActionView::class.java.simpleName

    private val verticalMargin = APP.resources.getDimension(R.dimen.swipeView_circle_margin)

    private var circleWidth = 0
    private var rootLayoutMin = 0f
    private var rootLayoutMax = 0f

    init {
        init()
    }

    private fun init() {
        if (isInEditMode) return

        View.inflate(context, R.layout.swipe_action_view, this)

        initDimensions()

        image.setOnTouchListener(MoveOnTouchListener{ move(it) })
    }

    private fun initDimensions() {
        image.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        image.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        circleWidth = image.width

                        rootLayoutMin = rootLayout.x + verticalMargin
                        rootLayoutMax = rootLayout.x + rootLayout.width - circleWidth - verticalMargin

                        Log.d(TAG, "circleWidth: $circleWidth, rootLayoutMin: $rootLayoutMin, rootLayoutMax: $rootLayoutMax")
                    }
                })
    }

    private fun move(x: Float) {
        image.x = getValueConsideringTheLimits(x, rootLayoutMin, rootLayoutMax)
    }

    private fun getValueConsideringTheLimits(value: Float, min: Float, max: Float) =
            Math.min(
                    Math.max(min, value),
                    max)

}