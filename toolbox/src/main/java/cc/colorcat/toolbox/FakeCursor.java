package cc.colorcat.toolbox;

import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxx on 2017/9/10.
 * xx.ch@outlook.com
 */
public class FakeCursor implements Cursor {
    //    private List<LinkedHashMap<String, Object>> data = new LinkedList<>();
    private String[] columnNames;
    private List<List<Object>> data = new ArrayList<>();
    private int mPosition = -1;

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public int getPosition() {
        return mPosition;
    }

    @Override
    public boolean move(int offset) {
        return moveToPosition(mPosition + offset);
    }

    @Override
    public boolean moveToPosition(int position) {
        final int count = getCount();
        if (position >= count) {
            mPosition = count;
            return false;
        }

        if (position < 0) {
            mPosition = -1;
            return false;
        }

        mPosition = position;
        return true;
    }

    @Override
    public boolean moveToFirst() {
        return moveToPosition(0);
    }

    @Override
    public boolean moveToLast() {
        return moveToPosition(getCount() - 1);
    }

    @Override
    public boolean moveToNext() {
        return moveToPosition(mPosition + 1);
    }

    @Override
    public boolean moveToPrevious() {
        return moveToPosition(mPosition - 1);
    }

    @Override
    public boolean isFirst() {
        return mPosition == 0 && getCount() != 0;
    }

    @Override
    public boolean isLast() {
        final int cnt = getCount();
        return mPosition == (cnt - 1) && cnt != 0;
    }

    @Override
    public boolean isBeforeFirst() {
        return getCount() == 0 || mPosition == -1;
    }

    @Override
    public boolean isAfterLast() {
        int cnt = getCount();
        return cnt == 0 || mPosition == cnt;
    }

    @Override
    public int getColumnIndex(String columnName) {
        for (int i = 0, size = columnNames.length; i < size; i++) {
            if (columnNames[i].equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
        int result = getColumnIndex(columnName);
        if (result == -1) {
            throw new IllegalArgumentException("column '" + columnName + "' does not exist");
        }
        return result;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return getColumnNames()[columnIndex];
    }

    @Override
    public String[] getColumnNames() {
        return this.columnNames;
    }

    @Override
    public int getColumnCount() {
        return getColumnNames().length;
    }

    @Override
    public byte[] getBlob(int columnIndex) {
        return (byte[]) get(columnIndex);
    }

    @Override
    public String getString(int columnIndex) {
        Object obj = data.get(mPosition).get(columnIndex);

        return (String) obj;
    }

    @Override
    public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {
        String result = getString(columnIndex);
        if (result != null) {
            char[] data = buffer.data;
            if (data == null || data.length < result.length()) {
                buffer.data = result.toCharArray();
            } else {
                result.getChars(0, result.length(), data, 0);
            }
            buffer.sizeCopied = result.length();
        } else {
            buffer.sizeCopied = 0;
        }
    }

    @Override
    public short getShort(int columnIndex) {
        return 0;
    }

    @Override
    public int getInt(int columnIndex) {
        return 0;
    }

    @Override
    public long getLong(int columnIndex) {
        return 0;
    }

    @Override
    public float getFloat(int columnIndex) {
        return 0;
    }

    @Override
    public double getDouble(int columnIndex) {
        return 0;
    }

    @Override
    public int getType(int columnIndex) {
        return 0;
    }

    @Override
    public boolean isNull(int columnIndex) {
        return false;
    }

    @Override
    public void deactivate() {

    }

    @Override
    public boolean requery() {
        return false;
    }

    @Override
    public void close() {

    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public void registerContentObserver(ContentObserver observer) {

    }

    @Override
    public void unregisterContentObserver(ContentObserver observer) {

    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void setNotificationUri(ContentResolver cr, Uri uri) {

    }

    @Override
    public Uri getNotificationUri() {
        return null;
    }

    @Override
    public boolean getWantsAllOnMoveCalls() {
        return false;
    }

    @Override
    public void setExtras(Bundle extras) {

    }

    @Override
    public Bundle getExtras() {
        return null;
    }

    @Override
    public Bundle respond(Bundle extras) {
        return null;
    }

    private Object get(int column) {
        int columnCount = this.columnNames.length;
        if (column < 0 || column >= columnCount) {
            throw new CursorIndexOutOfBoundsException("Requested column: "
                    + column + ", # of columns: " + columnCount);
        }
        if (mPosition < 0) {
            throw new CursorIndexOutOfBoundsException("Before first row.");
        }
        if (mPosition >= data.size()) {
            throw new CursorIndexOutOfBoundsException("After last row.");
        }
        return data.get(mPosition).get(column);
    }
}
