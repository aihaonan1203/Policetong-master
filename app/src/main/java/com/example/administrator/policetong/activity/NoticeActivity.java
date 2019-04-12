package com.example.administrator.policetong.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.policetong.MainActivity;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.bean.NoticeBean;
import com.github.nukc.stateview.StateView;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;


public class NoticeActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView acArrowBack;
    private ListView listView;
    private List<NoticeBean.MsgArrayBean> list;
    private StateView mStateView;
    private LinearLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_notice);
        initView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getMsg(List<NoticeBean.MsgArrayBean> list) {
        this.list = list;
    }

    private void initView() {
        frameLayout = findViewById(R.id.f_content);
        acArrowBack = findViewById(R.id.ac_arrow_back);
        acArrowBack.setOnClickListener(this);
        initmStateView(frameLayout);
        mStateView.showEmpty();
        if (list == null) {
            return;
        }
        if (list.size() == 0) {
            mStateView.showEmpty();
        } else {
            mStateView.showContent();
            listView = findViewById(R.id.lv_content);
            listView.setAdapter(new ListViewAdapter());
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    showDialog(list.get(i).getMsg(),list.get(i).getDate());
                }
            });
        }

    }

    private void showDialog(String msg,String time){
        new LovelyStandardDialog(NoticeActivity.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                .setTopColorRes(R.color.ic_launcher_background)
                .setButtonsColorRes(R.color.ic_launcher_background)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getString(R.string.app_name)+"-公告")
                .setMessage(msg+"\n\n"+time)
                .setPositiveButton(android.R.string.ok,null)
                .setPositiveButton("退出",null)
                .show();
    }

    @Override
    public void onClick(View view) {
        finish();
    }

    public void initmStateView(View view) {
        mStateView = StateView.inject(view);
        mStateView.setEmptyResource(R.layout.empty_layout);
    }

    private class ListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder=null;
            if (view==null){
                view = LayoutInflater.from(NoticeActivity.this).inflate(R.layout.notice_layout_item, viewGroup, false);
                holder=new ViewHolder(view);
                view.setTag(holder);
            }else {
                holder= (ViewHolder) view.getTag();
            }
            holder.tv_msg.setText(list.get(i).getMsg());
            holder.tv_time.setText(list.get(i).getDate());
            return view;
        }

        private class ViewHolder {
            public View rootView;
            public TextView tv_msg;
            public TextView tv_time;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.tv_msg = (TextView) rootView.findViewById(R.id.tv_msg);
                this.tv_time = (TextView) rootView.findViewById(R.id.tv_time);
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
