package com.jbo.jboshop.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;



/**
 * Created by dllo on 16/5/18.
 */
public abstract class BaseActivity extends AppCompatActivity {
//    protected BmobUser bmobUser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActivity();
//        bmobUser = BmobUser.getCurrentUser(this);
    }

    public abstract void initActivity();

    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }
}
