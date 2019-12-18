package com.example.fitness_app.fragments.achievements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.fitness_app.R;
import com.example.fitness_app.fragments.BaseFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AchievementsManagementViewPager extends BaseFragment {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private List<BaseFragment> fragments;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragments = new ArrayList<>();
        fragments.add(new AchievementManagementFragment());
        fragments.add(new AchievementsApprovalFragment());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_achievement_management_view_pager, container, false);

        tabLayout = view.findViewById(R.id.fragment_achievement_management_view_pager_tab_bar);
        viewPager = view.findViewById(R.id.fragment_achievement_management_view_pager_view_pager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.setFragments(fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<BaseFragment> fragments;
        public ViewPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }
        @NonNull
        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }
        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0: return "Management";
                case 1: return "Approval Requests";
            }
            throw new IllegalStateException("Invalid selection");
        }

        public void setFragments(List<BaseFragment> fragments){this.fragments = fragments;}
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
