package cc.colorcat.demo;

/**
 * Created by cxx on 2017/9/4.
 * xx.ch@outlook.com
 */
public class Favorite {
    private String name;
    private int checked;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "Favorite{" +
                "name='" + name + '\'' +
                ", checked=" + checked +
                '}';
    }
}
