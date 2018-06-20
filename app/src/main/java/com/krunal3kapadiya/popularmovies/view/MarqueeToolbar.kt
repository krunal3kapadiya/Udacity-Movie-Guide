package com.krunal3kapadiya.popularmovies.view

import android.content.Context
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.TextView

class MarqueeToolbar : Toolbar {

    internal var title: TextView? = null

    internal var reflected = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun setTitle(title: CharSequence) {
        if (!reflected) {
            reflected = reflectTitle()
        }
        super.setTitle(title)
        selectTitle()
    }

    override fun setTitle(resId: Int) {
        if (!reflected) {
            reflected = reflectTitle()
        }
        super.setTitle(resId)
        selectTitle()
    }

    private fun reflectTitle(): Boolean {
        try {
            val field = Toolbar::class.java.getDeclaredField("mTitleTextView")
            field.isAccessible = true
            title = field?.get(this) as TextView?
            title!!.ellipsize = TextUtils.TruncateAt.MARQUEE

            title!!.isFocusable = true
            title!!.isFocusableInTouchMode = true
            title!!.requestFocus()
            title!!.setSingleLine(true)
            title!!.isSelected = true

            field.isAccessible = true
            title!!.marqueeRepeatLimit = -1
            return true
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
            return false
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return false
        } catch (e: NullPointerException) {
            e.printStackTrace()
            return false
        }

    }

    fun selectTitle() {
        if (title != null)
            title!!.isSelected = true
    }
}