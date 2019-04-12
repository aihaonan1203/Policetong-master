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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.bean.Daily_bean;
import com.example.administrator.policetong.bean.Road_bean;
import com.example.administrator.policetong.bean.SafetyChecks_bean;
import com.example.administrator.policetong.bean.VisitRectification_bean;
import com.example.administrator.policetong.fragment.Fragment_manage;
import com.example.administrator.policetong.httppost.getNetInfo;
import com.example.administrator.policetong.utils.LoadingDialog;

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
public class PathParameter_manage extends Fragment {

    List<Road_bean> listbean;
    private ListView pc_manage_listview;
    private TextView nodata;
    private MyAdapter myAdapter;

    public PathParameter_manage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_path_parameter_manage, container, false);
        view.setClickable(true);
        initView(view);
        getNetData();
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

    private void showDialog(final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.fragment_path_parameter, null);
        Button cancle = view.findViewById(R.id.pp_add_cancle);
        Button submit = view.findViewById(R.id.pp_add_submit);
        submit.setText("修改");
        cancle.setVisibility(View.VISIBLE);
        final EditText pp_name = view.findViewById(R.id.pp_name);
        final EditText pp_nature = view.findViewById(R.id.pp_nature);
        final EditText pp_grade = view.findViewById(R.id.pp_grade);
        final EditText pp_pavement = view.findViewById(R.id.pp_pavement);
        final EditText pp_start = view.findViewById(R.id.pp_start);
        final EditText pp_end = view.findViewById(R.id.pp_end);
        final EditText pp_distance = view.findViewById(R.id.pp_distance);
        final Road_bean bean = listbean.get(id);
        pp_name.setText(bean.getName());
        pp_grade.setText(bean.getGrade());
        pp_start.setText(bean.getStart());
        pp_end.setText(bean.getEnd());
        pp_distance.setText(bean.getDistance());
        String ai[]=getResources().getStringArray(R.array.path);
        for (int i = 0; i < ai.length; i++) {
            if (bean.getNature().equals(ai[i])){
                pp_nature.setSelection(i);
            }
        }
        ai=getResources().getStringArray(R.array.num);
        for (int i = 0; i < ai.length; i++) {
            if (bean.getPavement().equals(ai[i])){
                pp_nature.setSelection(i);
            }
        }
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
                name = pp_name.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getContext(), "名字不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                grade = pp_grade.getText().toString().trim();
                if (TextUtils.isEmpty(grade)) {
                    Toast.makeText(getContext(), "等级不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                start = pp_start.getText().toString().trim();
                if (TextUtils.isEmpty(start)) {
                    Toast.makeText(getContext(), "起点不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                end = pp_end.getText().toString().trim();
                if (TextUtils.isEmpty(end)) {
                    Toast.makeText(getContext(), "终点不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                licheng = pp_distance.getText().toString().trim();
                if (TextUtils.isEmpty(licheng)) {
                    Toast.makeText(getContext(), "里程不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                nature = pp_nature.getText().toString();
                if (TextUtils.isEmpty(nature)) {
                    Toast.makeText(getContext(), "性质不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                pavement=pp_pavement.getText().toString();
                if (TextUtils.isEmpty(pavement)) {
                    Toast.makeText(getContext(), "路面状况不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.dismiss();
                LoadingDialog.showDialog(getActivity(), "正在提交内容！");
                amend_data(bean);
            }
        });
        dialog.show();
    }
    String start,end,name,grade,licheng,nature,pavement;
    public void get_data_form_server(Road_bean bean) {
        Map info=new HashMap();
        SharedPreferences sp=getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        info.put("username",sp.getString("username",""));
        info.put("userid",sp.getString("userid",""));
        info.put("name",name);
        info.put("nature",nature);
        info.put("grade",grade);
        info.put("pavement",pavement);
        info.put("start",start);
        info.put("end",end);
        info.put("distance",licheng);
        info.put("date", bean.getDate());
        info.put("group",sp.getString("group",""));
        info.put("detachment",sp.getString("detachment",""));
        getNetInfo.NetInfo(getActivity(), "insertroad", new JSONObject(info), new getNetInfo.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject object) throws JSONException {
                Log.e("onSuccess: ",object.toString() );
                if (object.getString("RESULT").equals("S")){
                    Toast.makeText(getActivity(), "提交成功", Toast.LENGTH_SHORT).show();
                    LoadingDialog.disDialog();
                    getNetData();
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

    private void amend_data(final Road_bean bean) {
        Map info = new HashMap();
        info.put("userid", bean.getUserid());
        info.put("date", bean.getDate());
        getNetInfo.NetInfo(getActivity(), "updateroad", new JSONObject(info), new getNetInfo.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject object) throws JSONException {
                Log.e("onSuccess: ",object.toString() );
                if (object.getString("RESULT").equals("S")) {
                    get_data_form_server(bean);
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



    public void getNetData() {
        listbean.clear();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        Map map = new HashMap();
        map.put("userid", sharedPreferences.getString("userid", ""));
        getNetInfo.NetInfoArray(getActivity(), "selectroad", new JSONObject(map), new getNetInfo.VolleyArrayCallback() {
            @Override
            public void onSuccess(JSONArray object) throws JSONException {
                Log.e("onSuccess: ",object.toString() );
                for (int i = 0; i < object.length(); i++) {
                    JSONObject j = (JSONObject) object.get(i);
                    if (j.getString("modify").equals("未修改")) {
                        listbean.add(new Road_bean(j.getString("pavement"),j.getString("date"),j.getString("detachment"),j.getString("distance"),j.getString("nature"),
                                j.getString("grade"),j.getString("name"),j.getString("start"),j.getString("end"),j.getString("grop"),j.getString("userid")));
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
    private void initView(View view) {
        listbean=new ArrayList<>();
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
                view = View.inflate(getActivity(), R.layout.safetychecks_item, null);
                view.setTag(new ViewHolder(view));
            }
            set_data_into_layout(i,listbean.get(i),(ViewHolder)view.getTag());
            return view;
        }

        @SuppressLint("SetTextI18n")
        private void set_data_into_layout(int i, Road_bean bean,ViewHolder holder) {
            holder.pc_item_id.setText(i+1+"");
            holder.pc_item_txt1.setText("名称："+bean.getName());
            holder.pc_item_txt2.setText("性质："+bean.getNature());
            holder.pc_item_txt3.setText("等级："+bean.getGrade());
            holder.pc_item_txt4.setText("路面："+bean.getPavement());
            holder.pc_item_txt5.setText("起点："+bean.getStart());
            holder.pc_item_txt6.setText("终点："+bean.getEnd());
            holder.pc_item_txt7.setText("里程："+bean.getDistance());
            holder.pc_item_txt8.setText("提交日期："+bean.getDate());
        }

        private   class ViewHolder {
            public View rootView;
            public TextView pc_item_id;
            public TextView pc_item_txt1;
            public TextView pc_item_txt2;
            public TextView pc_item_txt3;
            public TextView pc_item_txt4;
            public TextView pc_item_txt5;
            public TextView pc_item_txt6;
            public TextView pc_item_txt7;
            public TextView pc_item_txt8;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.pc_item_id = (TextView) rootView.findViewById(R.id.pc_item_id);
                this.pc_item_txt1 = (TextView) rootView.findViewById(R.id.pc_item_txt1);
                this.pc_item_txt2 = (TextView) rootView.findViewById(R.id.pc_item_txt2);
                this.pc_item_txt3 = (TextView) rootView.findViewById(R.id.pc_item_txt3);
                this.pc_item_txt4 = (TextView) rootView.findViewById(R.id.pc_item_txt4);
                this.pc_item_txt5 = (TextView) rootView.findViewById(R.id.pc_item_txt5);
                this.pc_item_txt6 = (TextView) rootView.findViewById(R.id.pc_item_txt6);
                this.pc_item_txt7 = (TextView) rootView.findViewById(R.id.pc_item_txt7);
                this.pc_item_txt8 = (TextView) rootView.findViewById(R.id.pc_item_txt8);
            }

        }
    }
}
