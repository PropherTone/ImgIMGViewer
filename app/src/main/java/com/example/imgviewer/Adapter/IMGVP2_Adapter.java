package com.example.imgviewer.Adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;

public class IMGVP2_Adapter extends FragmentStatePagerAdapter {

    public ArrayList<Integer> fragmentList = new ArrayList<>();
    public ArrayList<Fragment> fragments = new ArrayList<>();
    private int id = 0;

    public IMGVP2_Adapter(@NonNull FragmentManager fm, int behavior, ArrayList<Fragment> fragments) {
        super(fm, behavior);
        for (Fragment fragment : fragments) {
            this.fragments.add(fragment);
            fragmentList.add(id++);
        }
        notifyDataSetChanged();
    }

    public ArrayList<Fragment> getFragmentList() {
        return fragments;
    }

    public void addPageInPosition(int index, Fragment fragment){
        fragments.add(index, fragment);
        fragmentList.add(index, id++);
        notifyDataSetChanged();
    }

    public void addPage(Fragment fragment){
        fragments.add(fragment);
        fragmentList.add(id++);
        notifyDataSetChanged();
    }

    public void delPage(int index) {
        fragments.remove(index);
        fragmentList.remove(index);
        notifyDataSetChanged();
    }

    public void updatePage(ArrayList<Fragment> fragmentList) {
        fragments.clear();
        this.fragmentList.clear();
        for(int i = 0; i < fragmentList.size(); i++){
            fragments.add(fragmentList.get(i));
            this.fragmentList.add(id++);//注意这里是id++，不是i++。
        }
        notifyDataSetChanged();
    }

    @Override
    public @NotNull Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public int getItemPosition(@NotNull Object object) {
        if (object instanceof Fragment) {
            if (fragments.contains(object)) {
                return fragments.indexOf(object);
            } else {
                return POSITION_NONE;
            }
        }
        return super.getItemPosition(object);
    }

    public long getItemId(int position) {
        return fragmentList.get(position);
    }
}