package com.pikachu.convenientdelivery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.model.Result;

import java.util.List;

/**
 * SearchResultsAdapter
 */
public class SearchResultsAdapter extends BaseAdapter {
    private Context context;
    private List<Result> resultList;

    public SearchResultsAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Result> data) {
        this.resultList = data;
    }

    @Override
    public int getCount() {
        if (resultList != null) {
            return resultList.size();
        }
        return 0;
    }


    @Override
    public Result getItem(int i) {
        if (resultList != null) {
            return resultList.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            holder = new Holder();
            view = LayoutInflater.from(context).inflate(R.layout.adapter_inputtips, null);
            holder.mName = (TextView) view.findViewById(R.id.name);
            holder.mAddress = (TextView) view.findViewById(R.id.adress);
            view.setTag(holder);
        } else{
            holder = (Holder)view.getTag();
        }
        if(resultList == null) {
            return view;
        }
        holder.mName.setText(resultList.get(i).getName());
        String address = resultList.get(i).getAddress();
        if(address == null || address.equals("")){
            holder.mAddress.setVisibility(View.GONE);
        }else{
            holder.mAddress.setVisibility(View.VISIBLE);
            holder.mAddress.setText(address);
        }

        return view;
    }

    class Holder {
        TextView mName;
        TextView mAddress;
    }
}
