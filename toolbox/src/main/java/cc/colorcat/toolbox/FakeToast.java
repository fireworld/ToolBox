package cc.colorcat.toolbox;

import android.content.Context;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by cxx on 2017/8/14.
 * xx.ch@outlook.com
 */
public class FakeToast {
    private Toast mToast;
    private Queue<Message> msgQueue = new LinkedList<>();
    private Message mLast;

    public FakeToast(Context context) {
        mToast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_SHORT);
    }


    private static class Message {
        private CharSequence content;
        private long time;

        private Message(CharSequence content) {
            this.content = content;
            this.time = System.currentTimeMillis();
        }
    }
}
