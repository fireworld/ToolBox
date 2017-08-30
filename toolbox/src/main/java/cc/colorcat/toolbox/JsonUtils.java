package cc.colorcat.toolbox;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by cxx on 17-6-27.
 * xx.ch@outlook.com
 */
public class JsonUtils {
    private static final Gson GSON;
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    static {
        GSON = new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapterFactory(new NullStringAdapterFactory())
                .registerTypeAdapterFactory(new NullDateAdapterFactory(DATE_FORMAT))
                .create();
    }

    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }

    public static String toJson(List<String> names, List<String> values) {
        final int size = names.size();
        if (values.size() != size) {
            throw new IllegalArgumentException("names.size() != values.size()");
        }
        JsonObject obj = new JsonObject();
        for (int i = 0; i < size; ++i) {
            obj.addProperty(names.get(i), values.get(i));
        }
        return obj.toString();
    }

    public static <T> T fromJson(String json, Class<T> clz) {
        return GSON.fromJson(json, clz);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return GSON.fromJson(json, typeOfT);
    }

    private JsonUtils() {
        throw new AssertionError("no instance");
    }


    @SuppressWarnings("unchecked")
    private static class NullStringAdapterFactory implements TypeAdapterFactory {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<? super T> rawType = type.getRawType();
            if (!String.class.isAssignableFrom(rawType)) {
                return null;
            }
            return (TypeAdapter<T>) new NullStringAdapter();
        }
    }

    private static class NullStringAdapter extends TypeAdapter<String> {

        @Override
        public synchronized void write(JsonWriter out, String value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            out.value(value);
        }

        @Override
        public synchronized String read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return "";
            }
            return in.nextString();
        }
    }

    @SuppressWarnings("unchecked")
    private static class NullDateAdapterFactory implements TypeAdapterFactory {
        private final String format;

        NullDateAdapterFactory(String format) {
            this.format = format;
        }

        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<? super T> rawType = type.getRawType();
            if (!Date.class.isAssignableFrom(rawType)) {
                return null;
            }
            return (TypeAdapter<T>) new NullDateAdapter(format);
        }
    }

    private static class NullDateAdapter extends TypeAdapter<Date> {
        private final DateFormat dateFormatter;

        NullDateAdapter(String format) {
            dateFormatter = new SimpleDateFormat(format, Locale.getDefault());
        }

        @Override
        public void write(JsonWriter out, Date value) throws IOException {
            synchronized (dateFormatter) {
                if (value == null) {
                    out.nullValue();
                } else {
                    String dateString = dateFormatter.format(value);
                    out.value(dateString);
                }
            }
        }

        @Override
        public synchronized Date read(JsonReader in) throws IOException {
            JsonToken token = in.peek();
            if (token == JsonToken.NULL) {
                in.nextNull();
                return new Date();
            }
            if (token != JsonToken.STRING) {
                throw new JsonParseException("The date should be a string value");
            }
            try {
                return dateFormatter.parse(in.nextString());
            } catch (ParseException e) {
                throw new JsonParseException(e);
            }
        }
    }
}
