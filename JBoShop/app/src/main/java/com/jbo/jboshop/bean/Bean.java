package com.jbo.jboshop.bean;

import android.os.Parcel;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by Ls on 2016/8/27.
 */
public class Bean extends BmobObject implements Serializable{

    private String yewuyuanName;

    private String kehuName;

    private String date;

    private String num;

    public Bean() {
    }

    protected Bean(Parcel in) {
        yewuyuanName = in.readString();
        kehuName = in.readString();
        date = in.readString();
        num = in.readString();
    }


    public String getYewuyuanName() {
        return yewuyuanName;
    }

    public void setYewuyuanName(String yewuyuanName) {
        this.yewuyuanName = yewuyuanName;
    }

    public String getKehuName() {
        return kehuName;
    }

    public void setKehuName(String kehuName) {
        this.kehuName = kehuName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }


}
