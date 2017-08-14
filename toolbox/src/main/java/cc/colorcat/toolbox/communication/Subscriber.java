package cc.colorcat.toolbox.communication;

/**
 * Created by cxx on 2017/8/9.
 * xx.ch@outlook.com
 */
public interface Subscriber {

    void onReceive(Subject subject);
}
