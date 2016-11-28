package com.jbo.jboshop.fragment;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jbo.jboshop.R;
import com.jbo.jboshop.base.BaseFragment;
import com.jbo.jboshop.bean.Bean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Ls on 2016/8/27.
 */
public class OutputFragment extends BaseFragment implements View.OnClickListener {

    private EditText et_output_customer_name;
    private EditText et_output_salesman_name;
    private EditText et_output_date;
    private EditText et_output_num;

    private Button btn_search;

    private Adapter adapter;
    private PullToRefreshListView listView;
    private TextView textView;

    @Override
    public int setLayout() {
        return R.layout.fragment_output;
    }

    @Override
    public void initView(View view) {

        et_output_customer_name = (EditText) view.findViewById(R.id.et_output_customer_name);
        et_output_salesman_name = (EditText) view.findViewById(R.id.et_output_salesman_name);
        et_output_date = (EditText) view.findViewById(R.id.et_output_date);
        et_output_num = (EditText) view.findViewById(R.id.et_output_num);

        btn_search = (Button) view.findViewById(R.id.btn_search);
        listView = (PullToRefreshListView) view.findViewById(R.id.list);

        btn_search.setOnClickListener(this);

    }

    @Override
    public void initData() {

        adapter = new Adapter(getActivity());
        listView.getRefreshableView().setAdapter(adapter);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getList(true, true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getList(false, false);
            }
        });

        textView = new TextView(getActivity());
        textView.setGravity(Gravity.CENTER);
        textView.setText("暂无数据");
    }

    private int limit = 20;        // 每页的数据是10条

    /**
     * 查询数据的方法
     *
     * @param isPullDown 判断是否是下拉刷新
     */
    private void getList(final boolean isPullDown, final boolean isFirst) {

        BmobQuery<Bean> query = new BmobQuery<>();
        
        btn_search.setClickable(false);//查询不可连续点击
        btn_search.setBackgroundColor(0xFF8a8a8a);
        //添加查询条件
        if (!et_output_salesman_name.getText().toString().trim().equals("")) {

            query.addWhereEqualTo("yewuyuanName", et_output_salesman_name.getText().toString().trim());
        }

        if (!et_output_customer_name.getText().toString().trim().equals("")) {
            query.addWhereEqualTo("kehuName", et_output_customer_name.getText().toString().trim());
        }

        if (!et_output_date.getText().toString().trim().equals("")) {
            query.addWhereEqualTo("date", et_output_date.getText().toString().trim());
        }

        if (!et_output_num.getText().toString().trim().equals("")) {
            query.addWhereEqualTo("num", et_output_num.getText().toString().trim());
        }

        if (isPullDown) {
            query.setSkip(0);
        } else {
            // 跳过之前页数并去掉重复数据
            query.setSkip(adapter.getBeen().size());
        }

        //返回20条数据
        query.setLimit(limit);
        //执行查询方法
        query.findObjects(new FindListener<Bean>() {
            @Override
            public void done(List<Bean> object, BmobException e) {
                if (e == null) {
                    if (object.size() > 0) {
                        if (isPullDown) {
                            // 当是下拉刷新操作时, 重新添加
                            adapter.setBeen(object);
                        } else {
                            adapter.addBean(object);
                        }
                        Toast.makeText(context, "查询成功：共" + adapter.getBeen().size() + "条数据。", Toast.LENGTH_SHORT).show();
                        listView.setPullLabel("上拉刷新", PullToRefreshBase.Mode.PULL_FROM_END);
                        listView.setReleaseLabel("放开以刷新", PullToRefreshBase.Mode.PULL_FROM_END);
                        listView.setRefreshingLabel("正在载入", PullToRefreshBase.Mode.PULL_FROM_END);

                    } else {
                        listView.setEmptyView(textView);
                        Toast.makeText(context, "没有数据了", Toast.LENGTH_SHORT).show();
                        listView.setPullLabel("没有更多数据", PullToRefreshBase.Mode.PULL_FROM_END);
                        listView.setReleaseLabel("没有更多数据", PullToRefreshBase.Mode.PULL_FROM_END);
                        listView.setRefreshingLabel("没有更多数据", PullToRefreshBase.Mode.PULL_FROM_END);

                        if (isFirst) {
                            adapter.setBeen(new ArrayList<Bean>());
                        }
                    }

                    listView.onRefreshComplete();
                    adapter.notifyDataSetChanged();
                    btn_search.setClickable(true);
                    btn_search.setBackgroundColor(0xFF63F244);

                } else {
                    Toast.makeText(context, "查询失败", Toast.LENGTH_SHORT).show();
                    btn_search.setClickable(true);
                    btn_search.setBackgroundColor(0xFF63F244);
                    listView.onRefreshComplete();
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_search:
                getList(true, true);
                //隐藏键盘
                hideSoftKeyboard(et_output_date);
                hideSoftKeyboard(et_output_customer_name);
                hideSoftKeyboard(et_output_salesman_name);
                hideSoftKeyboard(et_output_num);
                break;
        }
    }


    /**
     * @Description 隐藏软键盘
     */
    public void hideSoftKeyboard(EditText editText) {
        InputMethodManager localInputMethodManager = (InputMethodManager) editText
                .getContext().getSystemService("input_method");
        localInputMethodManager.hideSoftInputFromWindow(
                editText.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    public class Adapter extends BaseAdapter {

        private Context context;
        private List<Bean> been;


        public void setBeen(List<Bean> been) {
            this.been = been;
            notifyDataSetChanged();
        }

        public void addBean(List<Bean> bean) {
            been.addAll(bean);
        }

        public Adapter(Context context) {
            this.context = context;
            been = new ArrayList<>();
        }

        public List<Bean> getBeen() {
            return been;
        }

        @Override
        public int getCount() {
            return been == null ? 0 : been.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            ViewHolder viewHolder = null;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_list, viewGroup, false);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.item_data_name.setText(been.get(i).getDate());
            viewHolder.item_kehu_name.setText(been.get(i).getKehuName());
            viewHolder.item_tiaoma.setText(been.get(i).getNum());
            viewHolder.item_yewuyuan_name.setText(been.get(i).getYewuyuanName());

            return view;
        }


        class ViewHolder {
            TextView item_kehu_name, item_tiaoma, item_yewuyuan_name, item_data_name;

            public ViewHolder(View itemView) {
                item_kehu_name = (TextView) itemView.findViewById(R.id.item_kehu_name);
                item_tiaoma = (TextView) itemView.findViewById(R.id.item_tiaoma);
                item_yewuyuan_name = (TextView) itemView.findViewById(R.id.item_yewuyuan_name);
                item_data_name = (TextView) itemView.findViewById(R.id.item_data_name);

            }
        }
    }
}
