package cc.colorcat.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cc.colorcat.toolbox.IoUtils;
import cc.colorcat.toolbox.L;
import cc.colorcat.toolbox.Op;
import cc.colorcat.toolbox.widget.LvAdapter;
import cc.colorcat.toolbox.widget.LvHolder;
import cc.colorcat.toolbox.widget.SimpleLvAdapter;
import cc.colorcat.vangogh.CircleTransformation;
import cc.colorcat.vangogh.VanGogh;

/**
 * Created by cxx on 2017/8/11.
 * xx.ch@outlook.com
 */
public class CourseActivity extends Activity {
    public static final String URL = "http://www.imooc.com/api/teacher?type=4&num=30";
    private Gson mGson = new Gson();

    private SwipeRefreshLayout mRootSrl;
    private BaseAdapter mAdapter;
    private List<Course> mList = new ArrayList<>(30);

    private Set<LvHolder> mStats = new HashSet<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        mRootSrl = (SwipeRefreshLayout) findViewById(R.id.srl_root);
        mRootSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        ListView listView = (ListView) findViewById(R.id.lv_courses);
//        mAdapter = createSimpleLvAdapter();
        mAdapter = createLvAdapter();
        listView.setAdapter(mAdapter);
        loadData();
    }

    private SimpleLvAdapter<Course> createSimpleLvAdapter() {
        return new SimpleLvAdapter<Course>(mList, R.layout.adapter_course2) {
            @Override
            public void bindView(LvHolder holder, Course course) {
                mStats.add(holder);
                Log.d("CourseActivity", "simple holder size = " + mStats.size());
                ImageView imageView = holder.getView(R.id.iv_icon);
                VanGogh.with(CourseActivity.this)
                        .load(course.getPicBig())
                        .addTransformation(new CircleTransformation(4, Color.WHITE))
                        .into(imageView);
//                Picasso.with(CourseActivity.this)
//                        .load(course.getPicBig())
//                        .into(imageView);
                holder.setText(R.id.tv_name, course.getName())
                        .setText(R.id.tv_description, course.getDescription());
            }
        };
    }

    private LvAdapter createLvAdapter() {
        return new LvAdapter() {
            @Override
            public int getViewTypeCount() {
                return 2;
            }

            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public Object getItem(int position) {
                return mList.get(position);
            }

            @Override
            public int getItemViewType(int position) {
                return position % 2 == 0 ? 0 : 1;
            }

            @Override
            public int getLayoutResId(int viewType) {
                switch (viewType) {
                    case 0:
                        return R.layout.adapter_course1;
                    case 1:
                        return R.layout.adapter_course2;
                    default:
                        throw new IllegalArgumentException("viewType == " + viewType);
                }
            }

            @Override
            public void bindView(LvHolder holder, int position) {
                Course course = mList.get(position);
                mStats.add(holder);
                Log.d("CourseActivity", "multi type holder size = " + mStats.size());
                ImageView imageView = holder.getView(R.id.iv_icon);
                VanGogh.with(CourseActivity.this)
                        .load(course.getPicBig())
                        .addTransformation(new CircleTransformation(4, Color.WHITE))
                        .into(imageView);
                holder.setText(R.id.tv_name, course.getName())
                        .setText(R.id.tv_description, course.getDescription());
            }
        };
    }

    private void loadData() {
        new AsyncTask<String, Void, List<Course>>() {
            @Override
            protected List<Course> doInBackground(String... params) {
                List<Course> courses = new ArrayList<>(30);
                try {
                    String json = IoUtils.loadString(URL);
                    Response<List<Course>> response = mGson.fromJson(json, new TypeToken<Response<List<Course>>>() {
                    }.getType());
                    courses.addAll(Op.nullElse(response.getData(), Collections.<Course>emptyList()));
                } catch (IOException e) {
                    L.e(e);
                }
                return courses;
            }

            @Override
            protected void onPostExecute(List<Course> courses) {
                super.onPostExecute(courses);
                mList.clear();
                mList.addAll(courses);
                mAdapter.notifyDataSetChanged();
                mRootSrl.setRefreshing(false);
            }
        }.execute(URL);
    }
}
