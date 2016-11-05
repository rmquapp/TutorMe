package com.accipio.tutorme;

/**
 * Created by rachel on 2016-11-04.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;


public class BubbleTextView extends MultiAutoCompleteTextView implements OnItemClickListener {

    public BubbleTextView(Context context) {
        super(context);
        init(context);
    }

    public BubbleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BubbleTextView(Context context, AttributeSet attrs,
                                          int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context context){
        setOnItemClickListener(this);
        addTextChangedListener(textWatcher);
    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if(count >= 1){
                if(s.charAt(start) == ' ') {
                    setBubbles();
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,int after) {}

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private void setBubbles() {

        SpannableStringBuilder sb = new SpannableStringBuilder();
        String fullString = getText().toString();

        String[] strings = fullString.split(" ");

        for (int i = 0; i < strings.length; i++) {

            String string = strings[i].toUpperCase();
            sb.append(string);

            if (fullString.charAt(fullString.length() - 1) != " ".charAt(0) && i == strings.length - 1) {
                break;
            }

            BitmapDrawable bd = new BitmapDrawable(createTokenView(string));
            bd.setBounds(0, 0, bd.getIntrinsicWidth(), bd.getIntrinsicHeight());

            int startIdx = sb.length() - (string.length());
            int endIdx = sb.length();

            sb.setSpan(new ImageSpan(bd), startIdx, endIdx, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            if (i < strings.length - 1) {
                sb.append(" ");
            } else if (fullString.charAt(fullString.length() - 1) == " ".charAt(0)) {
                sb.append(" ");
            }
        }
        setText(sb);
        setSelection(sb.length());
    }

    public Bitmap createTokenView(String text) {
        LayoutInflater lf = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TextView textView = (TextView) lf.inflate(R.layout.bubble_edittext, null);
        textView.setText(text);

        textView.measure(0, 0);
        textView.layout(0, 0, textView.getMeasuredWidth(), textView.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(textView.getWidth(), textView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(b);
        canvas.translate(-textView.getScrollX(), -textView.getScrollY());
        textView.draw(canvas);
        textView.setDrawingCacheEnabled(true);
        Bitmap cacheBmp = textView.getDrawingCache();
        Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
        textView.destroyDrawingCache();
        return viewBmp;
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        setBubbles();
    }
}