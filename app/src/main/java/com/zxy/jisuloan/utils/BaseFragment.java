package com.zxy.jisuloan.utils;

import androidx.fragment.app.Fragment;

import me.jessyan.autosize.internal.CustomAdapt;

/**
 * Create by Fang ShiXian
 * on 2019/9/4
 */
public abstract class BaseFragment extends Fragment implements CustomAdapt {


    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    @Override
    public float getSizeInDp() {
        return 667;
    }
}
