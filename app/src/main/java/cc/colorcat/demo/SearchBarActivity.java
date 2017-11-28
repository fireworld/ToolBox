package cc.colorcat.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import cc.colorcat.toolbox.L;
import cc.colorcat.toolbox.widget.SearchBar;

/**
 * Created by cxx on 17-10-27.
 * xx.ch@outlook.com
 */
public class SearchBarActivity extends Activity {
    private List<History> mHistories = new ArrayList<>();
    private SearchBar.SuggestionsAdapter mAdapter;
    private List<History> mData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar);
        fillHistories();

        final SearchBar searchBar = findViewById(R.id.sb_search);
        searchBar.setThreshold(1);
        mAdapter = new SuggestionsAdapter(this, R.layout.item_history, R.id.text1, mHistories);
        searchBar.setSuggestionsAdapter(mAdapter);
        searchBar.setOnQueryTextListener(new SearchBar.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                L.i("onQueryTextChange, newText = " + newText);
                return false;
            }
        });

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.clearFocus();
            }
        });
    }


    private void fillHistories() {
        mHistories.add(new History("中国湖北", System.currentTimeMillis()));
        mHistories.add(new History("中国武汉", System.currentTimeMillis()));
        mHistories.add(new History("地球中国广水", System.currentTimeMillis()));
        mHistories.add(new History("浙江杭州", System.currentTimeMillis()));
        mHistories.add(new History("宇宙杭州", System.currentTimeMillis()));
        mHistories.add(new History("恒大", System.currentTimeMillis()));
        mHistories.add(new History("恒大地产", System.currentTimeMillis()));
        mHistories.add(new History("恒大足球", System.currentTimeMillis()));
    }


    public static class SuggestionsAdapter extends ArrayAdapter<History> implements SearchBar.SuggestionsAdapter {
        public SuggestionsAdapter(@NonNull Context context, int resource, int textViewResourceId) {
            super(context, resource, textViewResourceId);
        }

        public SuggestionsAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<History> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @NonNull
        @Override
        public Filter getFilter() {
            return super.getFilter();
        }
    }


    public static class MyFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            return null;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

        }
    }

    public static class History implements Comparable<History> {
        private String words;
        private long time;

        public History(String words, long time) {
            this.words = words;
            this.time = time;
        }

        @Override
        public int compareTo(@NonNull History o) {
            if (time < o.time) return -1;
            if (time > o.time) return 1;
            return words.compareTo(o.words);
        }

        @Override
        public String toString() {
            return words;
        }
    }
}
