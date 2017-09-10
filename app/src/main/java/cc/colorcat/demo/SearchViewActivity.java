package cc.colorcat.demo;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.ResourceCursorAdapter;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.FilterQueryProvider;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cc.colorcat.toolbox.L;

/**
 * Created by cxx on 2017/9/10.
 * xx.ch@outlook.com
 */
public class SearchViewActivity extends Activity {
    private String[] mColumn = {"_id", "word"};
    private List<String[]> mHistory = new ArrayList<>();
    private MatrixCursor mCursor;
    private CursorAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);

        initData();
        mCursor = new MatrixCursor(mColumn, 8);
        for (String[] row : mHistory) {
            mCursor.addRow(row);
        }

        SearchView searchView = (SearchView) findViewById(R.id.sv_search);
        createAdapter();
        searchView.setSuggestionsAdapter(mAdapter);
        mAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                String[] words = constraint.toString().split("[ ,.]");
                L.i(Arrays.toString(words));
                MatrixCursor cursor = new MatrixCursor(mColumn, 20);
                for (String[] row : mHistory) {
                    String name = row[1];
                    for (String word : words) {
                        if (name.contains(word)) {
                            cursor.addRow(row);
                        }
                    }
                }
                return cursor;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(SearchViewActivity.this, "query " + query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(SearchViewActivity.this, "newText " + newText, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                Toast.makeText(SearchViewActivity.this, "onSuggestionSelect, position = " + position, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Toast.makeText(SearchViewActivity.this, "onSuggestionClick, position = " + position, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void initData() {
        mHistory.add(new String[]{String.valueOf(System.currentTimeMillis()), "中国湖北"});
        mHistory.add(new String[]{String.valueOf(System.currentTimeMillis()), "中国武汉"});
        mHistory.add(new String[]{String.valueOf(System.currentTimeMillis()), "地球中国广水"});
        mHistory.add(new String[]{String.valueOf(System.currentTimeMillis()), "浙江杭州"});
        mHistory.add(new String[]{String.valueOf(System.currentTimeMillis()), "宇宙杭州"});
    }

    private void createAdapter() {
        mAdapter = new ResourceCursorAdapter(this, android.R.layout.simple_dropdown_item_1line, mCursor) {
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setText(cursor.getString(1));
            }
        };
    }
}
