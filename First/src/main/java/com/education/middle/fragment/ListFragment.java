package com.education.middle.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.education.middle.MainActivity;
import com.education.middle.R;
import com.education.middle.data.LinkModel;
import com.education.middle.data.Row;
import com.education.middle.data.Sort;

import java.util.ArrayList;
import java.util.List;


public class ListFragment extends Fragment {
    private LinkModel model;
    private RecyclerView rcUris;
    private LinkAdapter adapter;
    public ListFragment() {
    }

    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle data){
        adapter = new LinkAdapter(getActivity(), new ArrayList<Row>());
        adapter.setOnClickListener((MainActivity)getActivity());
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rcUris = (RecyclerView) view.findViewById(R.id.rcUri);
        rcUris.setHasFixedSize(true);
        rcUris.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rcUris.getContext(), manager.getOrientation());
        rcUris.addItemDecoration(dividerItemDecoration);
        rcUris.setAdapter(adapter);
        model = ViewModelProviders.of(this).get(LinkModel.class);
        model.getStream().observe(this, new Observer<List<Row>>() {
            @Override
            public void onChanged(@Nullable List<Row> list) {
                adapter.update(list);
            }
        });
    }

    public void setSort(Sort by){
        adapter.sort(by);
    }
}
