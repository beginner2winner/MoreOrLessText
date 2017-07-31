package com.beginner2winner.moreorlesstext;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * From
 *   <link>https://stackoverflow.com/questions/20245862/make-ellipsized-a-textview-which-has-linkmovementmethod</link>
 *
 * Replaces
 *   TextView.setMovementMethod(LinkMovementMethod.getInstance());
 * With
 *   TextView.setOnTouchListener(new TouchTextView(spanText));
 *
 * Where setMovementMethod was required to get UrlSpans to act on click
 *
 * Created by Richard Green, © Copyright Beginner2Winner Ltd
 */

public class TouchTextView implements View.OnTouchListener {
    Spannable spannable;

    public TouchTextView (Spannable spannable){
        this.spannable = spannable;
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int action = event.getAction();
        if (!(v instanceof TextView)){
            return false;
        }

        TextView textView  = (TextView) v;

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {

            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= textView.getTotalPaddingLeft();
            y -= textView.getTotalPaddingTop();

            x += textView.getScrollX();
            y += textView.getScrollY();

            Layout layout = textView.getLayout();

            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            ClickableSpan[] link = spannable.getSpans(off, off, ClickableSpan.class);

            if (link.length != 0) {
                if (action == MotionEvent.ACTION_UP) {

                    link[0].onClick(textView);

                } else if (action == MotionEvent.ACTION_DOWN) {

                    Selection.setSelection(spannable,
                            spannable.getSpanStart(link[0]),
                            spannable.getSpanEnd(link[0]));
                }

                return true;
            } else {

                Selection.removeSelection(spannable);
            }
        }

        return false;
    }
}