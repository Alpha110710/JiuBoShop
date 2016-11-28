package com.jbo.jboshop.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jbo.jboshop.activity.CommonScanActivity;
import com.jbo.jboshop.R;
import com.jbo.jboshop.base.BaseFragment;
import com.jbo.jboshop.utils.Constant;
import com.jbo.jboshop.utils.JBoConfig;

/**
 * Created by Ls on 2016/8/27.
 */
public class InputFragment extends BaseFragment implements View.OnClickListener {

    private EditText et_salesman_name, et_customer_name;
    private Button btn_scan;

    @Override
    public int setLayout() {
        return R.layout.fragment_input;
    }

    @Override
    public void initView(View view) {
        et_salesman_name = (EditText) view.findViewById(R.id.et_salesman_name);
        et_customer_name = (EditText) view.findViewById(R.id.et_customer_name);
        btn_scan = (Button) view.findViewById(R.id.btn_scan);

        btn_scan.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_scan:
                if (et_salesman_name.getText().toString().trim().equals("")) {
                    Toast.makeText(context, "请输入业务员姓名", Toast.LENGTH_SHORT).show();
                    et_salesman_name.requestFocus();
                    return;
                }

                if (et_customer_name.getText().toString().trim().equals("")) {
                    Toast.makeText(context, "请输入客户名称", Toast.LENGTH_SHORT).show();
                    et_customer_name.requestFocus();
                    return;
                }

                Intent intent = new Intent(getActivity(), CommonScanActivity.class);
                intent.putExtra(JBoConfig.CUSTOMER_NAME, et_customer_name.getText().toString().trim());
                intent.putExtra(JBoConfig.SALESMAN_NAME, et_salesman_name.getText().toString().trim());
                intent.putExtra(Constant.REQUEST_SCAN_MODE, Constant.REQUEST_SCAN_MODE_BARCODE_MODE);
                startActivity(intent);
                break;
        }
    }
}
