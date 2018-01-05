package com.refresh.chotusalesv1.techicalservices;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.refresh.chotusalesv1.R;

public class KeyboardView extends FrameLayout implements View.OnClickListener {

    private EditText mPasswordField;

    public KeyboardView(Context context) {
        super(context);
        init();
    }

    public KeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //mPasswordField.setHint(ta.getString(R.styleable.KeyboardView_HintText));
        init();

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.KeyboardView, 0, 0);

        final int N = ta.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = ta.getIndex(i);
            switch (attr) {
                case R.styleable.KeyboardView_HintText:
                    //mPasswordField.setHint();

                    mPasswordField.setHint(ta.getString(attr));
                    break;
            }
        }
        ta.recycle();
    }

    public KeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.keyboard, this);
        initViews();
    }

    private void initViews() {
        mPasswordField = $(R.id.password_field);
        mPasswordField.setInputType(InputType.TYPE_NULL);
        $(R.id.t9_key_0).setOnClickListener(this);
        $(R.id.t9_key_1).setOnClickListener(this);
        $(R.id.t9_key_2).setOnClickListener(this);
        $(R.id.t9_key_3).setOnClickListener(this);
        $(R.id.t9_key_4).setOnClickListener(this);
        $(R.id.t9_key_5).setOnClickListener(this);
        $(R.id.t9_key_6).setOnClickListener(this);
        $(R.id.t9_key_7).setOnClickListener(this);
        $(R.id.t9_key_8).setOnClickListener(this);
        $(R.id.t9_key_9).setOnClickListener(this);
        $(R.id.t9_key_clear).setOnClickListener(this);
        $(R.id.t9_key_backspace).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // handle number button click
        if (v.getTag() != null && "number_button".equals(v.getTag())) {
            mPasswordField.append(((TextView) v).getText());
            return;
        }
        switch (v.getId()) {
            case R.id.t9_key_clear: { // handle clear button
                mPasswordField.setText(null);
            }
            break;
            case R.id.t9_key_backspace: { // handle backspace button
                // delete one character
                Editable editable = mPasswordField.getText();
                int charCount = editable.length();
                if (charCount > 0) {
                    editable.delete(charCount - 1, charCount);
                }
            }
            break;
        }
    }

    public String getInputText() {
        return mPasswordField.getText().toString();
    }

    public void SetText(String s)
    {
        mPasswordField.setHint(s);
    }


    protected <T extends View> T $(@IdRes int id) {
        return (T) super.findViewById(id);
    }
}