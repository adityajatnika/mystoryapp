package com.example.mystoryapp.ui.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
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
        error= context.getString(R.string.error_password)
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
}