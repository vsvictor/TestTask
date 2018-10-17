package com.education.middle.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.education.middle.R;
import com.education.middle.data.Row;
import com.education.middle.data.RowStatus;
import com.education.middle.data.Sort;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.ViewHolder> {

    public interface OnClicked{
        void onClicked(Row row);
    }

    private Context context;
    private List<Row> data;
    private LayoutInflater inflater;
    private OnClicked listener;

    public LinkAdapter(Context context, List<Row> data){
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position){
        Row r = data.get(position);
        if(r.getStatus() == RowStatus.LOADED) return 1;
        else if(r.getStatus() == RowStatus.ERROR) return 2;
        else  return 3;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType){
            case 1: {view = inflater.inflate(R.layout.loaded_item, parent, false);break;}
            case 2: {view = inflater.inflate(R.layout.error_item, parent, false);break;}
            case 3: {view = inflater.inflate(R.layout.undefined_item, parent, false);break;}
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final Row row = data.get(position);
        viewHolder.tvURL.setText(row.getUrl());
        viewHolder.tvDate.setText(row.getDate().toString());
        viewHolder.llRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) listener.onClicked(row);
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public void update(List<Row> list){
        data = list;
        notifyDataSetChanged();
    }

    public void sort(Sort sort){
        switch (sort){
            case DATE:{
                Collections.sort(data, new Comparator<Row>() {
                    @Override
                    public int compare(Row o1, Row o2) {
                        Long l = new Long(o2.getDate().getTime()-o1.getDate().getTime());
                        return l.intValue();
                    }
                });
                break;
            }
            case STATUS:{
                Collections.sort(data, new Comparator<Row>() {
                    @Override
                    public int compare(Row o1, Row o2) {
                        return o2.getStatus().toInt()-o1.getStatus().toInt();
                    }
                });
                break;
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public AppCompatTextView tvURL;
        public AppCompatTextView tvDate;
        public LinearLayout llRow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvURL = itemView.findViewById(R.id.tvLink);
            tvDate = itemView.findViewById(R.id.tvDate);
            llRow = itemView.findViewById(R.id.llRow);
        }
    }

    public void setOnClickListener(OnClicked listener){
        this.listener = listener;
    }
}