package com.com.x.huiyuan;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.com.x.AppModel.HttpResult;
import com.com.x.AppModel.UserModel;
import com.example.x.xcard.ApplicationClass;
import com.example.x.xcard.BaseActivity;
import com.example.x.xcard.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.x.custom.XNetUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import static com.example.x.xcard.ApplicationClass.APPService;

/**
 * Created by X on 16/9/5.
 */
public class HYManageVC extends BaseActivity {

    private TextView num;
    private EditText tel;
    private PullToRefreshListView list;
    private HYManageAdapter adapter;
    private List<UserModel> dataArr = new ArrayList<>();

    private int page = 1;
    private boolean end = false;
    String sid = ApplicationClass.APPDataCache.User.getShopid();

    private int selectRow = -1;
    private AlertView alert;

    Observable<HttpResult<List<UserModel>>> http;

    @Override
    protected void setupUi() {
        setContentView(R.layout.hy_manage);
        setPageTitle("会员管理");
        tel = (EditText)findViewById(R.id.hy_manage_tel);
        num = (TextView)findViewById(R.id.hy_manage_num);
        list = (PullToRefreshListView)findViewById(R.id.hy_manage_list);
        tel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                toSearch(editable.toString().trim());
            }
        });

        http = APPService.shopdGetShopUser(sid, page + "", "20");

        adapter = new HYManageAdapter();

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                selectRow = i-1;

                toInfo();
            }
        });

        list.setMode(PullToRefreshBase.Mode.BOTH);

        list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                XNetUtil.APPPrintln("onPullDownToRefresh ~~~~~~~~~");

                //new FinishRefresh().execute();
                if(tel.getText().toString().trim().length() == 0 || tel.getText().toString().trim().length() == 11)
                {
                    page = 1;
                    end = false;
                }

                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                XNetUtil.APPPrintln("onPullUpToRefresh ~~~~~~~~~");
                getData();

            }
        });

        list.getRefreshableView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long
                    id) {

                showAlert(position-1);

                return true;
            }
        });

        getData();

    }

    private void showAlert(final int p)
    {
        alert = new AlertView("提醒", "确定要删除该会员?", null, null,
                new String[]{"删除","取消"},
                mContext, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                System.out.println("点击了: "+position);

                if(position == 0)
                {
                    deleHD(p);
                }
            }
        });

        alert.show();
    }

    private void deleHD(final int p)
    {
        alert.dismissImmediately();
        alert = null;

        XNetUtil.Handle(APPService.shopdDelShopUser(dataArr.get(p).getId()), "删除会员成功", "删除会员失败", new XNetUtil.OnHttpResult<Boolean>() {

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                if(aBoolean)
                {
                    end = false;
                    page = 1;
                    getData();
                }
            }
        });

    }

    private void toInfo()
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable("model",dataArr.get(selectRow));

        pushVC(HYUserInfoVC.class,bundle);
    }


    private void toSearch(String str)
    {
        if(str.length() == 11)
        {
            end=false;
            page = 1;
            http = APPService.shopdGetUserInfoM(str, sid);
            getData();
        }
        else if(str.length() == 0)
        {
            end=false;
            page = 1;
            http = APPService.shopdGetShopUser(sid, page + "", "20");
            getData();
        }
        else
        {
            num.setText("全部共0位会员");
            end = true;
            dataArr.clear();
            adapter.notifyDataSetChanged();
            getData();
        }

    }

    @Override
    protected void setupData() {

    }

    private void getData() {


        XNetUtil.APPPrintln("do getData !!!!!!!!!!");

        if(end)
        {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {

                    list.onRefreshComplete();
                    if(tel.getText().toString().trim().length() == 0)
                    {
                        Toast.makeText(ApplicationClass.context, "没有更多了",
                                Toast.LENGTH_SHORT).show();
                    }


                    super.onPostExecute(aVoid);
                }
            }.execute();

            return;
        }

        XNetUtil.Handle(http, new XNetUtil.OnHttpResult<List<UserModel>>() {
            @Override
            public void onError(Throwable e) {
                list.onRefreshComplete();
                XNetUtil.APPPrintln(e);
            }

            @Override
            public void onSuccess(List<UserModel> models) {

                if(page == 1)
                {
                    dataArr.clear();
                }

                if(models.size() > 0)
                {
                    dataArr.addAll(models);
                }

                if(models.size() == 20)
                {
                    end = false;
                    page += 1;
                }
                else
                {
                    end = true;
                }

                num.setText("全部共"+dataArr.size()+"位会员");

                adapter.notifyDataSetChanged();
                list.onRefreshComplete();
            }
        });

    }


    /**
     * 定义ListView适配器MainListViewAdapter
     */
    class HYManageAdapter extends BaseAdapter {

        /**
         * 返回item的个数
         */
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return dataArr.size();
        }

        /**
         * 返回item的内容
         */
        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return dataArr.get(position);
        }

        /**
         * 返回item的id
         */
        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        /**
         * 返回item的视图
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListItemView listItemView;

            // 初始化item view
            if (convertView == null) {
                // 通过LayoutInflater将xml中定义的视图实例化到一个View中
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.hy_manage_cell, null);

                // 实例化一个封装类ListItemView，并实例化它的两个域
                listItemView = new ListItemView();
                listItemView.header = (ImageView) convertView
                        .findViewById(R.id.hy_manage_cell_img);
                listItemView.name = (TextView) convertView
                        .findViewById(R.id.hy_manage_cell_name);
                listItemView.tel = (TextView) convertView
                        .findViewById(R.id.hy_manage_cell_tel);
                listItemView.no = (TextView) convertView
                        .findViewById(R.id.hy_manage_cell_no);

                // 将ListItemView对象传递给convertView
                convertView.setTag(listItemView);
            } else {
                // 从converView中获取ListItemView对象
                listItemView = (ListItemView) convertView.getTag();
            }

            // 获取到mList中指定索引位置的资源
            String name = (String) dataArr.get(position).getTruename();
            String tel = (String) dataArr.get(position).getMobile();
            String no = (String) dataArr.get(position).getUid();

            listItemView.name.setText(name);
            listItemView.tel.setText(tel);
            //listItemView.no.setText("NO."+no);
            listItemView.no.setText("");

            // 返回convertView对象
            return convertView;
        }

    }

    /**
     * 封装两个视图组件的类
     */
    class ListItemView {
        ImageView header;
        TextView name;
        TextView tel;
        TextView no;
    }

}
