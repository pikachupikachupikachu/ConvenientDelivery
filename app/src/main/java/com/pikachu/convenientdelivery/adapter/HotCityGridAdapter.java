package com.pikachu.convenientdelivery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pikachu.convenientdelivery.R;

import java.util.ArrayList;
import java.util.List;

/**
 * HotCityGridAdapter
 */

public class HotCityGridAdapter extends BaseAdapter {
    private Context context;
    private List<String> cities;

    public HotCityGridAdapter(Context context) {
        this.context = context;
        cities = new ArrayList<>();
        cities.add("北京");
        cities.add("上海");
        cities.add("广州");
        cities.add("深圳");
        cities.add("杭州");
        cities.add("南京");
        cities.add("天津");
        cities.add("武汉");
        cities.add("重庆");
    }

    @Override
    public int getCount() {
        return cities == null ? 0 : cities.size();
    }

    @Override
    public String getItem(int position) {
        return cities == null ? null : cities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        HotCityViewHolder holder;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.cp_item_hot_city_gridview, parent, false);
            holder = new HotCityViewHolder();
            holder.name = (TextView) view.findViewById(R.id.tv_hot_city_name);
            view.setTag(holder);
        }else{
            holder = (HotCityViewHolder) view.getTag();
        }
        holder.name.setText(cities.get(position));
        return view;
    }

    public static class HotCityViewHolder{
        TextView name;
    }

}
