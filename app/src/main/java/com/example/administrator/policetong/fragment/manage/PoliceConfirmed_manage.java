package com.example.administrator.policetong.fragment.manage;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.bean.PoliceConfirmed_bean;
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
public class PoliceConfirmed_manage extends Fragment {

    private ListView pc_manage_listview;
    private List<PoliceConfirmed_bean> listbean;
    MyAdapter myAdapter;
    TextView nodata;

    public PoliceConfirmed_manage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_police_confirmed_manage, container, false);
        view.setClickable(true);
        initValues();
        initView(view);
        return view;
    }

    private void initValues() {
        LoadingDialog.showDialog(getActivity(),"正在请求数据");
        listbean = new ArrayList<>();
        getNetData();
    }

    public void shuaxinListview(){
        if (myAdapter==null){
            myAdapter=new MyAdapter();
            pc_manage_listview.setAdapter(myAdapter);
        }else {
            myAdapter.notifyDataSetChanged();
        }
    }

    private void initView(View view) {
        nodata=view.findViewById(R.id.nodata);
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
    private void showPopueWindow(final int id){
        View popView = View.inflate(getActivity(),R.layout.buttonpopwind,null);
        Button bt_camera =popView.findViewById(R.id.btn_pop_camera);
        Button bt_cancle =popView.findViewById(R.id.btn_pop_cancel);
        ((TextView)popView.findViewById(R.id.number)).setText(id+1+"");
        final PopupWindow popupWindow = new PopupWindow(popView,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        //点击外部popueWindow消失
        popupWindow.setAnimationStyle(R.style.anim_menu_bottombar);
        popupWindow.setOutsideTouchable(true);
        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(id);
                popupWindow.dismiss();

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
        popupWindow.showAtLocation(popView, Gravity.BOTTOM,0,0);

    }

    private void showDialog(final int id) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        View view=View.inflate(getActivity(),R.layout.fragment_police_confirmed_add,null);
        Button cancle=view.findViewById(R.id.pc_add_cancle);
        Button submit=view.findViewById(R.id.pc_add_submit);
        submit.setText("修改");
        cancle.setVisibility(View.VISIBLE);
        final EditText pc_add_time=view.findViewById(R.id.pc_add_time);
        final EditText pc_add_paddr=view.findViewById(R.id.pc_add_paddr);
        final EditText pc_add_unit=view.findViewById(R.id.pc_add_unit);
        final EditText pc_add_remark=view.findViewById(R.id.pc_add_remark);
        pc_add_paddr.setText(listbean.get(id).getPlace());
        pc_add_time.setText(listbean.get(id).getDate());
        pc_add_unit.setText(listbean.get(id).getUnit());
        pc_add_remark.setText(listbean.get(id).getOther());
        final RadioButton rb1=view.findViewById(R.id.pc_r1);
        RadioButton rb2=view.findViewById(R.id.pc_r2);
        if (listbean.get(id).getTurn().equals("是")){
            rb1.setChecked(true);
        }else {
            rb2.setChecked(true);
        }
        builder.setView(view);
        final AlertDialog dialog=builder.create();
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time = pc_add_time.getText().toString().trim();
                if (TextUtils.isEmpty(time)) {
                    Toast.makeText(getContext(), "到岗时间不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                String paddr = pc_add_paddr.getText().toString().trim();
                if (TextUtils.isEmpty(paddr)) {
                    Toast.makeText(getContext(), "岗点不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                String unit = pc_add_unit.getText().toString().trim();
                if (TextUtils.isEmpty(unit)) {
                    Toast.makeText(getContext(), "勤务单位不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                String remark = pc_add_remark.getText().toString().trim();
                if (remark.equals("")){
                    remark="无";
                }
                String tiaodeng;
                if (rb1.isChecked()){
                    tiaodeng="是";
                }else {
                    tiaodeng="否";
                }
                dialog.dismiss();
                LoadingDialog.showDialog(getActivity(),"正在提交内容！");
                amend_data(time,listbean.get(0).getUserid(),id,time,paddr,unit,tiaodeng,remark);
            }
        });
        dialog.show();
    }

    private void submitData(final int id, String time, String paddr, String unit, String tiaodeng, String remark) {
        Map info=new HashMap();
        SharedPreferences sp=getActivity().getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        info.put("userid",sp.getString("userid",""));
        info.put("place",paddr);
        info.put("longitude",listbean.get(id).getLongitude());
        info.put("latitude",listbean.get(id).getLatitude());
        info.put("unit",unit);
        info.put("turn",tiaodeng);
        info.put("date",time);
        info.put("other",remark);
        info.put("group",sp.getString("group",""));
        info.put("detachment",sp.getString("detachment",""));
        getNetInfo.NetInfo(getActivity(), "atdservlet", new JSONObject(info), new getNetInfo.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject object) throws JSONException {
                if (object.getString("RESULT").equals("S")){
                    LoadingDialog.disDialog();
                    Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
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

    private void amend_data(String date, String userid, final int id, final String time, final String paddr, final String unit, final String tiaodeng, final String remark) {
        Map info=new HashMap();
        info.put("userid",userid);
        info.put("date",date);
        getNetInfo.NetInfo(getActivity(), "updateatdservlet", new JSONObject(info), new getNetInfo.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject object) throws JSONException {
                if (object.getString("RESULT").equals("S")){
                    submitData(id,time,paddr,unit,tiaodeng,remark);
                }else {
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
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        Map map=new HashMap();
        map.put("userid",sharedPreferences.getString("userid",""));
        getNetInfo.NetInfoArray(getActivity(), "selectatdservlet", new JSONObject(map), new getNetInfo.VolleyArrayCallback() {
            @Override
            public void onSuccess(JSONArray object) throws JSONException {
                for (int i = 0; i < object.length(); i++) {
                    JSONObject j= (JSONObject) object.get(i);
                    if (j.getString("modify").equals("未修改")){
                        listbean.add(new PoliceConfirmed_bean(j.getString("userid"),j.getString("place"),j.getString("unit")
                        ,j.getString("turn"),j.getString("date"),j.getString("other"),j.getString("latitude"),j.getString("longitude")));
                    }
                }
                if (listbean.size()==0){
                    nodata.setVisibility(View.VISIBLE);
                    pc_manage_listview.setVisibility(View.GONE);
                }else {
                    Collections.reverse(listbean);
                    nodata.setVisibility(View.GONE);
                    pc_manage_listview.setVisibility(View.VISIBLE);
                }
                LoadingDialog.disDialog();
                shuaxinListview();
            }

            @Override
            public void onError(VolleyError volleyError) {
                LoadingDialog.disDialog();
                LoadingDialog.showToast_shibai(getActivity());
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
                view = View.inflate(getActivity(), R.layout.policeconfirmed_item, null);
                view.setTag(new ViewHolder(view));
            }
            Set_data_into_layout(i,listbean.get(i),(ViewHolder)view.getTag());
            return view;
        }

        @SuppressLint("SetTextI18n")
        private void Set_data_into_layout(int id, PoliceConfirmed_bean bean, ViewHolder holder) {
            holder.pc_item_id.setText(id+1+"");
            holder.pc_item_date.setText("时间："+bean.getDate());
            holder.pc_item_other.setText("备注："+bean.getOther());
            holder.pc_item_unit.setText("单位："+bean.getUnit());
            holder.pc_item_turn.setText("是否调灯："+bean.getTurn());
            holder.pc_item_paddr.setText("岗点："+bean.getPlace());
        }

        private class ViewHolder {
            public TextView pc_item_id;
            public TextView pc_item_unit;
            public TextView pc_item_paddr;
            public TextView pc_item_turn;
            public TextView pc_item_date;
            public TextView pc_item_other;

            public ViewHolder(View rootView) {
                this.pc_item_id = (TextView) rootView.findViewById(R.id.pc_item_id);
                this.pc_item_unit = (TextView) rootView.findViewById(R.id.pc_item_unit);
                this.pc_item_paddr = (TextView) rootView.findViewById(R.id.pc_item_paddr);
                this.pc_item_turn = (TextView) rootView.findViewById(R.id.pc_item_turn);
                this.pc_item_date = (TextView) rootView.findViewById(R.id.pc_item_date);
                this.pc_item_other = (TextView) rootView.findViewById(R.id.pc_item_other);
            }

        }
    }
}
