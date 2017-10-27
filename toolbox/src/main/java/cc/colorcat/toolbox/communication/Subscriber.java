package cc.colorcat.toolbox.communication;

/**
 * Created by cxx on 2017/8/9.
 * xx.ch@outlook.com
 */
public interface Subscriber {

    /**
     * Returns the flag indicating whether or not delivering the specified {@link Subject} should be intercepted,
     * This will prevent any other {@link Subscriber} after this from receiving the {@link Subject}.
     *
     * @return true if delivering the specified subject should be intercepted.
     */
    boolean onReceive(Subject subject);
}
