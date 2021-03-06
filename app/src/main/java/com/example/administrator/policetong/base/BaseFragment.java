package com.example.administrator.policetong.base;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Window;

import com.example.administrator.policetong.R;
import com.example.administrator.policetong.activity.FilePictureActivity;
import com.example.administrator.policetong.activity.PictureActivity;
import com.example.administrator.policetong.new_bean.UserBean;
import com.example.administrator.policetong.utils.SPUtils;
import com.example.administrator.policetong.utils.UIUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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


    protected UserBean userInfo;
    private Dialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            EventBus.getDefault().register(this);
        }catch (EventBusException e){

        }
        userInfo = SPUtils.getUserInfo(Objects.requireNonNull(getActivity()));
    }

    //    -------------------------------------新页面展示图片------------------------------------------------------------
    //显示图片
    public void showPicture(String imageUrlcurrent, ArrayList<String> imageUrls, int position) {
        if (imageUrls == null) {
            imageUrls = new ArrayList<>();
        }
        if (TextUtils.isEmpty(imageUrlcurrent)) {
            UIUtils.t("图片链接为空", false, UIUtils.T_ERROR);
            return;
        }
        Intent intent = new Intent(getActivity(), FilePictureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("image", imageUrlcurrent);
        bundle.putStringArrayList("images", imageUrls);
        bundle.putInt("position", position);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //一张图片
    public void showPicture(String imageUrlcurrent) {
        showPicture(imageUrlcurrent, null, 0);
    }

    public void showManyPicture(String imageUrlcurrent, ArrayList<String> imageUrls, int position) {
        if (imageUrls == null) {
            imageUrls = new ArrayList<>();
        }
        if (TextUtils.isEmpty(imageUrlcurrent)) {
            UIUtils.t("图片链接为空", false, UIUtils.T_ERROR);
            return;
        }

//        Dialog dia = new Dialog(this, R.style.alertDialog_style);
//        dia.setContentView(R.layout.activity_start_dialog);
//        // 可放大的imageView
//        MyZoomImageView imageView = (MyZoomImageView ) dia.findViewById(R.area_id.dialog_imageview);
//        Glide.with(this).load(imageUrlcurrent).into(imageView);
//        dia.show();

        Intent intent = new Intent(getActivity(), PictureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("image", imageUrlcurrent);
        bundle.putStringArrayList("images", imageUrls);
        bundle.putInt("position", position);
        intent.putExtras(bundle);
        startActivity(intent);
    }





    //创建Multipart, fieldName为表单字段名
    public static void createFilePart(MultipartBody.Part[] part, int i, File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        part[i] = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
    }

    protected List<LocalMedia> BaseSelectList;

    protected void takePhoto(){
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(9)
                .isCamera(true)
                .compress(true)// 是否压缩
                .selectionMedia(BaseSelectList)
                .forResult(CHOOSE_REQUEST);
    }

    protected void takeOnePhoto(){
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(5)
                .previewImage(false)
                .isCamera(true)
                .compress(true)// 是否压缩
                .selectionMedia(BaseSelectList)
                .forResult(CHOOSE_REQUEST);
    }


    protected void takeOnePhoto(int num){
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(num)
                .previewImage(false)
                .isCamera(true)
                .compress(true)// 是否压缩
                .selectionMedia(BaseSelectList)
                .forResult(CHOOSE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    BaseSelectList = PictureSelector.obtainMultipleResult(data);
                    getPhoto(BaseSelectList);
                    break;
            }
        }
    }

    protected Disposable disposable;


    public abstract void getPhoto(List<LocalMedia> selectList);

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable!=null&&disposable.isDisposed()){
            disposable.dispose();
        }
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    public void setDialog() {
        dialog = new Dialog(Objects.requireNonNull(getActivity()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_loading);
        if (dialog.isShowing()) {
            return;
        }
        UIUtils.doDialog(getActivity(), dialog);
//        dialog.setCanceledOnTouchOutside(false);  //能取消
        dialog.setCancelable(false);
    }

    public void closeDialog() {
        if (dialog == null) {
            return;
        }
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        UIUtils.closeDialog(getActivity(), dialog);
    }

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
