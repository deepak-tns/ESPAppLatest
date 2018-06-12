package com.tns.espapp.fragment_new;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tns.espapp.ListviewHelper;
import com.tns.espapp.R;
import com.tns.espapp.activity.HomeActivity;
import com.tns.espapp.adapter.CheckListFormAdapterListview;
import com.tns.espapp.database.ChecklistData;
import com.tns.espapp.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class RouteCheckListFragment extends Fragment {

    private DatabaseHandler db;

    public RouteCheckListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_route_check_list, container, false);

        ListView lst_check_list =(ListView)v.findViewById(R.id.listviewreports_routechecklist) ;
              db = new DatabaseHandler(getActivity());
        List<ChecklistData> checklistDatas = db.getAllChecklist();
        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add("New Templete");


        if(checklistDatas.size() >0){

            for(ChecklistData c : checklistDatas){
                stringArrayList.add(c.getFormno());

            }
        }
        Set<String> primesWithoutDuplicates = new LinkedHashSet<String>(stringArrayList); // now let's clear the ArrayList so that we can copy all elements from LinkedHashSet primes.clear(); // copying elements but without any duplicates primes.addAll(primesWithoutDuplicates);

        stringArrayList.clear(); // copying elements but without any duplicates primes.addAll(primesWithoutDuplicates);
        stringArrayList.addAll(primesWithoutDuplicates);

        CheckListFormAdapterListview adapter = new CheckListFormAdapterListview(
                getActivity(), R.layout.home_check_list_row_adapter,stringArrayList );

        lst_check_list.setAdapter(adapter);
        //ListviewHelper.getListViewSize(lst_check_list);
        adapter.notifyDataSetChanged();

        return v;
    }

}
