package com.example.ptut.padc_simplehabit_one.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ptut.padc_simplehabit_one.R;
import com.example.ptut.padc_simplehabit_one.adapters.SeriesNewAdapter;
import com.example.ptut.padc_simplehabit_one.controllers.ItemClickListener;
import com.example.ptut.padc_simplehabit_one.datas.views.EmptyLayout;
import com.example.ptut.padc_simplehabit_one.events.SeriesEvent;
import com.example.ptut.padc_simplehabit_one.fragments.base.BaseFragment;
import com.example.ptut.padc_simplehabit_one.models.CurrentProgramModel;
import com.example.ptut.padc_simplehabit_one.shared.SmartRecyclerView;
import com.example.ptut.padc_simplehabit_one.shared.UtilsHttp;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class Fragment_Series extends BaseFragment{

    @BindView(R.id.series_recycler)
    SmartRecyclerView seriesRecycler;
    @BindView(R.id.empty_layout)
    EmptyLayout emptyLayout;

    SeriesNewAdapter seriesAdapter;

    ItemClickListener clickListener;

    public static Fragment_Series newInstance() {
        Bundle args = new Bundle();
        Fragment_Series fragment = new Fragment_Series();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_series, container, false);
        ButterKnife.bind(this, v);

        if(UtilsHttp.isNetworkAvailable(getContext())){
            CurrentProgramModel.getInstance(getContext()).loadCurrentProgramData();
        }else{
            Toast.makeText(getContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
        }

        seriesRecycler.setEmptyView(emptyLayout);

        seriesRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        seriesAdapter=new SeriesNewAdapter(getContext(),clickListener);
        seriesRecycler.setAdapter(seriesAdapter);

        return v;
    }

    @SuppressLint("ShowToast")
    @Subscribe(threadMode=ThreadMode.MainThread)
    public void OnNetworkErrorEvent(SeriesEvent.NetworkErrorEvent errorEvent){
        Toast.makeText(getContext(),errorEvent.getMsg(),Toast.LENGTH_SHORT);
    }
    @SuppressLint("ShowToast")
    @Subscribe(threadMode=ThreadMode.MainThread)
    public void OnErrorEvent(SeriesEvent.ErrorEvent errorEvent){
        Toast.makeText(getContext(),errorEvent.getMsg(),Toast.LENGTH_SHORT);
    }


    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onHomeScreenEvent(SeriesEvent.HomeScreenEvent homeScreenEvent){
        seriesAdapter.setNewData(homeScreenEvent.getHomeScreenVOS());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        clickListener=(ItemClickListener) context;
    }
}
