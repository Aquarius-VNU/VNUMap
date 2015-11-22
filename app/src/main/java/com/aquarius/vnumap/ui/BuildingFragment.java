package com.aquarius.vnumap.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aquarius.vnumap.R;
import com.aquarius.vnumap.adapter.BuildingCardAdapter;

/**
 * Created by DangDo on 11/22/2015.
 */
public class BuildingFragment extends Fragment {
    RecyclerView recyclerView;

    public BuildingFragment(){
    }

    public static BuildingFragment newInstance(int tab){
        BuildingFragment fragment = new BuildingFragment();
        Bundle args = new Bundle();
        args.putInt("tab", tab);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View view =inflater.inflate(R.layout.fagment_building, container, false);
        final FragmentActivity activity = getActivity();

        recyclerView = (RecyclerView) view.findViewById(R.id.building_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);

        BuildingCardAdapter adapter = new BuildingCardAdapter(activity);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }
}
