package cc.colorcat.toolbox;

import java.util.Locale;

/**
 * Created by cxx on 17-8-15.
 * xx.ch@outlook.com
 */
public class Utils {

    /**
     * t1 和 t2 同时为 null 会返回 true
     */
    public static <T> boolean equals(T t1, T t2) {
        return t1 == t2 || t1 != null && t1.equals(t2);
    }

    public static String format(String format, Object... args) {
        return String.format(Locale.getDefault(), format, args);
    }

    public static boolean isMobilePhone(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("^1[0-9]{10}");
    }

    /**
     * 匹配形如 #FFFFFF 或 #FFFFFFFF 这样的颜色字符串
     */
    public static boolean isValidColor(String colorString) {
        return colorString != null && colorString.matches("^(#)(([0-9a-fA-F]{2}){3,4})$");
    }

    public static boolean isHttpUrl(String url) {
        return url != null && url.toLowerCase().matches("^(http)(s)?://(\\S)+");
    }

    public static boolean isEmpty(CharSequence text) {
        return text == null || text.length() == 0;
    }
}
