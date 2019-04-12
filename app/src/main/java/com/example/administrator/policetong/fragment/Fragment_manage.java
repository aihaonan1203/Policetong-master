package com.example.administrator.policetong.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.administrator.policetong.R;
import com.example.administrator.policetong.fragment.manage.DailyService_manage;
import com.example.administrator.policetong.fragment.manage.PathParameter_manage;
import com.example.administrator.policetong.fragment.manage.PoliceConfirmed_manage;
import com.example.administrator.policetong.fragment.manage.SafetyChecks_manage;
import com.example.administrator.policetong.fragment.manage.Study_manage;
import com.example.administrator.policetong.fragment.manage.VisitRectification_manage;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_manage extends Fragment implements View.OnClickListener {


    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;

    public Fragment_manage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_manage, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        btn1 = (Button) view.findViewById(R.id.btn1);
        btn2 = (Button) view.findViewById(R.id.btn2);
        btn3 = (Button) view.findViewById(R.id.btn3);
        btn4 = (Button) view.findViewById(R.id.btn4);
        btn5 = (Button) view.findViewById(R.id.btn5);
        btn6 = (Button) view.findViewById(R.id.btn6);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        (getActivity().findViewById(R.id.ac_arrow_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                getChildFragmentManager().beginTransaction().replace(R.id.pc_context,new PoliceConfirmed_manage()).commit();
                break;
            case R.id.btn2:
                getChildFragmentManager().beginTransaction().replace(R.id.pc_context,new SafetyChecks_manage()).commit();
                break;
            case R.id.btn3:
                getChildFragmentManager().beginTransaction().replace(R.id.pc_context,new VisitRectification_manage()).commit();
                break;
            case R.id.btn4:
                getChildFragmentManager().beginTransaction().replace(R.id.pc_context,new DailyService_manage()).commit();
                break;
            case R.id.btn5:
                getChildFragmentManager().beginTransaction().replace(R.id.pc_context,new PathParameter_manage()).commit();
                break;
            case R.id.btn6:
                getChildFragmentManager().beginTransaction().replace(R.id.pc_context,new Study_manage()).commit();
                break;
        }
    }
}
