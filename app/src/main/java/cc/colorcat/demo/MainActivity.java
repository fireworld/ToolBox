package cc.colorcat.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout mRoot;
    private Button mBtn1;
    private Button mBtn2;
    private Button mBtn3;
    private Button mBtn4;
    private Button mBtn5;
    private Button mBtn6;
    private Button mBtn7;
    private Button mBtn8;
    private Button mBtn9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRoot = new LinearLayout(this);
        mRoot.setOrientation(LinearLayout.VERTICAL);
        mRoot.setGravity(Gravity.CENTER);

        mBtn1 = fillButton("test communication");
        mBtn2 = fillButton("test SimpleLvAdapter");
        mBtn3 = fillButton("test SimpleRvAdapter");
        mBtn4 = fillButton("test DragViewActivity");
        mBtn5 = fillButton("test ternary text view");
        mBtn6 = fillButton("test FavoritesActivity");
        mBtn7 = fillButton("test SearchViewActivity");
        mBtn8 = fillButton("test SearchBarActivity");
        mBtn9 = fillButton("test WheelViewActivity");
        setContentView(mRoot);
    }


    @Override
    public void onClick(View v) {
        if (v == mBtn1) {
            navigateTo(AActivity.class);
        } else if (v == mBtn2) {
            navigateTo(CourseActivity.class);
        } else if (v == mBtn3) {
            navigateTo(CourseListActivity.class);
        } else if (v == mBtn4) {
            navigateTo(DragViewActivity.class);
        } else if (v == mBtn5) {
            navigateTo(TestActivity.class);
        } else if (v == mBtn6) {
            navigateTo(FavoritesActivity.class);
        } else if (v == mBtn7) {
            navigateTo(SearchViewActivity.class);
        } else if (v == mBtn8) {
            navigateTo(SearchBarActivity.class);
        } else if (v == mBtn9) {
            navigateTo(WheelViewActivity.class);
        }
    }

    private Button fillButton(String text) {
        Button btn = new Button(this);
        btn.setText(text);
        btn.setAllCaps(false);
        btn.setOnClickListener(this);
        mRoot.addView(btn);
        return btn;
    }

    private void navigateTo(Class<? extends Activity> cls) {
        startActivity(new Intent(this, cls));
    }
}
