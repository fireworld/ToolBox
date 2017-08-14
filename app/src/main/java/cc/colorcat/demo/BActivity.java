package cc.colorcat.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import cc.colorcat.toolbox.communication.Broker;
import cc.colorcat.toolbox.communication.Subject;
import cc.colorcat.toolbox.communication.Subscriber;

/**
 * Created by cxx on 2017/8/10.
 * xx.ch@outlook.com
 */
public class BActivity extends Activity implements View.OnClickListener, Subscriber {
    private LinearLayout mRoot;
    private Button mBtn1;
    private Button mBtn2;
    private Button mBtn3;
    private Button mBtn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRoot = new LinearLayout(this);
        mRoot.setOrientation(LinearLayout.VERTICAL);
        mRoot.setGravity(Gravity.CENTER);

        mBtn1 = fillButton("receive sticky");
        mBtn2 = fillButton("receive common");
        mBtn3 = fillButton("");
        mBtn4 = fillButton("post common");
        setContentView(mRoot);

        Broker.subscribeSticky(this);
    }

    @Override
    protected void onDestroy() {
        Broker.unsubscribe(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn1) {
        } else if (v == mBtn2) {

        } else if (v == mBtn3) {

        } else if (v == mBtn4) {
            Broker.publish(Subject.create("fromB", "extra from B"));
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

    @Override
    public void onReceive(Subject subject) {
        switch (subject.msg()) {
            case "fromA":
                mBtn1.setText(subject.extra().toString());
                break;
            default:
                break;
        }
    }
}
