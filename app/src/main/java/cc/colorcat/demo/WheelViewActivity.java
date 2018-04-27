package cc.colorcat.demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cc.colorcat.toolbox.JsonUtils;
import cc.colorcat.toolbox.L;
import cc.colorcat.toolbox.entity.City;
import cc.colorcat.toolbox.entity.Province;
import cc.colorcat.toolbox.entity.Region;
import cc.colorcat.toolbox.widget.WheelView;

/**
 * Created by cxx on 2018/4/27.
 * xx.ch@outlook.com
 */
public class WheelViewActivity extends Activity {
    private WheelView mProvinceWv;
    private List<Province> mProvinces = new ArrayList<>();
    private WheelView mCityWv;
    private List<City> mCities = new ArrayList<>();
    private WheelView mRegionWv;
    private List<Region> mRegions = new ArrayList<>();
    private TextView mAddressTv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_view);

        mProvinceWv = findViewById(R.id.wv_province);
        mProvinceWv.setViewBinder(new WheelView.ViewBinder() {
            @Override
            public boolean onBind(WheelView.ItemViewHolder holder, int position) {
                holder.textView.setText(mProvinces.get(position).getCnName().replace("[\\s\t]", "$"));
                return true;
            }
        });
        mProvinceWv.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                L.d("province selected, position=" + position);
                mCities.clear();
                mCities.addAll(mProvinces.get(position).getCities());
                mCityWv.setItemData(mCities);
            }
        });
        mCityWv = findViewById(R.id.wv_city);
        mCityWv.setViewBinder(new WheelView.ViewBinder() {
            @Override
            public boolean onBind(WheelView.ItemViewHolder holder, int position) {
                holder.textView.setText(mCities.get(position).getCnName().replace("[\\s\t]", "$"));
                return true;
            }
        });
        mCityWv.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                L.i("city selected, position=" + position);
                mRegions.clear();
                mRegions.addAll(mCities.get(position).getRegions());
                mRegionWv.setItemData(mRegions);
            }
        });
        mRegionWv = findViewById(R.id.wv_region);
        mRegionWv.setViewBinder(new WheelView.ViewBinder() {
            @Override
            public boolean onBind(WheelView.ItemViewHolder holder, int position) {
                holder.textView.setText(mRegions.get(position).getCnName().replace("[\\s\t]", "$"));
                return true;
            }
        });
        mRegionWv.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                L.e("region selected, position=" + position);
                String provName = mProvinces.get(mProvinceWv.getSelectedItemPosition()).getCnName();
                String cityName = mCities.get(mCityWv.getSelectedItemPosition()).getCnName();
                String regionName = mRegions.get(position).getCnName();
                mAddressTv.setText((provName + " " + cityName + " " + regionName));
            }
        });
        mAddressTv = findViewById(R.id.tv_address);
        initData();
    }

    @SuppressLint("StaticFieldLeak")
    private void initData() {
        new AsyncTask<Void, Void, List<Province>>() {

            @Override
            protected List<Province> doInBackground(Void... voids) {
                StringBuilder builder = new StringBuilder();
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.provinces)));
                    char[] buffer = new char[2048];
                    for (int length = br.read(buffer); length != -1; length = br.read(buffer)) {
                        builder.append(buffer, 0, length);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException ignore) {

                        }
                    }
                }
                return JsonUtils.fromJson(builder.toString(), new TypeToken<List<Province>>() {}.getType());
            }

            @Override
            protected void onPostExecute(List<Province> provinces) {
                super.onPostExecute(provinces);
                mProvinces.clear();
                mProvinces.addAll(provinces);
                mProvinceWv.setItemData(mProvinces);
            }
        }.execute();
    }
}
