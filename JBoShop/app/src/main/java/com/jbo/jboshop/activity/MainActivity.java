package com.jbo.jboshop.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import com.jbo.jboshop.R;
import com.jbo.jboshop.fragment.InputFragment;
import com.jbo.jboshop.fragment.OutputFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private RadioButton radio_one, radio_two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        replaceFragment(new InputFragment());
    }



    private void initView() {
        radio_one = (RadioButton) findViewById(R.id.radio_one);
        radio_two = (RadioButton) findViewById(R.id.radio_two);

        radio_one.setOnClickListener(this);
        radio_two.setOnClickListener(this);
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_main, fragment).commit();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.radio_one:
                replaceFragment(new InputFragment());
                break;
            case R.id.radio_two:
                replaceFragment(new OutputFragment());
                break;
        }
    }
}
