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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRoot = new LinearLayout(this);
        mRoot.setOrientation(LinearLayout.VERTICAL);
        mRoot.setGravity(Gravity.CENTER);

        mBtn1 = fillButton("test communication");
        mBtn2 = fillButton("test ViewHolder");
        setContentView(mRoot);
    }


    @Override
    public void onClick(View v) {
        if (v == mBtn1) {
            navigateTo(AActivity.class);
        } else if (v == mBtn2) {

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
