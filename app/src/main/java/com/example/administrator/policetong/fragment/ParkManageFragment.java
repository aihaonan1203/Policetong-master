package com.example.administrator.policetong.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.policetong.R;
import com.example.administrator.policetong.base.BaseFragment;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParkManageFragment extends BaseFragment {


    public ParkManageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_park_manage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {

    }

    @Override
    public void getPhoto(List<LocalMedia> selectList) {

    }
}
