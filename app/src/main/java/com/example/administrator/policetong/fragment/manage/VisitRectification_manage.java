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
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.base.BaseBean;
import com.example.administrator.policetong.base.BaseFragment;
import com.example.administrator.policetong.bean.SafetyChecks_bean;
import com.example.administrator.policetong.bean.VisitRectification_bean;
import com.example.administrator.policetong.fragment.Fragment_manage;
import com.example.administrator.policetong.httppost.getNetInfo;
import com.example.administrator.policetong.network.Network;
import com.example.administrator.policetong.new_bean.VisitBean;
import com.example.administrator.policetong.utils.LoadingDialog;
import com.google.gson.Gson;
import com.luck.picture.lib.entity.LocalMedia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class VisitRectification_manage extends BaseFragment {

    List<VisitBean> listbean;
    private ListView pc_manage_listview;
    private TextView nodata;
    private MyAdapter myAdapter;

    public VisitRectification_manage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_visitrectification_manage, container, false);
        view.setClickable(true);
        initValues();
        initView(view);
        return view;
    }

    private void initValues() {
        listbean = new ArrayList<>();
        LoadingDialog.showDialog(getActivity(), "正在获取数据");
        getNetData();
    }

    public void getNetData() {
        listbean.clear();
        Map<String,String> map=new HashMap<>();
        map.put("userid",userInfo.getUserId());
        disposable=Network.getPoliceApi().getVisit(RequestBody.create(MediaType.parse("application/json"),new JSONObject(map).toString()))
                .compose(BaseActivity.<BaseBean<List<VisitBean>>>applySchedulers())
                .subscribe(new Consumer<BaseBean<List<VisitBean>>>() {
                    @Override
                    public void accept(BaseBean<List<VisitBean>> listBaseBean) throws Exception {
                        if (listbean!=null&&listbean.size()>0){
                            listbean.clear();
                        }
                        listbean.addAll(listBaseBean.getData());
                        LoadingDialog.disDialog();
                        shuaxinListview();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getActivity(), "获取数据失败..", Toast.LENGTH_SHORT).show();
                        LoadingDialog.disDialog();
                    }
                });

//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
//        Map map = new HashMap();
//        map.put("userid", sharedPreferences.getString("userid", ""));
//        getNetInfo.NetInfoArray(getActivity(), "selectvisit", new JSONObject(map), new getNetInfo.VolleyArrayCallback() {
//            @Override
//            public void onSuccess(JSONArray object) throws JSONException {
//                for (int i = 0; i < object.length(); i++) {
//                    JSONObject j = (JSONObject) object.get(i);
//                    if (j.getString("modify").equals("未修改")) {
//                        listbean.add(new VisitRectification_bean(j.getString("date"), j.getString("detachment"), j.getString("unit"), j.getString("unitnature"), j.getString("img"),
//                                j.getString("grop"), j.getString("userid"), j.getString("content"), j.getString("objective")));
//                    }
//                }
//                if (listbean.size() == 0) {
//                    nodata.setVisibility(View.VISIBLE);
//                    pc_manage_listview.setVisibility(View.GONE);
//                } else {
//                    Collections.reverse(listbean);
//                    nodata.setVisibility(View.GONE);
//                    pc_manage_listview.setVisibility(View.VISIBLE);
//                }
//                LoadingDialog.disDialog();
//                shuaxinListview();
//            }
//
//            @Override
//            public void onError(VolleyError volleyError) {
//                LoadingDialog.showToast_shibai(getActivity());
//                LoadingDialog.disDialog();
//            }
//        });
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
        View view = View.inflate(getActivity(), R.layout.fragment_visitrectification_add, null);
        Button cancle = view.findViewById(R.id.visit_cancle);
        Button submit = view.findViewById(R.id.visit_submit);
        submit.setText("修改");
        cancle.setVisibility(View.VISIBLE);
        final EditText visit_unitname = view.findViewById(R.id.visit_unitname);
        final EditText visit_context = view.findViewById(R.id.visit_context);
        final EditText visit_unit = view.findViewById(R.id.visit_unit);
        final EditText visit_purpose = view.findViewById(R.id.visit_purpose);
        final EditText visit_time = view.findViewById(R.id.visit_time);
        final LinearLayout llay_content = view.findViewById(R.id.llay_content);
        llay_content.setVisibility(View.INVISIBLE);
        final VisitBean bean = listbean.get(id);
        visit_time.setText(bean.getVisitDate());
        visit_unitname.setText(bean.getUnit());
        visit_context.setText(bean.getContent());
        visit_unit.setText(bean.getUnitNature());
        visit_purpose.setText(bean.getVisitPurpose());
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
                unitname = visit_unitname.getText().toString().trim();
                if (TextUtils.isEmpty(unitname)) {
                    Toast.makeText(getContext(), "走访单位不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                String unittime = visit_time.getText().toString().trim();
                if (TextUtils.isEmpty(unittime)) {
                    Toast.makeText(getContext(), "走访单位不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                context = visit_context.getText().toString().trim();
                if (TextUtils.isEmpty(context)) {
                    Toast.makeText(getContext(), "简要内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                purpose=visit_purpose.getText().toString();
                if (TextUtils.isEmpty(purpose)) {
                    Toast.makeText(getContext(), "简要内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                unit=visit_unit.getText().toString();
                if (TextUtils.isEmpty(unit)) {
                    Toast.makeText(getContext(), "简要内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.dismiss();
                LoadingDialog.showDialog(getActivity(), "正在提交内容！");
                VisitBean visitBean=new VisitBean(id,userInfo.getUserId(),userInfo.getSquId(),unit,unitname,purpose,context,unittime,"2");
                String s = new Gson().toJson(visitBean);
                disposable=Network.getPoliceApi().addVisit(RequestBody.create(MediaType.parse("application/json"),s))
                        .compose(BaseActivity.<BaseBean>applySchedulers())
                        .subscribe(new Consumer<BaseBean>() {
                            @Override
                            public void accept(BaseBean bean) throws Exception {
                                LoadingDialog.disDialog();
                                if (bean.getCode()==0){
                                    Toast.makeText(getActivity(), "修改成功!", Toast.LENGTH_SHORT).show();
                                    initValues();
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(getActivity(), "修改失败!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        dialog.show();
    }

    private void amend_data(final VisitBean bean) {

    }
    String unitname,context,purpose,unit;

    @Override
    public void getPhoto(List<LocalMedia> selectList) {

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
            set_data_into_layout(i,listbean.get(i),(MyAdapter.ViewHolder)view.getTag());
            return view;
        }

        @SuppressLint("SetTextI18n")
        private void set_data_into_layout(int i, VisitBean bean,MyAdapter.ViewHolder holder) {
            holder.pc_item_id.setText(i+1+"");
            holder.pc_item_txt1.setText("走访单位："+bean.getUnit());
            holder.pc_item_txt2.setText("单位性质："+bean.getUnitNature());
            holder.pc_item_txt3.setText("走访目的："+bean.getVisitPurpose());
            holder.pc_item_txt4.setText("简要内容："+bean.getContent());
            holder.pc_item_txt5.setText("走访时间："+bean.getVisitDate());
        }

        private   class ViewHolder {
            public View rootView;
            public TextView pc_item_id;
            public TextView pc_item_txt1;
            public TextView pc_item_txt2;
            public TextView pc_item_txt3;
            public TextView pc_item_txt4;
            public TextView pc_item_txt5;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.pc_item_id =rootView.findViewById(R.id.pc_item_id);
                this.pc_item_txt1 = rootView.findViewById(R.id.pc_item_txt1);
                this.pc_item_txt2 = rootView.findViewById(R.id.pc_item_txt2);
                this.pc_item_txt3 = rootView.findViewById(R.id.pc_item_txt3);
                this.pc_item_txt4 = rootView.findViewById(R.id.pc_item_txt4);
                this.pc_item_txt5 = rootView.findViewById(R.id.pc_item_txt5);
            }

        }
    }
}
