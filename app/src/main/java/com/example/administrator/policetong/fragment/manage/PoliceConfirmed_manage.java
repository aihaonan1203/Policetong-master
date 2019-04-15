package com.example.administrator.policetong.fragment.manage;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.administrator.policetong.MainActivity;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.activity.ModulesActivity;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.base.BaseBean;
import com.example.administrator.policetong.base.BaseFragment;
import com.example.administrator.policetong.bean.EvenMsg;
import com.example.administrator.policetong.bean.PathBean;
import com.example.administrator.policetong.bean.PoliceMent;
import com.example.administrator.policetong.fragment.Fragment_manage;
import com.example.administrator.policetong.fragment.PoliceFragment;
import com.example.administrator.policetong.httppost.getNetInfo;
import com.example.administrator.policetong.network.Network;
import com.example.administrator.policetong.new_bean.JingBaoBean;
import com.example.administrator.policetong.utils.GsonUtil;
import com.example.administrator.policetong.utils.LoadingDialog;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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
public class PoliceConfirmed_manage extends BaseFragment {

    private ListView pc_manage_listview;
    private List<JingBaoBean> listbean;
    private MyAdapter myAdapter;
    private TextView nodata;

    public PoliceConfirmed_manage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_police_confirmed_manage, container, false);
        view.setClickable(true);
        initView(view);
        initValues();
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
//                showDialog(id);
                popupWindow.dismiss();
                Intent intent = new Intent(getActivity(), ModulesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", 11);
                intent.putExtra("data", bundle);
                EventBus.getDefault().postSticky(listbean.get(id));
                startActivity(intent);

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX(String msg) {
        if (msg.equals("1")){
            initValues();
        }
    }


    public void getNetData() {
        Map<String,String> map=new HashMap<>();
        map.put("userid",userInfo.getUserId());
        disposable=Network.getPoliceApi().getGuard(RequestBody.create(MediaType.parse("application/json"),new JSONObject(map).toString()))
                .compose(BaseActivity.<BaseBean<List<JingBaoBean>>>applySchedulers())
                .subscribe(new Consumer<BaseBean<List<JingBaoBean>>>() {
                    @Override
                    public void accept(BaseBean<List<JingBaoBean>> bean) throws Exception {
                        if (bean.getCode()==0){
                            listbean=bean.getData();
                            if (bean.getData().size()==0){
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
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
//        listbean.clear();
//        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
//        Map map=new HashMap();
//        map.put("userid",sharedPreferences.getString("userid",""));
//        getNetInfo.NetInfoArray(getActivity(), "selectatdservlet", new JSONObject(map), new getNetInfo.VolleyArrayCallback() {
//            @Override
//            public void onSuccess(JSONArray object) throws JSONException {
//                for (int i = 0; i < object.length(); i++) {
//                    JSONObject j= (JSONObject) object.get(i);
//                    if (j.getString("modify").equals("未修改")){
//                        listbean.add(new PoliceConfirmed_bean(j.getString("userid"),j.getString("place"),j.getString("unit")
//                        ,j.getString("turn"),j.getString("date"),j.getString("other"),j.getString("latitude"),j.getString("longitude")));
//                    }
//                }
//                if (listbean.size()==0){
//                    nodata.setVisibility(View.VISIBLE);
//                    pc_manage_listview.setVisibility(View.GONE);
//                }else {
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
//                LoadingDialog.disDialog();
//                LoadingDialog.showToast_shibai(getActivity());
//            }
//        });
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
                view = View.inflate(getActivity(), R.layout.policeconfirmed_item, null);
                view.setTag(new ViewHolder(view));
            }
            Set_data_into_layout(i,listbean.get(i),(ViewHolder)view.getTag());
            return view;
        }

        @SuppressLint("SetTextI18n")
        private void Set_data_into_layout(int id, JingBaoBean bean, ViewHolder holder) {
            holder.pc_item_id.setText(id+1+"");
            holder.pc_item_date.setText("时间："+bean.getTaskDate()+" "+bean.getDutyTime());
            holder.pc_item_other.setText("备注："+bean.getOther());
            holder.pc_title.setText(bean.getTitle());
            holder.pc_luxian.setText("路线:"+bean.getRoadMap());
            holder.pc_miaoshu.setText("描述："+bean.getDescription());
            holder.pc_lingdao.setText("领导："+bean.getTaskLeader());
            List<PoliceMent> list = GsonUtil.parseJsonArrayWithGson(bean.getPoliceMent(), PoliceMent.class);
            StringBuilder string=new StringBuilder();
            string.append("\n");
            for (int i = 0; i < list.size(); i++) {
                string.append(list.get(i).getPlace()).append(":").append(list.get(i).getSquName()).append(list.get(i).getNumer()).append(list.get(i).getDiaodeng());
                if (i!=list.size()-1){
                    string.append("\n");
                }
            }
            holder.pc_jingli.setText("警力部署："+string.toString());
        }

        private class ViewHolder {
            public TextView pc_item_id;
            public TextView pc_title;
            public TextView pc_miaoshu;
            public TextView pc_item_date;
            public TextView pc_item_other;
            public TextView pc_luxian;
            public TextView pc_jingli;
            public TextView pc_lingdao;

            public ViewHolder(View rootView) {
                this.pc_item_id = (TextView) rootView.findViewById(R.id.pc_item_id);
                this.pc_title = (TextView) rootView.findViewById(R.id.pc_title);
                this.pc_miaoshu = (TextView) rootView.findViewById(R.id.pc_miaoshu);
                this.pc_lingdao = (TextView) rootView.findViewById(R.id.pc_lingdao);
                this.pc_luxian = (TextView) rootView.findViewById(R.id.pc_luxian);
                this.pc_jingli = (TextView) rootView.findViewById(R.id.pc_jingli);
                this.pc_item_date = (TextView) rootView.findViewById(R.id.pc_item_date);
                this.pc_item_other = (TextView) rootView.findViewById(R.id.pc_item_other);
            }

        }
    }
}
