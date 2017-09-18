package cc.colorcat.toolbox.communication;

/**
 * Created by cxx on 2017/8/9.
 * xx.ch@outlook.com
 */
public interface Subscriber {

    /**
     * Returns the flag indicating whether or not the specified {@link Subject} should be cleared,
     * This will prevent any other {@link Subscriber} from receiving the {@link Subject}.
     *
     * @return true if the specified subject should be cleared.
     */
    boolean onReceive(Subject subject);
}
