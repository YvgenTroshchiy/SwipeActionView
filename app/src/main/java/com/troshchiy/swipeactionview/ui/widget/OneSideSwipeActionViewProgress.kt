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
import kotlinx.android.synthetic.main.one_side_swipe_action_view_progress.view.*

class OneSideSwipeActionViewProgress @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private var TAG = getLogTag<OneSideSwipeActionViewProgress>()

    var onAccept: () -> Unit = {}

    private val threshold = APP.dimension(R.dimen.swipeView_threshold)

    private var initialSliderX = 0f
    private var sliderWidth = 0
    private var minSliderX = 0f
    private var maxSliderX = 0f

    private var lastSwipeColor = Color.WHITE
    private var acceptColor = context.color(R.color.accept)

    private lateinit var drawable: Drawable

    private val animDuration = 400L

    init {
        init()
    }

    private fun init() {
        if (isInEditMode) return

        inflate(context, R.layout.one_side_swipe_action_view_progress, this)

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
        }
    }

    private fun getColorByMove(c: Int, value: Float) =
            Color.argb(Math.round(Color.alpha(c) * value), Color.red(c), Color.green(c), Color.blue(c))

    private fun actionUp(x: Float) {
        when {
            x >= maxSliderX - threshold -> acceptSwipe()
            else -> bringBackSlider()
        }
    }

    private fun acceptSwipe() {
        slider.animateX(maxSliderX)
        onAccept()
    }

    private fun bringBackSlider() {
        slider.animateX(initialSliderX)
        animRootLayoutBg(lastSwipeColor, Color.WHITE)

        debugBackState()
    }

    private fun debugBackState() {
        setText("Swipe to Accept")
        hideProgress()
    }

    private fun animRootLayoutBg(colorFrom: Int, colorTo: Int) {
        val animator = ValueAnimator.ofArgb(colorFrom, colorTo)
        lastSwipeColor = colorTo
        animator.addUpdateListener { drawable.setColorFilter(it.animatedValue as Int, PorterDuff.Mode.SRC_ATOP) }
        animator.interpolator = DecelerateInterpolator()
        animator.setDuration(animDuration).start()
    }

    private fun getValueConsideringTheLimits(value: Float, min: Float, max: Float) = Math.min(Math.max(min, value), max)

    fun showProgress() = toggleProgress(1)
    fun hideProgress() = toggleProgress(0)

    private fun toggleProgress(childIndex: Int) = slider.post { slider.displayedChild = childIndex }

    fun setText(text: String) {
        textView.text = text
    }

    fun setImage(imageId: Int) = image.setImageDrawable(APP.getDrawable(imageId))

}