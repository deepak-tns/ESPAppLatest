package com.tns.espapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.tns.espapp.DataModel;
import com.tns.espapp.R;
import com.tns.espapp.activity.AccountStatementsActivity;
import com.tns.espapp.activity.AttendanceActivity;
import com.tns.espapp.activity.BillInfoActivity;
import com.tns.espapp.activity.EntitlementActivity;
import com.tns.espapp.activity.FeedbackActivity;
import com.tns.espapp.activity.FeedbackHistoryActivity;
import com.tns.espapp.activity.InfoBullteinActivity;
import com.tns.espapp.activity.LeaveApplyActivity;
import com.tns.espapp.activity.LeaveLedgerActivity;
import com.tns.espapp.activity.LeaveSummaryActivity;
import com.tns.espapp.activity.LeaveTransactionActivity;
import com.tns.espapp.activity.LocationHistoryActivity;
import com.tns.espapp.activity.OPEntryActivity;
import com.tns.espapp.activity.PersonalInfoActivity;
import com.tns.espapp.activity.CurrentLocationActivity;
import com.tns.espapp.activity.SalaryActivity;
import com.tns.espapp.activity.StoreInfoActivity;
import com.tns.espapp.activity.TDSActivity;
import com.tns.espapp.activity.TaxiFormActivity;
import com.tns.espapp.activity.TaxiHistoryActivity;
import com.tns.espapp.adapter.ImageAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaxiFragment extends Fragment {
    private ArrayList<DataModel> list;
    private View view;
    private GridView gridview;
    private Intent intent;


    public TaxiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =   inflater.inflate(R.layout.fragment_taxi, container, false);
        getLayoutsId(view);

        createData();
        ImageAdapter gridAdapter = new ImageAdapter(getContext(),list);
        gridview.setAdapter(gridAdapter);
        setOnClickListener();

        // Inflate the layout for this fragment
        return view;
    }
    private void getLayoutsId(View view)
    {
        gridview = (GridView)view.findViewById(R.id.gridview);
    }
    private void createData()
    {
        DataModel dataModel;
        list = new ArrayList<>();

        dataModel  = new DataModel();
        dataModel.setText("Taxi Form");
        dataModel.setImage(R.drawable.taxi);
        list.add(dataModel);    dataModel  = new DataModel();
        dataModel.setText("Taxi History");
        dataModel.setImage(R.drawable.taxihistory);
        list.add(dataModel);
        dataModel  = new DataModel();
        dataModel.setText("Location History");
        dataModel.setImage(R.drawable.locationhistory);
        list.add(dataModel);
        dataModel  = new DataModel();
        dataModel.setText("Current Location");
        dataModel.setImage(R.drawable.currentlocation);
        list.add(dataModel);
        dataModel  = new DataModel();
        dataModel.setText("Feedback");
        dataModel.setImage(R.drawable.feedback);
        list.add(dataModel);
        dataModel  = new DataModel();
        dataModel.setText("Feedback History");
        dataModel.setImage(R.drawable.feedbackhistory);
        list.add(dataModel);



    }
    private void setOnClickListener()
    {
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                switch (position) {
                    case 0:
                        intent = new Intent(getActivity(), PersonalInfoActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getActivity(), InfoBullteinActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(getActivity(), LeaveApplyActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(getActivity(), LeaveSummaryActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(getActivity(), LeaveTransactionActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(getActivity(), LeaveLedgerActivity.class);
                        startActivity(intent);
                        break;
                    case 6:
                        intent = new Intent(getActivity(), StoreInfoActivity.class);
                        startActivity(intent);
                        break;
                    case 7:
                        intent = new Intent(getActivity(), AccountStatementsActivity.class);
                        startActivity(intent);
                        break;
                    case 8:
                        intent = new Intent(getActivity(), BillInfoActivity.class);
                        startActivity(intent);
                        break;
                    case 9:
                        intent = new Intent(getActivity(), EntitlementActivity.class);
                        startActivity(intent);
                        break;
                    case 10:
                        intent = new Intent(getActivity(), AttendanceActivity.class);
                        startActivity(intent);
                        break;
                    case 11:
                        intent = new Intent(getActivity(), OPEntryActivity.class);
                        startActivity(intent);
                        break;
                    case 12:
                        intent = new Intent(getActivity(), SalaryActivity.class);
                        startActivity(intent);
                        break;
                    case 13:
                        intent = new Intent(getActivity(), TDSActivity.class);
                        startActivity(intent);
                        break;
                    case 14:
                        intent = new Intent(getActivity(), TaxiFormActivity.class);
                        startActivity(intent);
                        break;
                    case 15:
                        intent = new Intent(getActivity(), TaxiHistoryActivity.class);
                        startActivity(intent);
                        break;
                    case 16:
                        intent = new Intent(getActivity(), LocationHistoryActivity.class);
                        startActivity(intent);
                        break;
                    case 17:
                        intent = new Intent(getActivity(), CurrentLocationActivity.class);
                        startActivity(intent);
                        break;
                    case 18:
                        intent = new Intent(getActivity(), FeedbackActivity.class);
                        startActivity(intent);
                        break;
                    case 19:
                        intent = new Intent(getActivity(), FeedbackHistoryActivity.class);
                        startActivity(intent);
                        break;


                }

            }
        });
    }

}
