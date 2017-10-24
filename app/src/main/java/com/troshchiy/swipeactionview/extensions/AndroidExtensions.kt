package com.troshchiy.swipeactionview.extensions

import android.content.Context
import android.view.View
import android.widget.Toast

fun Context.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(this, text, duration).show()

fun Context.dpToPx(dp: Float): Int = (dp * resources.displayMetrics.density + 0.5f).toInt()

fun View.animateX(x: Float, duration: Long = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()) =
        animate().x(x).setDuration(duration).start()