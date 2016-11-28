package com.jbo.jboshop.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dllo on 16/5/18.
 */
public abstract class BaseFragment extends Fragment {

    protected Context context;
//    protected BmobUser bmobUser;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
//        bmobUser = BmobUser.getCurrentUser(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(setLayout(), container, false);
    }

    public abstract int setLayout();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    public abstract void initView(View view);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    public abstract void initData();

    protected <T extends View> T findView(int id) {
        return (T) getView().findViewById(id);
    }
}
