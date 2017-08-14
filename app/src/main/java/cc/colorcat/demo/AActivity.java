package cc.colorcat.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import cc.colorcat.util.communication.Broker;
import cc.colorcat.util.communication.Subject;
import cc.colorcat.util.communication.Subscriber;

/**
 * Created by cxx on 2017/8/10.
 * xx.ch@outlook.com
 */
public class AActivity extends Activity implements View.OnClickListener {
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

        mBtn1 = fillButton("post sticky");
        mBtn2 = fillButton("receive common");
        mBtn3 = fillButton("clear last");
        mBtn4 = fillButton("to BActivity");
        setContentView(mRoot);

        Broker.subscribe(mSubscriber);
    }

    private Subscriber mSubscriber = new Subscriber() {
        @Override
        public void onReceive(Subject subject) {
            switch (subject.msg()) {
                case "fromB":
                    mBtn2.setText(subject.extra().toString());
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    protected void onDestroy() {
        Broker.unsubscribe(mSubscriber);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v == mBtn1) {
            Broker.publishSticky(Subject.create("fromA", "extra from A"));
        } else if (v == mBtn2) {

        } else if (v == mBtn3) {
            Broker.clearLast();
        } else if (v == mBtn4) {
            navigateTo(BActivity.class);
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
