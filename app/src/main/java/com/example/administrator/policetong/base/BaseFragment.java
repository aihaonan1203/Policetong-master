package com.example.administrator.policetong.base;

import android.content.Intent;
import android.support.annotation.Keep;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.luck.picture.lib.config.PictureConfig.CHOOSE_REQUEST;


/**
 * <p>Fragment的基类</p>
 *
 * @author 张华洋
 * @name BaseFragment
 */
@Keep
public abstract class BaseFragment extends Fragment {


    protected List<LocalMedia> selectList;

    protected void takePhoto(){
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(9)
                .isCamera(true)
                .selectionMedia(selectList)
                .forResult(CHOOSE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    getPhoto(selectList);
                    break;
            }
        }
    }


    public abstract void getPhoto(List<LocalMedia> selectList);

    //
//    protected BaseActivity mActivity;
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        this.mActivity = (BaseActivity) context;
//    }
//
//
//    /**
//     * 获取宿主Activity
//     *
//     * @return BaseActivity
//     */
//    protected BaseActivity getHoldingActivity() {
//        return mActivity;
//    }
//
//
//    /**
//     * 添加fragment
//     *
//     * @param fragment
//     * @param frameId
//     */
//    protected void addFragment(BaseFragment fragment, @IdRes int frameId) {
//        Utils.checkNotNull(fragment);
//        getHoldingActivity().addFragment(fragment, frameId);
//
//    }
//
//
//    /**
//     * 替换fragment
//     *
//     * @param fragment
//     * @param frameId
//     */
//    protected void replaceFragment(BaseFragment fragment, @IdRes int frameId) {
//        Utils.checkNotNull(fragment);
//        getHoldingActivity().replaceFragment(fragment, frameId);
//    }
//
//
//    /**
//     * 隐藏fragment
//     *
//     * @param fragment
//     */
//    protected void hideFragment(BaseFragment fragment) {
//        Utils.checkNotNull(fragment);
//        getHoldingActivity().hideFragment(fragment);
//    }
//
//
//    /**
//     * 显示fragment
//     *
//     * @param fragment
//     */
//    protected void showFragment(BaseFragment fragment) {
//        Utils.checkNotNull(fragment);
//        getHoldingActivity().showFragment(fragment);
//    }
//
//
//    /**
//     * 移除Fragment
//     *
//     * @param fragment
//     */
//    protected void removeFragment(BaseFragment fragment) {
//        Utils.checkNotNull(fragment);
//        getHoldingActivity().removeFragment(fragment);
//
//    }
//
//
//    /**
//     * 弹出栈顶部的Fragment
//     */
//    protected void popFragment() {
//        getHoldingActivity().popFragment();
//    }

}
