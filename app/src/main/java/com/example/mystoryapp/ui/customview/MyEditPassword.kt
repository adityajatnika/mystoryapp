package com.example.mystoryapp.ui.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.mystoryapp.R

class MyEditPassword : AppCompatEditText {
    private lateinit var checklistIcon: Drawable
    private lateinit var passwordIcon: Drawable
    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        checklistIcon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_check_24) as Drawable
        passwordIcon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_lock_24) as Drawable
        setIconDrawables()
        hint = "Password"

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//                setEditDrawables()
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    if (s.toString().length > 5)showCheck()else showError()
                }
            }
            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }

    private fun showCheck() {
        setIconDrawables(passwordIcon, null, checklistIcon, null)
    }

    private fun showError() {
        setIconDrawables()
        error= "Must be at least 6 characters"
    }

    private fun showCheckButton() {
//        setEditDrawables(endOfTheText = checklistIcon)
    }

    private fun setIconDrawables(
        startOfTheText: Drawable? = passwordIcon,
        topOfTheText:Drawable? = null,
        endOfTheText:Drawable? = null,
        bottomOfTheText: Drawable? = null
    ){
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

//    override fun onTouch(v: View?, event: MotionEvent): Boolean {
//        if (compoundDrawables[2] != null) {
//            val clearButtonStart: Float
//            val clearButtonEnd: Float
//            var isClearButtonClicked = false
//            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
//                clearButtonEnd = (warningButton.intrinsicWidth + paddingStart).toFloat()
//                when {
//                    event.x < clearButtonEnd -> isClearButtonClicked = true
//                }
//            } else {
//                clearButtonStart = (width - paddingEnd - warningButton.intrinsicWidth).toFloat()
//                when {
//                    event.x > clearButtonStart -> isClearButtonClicked = true
//                }
//            }
//            if (isClearButtonClicked) {
//                when (event.action) {
//                    MotionEvent.ACTION_DOWN -> {
//                        warningButton = ContextCompat.getDrawable(context, R.drawable.ic_baseline_error_outline_24) as Drawable
//                        showClearButton()
//                        return true
//                    }
//                    MotionEvent.ACTION_UP -> {
//                        warningButton = ContextCompat.getDrawable(context, R.drawable.ic_baseline_error_outline_24) as Drawable
//                        when {
//                            text != null -> text?.clear()
//                        }
//                        hideClearButton()
//                        return true
//                    }
//                    else -> return false
//                }
//            } else -> return false
//        }
//        return false
//    }
}