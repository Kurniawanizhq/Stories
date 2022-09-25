package com.eone.submission2_bpai.components

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.eone.submission2_bpai.utils.Helper.isValidEmail
import com.eone.submission2_bpai.R

class EmailEditText : AppCompatEditText, View.OnTouchListener {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        when {
            focused -> {
                clearButton.setTint(ContextCompat.getColor(context, R.color.skin_color))
            }
            !focused -> {
                clearButton.setTint(ContextCompat.getColor(context, R.color.cream))
            }
        }
    }

    init {
        maxLines = 1
        setTextColor(ContextCompat.getColor(context, R.color.white))
        setHintTextColor(ContextCompat.getColor(context, R.color.gray))
        hint = "Email"
        clearButton =
            ContextCompat.getDrawable(context, R.drawable.ic_clear) as Drawable
        background =if(!isEnabled){
            ContextCompat.getDrawable(context, R.drawable.bg_edittext) as Drawable
        }else{
            ContextCompat.getDrawable(context, R.drawable.bg_outlined_textfield) as Drawable
        }
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (text?.isNotEmpty() == true) {
                    if (!text.toString().isValidEmail()) {
                        error = context.getString(R.string.invalid_email)
                    } else {
                        hideClearIcon()
                        showClearIcon()
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }


    private var clearButton: Drawable

    private fun showClearIcon() {
        setEndIcon(icon = clearButton)
    }

    private fun hideClearIcon() {
        setEndIcon()
    }

    private fun setEndIcon(@Nullable icon: Drawable? = null) {
        setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        compoundDrawables[2]?.let {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClicked = false
            if (v?.layoutDirection == LAYOUT_DIRECTION_RTL) {
                clearButtonStart = (clearButton.intrinsicWidth + paddingStart).toFloat()
                when {
                    event!!.x <= clearButtonStart -> isClicked = true
                }
            } else {
                clearButtonEnd = (width - paddingEnd - clearButton.intrinsicWidth).toFloat()
                when {
                    event!!.x >= clearButtonEnd -> isClicked = true
                }
            }
            if (isClicked) {
                when (event!!.action) {
                    MotionEvent.ACTION_UP -> {
                        clearButton = ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_clear
                        ) as Drawable
                        text?.clear()
                        hideClearIcon()
                        error = context.getString(R.string.invalid_email)
                        return true
                    }
                }
            }
            return false
        }
        return false
    }


}