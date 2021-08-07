package bsmart.technology.rru.pages;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import bsmart.technology.rru.R;
import bsmart.technology.rru.base.BaseFragment;
import bsmart.technology.rru.base.utils.TabMainItemView;
import bsmart.technology.rru.pages.BarCodeFragment;
import bsmart.technology.rru.pages.MainBaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HAActivity extends MainBaseActivity {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);

        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(mainAdapter);
        viewPager.setOnTouchListener((v, event) -> true);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(mOnTabSelectedListener);

        ViewGroup.LayoutParams layoutParams =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);

        TabMainItemView mNewCaseTab = new TabMainItemView(this);
        mNewCaseTab.setResource(R.mipmap.tabbar_qrcode_nor,
                R.mipmap.tabbar_qrcode_sel,
                "HA");
        mNewCaseTab.setLayoutParams(layoutParams);
        mNewCaseTab.onSelected();
        tabLayout.getTabAt(0).setCustomView(mNewCaseTab);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = fragments.get(0);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    private TabLayout.BaseOnTabSelectedListener mOnTabSelectedListener
            = new TabLayout.BaseOnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            ((TabMainItemView) tab.getCustomView()).onSelected();
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            ((TabMainItemView) tab.getCustomView()).onUnSelected();
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    private List<BaseFragment> fragments = new ArrayList<>();

    private class MainAdapter extends FragmentPagerAdapter
            implements ViewPager.OnPageChangeListener {

        public MainAdapter(FragmentManager fm) {
            super(fm);
            fragments.add(new BarCodeFragment());
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public void onPageScrolled(int position,
                                   float positionOffset,
                                   int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
