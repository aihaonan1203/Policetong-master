package com.example.administrator.policetong.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Keep;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;


import com.example.administrator.policetong.R;
import com.example.administrator.policetong.bean.new_bean.UserInfo;
import com.example.administrator.policetong.new_bean.UserBean;
import com.example.administrator.policetong.utils.SPUtils;
import com.example.administrator.policetong.utils.Utils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusException;

import java.util.List;

import javax.xml.transform.Transformer;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.luck.picture.lib.config.PictureConfig.CHOOSE_REQUEST;


/**
 * <p>Activity基类 </p>
 *
 * @author 2016/12/2 17:33
 * @version V1.0.0
 * @name BaseActivity
 */
@Keep
public abstract class BaseActivity extends AppCompatActivity {

    protected UserInfo userInfo;
    private List<LocalMedia> selectList;

    /**
     * 封装的findViewByID方法
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T $(@IdRes int id) {
        return (T) super.findViewById(id);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo=App.userInfo;
        try {
            EventBus.getDefault().register(this);
        }catch (EventBusException e){
            Log.e("onCreate: ","没有绑定event事件" );
        }
        ViewManager.getInstance().addActivity(this);
    }

    protected Disposable disposable;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        if (disposable!=null&&disposable.isDisposed()){
            disposable.dispose();
        }
        ViewManager.getInstance().finishActivity(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    /**
     * Setup the toolbar.
     *
     * @param toolbar   toolbar
     * @param hideTitle 是否隐藏Title
     */
    protected void setupToolBar(Toolbar toolbar, boolean hideTitle) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
            actionBar.setDisplayShowHomeEnabled(true);
            if (hideTitle) {
                //隐藏Title
                actionBar.setDisplayShowTitleEnabled(false);
            }
        }
    }


    /**
     * 添加fragment
     *
     * @param fragment
     * @param frameId
     */
    protected void addFragment(BaseFragment fragment, @IdRes int frameId) {
        Utils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .add(frameId, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();

    }

    protected void takePhoto(){
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(9)
                .isCamera(true)
                .compress(true)// 是否压缩
                .selectionMedia(selectList)
                .forResult(CHOOSE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    if (photo!=null){
                        photo.getPhoto(selectList);
                    }
                    break;
            }
        }
    }

    private PhotoCallBack photo;
    public interface PhotoCallBack{
        void getPhoto(List<LocalMedia> selectList);
    }

    public PhotoCallBack getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoCallBack photo) {
        this.photo = photo;
    }

    /**
     * 替换fragment
     * @param fragment
     * @param frameId
     */
    protected void replaceFragment(BaseFragment fragment, @IdRes int frameId) {
        Utils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .replace(frameId, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();

    }


    /**
     * 隐藏fragment
     * @param fragment
     */
    protected void hideFragment(BaseFragment fragment) {
        Utils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .hide(fragment)
                .commitAllowingStateLoss();

    }


    /**
     * 显示fragment
     * @param fragment
     */
    protected void showFragment(BaseFragment fragment) {
        Utils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .show(fragment)
                .commitAllowingStateLoss();

    }


    /**
     * 移除fragment
     * @param fragment
     */
    protected void removeFragment(BaseFragment fragment) {
        Utils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .commitAllowingStateLoss();

    }

    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return (ObservableTransformer<T, T>) new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    /**
     * 弹出栈顶部的Fragment
     */
    protected void popFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }


    /**
     * 初始化SwipeRefreshLayout
     * @param refreshLayout
     */
    protected void initSwipeRefreshLayout(SwipeRefreshLayout refreshLayout,boolean isAutoRefresh){
        //设置下拉出现小圆圈是否是缩放出现，出现的位置，最大的下拉位置
        refreshLayout.setProgressViewOffset(true, 0, 50);

        //设置下拉圆圈的大小，两个值 LARGE， DEFAULT
        refreshLayout.setSize(SwipeRefreshLayout.LARGE);

        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        refreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //只设置一种颜色
//        refreshLayout.setColorSchemeColors(getResources()
//                .getColor(android.R.color.black));

        // 通过 setEnabled(false) 禁用下拉刷新
        refreshLayout.setEnabled(true);

        //设置手势下拉刷新的监听
        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // 刷新动画开始后回调到此方法
                        onRefreshLoadData();
                    }
                }
        );

        if(isAutoRefresh) {
            autoRefresh(refreshLayout);
        }
    }

    /**
     * 自动刷新
     * @param refreshLayout
     */
    private void autoRefresh(SwipeRefreshLayout refreshLayout){
        refreshLayout.measure(0,0);
        refreshLayout.setRefreshing(true);
    }

    protected void onRefreshLoadData(){

    }

}
