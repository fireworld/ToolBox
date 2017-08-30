package cc.colorcat.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

/**
 * Created by cxx on 2017/8/30.
 * xx.ch@outlook.com
 */
public class TestActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test);

        TextView textView = (TextView) findViewById(R.id.text_view);
//        textView.setText("fsdf");
    }
}
