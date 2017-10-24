package com.troshchiy.swipeactionview.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
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

    private val threshold = APP.dpToPx(36f)

    private var initialSliderX = 0f
    private var sliderWidth = 0
    private var minSliderX = 0f
    private var maxX = 0f

    private lateinit var drawable: Drawable

    private val animDuration = 400L

    init {
        init()
    }

    private fun init() {
        if (isInEditMode) return

        View.inflate(context, R.layout.swipe_action_view, this)

        initDimensions()

        setRootLayoutBg()

        slider.setOnTouchListener(MoveOnTouchListener({ actionMove(it) }, { actionUp(it) }))
    }

    private fun initDimensions() {
        slider.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        slider.viewTreeObserver.removeOnGlobalLayoutListener(this)

                        initialSliderX = slider.x
                        sliderWidth = slider.width

                        minSliderX = rootLayout.x
                        maxX = rootLayout.x + rootLayout.width - sliderWidth

                        Log.d(TAG, "sliderWidth: $sliderWidth, minSliderX: $minSliderX, maxX: $maxX")
                    }
                })
    }

    private fun setRootLayoutBg() {
        drawable = ContextCompat.getDrawable(context, R.drawable.swipe_action_view_background).mutate()
        rootLayout.background = drawable
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

    private fun acceptSwipe() {
        logD(TAG, "acceptSwipe")
        context.toast("acceptSwipe")

        slider.animateX(maxX)

        val colorFrom = context.color(android.R.color.white)
        val colorTo = context.color(R.color.accept)
        animRootLayoutBg(colorFrom, colorTo)
    }

    private fun rejectSwipe() {
        logD(TAG, "rejectSwipe")
        context.toast("rejectSwipe")

        slider.animateX(minSliderX)

        val colorFrom = context.color(android.R.color.white)
        val colorTo = context.color(R.color.reject)
        animRootLayoutBg(colorFrom, colorTo)
    }

    private fun bringBackSlider() {
        logD(TAG, "bringBackSlider")
        slider.animateX(initialSliderX)

        val colorFrom = context.color(R.color.accept)
        val colorTo = Color.WHITE
        animRootLayoutBg(colorFrom, colorTo)
    }

    private fun animRootLayoutBg(colorFrom: Int, colorTo: Int) {
        val animator = ValueAnimator.ofArgb(colorFrom, colorTo)
        animator.addUpdateListener { drawable.setColorFilter(it.animatedValue as Int, PorterDuff.Mode.SRC_ATOP) }
        animator.setDuration(animDuration).start()
    }

    private fun getAnimColor(color: Int, value: Float) =
            Color.argb(
                    Math.round(Color.alpha(color) * value),
                    Color.red(color),
                    Color.green(color),
                    Color.blue(color))

    private fun getRootLayoutBg(): Int {
        val background = rootLayout.background
        return (background as? ColorDrawable)?.color ?: Color.WHITE
    }

    private fun getValueConsideringTheLimits(value: Float, min: Float, max: Float) = Math.min(Math.max(min, value), max)

}