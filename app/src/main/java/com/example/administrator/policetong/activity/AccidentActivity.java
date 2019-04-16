package com.example.administrator.policetong.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.policetong.R;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.base.BaseBean;
import com.example.administrator.policetong.network.Network;
import com.example.administrator.policetong.new_bean.AccidentBean;
import com.example.administrator.policetong.new_bean.CarBean;
import com.example.administrator.policetong.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AccidentActivity extends BaseActivity {


    private ImageView acArrowBack;
    private ListView lvTcc;
    private List<AccidentBean> data=new ArrayList<>();
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident);
        initView();
        initAdapter();
        getNetData();
    }

    private void getNetData() {
        Map<String,String> map=new HashMap<>();
        map.put("userId",userInfo.getUserId());
        disposable = Network.getPoliceApi().getAccident(RequestBody.create(MediaType.parse("application/json"),new JSONObject(map).toString()))
                .compose(BaseActivity.<BaseBean<List<AccidentBean>>>applySchedulers())
                .subscribe(new Consumer<BaseBean<List<AccidentBean>>>() {
                    @Override
                    public void accept(BaseBean<List<AccidentBean>> listBaseBean) throws Exception {
                        if (data!=null){
                            data.clear();
                        }
                        assert data != null;
                        data.addAll(listBaseBean.getData());
                        initAdapter();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("accept: ","" );
                    }
                });


    }

    private void initAdapter() {
        if (adapter==null){
            adapter = new MyAdapter();
            lvTcc.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }
    }

    private void initView() {
        acArrowBack = findViewById(R.id.ac_arrow_back);
        lvTcc = findViewById(R.id.lv_tcc);
        acArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        lvTcc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showPopueWindow(i);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX(String msg) {
        if (msg.equals("1")){
            getNetData();
        }
    }


    @SuppressLint("SetTextI18n")
    private void showPopueWindow(final int id) {
        View popView = View.inflate(this, R.layout.buttonpopwind, null);
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
//                showDialog(id);
                popupWindow.dismiss();
                EventBus.getDefault().postSticky(data.get(id));
                Intent intent = new Intent(AccidentActivity.this, ModulesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", 12);
                intent.putExtra("data", bundle);
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
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        //popupWindow出现屏幕变为半透明
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 0);
    }


    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(AccidentActivity.this, R.layout.car_park_item, null);
                view.setTag(new MyAdapter.ViewHolder(view));
            }
            Set_data_into_layout(i, data.get(i), (MyAdapter.ViewHolder) view.getTag());
            return view;
        }

        @SuppressLint("SetTextI18n")
        private void Set_data_into_layout(int id, AccidentBean bean, MyAdapter.ViewHolder holder) {
            holder.pc_item_id.setText(id + 1 + "");
            holder.pc_item_other.setText("受伤情况:"+bean.getHurtType());
            holder.pc_item_other.setText("车损:"+bean.getCarAmage());
            holder.tv_in_time.setText("时间:"+bean.getDate());
            holder.tv_model.setText("车辆类型:"+bean.getCarType());
            holder.tv_carNumber.setText("参与方:"+bean.getParticipant());
            holder.tv_type.setText("类型:"+bean.getType());
        }


        private class ViewHolder {
            public View rootView;
            public TextView pc_item_id;
            public TextView tv_carNumber;
            public TextView tv_type;
            public TextView tv_model;
            public TextView pc_item_other;
            public TextView tv_in_time;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.pc_item_id = (TextView) rootView.findViewById(R.id.pc_item_id);
                this.tv_carNumber = (TextView) rootView.findViewById(R.id.tv_carNumber);
                this.tv_type = (TextView) rootView.findViewById(R.id.tv_type);
                this.tv_model = (TextView) rootView.findViewById(R.id.tv_model);
                this.pc_item_other = (TextView) rootView.findViewById(R.id.pc_item_other);
                this.tv_in_time = (TextView) rootView.findViewById(R.id.tv_in_time);
            }

        }
    }
}
