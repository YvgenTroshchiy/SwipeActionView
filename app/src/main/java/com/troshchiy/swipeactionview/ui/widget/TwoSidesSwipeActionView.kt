package com.troshchiy.swipeactionview.ui.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.ViewTreeObserver
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import com.troshchiy.swipeactionview.App.Companion.APP
import com.troshchiy.swipeactionview.R
import com.troshchiy.swipeactionview.extensions.*
import kotlinx.android.synthetic.main.two_side_swipe_action_view.view.*


class TwoSidesSwipeActionView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private var TAG = getLogTag<TwoSidesSwipeActionView>()

    var onAccept: () -> Unit = {}
    var onReject: () -> Unit = {}

    private val threshold = APP.dimension(R.dimen.swipeView_swipeThreshold)

    private var initialSliderX = 0f
    private var sliderWidth = 0
    private var minSliderX = 0f
    private var maxSliderX = 0f

    private var lastSwipeColor = Color.WHITE
    private var acceptColor = context.color(R.color.accept)
    private var rejectColor = context.color(R.color.reject)

    private lateinit var drawable: Drawable

    private val animDuration = 400L

    init {
        init()
    }

    private fun init() {
        if (isInEditMode) return

        inflate(context, R.layout.two_side_swipe_action_view, this)

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
                        maxSliderX = rootLayout.x + rootLayout.width - sliderWidth

                        logD(TAG, "initialSliderX: $initialSliderX, sliderWidth: $sliderWidth, minSliderX: $minSliderX, maxSliderX: $maxSliderX")
                    }
                })
    }

    private fun setRootLayoutBg() {
        drawable = ContextCompat.getDrawable(context, R.drawable.swipe_action_view_background).mutate()
        rootLayout.background = drawable
    }

    private fun actionMove(dx: Float) {
        slider.x = getValueConsideringTheLimits(slider.x + dx, minSliderX, maxSliderX)
        changeSliderBgColor()
    }

    private fun changeSliderBgColor() {
        // initialSliderX: 534.0, sliderWidth: 259, minSliderX: 0.0, maxSliderX: 1069.0
        val x = slider.x

        if (x > initialSliderX) { // Move Right
            val swipeRatio: Float = (maxSliderX - initialSliderX)
            val ratio: Float = (x - initialSliderX) / swipeRatio
            lastSwipeColor = getColorByMove(acceptColor, ratio)
            drawable.setColorFilter(lastSwipeColor, PorterDuff.Mode.SRC_ATOP)
        } else { // Move Left
            val ratio: Float = (initialSliderX - x) / initialSliderX
            lastSwipeColor = getColorByMove(rejectColor, ratio)
            drawable.setColorFilter(lastSwipeColor, PorterDuff.Mode.SRC_ATOP)
        }
    }

    private fun getColorByMove(c: Int, value: Float) =
            Color.argb(Math.round(Color.alpha(c) * value), Color.red(c), Color.green(c), Color.blue(c))

    private fun actionUp(x: Float) {
        when {
            x <= minSliderX + threshold -> rejectSwipe()
            x >= maxSliderX - threshold -> acceptSwipe()
            else -> bringBackSlider()
        }
    }

    private fun acceptSwipe() {
        slider.animateX(maxSliderX)
        animRootLayoutBg(lastSwipeColor, acceptColor)
        onAccept()
    }

    private fun rejectSwipe() {
        slider.animateX(minSliderX)
        animRootLayoutBg(lastSwipeColor, context.color(R.color.reject))
        onReject()
    }

    private fun bringBackSlider() {
        slider.animateX(initialSliderX)
        animRootLayoutBg(lastSwipeColor, Color.WHITE)
    }

    private fun animRootLayoutBg(colorFrom: Int, colorTo: Int) {
        val animator = ValueAnimator.ofArgb(colorFrom, colorTo)
        lastSwipeColor = colorTo
        animator.addUpdateListener { drawable.setColorFilter(it.animatedValue as Int, PorterDuff.Mode.SRC_ATOP) }
        animator.interpolator = DecelerateInterpolator()
        animator.setDuration(animDuration).start()
    }

    private fun getValueConsideringTheLimits(value: Float, min: Float, max: Float) = Math.min(Math.max(min, value), max)

}