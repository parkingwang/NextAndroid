package com.github.yoojia.next.inputs;

import android.widget.TextView;

/**
 * Input wrapper form TextViews(TextView, EditText, Button ...)
 *
 * @author 陈小锅 (yoojia.chen@gmail.com)
 */
public class TextInput<T extends TextView> implements Input{

    public final T inputView;

    public TextInput(T input) {
        inputView = input;
    }

    @Override
    public String onLoadValue() {
        return String.valueOf(inputView.getText());
    }

}
