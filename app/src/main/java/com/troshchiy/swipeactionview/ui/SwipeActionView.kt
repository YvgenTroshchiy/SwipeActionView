package com.troshchiy.swipeactionview.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import com.troshchiy.swipeactionview.App.Companion.APP
import com.troshchiy.swipeactionview.R
import com.troshchiy.swipeactionview.extensions.*
import kotlinx.android.synthetic.main.swipe_action_view.view.*


class SwipeActionView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private var TAG = getLogTag<SwipeActionView>()

    private val verticalMargin = APP.resources.getDimension(R.dimen.swipeView_circle_margin)
    private val threshold = APP.dpToPx(36f)

    private var initialSliderX = 0f
    private var sliderWidth = 0
    private var minSliderX = 0f
    private var maxX = 0f

    init {
        init()
    }

    private fun init() {
        if (isInEditMode) return

        View.inflate(context, R.layout.swipe_action_view, this)

        initDimensions()

        slider.setOnTouchListener(MoveOnTouchListener({ actionMove(it) }, { actionUp(it) }))
    }

    private fun initDimensions() {
        slider.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        slider.viewTreeObserver.removeOnGlobalLayoutListener(this)

                        initialSliderX = slider.x
                        sliderWidth = slider.width

                        minSliderX = rootLayout.x + verticalMargin
                        maxX = rootLayout.x + rootLayout.width - sliderWidth - verticalMargin

                        Log.d(TAG, "sliderWidth: $sliderWidth, minSliderX: $minSliderX, maxX: $maxX")
                    }
                })
    }

    private fun actionMove(x: Float) {
        slider.x = getValueConsideringTheLimits(x, minSliderX, maxX)
    }

    private fun actionUp(x: Float) {
        when {
            x <= minSliderX + threshold -> rejectSwipe()
            x >= maxX - threshold -> acceptSwipe()
            else -> bringBackSlider()
        }
    }

    private fun rejectSwipe() {
        logD(TAG, "rejectSwipe")
        context.toast("rejectSwipe")

        slider.animateX(minSliderX)
    }

    private fun acceptSwipe() {
        logD(TAG, "acceptSwipe")
        context.toast("acceptSwipe")

        slider.animateX(maxX)
    }

    private fun bringBackSlider() {
        //TODO: Change color
        slider.animateX(initialSliderX)
    }

    private fun getValueConsideringTheLimits(value: Float, min: Float, max: Float) = Math.min(Math.max(min, value), max)

}