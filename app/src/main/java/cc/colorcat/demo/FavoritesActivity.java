package cc.colorcat.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import cc.colorcat.toolbox.widget.OnRvItemClickListener;
import cc.colorcat.toolbox.widget.RvAdapter;
import cc.colorcat.toolbox.widget.RvHolder;
import cc.colorcat.toolbox.widget.SimpleRvAdapter;

/**
 * Created by cxx on 2017/9/4.
 * xx.ch@outlook.com
 */
public class FavoritesActivity extends Activity {
    private RvAdapter mAdapter;
    private List<Favorite> mFavorites = new ArrayList<>();
    private View mRoot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout root = new LinearLayout(this);
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Button button = new Button(this);
        button.setText("showWindow");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });
        root.addView(button);
        setContentView(root);
        mRoot = button;
        initData();
    }

    private Random mRandom = new Random();
    private char[] mChars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k'};

    private void initData() {
        int size = mChars.length;
        for (int i = 0; i < 12; i++) {
            StringBuilder name = new StringBuilder();
            int length = mRandom.nextInt(3) + 2;
            for (int j = 0; j < length; j++) {
                name.append(mChars[mRandom.nextInt(size)]);
            }
            Favorite favorite = new Favorite();
            favorite.setName(name.toString());
            favorite.setChecked(mRandom.nextInt(2));
            mFavorites.add(favorite);
        }
    }

    private PopupWindow mWidow;

    private void showPopupWindow() {
        if (mWidow == null) {
            FavoriteSelector selector = new FavoriteSelector(this, mFavorites);
            mWidow = new PopupWindow(this);
            selector.attachToWindow(mWidow);
            mWidow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mWidow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        }
        mWidow.showAsDropDown(mRoot);
    }

    private static class FavoriteSelector {
        private Context mContext;
        private RvAdapter mAdapter;
        private List<Favorite> mFavorites;
        private PopupWindow mWindow;

        private FavoriteSelector(Context context, List<Favorite> favorites) {
            mContext = context;
            mFavorites = favorites;
        }

        private void attachToWindow(PopupWindow window) {
            mWindow = window;
            View root = LayoutInflater.from(mContext).inflate(R.layout.layout_favorites, null);
            root.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mWindow.isShowing()) mWindow.dismiss();
                }
            });
            RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.rv_favorites);
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 5));
            mAdapter = createRvAdapter();
            recyclerView.setAdapter(mAdapter);
            addItemHelper(recyclerView);
            mWindow.setContentView(root);
        }

        private RvAdapter createRvAdapter() {
            return new SimpleRvAdapter<Favorite>(mFavorites, R.layout.adapter_favorites) {
                @Override
                public void bindView(RvHolder holder, final Favorite data) {
                    RvHolder.Helper helper = holder.getHelper();
                    helper.setText(R.id.cb_item, data.getName())
                            .setChecked(R.id.cb_item, data.getChecked() == 1)
                            .setOnCheckedChangeListener(R.id.cb_item, new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    data.setChecked(isChecked ? 1 : 0);
                                }
                            });
                    if (helper.getPosition() == 0) {
                        helper.getRoot().setEnabled(false);
                    }
                }
            };
        }

        private void addItemHelper(RecyclerView recyclerView) {
            final ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
                @Override
                public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                    int swipeFlags = 0;
                    int drawFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                    if (manager instanceof GridLayoutManager || manager instanceof StaggeredGridLayoutManager) {
                        drawFlags |= ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                    }
                    return makeMovementFlags(drawFlags, swipeFlags);
                }

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    int from = viewHolder.getAdapterPosition();
                    int to = target.getAdapterPosition();
                    Collections.swap(mFavorites, from, to);
                    mAdapter.notifyItemMoved(from, to);
                    return true;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                }

                @Override
                public float getMoveThreshold(RecyclerView.ViewHolder viewHolder) {
                    return super.getMoveThreshold(viewHolder);
                }

                @Override
                public boolean isLongPressDragEnabled() {
                    return false;
//                    return super.isLongPressDragEnabled();
                }

                @Override
                public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {
//                    return super.canDropOver(recyclerView, current, target);
                    return target.getAdapterPosition() != 0;
                }
            });
            recyclerView.addOnItemTouchListener(new OnRvItemClickListener() {
                @Override
                public void onItemLongClick(RecyclerView.ViewHolder holder) {
                    super.onItemLongClick(holder);
                    if (holder.getAdapterPosition() != 0) {
                        helper.startDrag(holder);
                    }
                }
            });
            helper.attachToRecyclerView(recyclerView);
        }
    }
}
