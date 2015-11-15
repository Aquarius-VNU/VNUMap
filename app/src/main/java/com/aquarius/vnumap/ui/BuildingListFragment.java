package com.aquarius.vnumap.ui;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.aquarius.vnumap.R;
import com.aquarius.vnumap.adapter.CustomListAdapter;

/**
 * Created by DangDo on 11/15/2015.
 */
public class BuildingListFragment extends ListFragment {

    private Integer[] imgIds ={
            R.drawable.hoamat, R.drawable.matmat,
            R.drawable.nhiethuyet, R.drawable.nhonho,
            R.drawable.oto, R.drawable.sodo
    };
    private String[] imgNames = {
            "1", "2", "3", "4", "5", "6"
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        CustomListAdapter adapter = new CustomListAdapter(getActivity(), imgNames, imgIds);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){

    }
}
