package com.example.administrator.policetong.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.administrator.policetong.R;
import com.example.administrator.policetong.base.BaseActivity;
import com.example.administrator.policetong.utils.imageloader.ILFactory;
import com.example.administrator.policetong.utils.imageloader.ILoader;
import com.example.administrator.policetong.view.PhotoViewPager;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;


public class PictureActivity extends BaseActivity {


    private PhotoViewPager mViewPager;
    private TextView title_name;
    private TextView tvNum;
    private Toolbar tl_custom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        initView();
        initToolbar();
    }


    private List<String> urls;

    private void initToolbar() {
        setupToolBar(tl_custom, false);
        title_name.setText("照片详情");
    }

    protected void initView() {
//        1 获取图片地址的List 和当前图片的地址
        mViewPager = findViewById(R.id.picture_viewpager);
        title_name =  findViewById(R.id.title_name);
        tvNum =  findViewById(R.id.tvNum);
        tl_custom =  findViewById(R.id.tl_custom);

        List<String> images = getIntent().getStringArrayListExtra("images");

        String currentImage = (String) getIntent().getSerializableExtra("image");

        int position = getIntent().getIntExtra("position", -1);

        if (images.size() > 0) {
            urls = images;
            tvNum.setText(String.valueOf("1/"+images.size()));
        } else {
            urls = new ArrayList<>();
            urls.add(currentImage);
        }
        int currentItem = 0;
        //如果没有获取
        if (position == -1) {
            //次函数貌似没有用 不能比较出两个字符串是否相同
            for (int i = 0; i < urls.size(); i++) {
                if ((urls.get(i)).equals(currentImage)) {
                    currentItem = i;
                }
            }
        } else {
            currentItem = position;
        }
        SamplePagerAdapter adapter = new SamplePagerAdapter();
        //设置网址
        adapter.setUrls(urls);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(currentItem);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                tvNum.setText(String.valueOf((i+1)+"/"+urls.size()));
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    static class SamplePagerAdapter extends PagerAdapter {

        public void setUrls(List<String> urls) {
            this.urls = urls;
        }

        private List<String> urls;

        @Override
        public int getCount() {
            return urls.size();
        }

        @NonNull
        @Override
        public View instantiateItem(@NonNull ViewGroup container, final int position) {
            final PhotoView photoView = new PhotoView(container.getContext());
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            Glide.with(photoView.getContext()).load(String.valueOf("https://pic.jjedd.net:9000/"+urls.get(position))).apply(new RequestOptions().placeholder(R.drawable.bg_loading).error(R.drawable.empty_img)).into(photoView);
//            ILFactory.getLoader().loadNet(photoView,String.valueOf("https://pic.jjedd.net:9000/"+urls.get(position)),new ILoader.Options(R.drawable.bg_loading, R.drawable.empty_img));
            return photoView;

        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

}
