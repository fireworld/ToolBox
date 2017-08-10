package cc.colorcat.util.communication;


import android.os.Handler;
import android.os.Looper;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by cxx on 2017/8/9.
 * xx.ch@outlook.com
 */
public final class Broker {
    private static volatile Broker instance;

    private Handler handler = new Handler(Looper.getMainLooper());
    private Queue<Subscriber> subscribers = new ConcurrentLinkedQueue<>();
    private Reference<Subject> last;

    public static boolean subscribe(Subscriber subscriber) {
        return getInstance().realSubscribe(subscriber, false);
    }

    public static boolean subscribeSticky(Subscriber subscriber) {
        return getInstance().realSubscribe(subscriber, true);
    }

    public static boolean unsubscribe(Subscriber subscriber) {
        return getInstance().realUnsubscribe(subscriber);
    }

    public static void publish(Subject subject) {
        getInstance().realPublish(subject, false);
    }

    public static void publishSticky(Subject subject) {
        getInstance().realPublish(subject, true);
    }

    public static void clearLast() {
        getInstance().realClearLast();
    }

    public static void destroy() {
        getInstance().realDestroy();
    }

    private static Broker getInstance() {
        if (instance == null) {
            synchronized (Broker.class) {
                if (instance == null) {
                    instance = new Broker();
                }
            }
        }
        return instance;
    }

    private Broker() {

    }

    private boolean realSubscribe(Subscriber subscriber, boolean sticky) {
        boolean success = !subscribers.contains(checkNotNull(subscriber)) && subscribers.add(subscriber);
        if (success && sticky) {
            deliverLast(subscriber);
        }
        return success;
    }

    private void deliverLast(Subscriber subscriber) {
        Subject subject;
        if (last != null && (subject = last.get()) != null) {
            if (isMainThread()) {
                subscriber.onReceive(subject);
            } else {
                handler.post(new Single(subscriber, subject));
            }
        }
    }

    private boolean realUnsubscribe(Subscriber subscriber) {
        return subscribers.remove(checkNotNull(subscriber));
    }

    private void realPublish(Subject subject, boolean sticky) {
        checkNotNull(subject);
        if (sticky) {
            last = new SoftReference<>(subject);
        }
        if (!subscribers.isEmpty()) {
            if (isMainThread()) {
                deliverMulti(subscribers, subject);
            } else {
                handler.post(new Multi(subscribers, subject));
            }
        }
    }

    private void realClearLast() {
        if (last != null) {
            last.clear();
        }
    }

    private void realDestroy() {
        subscribers.clear();
        realClearLast();
        instance = null;
    }

    private static class Single implements Runnable {
        private Subscriber subscriber;
        private Subject subject;

        private Single(Subscriber subscriber, Subject subject) {
            this.subscriber = subscriber;
            this.subject = subject;
        }

        @Override
        public void run() {
            subscriber.onReceive(subject);
        }
    }


    private static class Multi implements Runnable {
        private Queue<Subscriber> subscribers;
        private Subject subject;

        private Multi(Queue<Subscriber> subscribers, Subject subject) {
            this.subscribers = subscribers;
            this.subject = subject;
        }

        @Override
        public void run() {
            deliverMulti(subscribers, subject);
        }
    }


    private static void deliverMulti(Queue<Subscriber> subscribers, Subject subject) {
        for (Subscriber subscriber : subscribers) {
            subscriber.onReceive(subject);
        }
    }

    private static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    private static <T> T checkNotNull(T value) {
        if (value == null) {
            throw new NullPointerException("value == null");
        }
        return value;
    }
}
