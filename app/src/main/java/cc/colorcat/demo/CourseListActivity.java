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

import cc.colorcat.toolbox.IoUtils;
import cc.colorcat.toolbox.L;
import cc.colorcat.toolbox.Op;
import cc.colorcat.toolbox.widget.OnRvItemClickListener;
import cc.colorcat.toolbox.widget.RvAdapter;
import cc.colorcat.toolbox.widget.RvHolder;
import cc.colorcat.toolbox.widget.SimpleRvAdapter;
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
    private RecyclerView.Adapter<RvHolder> mAdapter;

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
        mAdapter = createSimpleRvAdapter();
//        mAdapter = createRecyclerViewAdapter();
//        mAdapter = createRvAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new OnRvItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder) {
                super.onItemClick(holder);
                L.e("CourseActivity", "position = " + holder.getAdapterPosition());
            }

            @Override
            public void onItemLongClick(final RecyclerView.ViewHolder holder) {
                super.onItemLongClick(holder);
                new AlertDialog.Builder(CourseListActivity.this).setTitle("Tip").setMessage("delete?").setCancelable(false)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int position = holder.getAdapterPosition();
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
        refreshData();
    }

    private Set<RvHolder.Helper> mHelperStats = new HashSet<>();
    private Set<RvHolder> mHolderStats = new HashSet<>();
    private Set<View> mViewStats = new HashSet<>();

    private SimpleRvAdapter<Course> createSimpleRvAdapter() {
        SimpleRvAdapter<Course> adapter = new SimpleRvAdapter<Course>(mList, R.layout.adapter_course_list2) {
            @Override
            public void bindView(RvHolder holder, Course course) {
                mHolderStats.add(holder);
                Log.e("CourseActivity", "SimpleRvAdapter holder size = " + mHolderStats.size());
                RvHolder.Helper helper = holder.getHelper();
                mHelperStats.add(helper);
                Log.d("CourseActivity", "SimpleRvAdapter helper size = " + mHelperStats.size());
                mViewStats.add(helper.getRoot());
                Log.i("CourseActivity", "SimpleRvAdapter view size = " + mViewStats.size());
                ImageView imageView = helper.getView(R.id.iv_icon);
                VanGogh.with(CourseListActivity.this)
                        .load(course.getPicBig())
                        .addTransformation(new CircleTransformation(4, Color.WHITE))
                        .into(imageView);
                helper.setText(R.id.tv_name, course.getName())
                        .setText(R.id.tv_description, course.getDescription());
            }
        };
        return adapter;
    }

    private RecyclerView.Adapter<RvHolder> createRecyclerViewAdapter() {
        return new RecyclerView.Adapter<RvHolder>() {
            @Override
            public RvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_course_list2, parent, false);
                return new RvHolder(itemView);
            }

            @Override
            public void onBindViewHolder(RvHolder holder, int position) {
                mHolderStats.add(holder);
                Log.e("CourseActivity", "RvAdapter common holder size = " + mHolderStats.size());
                RvHolder.Helper helper = holder.getHelper();
                mHelperStats.add(helper);
                Log.d("CourseActivity", "RvAdapter common helper size = " + mHelperStats.size());
                mViewStats.add(helper.getRoot());
                Log.i("CourseActivity", "RvAdapter common view size = " + mViewStats.size());

                Course course = mList.get(position);
                ImageView imageView = helper.getView(R.id.iv_icon);
                VanGogh.with(CourseListActivity.this)
                        .load(course.getPicBig())
                        .addTransformation(new CircleTransformation(4, Color.WHITE))
                        .into(imageView);
                helper.setText(R.id.tv_name, course.getName())
                        .setText(R.id.tv_description, course.getDescription());
            }

            @Override
            public int getItemCount() {
                return mList.size();
            }
        };
    }

    private RvAdapter createRvAdapter() {
        RvAdapter adapter = new RvAdapter() {
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
            public void bindView(RvHolder holder, int position) {
                mHolderStats.add(holder);
                Log.e("CourseActivity", "RvAdapter holder size = " + mHolderStats.size());
                RvHolder.Helper helper = holder.getHelper();
                mHelperStats.add(helper);
                Log.d("CourseActivity", "RvAdapter helper size = " + mHelperStats.size());
                mViewStats.add(helper.getRoot());
                Log.i("CourseActivity", "RvAdapter view size = " + mViewStats.size());

                Course course = mList.get(position);
                ImageView imageView = helper.getView(R.id.iv_icon);
                VanGogh.with(CourseListActivity.this)
                        .load(course.getPicBig())
                        .addTransformation(new CircleTransformation(4, Color.WHITE))
                        .into(imageView);
                helper.setText(R.id.tv_name, course.getName())
                        .setText(R.id.tv_description, course.getDescription());
            }

            @Override
            public int getItemCount() {
                return mList.size();
            }
        };
        return adapter;
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

