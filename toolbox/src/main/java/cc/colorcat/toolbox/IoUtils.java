package cc.colorcat.toolbox;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by cxx on 2017/8/11.
 * xx.ch@outlook.com
 */
public class IoUtils {

    public static byte[] toByteAndClose(InputStream in) throws IOException {
        byte[] result = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            justDump(in, out);
            result = out.toByteArray();
        } finally {
            close(in, out);
        }
        return result;
    }

    public static String loadString(String httpUrl) throws IOException {
        return loadString(httpUrl, Charset.defaultCharset());
    }

    public static String loadString(String httpUrl, String charsetName) throws IOException {
        return loadString(httpUrl, Charset.forName(charsetName));
    }

    public static String loadString(String httpUrl, Charset cs) throws IOException {
        String result = "";
        HttpURLConnection conn = null;
        InputStream in = null;
        try {
            conn = (HttpURLConnection) new URL(httpUrl).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoInput(true);
            conn.setDoOutput(false);
            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                in = conn.getInputStream();
                result = justRead(in, cs);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            close(in);
        }
        return result;
    }

    public static String readAndClose(InputStream in) throws IOException {
        return readAndClose(in, Charset.defaultCharset());
    }

    public static String readAndClose(InputStream in, String charsetName) throws IOException {
        return readAndClose(in, Charset.forName(charsetName));
    }

    public static String readAndClose(InputStream in, Charset cs) throws IOException {
        String result = "";
        try {
            result = justRead(in, cs);
        } finally {
            close(in);
        }
        return result;
    }

    public static String justRead(InputStream in, Charset cs) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, cs));
        char[] buffer = new char[1024];
        for (int count = reader.read(buffer); count != -1; count = reader.read(buffer)) {
            sb.append(buffer, 0, count);
        }
        return sb.toString();
    }

    public static boolean quietlyDumpAndClose(InputStream in, OutputStream out) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(in);
            bos = new BufferedOutputStream(out);
            justDump(bis, bos);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(bis, bos);
        }
        return false;
    }

    public static void dumpAndClose(InputStream in, OutputStream out) throws IOException {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(in);
            bos = new BufferedOutputStream(out);
            justDump(bis, bos);
        } finally {
            close(bis, bos);
        }
    }

    public static void justDump(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[2048];
        for (int length = in.read(buffer); length != -1; length = in.read(buffer)) {
            out.write(buffer, 0, length);
        }
        out.flush();
    }

    public static void close(Closeable... cs) {
        for (Closeable c : cs) {
            close(c);
        }
    }

    public static void close(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException ignore) {

            }
        }
    }

    private IoUtils() {
        throw new AssertionError("no instance");
    }
}
