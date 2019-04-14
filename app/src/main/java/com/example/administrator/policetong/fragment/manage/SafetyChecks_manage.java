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
import com.example.administrator.policetong.bean.SafetyChecks_bean;
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
public class SafetyChecks_manage extends Fragment {

    private ListView pc_manage_listview;
    private List<SafetyChecks_bean> listbean;
    MyAdapter myAdapter;
    TextView nodata;

    public SafetyChecks_manage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_safety_checks_manage, container, false);
        view.setClickable(true);
        initValues();
        initView(view);
        return view;
    }

    private void initValues() {
        listbean = new ArrayList<>();
        LoadingDialog.showDialog(getActivity(),"正在获取数据");
        getNetData();
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
        nodata = view.findViewById(R.id.nodata);
        pc_manage_listview = (ListView) view.findViewById(R.id.pc_manage_listview);
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
        View view = View.inflate(getActivity(), R.layout.fragment_safety_checks_add, null);
        Button cancle = view.findViewById(R.id.pc_add_cancle);
        Button submit = view.findViewById(R.id.safety_add_submit);
        submit.setText("修改");
        cancle.setVisibility(View.VISIBLE);
        final EditText safety_time = view.findViewById(R.id.safety_time);
        final EditText safety_paddr = view.findViewById(R.id.safety_paddr);
        final EditText safety_licheng = view.findViewById(R.id.safety_lidao);
        final EditText safety_unit = view.findViewById(R.id.safety_unit);
        final EditText safety_fxunit = view.findViewById(R.id.safety_fxunit);
        final EditText safety_shangbao = view.findViewById(R.id.safety_shangbao);
        final EditText safety_zhenggai = view.findViewById(R.id.safety_zhenggai);
        final EditText safety_zgtime = view.findViewById(R.id.safety_zgtime);
        final SafetyChecks_bean bean = listbean.get(id);
        safety_time.setText(bean.getDate());
        safety_paddr.setText(bean.getRoad());
        safety_licheng.setText(bean.getDistance());
        safety_unit.setText(bean.getAsunit());
        safety_fxunit.setText(bean.getFdunit());
        safety_shangbao.setText(bean.getReport());
        safety_zhenggai.setText(bean.getRectify());
        safety_zgtime.setText(bean.getRectifydate());
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
                String time = safety_time.getText().toString().trim();
                if (TextUtils.isEmpty(time)) {
                    Toast.makeText(getContext(), "时间不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                String paddr = safety_paddr.getText().toString().trim();
                if (TextUtils.isEmpty(paddr)) {
                    Toast.makeText(getContext(), "道路不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                String licheng = safety_licheng.getText().toString().trim();
                if (TextUtils.isEmpty(licheng)) {
                    Toast.makeText(getContext(), "里程不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                String unit = safety_unit.getText().toString().trim();
                if (TextUtils.isEmpty(unit)) {
                    Toast.makeText(getContext(), "所属单位不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                String fxunit = safety_fxunit.getText().toString().trim();
                if (TextUtils.isEmpty(fxunit)) {
                    Toast.makeText(getContext(), "发现不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                String shangbao = safety_shangbao.getText().toString().trim();
                if (TextUtils.isEmpty(shangbao)) {
                    Toast.makeText(getContext(), "是否上报不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                String zhenggai = safety_zhenggai.getText().toString().trim();
                if (TextUtils.isEmpty(zhenggai)) {
                    Toast.makeText(getContext(), "是否整改不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                String zgtime = safety_zgtime.getText().toString().trim();
                if (TextUtils.isEmpty(zgtime)) {
                    Toast.makeText(getContext(), "整改时间不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                LoadingDialog.showDialog(getActivity(), "正在提交内容！");
                dialog.dismiss();
                amend_data(bean.getUserid(),time,paddr,licheng,unit,fxunit,shangbao,bean.getImg(),zhenggai,zgtime);
            }
        });
        dialog.show();
    }

    private void submitData(String date, String road, String distance, String asunit, String fdunit,String report, String img, String rectify,String rectifydate) {
        Map info=new HashMap();
        SharedPreferences sp=getActivity().getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        info.put("username",sp.getString("username",""));
        info.put("userid",sp.getString("userid",""));
        info.put("date",date);
        info.put("road",road);
        info.put("distance",distance);
        info.put("asunit",asunit);
        info.put("fdunit",fdunit);
        info.put("report",report);
        info.put("img",img);
        info.put("rectify",rectify);
        info.put("rectifydate",rectifydate);
        info.put("group",sp.getString("group",""));
        info.put("detachment",sp.getString("detachment",""));
        getNetInfo.NetInfo(getActivity(), "insertdanger", new JSONObject(info), new getNetInfo.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject object) throws JSONException {
                Log.e("onSuccess=====: ",object.toString() );
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

    private void amend_data(String userid, final String date, final String road, final String distance, final String asunit, final String fdunit, final String report, final String img, final String rectify, final String rectifydate) {
        Map info = new HashMap();
        info.put("userid", userid);
        info.put("date", date);
        getNetInfo.NetInfo(getActivity(), "updatedanger", new JSONObject(info), new getNetInfo.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject object) throws JSONException {
                Log.e("onSuccess: ",object.toString() );
                if (object.getString("RESULT").equals("S")) {
                    submitData(date,road,distance,asunit,fdunit,report,img,rectify,rectifydate);
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
        getNetInfo.NetInfoArray(getActivity(), "selectdanger", new JSONObject(map), new getNetInfo.VolleyArrayCallback() {
            @Override
            public void onSuccess(JSONArray object) throws JSONException {
                for (int i = 0; i < object.length(); i++) {
                    JSONObject j = (JSONObject) object.get(i);
                    if (j.getString("modify").equals("未修改")) {
                        listbean.add(new SafetyChecks_bean(j.getString("date"), j.getString("detachment"), j.getString("img"), j.getString("rectifydate"), j.getString("distance"),
                                j.getString("fdunit"), j.getString("asunit"), j.getString("userid"), j.getString("rectify"), j.getString("road"), j.getString("report"),
                                j.getString("grop")));
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
        private void set_data_into_layout(int i, SafetyChecks_bean bean, ViewHolder holder) {
            holder.pc_item_id.setText(i+1+"");
            holder.pc_item_txt1.setText("日期："+bean.getDate());
            holder.pc_item_txt2.setText("街道："+bean.getRoad());
            holder.pc_item_txt3.setText("里程："+bean.getDistance());
            holder.pc_item_txt4.setText("所属行政单位："+bean.getAsunit());
            holder.pc_item_txt5.setText("发现单位："+bean.getFdunit());
            holder.pc_item_txt6.setText("是否报告："+bean.getReport());
            holder.pc_item_txt7.setText("是否整改："+bean.getRectify());
            holder.pc_item_txt8.setText("整改日期："+bean.getRectifydate());
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
