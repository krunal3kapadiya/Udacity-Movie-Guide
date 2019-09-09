package com.krunal3kapadiya.popularmovies.data

import android.widget.ImageView

/**
 * @author krunal kapadiya
 * @link https://krunal3kapadiya.app
 * @date 14,April,2019
 */

interface OnItemClick {
    fun onItemClick(
            pos: Int,
            view: ImageView?,
            title: String,
            id: Int,
            themeDarkColor: Int,
            themeLightColor: Int
    )
}