package com.example.administrator.policetong.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.activity.ModulesActivity;
import com.example.administrator.policetong.activity.SelectPathActivity;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.base.BaseBean;
import com.example.administrator.policetong.base.BaseFragment;
import com.example.administrator.policetong.bean.EvenMsg;
import com.example.administrator.policetong.bean.PathBean;
import com.example.administrator.policetong.bean.PoliceMent;
import com.example.administrator.policetong.network.Network;
import com.example.administrator.policetong.new_bean.JingBaoBean;
import com.example.administrator.policetong.new_bean.ZDBean;
import com.example.administrator.policetong.utils.GsonUtil;
import com.example.administrator.policetong.utils.LoadingDialog;
import com.example.administrator.policetong.utils.Util;
import com.google.gson.Gson;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class PoliceFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private Button btnPath;

    private EditText pcAddTime;
    private EditText jb_lingdao;
    private EditText et_miaoshu;
    private Button pcTime;
    private Button btnJingli;
    private RecyclerView rvRecyclerView2;
    private TextView tv_title;
    private String[] array1;
    private String[] array2;
    private List<PoliceMent> list = new ArrayList<>();
    private MyAdapter myAdapter;
    private EditText etBeizhu;
    private Button btnSubmit;
    private List<PathBean> data=new ArrayList<>();
    private JingBaoBean jingBaoBean;
    private ModulesActivity activity;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activity = (ModulesActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_police_layout, container, false);
        view.setClickable(true);
        initView(view);
        Util.RequestOption(getActivity(), "option9", new Util.RequestOptionCallBack() {
            @Override
            public void CallBack(List<String> list) {
                array1 = list.toArray(new String[0]);
            }
        });
        getSqu();
        assert activity != null;
        if (jingBaoBean!=null&& activity.id==11){
            tv_title.setText(jingBaoBean.getTitle());
            pcAddTime.setText(jingBaoBean.getTaskDate()+" "+jingBaoBean.getDutyTime());
            jb_lingdao.setText(jingBaoBean.getTaskLeader());
            etBeizhu.setText(jingBaoBean.getOther());
            et_miaoshu.setText(jingBaoBean.getDescription());
            String roadMap = jingBaoBean.getRoadMap();
            String[] split = roadMap.split("—");
            List<PathBean> list1=new ArrayList<>();
            for (int i = 0; i < split.length; i++) {
                list1.add(new PathBean(split[i],""));
            }
            data.addAll(list1);
            recyclerView.setAdapter(new RevyZltjAdapter(getActivity(), data));
            String policeMent = jingBaoBean.getPoliceMent();
            list.addAll(GsonUtil.parseJsonArrayWithGson(policeMent, PoliceMent.class));
            myAdapter.notifyDataSetChanged();
        }
        return view;
    }



    private void getSqu() {
        disposable = Network.getPoliceApi(false).getSqu()
                .compose(BaseActivity.<BaseBean<List<ZDBean>>>applySchedulers())
                .subscribe(new Consumer<BaseBean<List<ZDBean>>>() {
                    @Override
                    public void accept(BaseBean<List<ZDBean>> bean) throws Exception {
                        if (bean.getCode() != 0) {
                            Toast.makeText(getActivity(), "获取中队信息失败", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ArrayList<String> zDdata = new ArrayList<>();
                        for (int i = 0; i < bean.getData().size(); i++) {
                            zDdata.add(bean.getData().get(i).getSquName());
                        }
                        array2 = zDdata.toArray(new String[0]);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    private void showDialog() {
        if (array1 == null) {
            array1 = new String[]{};
        }
        if (array2 == null) {
            array2 = new String[]{};
        }
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.jingli_layout, null, false);
        final EditText dialog_pq = view1.findViewById(R.id.dialog_pq);
        final EditText dialog_rs = view1.findViewById(R.id.dialog_rs);
        final EditText dialog_gd = view1.findViewById(R.id.pc_add_time);
        Button dialog_btn = view1.findViewById(R.id.pc_time);
        Button btn_pq = view1.findViewById(R.id.btn_pq);
        Button dialog_yes = view1.findViewById(R.id.dialog_ok);
        Button dialog_no = view1.findViewById(R.id.dialog_no);
        final RadioButton rb_yes = view1.findViewById(R.id.rb_yes);
        Util.setRadioDateIntoDialog(getActivity(), dialog_gd, dialog_btn, array1);
        Util.setRadioDateIntoDialog(getActivity(), dialog_pq, btn_pq, array2);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).setView(view1).create();
        dialog.show();
        dialog_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String renshu = dialog_rs.getText().toString();
                String paiqian = dialog_pq.getText().toString();
                String gandian = dialog_gd.getText().toString();
                if (!renshu.isEmpty() && !paiqian.isEmpty() && !gandian.isEmpty()) {
                    dialog.dismiss();
                    if (rb_yes.isChecked()) {
                        list.add(new PoliceMent(list.size() + 1, gandian, paiqian, 1, renshu + "人", "(调灯)"));
                    } else {
                        list.add(new PoliceMent(list.size() + 1, gandian, paiqian, 1, renshu + "人", ""));
                    }
                    myAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    @Override
    public void getPhoto(List<LocalMedia> selectList) {
    }

    @SuppressLint("SetTextI18n")
    private void initView(View view) {
        btnPath = view.findViewById(R.id.btn_path);
        recyclerView = view.findViewById(R.id.rv_RecyclerView);
        jb_lingdao = view.findViewById(R.id.jb_lingdao);
        et_miaoshu = view.findViewById(R.id.et_miaoshu);
        pcAddTime = view.findViewById(R.id.pc_add_time);
        pcTime = view.findViewById(R.id.pc_time);
        tv_title = view.findViewById(R.id.tv_title);
        btnJingli = view.findViewById(R.id.btn_jingli);
        rvRecyclerView2 = view.findViewById(R.id.rv_RecyclerView2);
        rvRecyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        myAdapter = new MyAdapter(getActivity(), list);
        rvRecyclerView2.setAdapter(myAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        tv_title.setText("第二大队" + LoadingDialog.getTime4() + "安全保卫工作方案");
        btnPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SelectPathActivity.class));
            }
        });
        btnJingli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        pcTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickDialog dialog = new DatePickDialog(getActivity());
                //设置上下年分限制
                dialog.setYearLimt(5);
                //设置标题
                dialog.setTitle("选择时间");
                //设置类型
                dialog.setType(DateType.TYPE_ALL);
                //设置消息体的显示格式，日期格式
                dialog.setMessageFormat("yyyy-MM-dd HH:mm");
                //设置点击确定按钮回调
                dialog.setOnSureLisener(new OnSureLisener() {
                    @Override
                    public void onSure(Date date) {
                        @SuppressLint("SimpleDateFormat") String string = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                        pcAddTime.setText(string);
                    }
                });
                dialog.show();
            }
        });
        etBeizhu = view.findViewById(R.id.et_beizhu);
        btnSubmit = view.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }

    private void submit() {
        String lingdao = jb_lingdao.getText().toString().trim();
        String time = pcAddTime.getText().toString().trim();
        String beizhu = etBeizhu.getText().toString().trim();
        String miaoshu = et_miaoshu.getText().toString().trim();
        if (miaoshu.isEmpty()||lingdao.isEmpty()||time.isEmpty()||beizhu.isEmpty()||list.size()==0||data==null||data.size()==0){
            Toast.makeText(getActivity(), "请把信息补充完整！", Toast.LENGTH_SHORT).show();
            return;
        }
        StringBuffer string=new StringBuffer();
        for (int i = 0; i < data.size(); i++) {
            if (i==data.size()-1){
                string.append(data.get(i).getName());
            }else {
                string.append(data.get(i).getName()).append("—");
            }
        }
        String[] split = time.split(" ");
        Gson gson=new Gson();
        String json = gson.toJson(list);
        JingBaoBean bean=new JingBaoBean();
        bean.setUserId(userInfo.getUserId());
        bean.setDescription(miaoshu);
        bean.setTaskDate(split[0]);
        bean.setDutyTime(split[1]);
        bean.setTaskLeader(lingdao);
        bean.setOther(beizhu);
        bean.setRoadMap(string.toString());
        bean.setPoliceMent(json);
        if (activity.id==11){
            bean.setId(jingBaoBean.getId());
        }
        bean.setTitle(tv_title.getText().toString().trim());
        String s = gson.toJson(bean);
        LoadingDialog.showDialog(getActivity(),"正在提交...");
        disposable= Network.getPoliceApi(false).addGuard(RequestBody.create(MediaType.parse("application/json"),s))
                .compose(BaseActivity.<BaseBean>applySchedulers())
                .subscribe(new Consumer<BaseBean>() {
                    @Override
                    public void accept(BaseBean bean) throws Exception {
                        LoadingDialog.disDialog();
                        if (bean.getCode()==0){
                            Toast.makeText(getActivity(), "提交成功！", Toast.LENGTH_SHORT).show();
                            Objects.requireNonNull(getActivity()).finish();
                            if (activity.id==11){
                                EventBus.getDefault().post("1");
                            }
                        }else {
                            Toast.makeText(getActivity(), "提交失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LoadingDialog.disDialog();
                        Toast.makeText(getActivity(), "提交失败！", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX(EvenMsg<List<PathBean>> messageEvent) {
        data = messageEvent.getData();
        recyclerView.setAdapter(new RevyZltjAdapter(getActivity(), data));
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void XX(JingBaoBean bean) {
        jingBaoBean = bean;
    }

    private class RevyZltjAdapter extends RecyclerView.Adapter<RevyZltjAdapter.ViewHolder> {

        private Context mContext;
        private List<PathBean> data;

        public RevyZltjAdapter(Context mContext, List<PathBean> data) {
            this.mContext = mContext;
            setData(data);
        }

        public void setData(List<PathBean> data) {
            if (data == null) {
                data = new ArrayList<>();
            }
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.yllistview_layout_item, parent, false);
            ViewHolder hol = new ViewHolder(view);
            return hol;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.tv_name.setText(data.get(position).getName());
            if (position == data.size() - 1) {
                holder.tv_xian.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private TextView tv_name;
            private TextView tv_xian;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_name = itemView.findViewById(R.id.tv_name);
                tv_xian = itemView.findViewById(R.id.tv_xian);
            }
        }
    }


    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private Context mContext;
        private List<PoliceMent> ments;

        public MyAdapter(Context mContext, List<PoliceMent> ments) {
            this.mContext = mContext;
            this.ments = ments;
        }

        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.yllistview_layout_item, parent, false);
            ViewHolder hol = new ViewHolder(view);
            return hol;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            PoliceMent bean = ments.get(position);
            holder.tv_name.setText(bean.getPlace() + ":" + bean.getSquName() + bean.getNumer() + bean.getDiaodeng());
            holder.tv_xian.setVisibility(View.GONE);
        }

        @Override
        public int getItemCount() {
            return ments.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private TextView tv_name;
            private TextView tv_xian;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_name = itemView.findViewById(R.id.tv_name);
                tv_xian = itemView.findViewById(R.id.tv_xian);
            }
        }
    }
}
