package com.troshchiy.swipeactionview

import android.content.Context

fun Context.dpToPx(dp: Float): Int = (dp * resources.displayMetrics.density + 0.5f).toInt()