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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.bean.Daily_bean;
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
public class DailyService_manage extends Fragment {
    List<Daily_bean> listbean;
    private ListView pc_manage_listview;
    private TextView nodata;
    private MyAdapter myAdapter;

    public DailyService_manage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_daily_service_manage, container, false);
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
        View view = View.inflate(getActivity(), R.layout.fragment_daily, null);
        Button cancle = view.findViewById(R.id.ds_add_cancle);
        Button submit = view.findViewById(R.id.ds_add_submit);
        submit.setText("修改");
        cancle.setVisibility(View.VISIBLE);
        LinearLayout linearLayout1=view.findViewById(R.id.mLinerLayout1);
        LinearLayout linearLayout2=view.findViewById(R.id.mLinerLayout2);
        linearLayout2.setVisibility(View.VISIBLE);
        linearLayout1.setVisibility(View.GONE);
        TextView textView=view.findViewById(R.id.ds_text);
        textView.setVisibility(View.GONE);
        final EditText ds_start_time = view.findViewById(R.id.ds_start_time);
        final EditText ds_end_time = view.findViewById(R.id.ds_end_time);
        final EditText ds_police = view.findViewById(R.id.ds_police);
        final EditText ds_paddr = view.findViewById(R.id.ds_paddr);
        final EditText ds_licheng = view.findViewById(R.id.ds_licheng);
        final EditText ds_context = view.findViewById(R.id.ds_context);
        final EditText ds_zdcc = view.findViewById(R.id.input_data);
        final EditText ds_qwtype = view.findViewById(R.id.ds_qwtype);
        final Daily_bean bean = listbean.get(id);
        ds_start_time.setText(bean.getBegintime());
        ds_end_time.setText(bean.getEndtime());
        ds_police.setText(bean.getForces());
        ds_paddr.setText(bean.getRoad());
        ds_licheng.setText(bean.getDistance());
        ds_zdcc.setText(bean.getBehavior());
        ds_context.setText(bean.getContent());
        String ai[]=getResources().getStringArray(R.array.qwtype);
        for (int i = 0; i < ai.length; i++) {
            if (bean.getWorktype().equals(ai[i])){
                ds_qwtype.setSelection(i);
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
                starttime = ds_start_time.getText().toString().trim();
                if (TextUtils.isEmpty(starttime)) {
                    Toast.makeText(getContext(), "开始时间不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                endtime = ds_end_time.getText().toString().trim();
                if (TextUtils.isEmpty(endtime)) {
                    Toast.makeText(getContext(), "结束时间不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                police = ds_police.getText().toString().trim();
                if (TextUtils.isEmpty(police)) {
                    Toast.makeText(getContext(), "出动警力不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                paddr = ds_paddr.getText().toString().trim();
                if (TextUtils.isEmpty(paddr)) {
                    Toast.makeText(getContext(), "道路不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                licheng = ds_licheng.getText().toString().trim();
                if (TextUtils.isEmpty(licheng)) {
                    Toast.makeText(getContext(), "里程/参照物不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                context = ds_context.getText().toString().trim();
                if (TextUtils.isEmpty(context)) {
                    Toast.makeText(getContext(), "简要内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                data=ds_zdcc.getText().toString().trim();
                if (TextUtils.isEmpty(data)){
                    Toast.makeText(getContext(), "您还没有选择重点查处违法行为", Toast.LENGTH_SHORT).show();
                    return;
                }
                s = ds_qwtype.getText().toString();
                if (TextUtils.isEmpty(s)){
                    Toast.makeText(getContext(), "勤务类型不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.dismiss();
                LoadingDialog.showDialog(getActivity(), "正在提交内容！");
                amend_data(bean);
            }
        });
        dialog.show();
    }
    String starttime,endtime,police,paddr,licheng,context,s,data;

    private void amend_data(final Daily_bean bean) {
        Map info = new HashMap();
        info.put("userid", bean.getUserid());
        info.put("date", bean.getDate());
        getNetInfo.NetInfo(getActivity(), "updatedaily", new JSONObject(info), new getNetInfo.VolleyCallback() {
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

    public void get_data_form_server(Daily_bean bean) {
        Map info=new HashMap();
        SharedPreferences sp=getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        info.put("username",sp.getString("username",""));
        info.put("userid",sp.getString("userid",""));
        info.put("begintime",starttime);
        info.put("endtime",endtime);
        info.put("forces",police);
        info.put("road",paddr);
        info.put("img",bean.getImg());
        info.put("date",bean.getDate());
        info.put("distance",licheng);
        info.put("worktype",s);
        info.put("content",context);
        info.put("behavior",data);
        info.put("group",sp.getString("group",""));
        info.put("detachment",sp.getString("detachment",""));
        getNetInfo.NetInfo(getActivity(), "insertdaily", new JSONObject(info), new getNetInfo.VolleyCallback() {
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
                view = View.inflate(getActivity(), R.layout.dailyservice_item, null);
                view.setTag(new MyAdapter.ViewHolder(view));
            }
            set_data_into_layout(i,listbean.get(i),(MyAdapter.ViewHolder)view.getTag());
            return view;
        }

        @SuppressLint("SetTextI18n")
        private void set_data_into_layout(int i, Daily_bean bean,ViewHolder holder) {
            holder.pc_item_id.setText(i+1+"");
            holder.ds_item_starttime.setText("开始时间："+bean.getBegintime());
            holder.ds_item_endtime.setText("结束时间："+bean.getEndtime());
            holder.ds_item_jingli.setText("出动警力："+bean.getForces());
            holder.ds_item_paddr.setText("道路："+bean.getRoad());
            holder.ds_item_licheng.setText("里程/参照物："+bean.getDistance());
            holder.ds_item_qwtype.setText("勤务类型："+bean.getWorktype());
            holder.ds_item_adccwfxw.setText("重点查处违法行为："+bean.getBehavior());
            holder.ds_item_context.setText("简要内容："+bean.getContent());
            holder.ds_item_time.setText("提交时间："+bean.getDate());
        }

        private class ViewHolder {
            public View rootView;
            public TextView pc_item_id;
            public TextView ds_item_starttime;
            public TextView ds_item_endtime;
            public TextView ds_item_jingli;
            public TextView ds_item_paddr;
            public TextView ds_item_licheng;
            public TextView ds_item_qwtype;
            public TextView ds_item_adccwfxw;
            public TextView ds_item_context;
            public TextView ds_item_time;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.pc_item_id = (TextView) rootView.findViewById(R.id.pc_item_id);
                this.ds_item_starttime = (TextView) rootView.findViewById(R.id.ds_item_starttime);
                this.ds_item_endtime = (TextView) rootView.findViewById(R.id.ds_item_endtime);
                this.ds_item_jingli = (TextView) rootView.findViewById(R.id.ds_item_jingli);
                this.ds_item_paddr = (TextView) rootView.findViewById(R.id.ds_item_paddr);
                this.ds_item_licheng = (TextView) rootView.findViewById(R.id.ds_item_licheng);
                this.ds_item_qwtype = (TextView) rootView.findViewById(R.id.ds_item_qwtype);
                this.ds_item_adccwfxw = (TextView) rootView.findViewById(R.id.ds_item_adccwfxw);
                this.ds_item_context = (TextView) rootView.findViewById(R.id.ds_item_context);
                this.ds_item_time = (TextView) rootView.findViewById(R.id.ds_item_time);
            }
        }
    }
    public void getNetData() {
        listbean.clear();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        Map map = new HashMap();
        map.put("userid", sharedPreferences.getString("userid", ""));
        getNetInfo.NetInfoArray(getActivity(), "selectdaily", new JSONObject(map), new getNetInfo.VolleyArrayCallback() {
            @Override
            public void onSuccess(JSONArray object) throws JSONException {
                for (int i = 0; i < object.length(); i++) {
                    JSONObject j = (JSONObject) object.get(i);
                    if (j.getString("modify").equals("未修改")) {
                        listbean.add(new Daily_bean(j.getString("date"),j.getString("detachment"),j.getString("img"),j.getString("distance"),j.getString("endtime"),
                                j.getString("worktype"),j.getString("begintime"),j.getString("userid"),j.getString("content"),j.getString("road"),j.getString("forces"),
                                j.getString("behavior"),j.getString("grop")));
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
}
