package com.pikachu.convenientdelivery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.model.City;

import java.util.List;

/**
 * CityResultListAdapter
 */
public class CityResultListAdapter extends BaseAdapter {
    
    private Context context;
    private List<City> cities;

    public CityResultListAdapter(Context context, List<City> cities) {
        this.cities = cities;
        this.context = context;
    }

    public void changeData(List<City> list){
        if (cities == null) {
            cities = list;
        } else {
            cities.clear();
            cities.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return cities == null ? 0 : cities.size();
    }

    @Override
    public City getItem(int position) {
        return cities == null ? null : cities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ResultViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.cp_item_search_result_listview, parent, false);
            holder = new ResultViewHolder();
            holder.name = (TextView) view.findViewById(R.id.tv_item_result_list_view_name);
            view.setTag(holder);
        } else {
            holder = (ResultViewHolder) view.getTag();
        }
        holder.name.setText(cities.get(position).getName());
        return view;
    }

    public static class ResultViewHolder {
        TextView name;
    }
    
}
