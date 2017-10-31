package com.troshchiy.swipeactionview.extensions

import android.content.Context
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Toast


fun Context.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(this, text, duration).show()

fun Context.color(@ColorRes res: Int) = ContextCompat.getColor(this, res)

fun Context.dpToPx(dp: Float): Int = (dp * resources.displayMetrics.density + 0.5f).toInt()

fun Context.dimension(res: Int): Float = resources.getDimension(res)

fun View.animateX(x: Float, duration: Long = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()) =
        animate().x(x).setDuration(duration).start()