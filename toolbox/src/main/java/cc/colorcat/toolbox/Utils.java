package cc.colorcat.toolbox;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static String decode(String unicode) {
        StringBuilder builder = new StringBuilder(unicode.length());
        Matcher matcher = Pattern.compile("\\\\u[0-9a-fA-F]{4}").matcher(unicode);
        int last = 0;
        for (int start, end = 0; matcher.find(end); last = end) {
            start = matcher.start();
            end = matcher.end();
            builder.append(unicode.substring(last, start))
                    .append((char) Integer.parseInt(unicode.substring(start + 2, end), 16));
        }
        return builder.append(unicode.substring(last)).toString();
    }

    private Utils() {
        throw new AssertionError("no instance");
    }
}
