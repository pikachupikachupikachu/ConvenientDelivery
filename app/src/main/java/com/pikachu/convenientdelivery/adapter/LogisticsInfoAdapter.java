package com.pikachu.convenientdelivery.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.databinding.ItemLogisticsInfoBinding;
import com.pikachu.convenientdelivery.model.LogisticsInfo;

import java.util.Collections;
import java.util.List;

/**
 * LogisticsInfoAdapter
 */

public class LogisticsInfoAdapter extends BaseAdapter {

    private Context context;
    private List<LogisticsInfo> logisticsInfoList;

    public LogisticsInfoAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<LogisticsInfo> data) {
        Collections.reverse(data);
        this.logisticsInfoList = data;
    }

    @Override
    public int getCount() {
        if (logisticsInfoList != null) {
            return logisticsInfoList.size();
        }
        return 0;
    }

    @Override
    public LogisticsInfo getItem(int position) {
        return this.logisticsInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ItemLogisticsInfoBinding binding = DataBindingUtil.getBinding(view);
        if (binding == null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_logistics_info, parent, false);
            view = binding.getRoot();
        }
        if (logisticsInfoList == null) {
            return view;
        }
        binding.time.setText(logisticsInfoList.get(position).getTime());
        binding.info.setText(logisticsInfoList.get(position).getInfo());
        return view;
    }

}
