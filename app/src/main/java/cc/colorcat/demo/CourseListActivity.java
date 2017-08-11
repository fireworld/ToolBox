package cc.colorcat.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cc.colorcat.util.IoUtils;
import cc.colorcat.util.L;
import cc.colorcat.util.Op;
import cc.colorcat.util.widget.RvHolder;
import cc.colorcat.util.widget.SimpleRvAdapter;
import cc.colorcat.vangogh.CircleTransformation;
import cc.colorcat.vangogh.VanGogh;

/**
 * Created by cxx on 2017/8/11.
 * xx.ch@outlook.com
 */
public class CourseListActivity extends Activity {
    public static final String URL = "http://www.imooc.com/api/teacher?type=4&num=30";
    private Gson mGson = new Gson();

    private SwipeRefreshLayout mRootSrl;
    private RecyclerView mRecyclerView;
    private SimpleRvAdapter<Course> mAdapter;

    private Set<RvHolder.Holder> mStats = new HashSet<>();
    private Set<RvHolder> mStats2 = new HashSet<>();

    private List<Course> mList = new ArrayList<>(30);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        mRootSrl = (SwipeRefreshLayout) findViewById(R.id.srl_root);
        mRootSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_courses);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//        mAdapter = createSimpleRvAdapter();
        mRecyclerView.setAdapter(create());
        refreshData();
    }

    private SimpleRvAdapter<Course> createSimpleRvAdapter() {
        SimpleRvAdapter<Course> adapter = new SimpleRvAdapter<Course>(mList, R.layout.adapter_course_list) {
            @Override
            public void bindView(RvHolder.Holder holder, Course course) {
                mStats.add(holder);
                Log.d("CourseActivity", "holder size = " + mStats.size());
                ImageView imageView = holder.getView(R.id.iv_icon);
                VanGogh.with(CourseListActivity.this)
                        .load(course.getPicBig())
                        .addTransformation(new CircleTransformation(4, Color.WHITE))
                        .into(imageView);
                holder.setText(R.id.tv_name, course.getName())
                        .setText(R.id.tv_description, course.getDescription());
            }
        };
        adapter.setOnItemLongClickListener(new SimpleRvAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final int position) {
                new AlertDialog.Builder(CourseListActivity.this).setTitle("Tip").setMessage("delete?").setCancelable(false)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mList.remove(position);
                                mAdapter.notifyItemRemoved(position);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
            }
        });
        return adapter;
    }

    private RecyclerView.Adapter<RvHolder> create() {
        return new RecyclerView.Adapter<RvHolder>() {
            @Override
            public RvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_course_list, parent, false);
                return new RvHolder(itemView, null);
            }

            @Override
            public void onBindViewHolder(RvHolder holder, int position) {
                mStats2.add(holder);
                RvHolder.Holder h = holder.getHolder();
                Course course = mList.get(position);
                Log.d("CourseActivity", "holder size = " + mStats2.size());
                ImageView imageView = h.getView(R.id.iv_icon);
                VanGogh.with(CourseListActivity.this)
                        .load(course.getPicBig())
                        .addTransformation(new CircleTransformation(4, Color.WHITE))
                        .into(imageView);
                h.setText(R.id.tv_name, course.getName())
                        .setText(R.id.tv_description, course.getDescription());
            }

            @Override
            public int getItemCount() {
                return mList.size();
            }
        };
    }

    private void refreshData() {
        new AsyncTask<String, Void, List<Course>>() {
            @Override
            protected List<Course> doInBackground(String... params) {
                List<Course> courses = new ArrayList<>(30);
                try {
                    String json = IoUtils.loadString(URL);
                    Response<List<Course>> response = mGson.fromJson(json, new TypeToken<Response<List<Course>>>() {}.getType());
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

