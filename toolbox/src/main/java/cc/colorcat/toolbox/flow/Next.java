package cc.colorcat.toolbox.flow;

import java.util.List;

/**
 * Created by cxx on 17-9-20.
 * xx.ch@outlook.com
 */
public interface Next<T> {

    void onNext(List<T> data);
}
