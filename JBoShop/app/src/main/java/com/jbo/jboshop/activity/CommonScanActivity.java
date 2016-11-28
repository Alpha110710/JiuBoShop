/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jbo.jboshop.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.jbo.jboshop.R;
import com.jbo.jboshop.bean.Bean;
import com.jbo.jboshop.utils.Constant;
import com.jbo.jboshop.utils.JBoConfig;
import com.jbo.jboshop.zxing.ScanListener;
import com.jbo.jboshop.zxing.ScanManager;
import com.jbo.jboshop.zxing.decode.DecodeThread;
import com.jbo.jboshop.zxing.decode.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


/**
 * 二维码扫描使用
 */
public final class CommonScanActivity extends Activity implements ScanListener, View.OnClickListener {
    static final String TAG = CommonScanActivity.class.getSimpleName();
    SurfaceView scanPreview = null;
    View scanContainer;
    View scanCropView;
    ImageView scanLine;
    ScanManager scanManager;
    TextView iv_light;
    TextView qrcode_g_gallery;
    TextView qrcode_ic_back;
    final int PHOTOREQUESTCODE = 1111;

    private String customerName = "", salesmanName = "", date = "", num = "";
    private ArrayList<String> nums = new ArrayList<>();

    @Bind(R.id.service_register_rescan)
    Button rescan;
    @Bind(R.id.scan_image)
    ImageView scan_image;
    @Bind(R.id.authorize_return)
    ImageView authorize_return;
    private int scanMode;//扫描模型（条形，二维码，全部）

    @Bind(R.id.common_title_TV_center)
    TextView title;
    @Bind(R.id.scan_hint)
    TextView scan_hint;
    @Bind(R.id.tv_scan_result)
    TextView tv_scan_result;

    private Button service_register_chongsao;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        customerName = getIntent().getStringExtra(JBoConfig.CUSTOMER_NAME);
        salesmanName = getIntent().getStringExtra(JBoConfig.SALESMAN_NAME);

        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = sDateFormat.format(new java.util.Date());

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_scan_code);
        ButterKnife.bind(this);
        scanMode = getIntent().getIntExtra(Constant.REQUEST_SCAN_MODE, Constant.REQUEST_SCAN_MODE_ALL_MODE);
        initView();
    }


    void initView() {
        switch (scanMode) {
            case DecodeThread.BARCODE_MODE:
                title.setText(R.string.scan_barcode_title);
                scan_hint.setText(R.string.scan_barcode_hint);
                break;
            case DecodeThread.QRCODE_MODE:
                title.setText(R.string.scan_qrcode_title);
                scan_hint.setText(R.string.scan_qrcode_hint);
                break;
            case DecodeThread.ALL_MODE:
                title.setText(R.string.scan_allcode_title);
                scan_hint.setText(R.string.scan_allcode_hint);
                break;
        }
        service_register_chongsao = (Button) findViewById(R.id.service_register_chongsao);
        scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
        scanContainer = findViewById(R.id.capture_container);
        scanCropView = findViewById(R.id.capture_crop_view);
        scanLine = (ImageView) findViewById(R.id.capture_scan_line);
        qrcode_g_gallery = (TextView) findViewById(R.id.qrcode_g_gallery);
        qrcode_g_gallery.setOnClickListener(this);
        qrcode_ic_back = (TextView) findViewById(R.id.qrcode_ic_back);
        qrcode_ic_back.setOnClickListener(this);
        iv_light = (TextView) findViewById(R.id.iv_light);
        iv_light.setOnClickListener(this);
        rescan.setOnClickListener(this);
        authorize_return.setOnClickListener(this);
        service_register_chongsao.setOnClickListener(this);
        //构造出扫描管理器
        scanManager = new ScanManager(this, scanPreview, scanContainer, scanCropView, scanLine, scanMode, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        scanManager.onResume();
        rescan.setVisibility(View.VISIBLE);
        scan_image.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        scanManager.onPause();
    }

    /**
     *  扫描回调
     */
    public void scanResult(Result rawResult, Bundle bundle) {
        //扫描成功后，扫描器不会再连续扫描，如需连续扫描，调用reScan()方法。
        //scanManager.reScan();
        //Toast.makeText(this, "result="+rawResult.getText(), Toast.LENGTH_LONG).show();

        if (!scanManager.isScanning()) { //如果当前不是在扫描状态
            //设置再次扫描按钮出现
            rescan.setVisibility(View.VISIBLE);
            scan_image.setVisibility(View.VISIBLE);
            Bitmap barcode = null;
            byte[] compressedBitmap = bundle.getByteArray(DecodeThread.BARCODE_BITMAP);
            if (compressedBitmap != null) {
                barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
                barcode = barcode.copy(Bitmap.Config.ARGB_8888, true);
            }
            scan_image.setImageBitmap(barcode);
        }
        rescan.setVisibility(View.VISIBLE);
        scan_image.setVisibility(View.VISIBLE);
        tv_scan_result.setVisibility(View.VISIBLE);
        tv_scan_result.setText("业务员姓名 :" + customerName + "\n客户名称 :" + salesmanName + "\n条码编号 :" + rawResult.getText() + "\n保存日期 :" + date);

        rescan.setBackgroundColor(0xff63F244);
        rescan.setClickable(true);
        rescan.setText("确认保存");

        num = rawResult.getText();


    }

    void startScan() {
        if (rescan.getVisibility() == View.VISIBLE) {
            rescan.setVisibility(View.VISIBLE);
            scan_image.setVisibility(View.GONE);
            scanManager.reScan();
        }
    }

    @Override
    public void scanError(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        //相机扫描出错时
        if (e.getMessage() != null && e.getMessage().startsWith("相机")) {
            scanPreview.setVisibility(View.INVISIBLE);
        }
    }

    public void showPictures(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String photo_path;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTOREQUESTCODE:
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = this.getContentResolver().query(data.getData(), proj, null, null, null);
                    if (cursor.moveToFirst()) {
                        int colum_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        photo_path = cursor.getString(colum_index);
                        if (photo_path == null) {
                            photo_path = Utils.getPath(getApplicationContext(), data.getData());
                        }
                        scanManager.scanningImage(photo_path);
                    }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qrcode_g_gallery:
                showPictures(PHOTOREQUESTCODE);
                break;
            case R.id.iv_light:
                scanManager.switchLight();
                break;
            case R.id.qrcode_ic_back:
                finish();
                break;
            case R.id.service_register_rescan:
                //保存数据
                if (salesmanName.equals("") || customerName.equals("") || date.equals("") || num.equals("")) {
                    return;
                }

                if (nums.contains(num)) {
                    Toast.makeText(this, "此二维码您已经扫过码, 请不要重复添加", Toast.LENGTH_SHORT).show();
                    return;
                }
                nums.add(num);
                rescan.setBackgroundColor(0xff8A8A8A);
                rescan.setClickable(false);
                //确认保存
                Bean bean = new Bean();
                bean.setKehuName(customerName);
                bean.setDate(date);
                bean.setYewuyuanName(salesmanName);
                bean.setNum(num);

                bean.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            rescan.setText("保存成功");
                        } else {
                            rescan.setText("保存失败");
                            if (e.getErrorCode() == 9010){
                                Toast.makeText(CommonScanActivity.this, "网络超时", Toast.LENGTH_SHORT).show();
                            }else if (e.getErrorCode() == 9016){
                                Toast.makeText(CommonScanActivity.this, "无网络连接，请检查您的手机网络。", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(CommonScanActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });

                break;

            case R.id.service_register_chongsao:
                startScan();//再次扫描

                break;
            case R.id.authorize_return:
                finish();
                break;
            default:
                break;
        }
    }

}