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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.base.BaseBean;
import com.example.administrator.policetong.base.BaseFragment;
import com.example.administrator.policetong.bean.Study_bean;
import com.example.administrator.policetong.bean.VisitRectification_bean;
import com.example.administrator.policetong.fragment.Fragment_manage;
import com.example.administrator.policetong.httppost.getNetInfo;
import com.example.administrator.policetong.network.Network;
import com.example.administrator.policetong.new_bean.StudyBean;
import com.example.administrator.policetong.new_bean.ZDBean;
import com.example.administrator.policetong.utils.LoadingDialog;
import com.example.administrator.policetong.utils.NetworkChangeListener;
import com.google.gson.Gson;
import com.luck.picture.lib.entity.LocalMedia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class Study_manage extends BaseFragment {

    List<StudyBean> listbean;
    private ListView pc_manage_listview;
    private TextView nodata;
    private MyAdapter myAdapter;
    private HashMap<Object, Object> map;

    public Study_manage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_study_manage, container, false);
        initView(view);
        getSqu();
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

    private void getSqu() {
        disposable = Network.getPoliceApi().getSqu()
                .compose(BaseActivity.<BaseBean<List<ZDBean>>>applySchedulers())
                .subscribe(new Consumer<BaseBean<List<ZDBean>>>() {
                    @SuppressLint("UseSparseArrays")
                    @Override
                    public void accept(BaseBean<List<ZDBean>> bean) throws Exception {
                        if (bean.getCode() != 0) {
                            Toast.makeText(getActivity(), "获取中队信息失败", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        List<ZDBean> data = bean.getData();
                        map = new HashMap<>();
                        for (int i = 0; i <data.size() ; i++) {
                            ZDBean zdBean = data.get(i);
                            map.put(zdBean.getId(),zdBean.getSquName());
                        }
                        initValues();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void showDialog(final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.fragment_studdy, null);
        Button cancle = view.findViewById(R.id.stu_cancle);
        Button submit = view.findViewById(R.id.stu_submit);
        submit.setText("修改");
        cancle.setVisibility(View.VISIBLE);
        final EditText stu_study_time = view.findViewById(R.id.stu_study_time);
        final EditText stu_zd = view.findViewById(R.id.stu_zd);
        final EditText stu_context = view.findViewById(R.id.stu_context);
        final EditText stu_study_site = view.findViewById(R.id.stu_study_site);
        final LinearLayout llyt_photo = view.findViewById(R.id.llyt_photo);
        llyt_photo.setVisibility(View.GONE);
        final StudyBean bean = listbean.get(id);
        stu_study_time.setText(bean.getStudyTime());
        Object o = map.get(bean.getSquId());
        if (o!=null){
            stu_zd.setText("所属中队:"+o.toString());
        }else
        {
            stu_zd.setText(String.valueOf(bean.getSquId()));
        }

        stu_context.setText(bean.getContent());
        stu_study_site.setText(bean.getPlace());
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
                    Toast.makeText(getContext(), "学习时间不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                String zd = stu_zd.getText().toString().trim();
                if (TextUtils.isEmpty(zd)) {
                    Toast.makeText(getContext(), "所属中队不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                String site = stu_study_site.getText().toString().trim();
                if (TextUtils.isEmpty(zd)) {
                    Toast.makeText(getContext(), "学习地点不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                String context = stu_context.getText().toString().trim();
                if (TextUtils.isEmpty(context)) {
                    Toast.makeText(getContext(), "学习内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                LoadingDialog.showDialog(getActivity(), "正在提交...");
                StudyBean bean1=new StudyBean(bean.getId(),userInfo.getUserId(),context,site,name,userInfo.getSquId(),"2");
                String s = new Gson().toJson(bean1);
                disposable=Network.getPoliceApi().addStudy(RequestBody.create(MediaType.parse("application/json"),s))
                        .compose(BaseActivity.<BaseBean>applySchedulers()).subscribe(new Consumer<BaseBean>() {
                            @Override
                            public void accept(BaseBean bean) throws Exception {
                                if (bean.getCode()==0){
                                    LoadingDialog.disDialog();
                                    Toast.makeText(getActivity(), "提交成功...", Toast.LENGTH_SHORT).show();
                                    shuaxinListview();
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                LoadingDialog.disDialog();
                                Toast.makeText(getActivity(), "提交失败...", Toast.LENGTH_SHORT).show();
                                Log.e("accept: ","" );
                            }
                        });
            }
        });
        dialog.show();
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
        Map map = new HashMap();
        map.put("userId",userInfo.getUserId());
        disposable=Network.getPoliceApi().getStudy(RequestBody.create(MediaType.parse("application/json"),new JSONObject(map).toString()))
                .compose(BaseActivity.<BaseBean<List<StudyBean>>>applySchedulers())
                .subscribe(new Consumer<BaseBean<List<StudyBean>>>() {
                    @Override
                    public void accept(BaseBean<List<StudyBean>> listBaseBean) throws Exception {
                        LoadingDialog.disDialog();
                        if (listBaseBean.getCode()==0){
                            if (listbean!=null) {
                                listbean.clear();
                            }
                            assert listbean != null;
                            listbean.addAll(listBaseBean.getData());
                            shuaxinListview();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

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
            set_data_into_layout(i, listbean.get(i), (ViewHolder) view.getTag());
            return view;
        }

        @SuppressLint("SetTextI18n")
        private void set_data_into_layout(int i, StudyBean bean, MyAdapter.ViewHolder holder) {
            holder.pc_item_id.setText(i + 1 + "");
            holder.pc_item_txt1.setText("学习时间："+bean.getStudyTime());
            holder.pc_item_txt2.setText("记录地点："+bean.getPlace());
            Object o = map.get(bean.getSquId());
            if (o!=null){
                holder.pc_item_txt3.setText("所属中队："+o.toString());
            }else
            {
                holder.pc_item_txt3.setText("所属中队："+bean.getSquId());
            }
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
