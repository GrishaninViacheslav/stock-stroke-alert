package io.github.grishaninvyacheslav.stock_stroke_alert.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

class EditTextThatLosesFocusWhenKeyboardIsHidden : EditText {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes)

    override fun onEditorAction(actionCode: Int) {
        super.onEditorAction(actionCode)
        if (actionCode == EditorInfo.IME_ACTION_DONE) {
            clearFocus()
            // Почему то, super.onEditorAction(actionCode) не прячет клавиатуру,
            // если после его вызова выполняется clearFocus(),
            // поэтому здесь клавиатура прячестся повторно
            (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
                if (isActive(this@EditTextThatLosesFocusWhenKeyboardIsHidden)) {
                    hideSoftInputFromWindow(windowToken, 0)
                }
            }
        }
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_BACK) {
            clearFocus()
            return false
        }
        return super.dispatchKeyEvent(event)
    }
}