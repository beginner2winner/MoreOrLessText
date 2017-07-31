package com.beginner2winner.moreorlesstext.sample;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.beginner2winner.moreorlesstext.MoreOrLessText;
import com.beginner2winner.moreorlesstext.R;

public class MainActivity extends AppCompatActivity {

    MoreOrLessText tv;
    MoreOrLessText tv2;
    Button button;

    final String htmlText = "<b>Hello</b> World! <a href=\"wibble\">1234567890</a> Hello World! <a href=\"wibble2\">1234567890</a> Hello World! 1234567890 Hello World! 1234567890 Hello World! 1234567890 Hello World! 1234567890 Hello World! 1234567890 Hello World! 1234567890 Hello World! 1234567890 END.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        tv = (MoreOrLessText) findViewById(R.id.tv);

        tv2 = (MoreOrLessText) findViewById(R.id.tv2);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpannableStringBuilder spanText =
                        getSpannable(
                                htmlText,
                                null,
                                new OnSpanClickListener() {
                                    @Override
                                    public void onClick(String url) {
                                        Toast.makeText(MainActivity.this, "You clicked on an anchor link", Toast.LENGTH_SHORT).show();
                                    }
                                });

                tv2.setText(spanText);
                tv2.invalidateForMore();
            }
        });
    }

    public interface OnSpanClickListener {
        void onClick(String url);
    }

    public static SpannableStringBuilder getSpannable(
            String source,
            Html.ImageGetter imageGetter,
            final OnSpanClickListener clickHandler
    ) {

        SpannableStringBuilder b;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            b = (SpannableStringBuilder) Html.fromHtml(
                    source,
                    Html.FROM_HTML_MODE_LEGACY,
                    imageGetter,
                    null
            );

        } else {

            b = (SpannableStringBuilder) Html.fromHtml(
                    source,
                    imageGetter,
                    null
            );
        }

        for (URLSpan s : b.getSpans(0, b.length(), URLSpan.class)) {

            URLSpan newSpan = new URLSpan(s.getURL()) {

                @Override
                public void onClick(View view) {

                    clickHandler.onClick(getURL());
                }
            };

            b.setSpan(newSpan, b.getSpanStart(s), b.getSpanEnd(s), b.getSpanFlags(s));
            b.removeSpan(s);
        }
        return b;
    }
}
