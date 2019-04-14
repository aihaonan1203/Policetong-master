package com.example.administrator.policetong.fragment.manage;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.bean.Study_bean;
import com.example.administrator.policetong.bean.VisitRectification_bean;
import com.example.administrator.policetong.fragment.Fragment_manage;
import com.example.administrator.policetong.httppost.getNetInfo;
import com.example.administrator.policetong.utils.LoadingDialog;
import com.example.administrator.policetong.utils.NetworkChangeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Study_manage extends Fragment {

    List<Study_bean> listbean;
    private ListView pc_manage_listview;
    private TextView nodata;
    private MyAdapter myAdapter;
    public Study_manage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_study_manage, container, false);
        initView(view);
        initValues();
        return view;
    }


    @SuppressLint("SetTextI18n")
    private void showPopueWindow(final int id) {
        View popView = View.inflate(getActivity(), R.layout.buttonpopwind, null);
        Button bt_camera = popView.findViewById(R.id.btn_pop_camera);
        Button bt_cancle = popView.findViewById(R.id.btn_pop_cancel);
        ((TextView) popView.findViewById(R.id.number)).setText(id + 1 + "");
        final PopupWindow popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        //点击外部popueWindow消失
        popupWindow.setAnimationStyle(R.style.anim_menu_bottombar);
        popupWindow.setOutsideTouchable(true);
        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                showDialog(id);
            }

        });
        bt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //popupWindow消失屏幕变为不透明
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1.0f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        //popupWindow出现屏幕变为半透明
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.5f;
        getActivity().getWindow().setAttributes(lp);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 0);

    }

    private void initValues() {
        listbean = new ArrayList<>();
        LoadingDialog.showDialog(getActivity(), "正在获取数据");
        getNetData();
    }

    private void showDialog(final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.fragment_studdy, null);
        Button cancle = view.findViewById(R.id.stu_cancle);
        Button submit = view.findViewById(R.id.stu_submit);
        submit.setText("修改");
        cancle.setVisibility(View.VISIBLE);
        final EditText stu_study_time = view.findViewById(R.id.stu_study_time);
        final EditText stu_time = view.findViewById(R.id.stu_time);
        final EditText stu_zd = view.findViewById(R.id.stu_zd);
        final EditText stu_context = view.findViewById(R.id.stu_context);
        final Study_bean bean = listbean.get(id);
        stu_study_time.setText(bean.getUsername());
        stu_time.setText(bean.getDate());
        stu_zd.setText(bean.getDetachment());
        stu_context.setText(bean.getContent());
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = stu_study_time.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getContext(), "姓名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                String zd = stu_zd.getText().toString().trim();
                if (TextUtils.isEmpty(zd)) {
                    Toast.makeText(getContext(), "所属中队能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                String time = stu_time.getText().toString().trim();
                if (TextUtils.isEmpty(time)) {
                    Toast.makeText(getContext(), "学习时间不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                String context = stu_context.getText().toString().trim();
                if (TextUtils.isEmpty(context)) {
                    Toast.makeText(getContext(), "学习内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.dismiss();
                LoadingDialog.showDialog(getActivity(), "正在提交内容！");
                amend_data(new Study_bean(time,zd,bean.getGrop(),bean.getUserid(),context,name));
            }
        });
        dialog.show();
    }

    private void amend_data(final Study_bean bean) {
        Map info = new HashMap();
        info.put("userid", bean.getUserid());
        info.put("date", bean.getDate());
        getNetInfo.NetInfo(getActivity(), "updatestudy", new JSONObject(info), new getNetInfo.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject object) throws JSONException {
                Log.e("onSuccess: ",object.toString() );
                if (object.getString("RESULT").equals("S")) {
                    setDataToService(bean);
                } else {
                    Toast.makeText(getActivity(), "修改失败", Toast.LENGTH_SHORT).show();
                    LoadingDialog.disDialog();
                }
            }

            @Override
            public void onError(VolleyError volleyError) {
                LoadingDialog.disDialog();
                Toast.makeText(getActivity(), "提交失败，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDataToService(Study_bean bean) {
        Map info=new HashMap();
        info.put("username",bean.getUsername());
        info.put("userid",bean.getUserid());
        info.put("date",bean.getDate());
        info.put("content",bean.getContent());
        info.put("detachment",bean.getDetachment());
        getNetInfo.NetInfo(getActivity(), "insertstudys", new JSONObject(info), new getNetInfo.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject object) throws JSONException {
                Log.e("onSuccess: ",object.toString() );
                if (object.getString("RESULT").equals("S")){
                    Toast.makeText(getActivity(), "提交成功", Toast.LENGTH_SHORT).show();
                    LoadingDialog.disDialog();
                    initValues();
                }else {
                    Toast.makeText(getActivity(), "提交失败", Toast.LENGTH_SHORT).show();
                    LoadingDialog.disDialog();
                }
            }

            @Override
            public void onError(VolleyError volleyError) {
                LoadingDialog.disDialog();
                Toast.makeText(getActivity(), "提交失败，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initView(View view) {
        pc_manage_listview =view.findViewById(R.id.pc_manage_listview);
        nodata = view.findViewById(R.id.nodata);
        pc_manage_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showPopueWindow(i);
            }
        });
        (getActivity().findViewById(R.id.ac_arrow_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragment().getChildFragmentManager().beginTransaction().replace(R.id.pc_context,new Fragment_manage()).commit();
            }
        });
    }

    public void getNetData() {
        listbean.clear();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        Map map = new HashMap();
        map.put("userid", sharedPreferences.getString("userid", ""));
        getNetInfo.NetInfoArray(getActivity(), "selectstudy", new JSONObject(map), new getNetInfo.VolleyArrayCallback() {
            @Override
            public void onSuccess(JSONArray object) throws JSONException {
                for (int i = 0; i < object.length(); i++) {
                    JSONObject j = (JSONObject) object.get(i);
                    if (j.getString("modify").equals("未修改")) {
                        listbean.add(new Study_bean(j.getString("date"), j.getString("detachment"), j.getString("grop"), j.getString("userid"), j.getString("content"),
                                j.getString("username")));
                    }
                }
                if (listbean.size() == 0) {
                    nodata.setVisibility(View.VISIBLE);
                    pc_manage_listview.setVisibility(View.GONE);
                } else {
                    Collections.reverse(listbean);
                    nodata.setVisibility(View.GONE);
                    pc_manage_listview.setVisibility(View.VISIBLE);
                }
                LoadingDialog.disDialog();
                shuaxinListview();
            }

            @Override
            public void onError(VolleyError volleyError) {
                LoadingDialog.showToast_shibai(getActivity());
                LoadingDialog.disDialog();
            }
        });
    }

    public void shuaxinListview() {
        if (myAdapter == null) {
            myAdapter = new MyAdapter();
            pc_manage_listview.setAdapter(myAdapter);
        } else {
            myAdapter.notifyDataSetChanged();
        }
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listbean.size();
        }

        @Override
        public Object getItem(int i) {
            return listbean.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(getActivity(), R.layout.visitrectification_item, null);
                view.setTag(new MyAdapter.ViewHolder(view));
            }
            set_data_into_layout(i, listbean.get(i), (ViewHolder) view.getTag());
            return view;
        }

        @SuppressLint("SetTextI18n")
        private void set_data_into_layout(int i, Study_bean bean, MyAdapter.ViewHolder holder) {
            holder.pc_item_id.setText(i + 1 + "");
            holder.pc_item_txt1.setText("姓名："+bean.getUsername());
            holder.pc_item_txt2.setText("记录时间："+bean.getDate());
            holder.pc_item_txt3.setText("所属中队："+bean.getDetachment());
            holder.pc_item_txt4.setText("学习内容："+bean.getContent());
            holder.pc_item_txt5.setVisibility(View.GONE);
        }

        private class ViewHolder {
            public View rootView;
            public TextView pc_item_id;
            public TextView pc_item_txt1;
            public TextView pc_item_txt2;
            public TextView pc_item_txt3;
            public TextView pc_item_txt4;
            public TextView pc_item_txt5;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.pc_item_id = rootView.findViewById(R.id.pc_item_id);
                this.pc_item_txt1 = rootView.findViewById(R.id.pc_item_txt1);
                this.pc_item_txt2 = rootView.findViewById(R.id.pc_item_txt2);
                this.pc_item_txt3 = rootView.findViewById(R.id.pc_item_txt3);
                this.pc_item_txt4 = rootView.findViewById(R.id.pc_item_txt4);
                this.pc_item_txt5 = rootView.findViewById(R.id.pc_item_txt5);
            }

        }
    }
}
