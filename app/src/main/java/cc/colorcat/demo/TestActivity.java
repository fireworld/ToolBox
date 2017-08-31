package cc.colorcat.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

import cc.colorcat.toolbox.widget.TernaryTextView;

/**
 * Created by cxx on 2017/8/30.
 * xx.ch@outlook.com
 */
public class TestActivity extends Activity {
    private static final Random RANDOM = new Random();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test);

        TextView textView = (TextView) findViewById(R.id.text_view);
//        textView.setTextSize(12);
//        textView.getTextSize();
//        textView.setTextColor();
//        textView.setText("test");

        final TernaryTextView ttv = (TernaryTextView) findViewById(R.id.ttv);
//        ttv.setText("01234");
        ttv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ttv.setTextSize(ttv.getTextSize() + 5);
//                ttv.setTextColor(Color.argb(255, RANDOM.nextInt(256), RANDOM.nextInt(256), RANDOM.nextInt(256)));
//                ttv.setText(Integer.toString(RANDOM.nextInt()));
                ttv.addPaintFlag(TextPaint.FAKE_BOLD_TEXT_FLAG);
            }
        });
    }
}
