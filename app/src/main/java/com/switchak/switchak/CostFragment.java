package com.switchak.switchak;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class CostFragment extends Fragment implements Observer {
    private PieChart pieChart;

    private RoomsAdapter mAdapter;
    private List<PieEntry> pieEntries;
    private PieDataSet dataSet;
    private PieData pieData;
    private long beginningTime = FirebaseUtils.getInstance().getBeginningTime();
    private long endTime = FirebaseUtils.getInstance().getEndTime();
    private String totalReading ;
    private double cost ;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_cost, container, false);
        RecyclerView mRoomsList = rootView.findViewById(R.id.rv_cost_rooms);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRoomsList.setLayoutManager(layoutManager);
        mAdapter = new RoomsAdapter("history");
        mRoomsList.setAdapter(mAdapter);
        mRoomsList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        Fragment changePeriodFragment = new ChangePeriodFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_change_period, changePeriodFragment);
        transaction.commit();

        pieChart = rootView.findViewById(R.id.pie_chart);
        pieEntries = House.getInstance().getPieEntries();

        dataSet = new PieDataSet(pieEntries, "Usage percentage");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(15f);
        dataSet.setSliceSpace(5);


        pieChart.setUsePercentValues(true);


        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(false);
        pieChart.setHighlightPerTapEnabled(true);

        pieChart.setEntryLabelColor(Color.WHITE);

        pieData = new PieData(dataSet);

        FirebaseUtils.getInstance().addObserver(this);
        update(null, null);
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        pieChart.animateXY(1000, 1000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FirebaseUtils.getInstance().deleteObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {

        mAdapter.notifyDataSetChanged();

        if (House.getInstance().getPieEntries().size() > 0)
            pieChart.setData(pieData);

        if (arg != null && (int) arg == FirebaseUtils.READING_ADDED) {
            float timeOfLastReading = House.getInstance().getEntries().get(House.getInstance().getEntries().size() - 1).getX();
            if (timeOfLastReading >= beginningTime
                    && timeOfLastReading < endTime) {
                for (int i = 0; i < House.getInstance().getRooms().size(); i++) {
                    pieEntries.set(i, new PieEntry(pieEntries.get(i).getValue() + House.getInstance().getRooms().get(i)
                            .getReadings().get(House.getInstance().getEntries().size() - 1)));
                }
                pieData.notifyDataChanged();
                pieChart.notifyDataSetChanged();
                pieChart.invalidate();
            }
        }


        //If user changed period
        if (arg != null && (int) arg == FirebaseUtils.PERIOD_CHANGED) {
            beginningTime = FirebaseUtils.getInstance().getBeginningTime();
            endTime = FirebaseUtils.getInstance().getEndTime();
            for (int i = 0; i < House.getInstance().getRooms().size(); i++) {
                float roomIReading = 0;
                for (int j = 0; j < House.getInstance().getEntries().size(); j++) {
                    float timeOfJReading = House.getInstance().getEntries().get(j).getX();
                    if (timeOfJReading >= beginningTime && timeOfJReading < endTime)
                        roomIReading = roomIReading + House.getInstance().getRooms().get(i).getReadings().get(j);
                }
                pieEntries.set(i, new PieEntry(roomIReading));
            }
            pieData.notifyDataChanged();
            pieChart.notifyDataSetChanged();
            pieChart.animateXY(1000, 1000);
        }
    }


    public double cost (Observable observe , float value ){
        totalReading= String.valueOf(FirebaseUtils.getInstance().getTotalLatestReading());
        value = Float.parseFloat(totalReading);
        if(value>=0 && value<=50)
            cost =value * 0.13;
        else if(value>=51 && value<=100)
            cost=value*0.22;
        else if(value>100 && value<=200)
            cost=value*0.22;
        else if(value>200 && value<=350)
            cost=value*0.45;
        else if(value>350 && value<=650)
            cost=value*0.55;
        else if(value>650 && value<=1000)
            cost=value*0.95;
        else if(value>1000)
            cost=value*1.35;
        return cost ;

    }
}

