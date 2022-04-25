package com.example.mystoryapp.ui.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.mystoryapp.R

class MyEditEmail : AppCompatEditText {
    private lateinit var emailIcon: Drawable
    private lateinit var checklistIcon: Drawable
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
        emailIcon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_person_24) as Drawable
        checklistIcon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_check_24) as Drawable
        setIconDrawables(startOfTheText = emailIcon)
        hint = "Email Address"

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    if (!Patterns.EMAIL_ADDRESS.matcher(s.toString())
                            .matches()
                    ) showError() else showCheck()
                }
            }
            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    private fun showCheck() {
        setIconDrawables(endOfTheText = checklistIcon)
    }


    private fun showError() {
        error = "Must be valid email"
    }

    private fun setIconDrawables(
        startOfTheText: Drawable? = emailIcon,
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