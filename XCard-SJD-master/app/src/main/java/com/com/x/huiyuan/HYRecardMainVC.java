package com.com.x.huiyuan;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.com.x.AppModel.MoneyDetailModel;
import com.com.x.xiaoxi.MSGSendMSGVC;
import com.example.x.xcard.ApplicationClass;
import com.example.x.xcard.BaseActivity;
import com.example.x.xcard.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.x.custom.DensityUtil;
import com.x.custom.XNetUtil;
import com.x.custom.XNotificationCenter;

import java.util.ArrayList;
import java.util.List;

import static com.example.x.xcard.ApplicationClass.APPService;

/**
 * Created by X on 2016/10/11.
 */

public class HYRecardMainVC extends BaseActivity {

    private PullToRefreshListView list;
    private HYRecardAdapter adapter;
    private List<MoneyDetailModel> dataArr = new ArrayList<>();

    private int page = 1;
    private boolean end = false;
    String sid = ApplicationClass.APPDataCache.User.getShopid();

    private String cid = "";
    private String uid = "";

    @Override
    protected void setupUi() {
        setContentView(R.layout.hy_recard);
        setPageTitle("充值消费记录");

        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null && bundle.containsKey("uid"))
        {
            uid = bundle.getString("uid");
            cid = bundle.getString("cid");
        }

        list = (PullToRefreshListView) findViewById(R.id.hy_recard_list);

        adapter = new HYRecardAdapter();

        list.setAdapter(adapter);

        list.setMode(PullToRefreshBase.Mode.BOTH);

        list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                XNetUtil.APPPrintln("onPullDownToRefresh ~~~~~~~~~");

                //new FinishRefresh().execute();
                page = 1;
                end = false;
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                XNetUtil.APPPrintln("onPullUpToRefresh ~~~~~~~~~");
                getData();

            }
        });

        getData();

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
                    Toast.makeText(ApplicationClass.context, "没有更多了",
                            Toast.LENGTH_SHORT).show();

                    super.onPostExecute(aVoid);
                }
            }.execute();

            return;
        }

        XNetUtil.Handle(APPService.hykGetUserMoneys(uid,cid, page + "", "20"), new XNetUtil.OnHttpResult<List<MoneyDetailModel>>() {
            @Override
            public void onError(Throwable e) {

                XNetUtil.APPPrintln(e);
            }

            @Override
            public void onSuccess(List<MoneyDetailModel> models) {

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

                adapter.notifyDataSetChanged();
                list.onRefreshComplete();
            }
        });

    }


    /**
     * 定义ListView适配器MainListViewAdapter
     */
    class HYRecardAdapter extends BaseAdapter {

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
            HYRecardMainVC.ListItemView listItemView;

            // 初始化item view
            if (convertView == null) {
                // 通过LayoutInflater将xml中定义的视图实例化到一个View中
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.hy_recard_cell, null);

                // 实例化一个封装类ListItemView，并实例化它的两个域
                listItemView = new HYRecardMainVC.ListItemView();

                listItemView.state = (TextView) convertView
                        .findViewById(R.id.hy_recard_state);
                listItemView.time = (TextView) convertView
                        .findViewById(R.id.hy_recard_time);
                listItemView.type = (TextView) convertView
                        .findViewById(R.id.hy_recard_type);
                listItemView.value = (TextView) convertView
                        .findViewById(R.id.hy_recard_value);
                listItemView.money = (TextView) convertView
                        .findViewById(R.id.hy_recard_money);
                listItemView.bak = (TextView) convertView
                        .findViewById(R.id.hy_recard_bak);

                // 将ListItemView对象传递给convertView
                convertView.setTag(listItemView);
            } else {
                // 从converView中获取ListItemView对象
                listItemView = (HYRecardMainVC.ListItemView) convertView.getTag();
            }

            MoneyDetailModel model = dataArr.get(position);

            listItemView.time.setText(model.getCreate_time());
            listItemView.bak.setText("备注: "+model.getBak());

            if(model.getXftype().equals("1"))
            {
                int c = Color.parseColor("#239400");
                listItemView.value.setTextColor(c);

                switch (model.getCardtype()) {
                    case "1":
                        listItemView.value.setText("充值: +"+model.getValue()+"次");
                        listItemView.money.setText("现金: "+model.getMoney()+"元");
                        break;

                    case "2":
                        listItemView.value.setText("充值: +"+model.getValue()+"元");
                        listItemView.money.setText("现金: "+model.getMoney()+"元");
                        break;

                    default:
                        break;
                }
            }
            else
            {
                int c = Color.parseColor("#bd0000");
                listItemView.value.setTextColor(c);

                listItemView.money.setText("");
                switch (model.getCardtype()) {
                    case "1":
                        listItemView.value.setText("消费: -"+model.getValue()+"次");
                        listItemView.money.setVisibility(TextView.GONE);
                        break;

                    case "2":
                        listItemView.value.setText("消费: -"+model.getValue()+"元");
                        listItemView.money.setVisibility(TextView.GONE);
                        break;
                    case "3":
                        listItemView.money.setVisibility(TextView.VISIBLE);
                        listItemView.value.setText("消费: "+model.getMoney()+"元");
                        listItemView.money.setText("折扣后金额: "+model.getValue()+"元");
                        break;
                    case "4":
                        listItemView.money.setVisibility(TextView.VISIBLE);
                        listItemView.value.setText("消费: "+model.getMoney()+"元");
                        listItemView.money.setText("获得积分: "+model.getValue()+"分");
                        break;
                    default:
                        break;
                }
            }

            // 返回convertView对象
            return convertView;
        }

    }

    /**
     * 封装两个视图组件的类
     */
    class ListItemView {
        TextView time;
        TextView state;
        TextView type;
        TextView value;
        TextView money;
        TextView bak;
    }

}
