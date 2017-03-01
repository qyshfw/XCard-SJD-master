package com.com.x.card;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.com.x.AppModel.HttpResult;
import com.com.x.AppModel.MoneyDetailModel;
import com.com.x.AppModel.UserModel;
import com.com.x.huiyuan.HYUserInfoVC;
import com.example.x.xcard.ApplicationClass;
import com.example.x.xcard.BaseActivity;
import com.example.x.xcard.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.x.custom.DensityUtil;
import com.x.custom.XActivityindicator;
import com.x.custom.XNetUtil;
import com.x.custom.XNotificationCenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

import static com.example.x.xcard.ApplicationClass.APPService;
import static com.robin.lazy.util.DateKit.getDate;

/**
 * Created by X on 16/9/6.
 */
public class CZDetailVC extends BaseActivity {
    private PullToRefreshListView list;
    private CZDetailAdapter adapter;
    private List<MoneyDetailModel> dataArr = new ArrayList<>();

    private int page = 1;
    private boolean end = false;
    String sid = ApplicationClass.APPDataCache.User.getShopid();

    Observable<HttpResult<List<MoneyDetailModel>>> http;

    private TimePickerView datePicker;
    private boolean isStime = false;
    private Date sdate;
    private  Date edate;

    private TextView stime;
    private TextView etime;
    private TextView tatle;

    private String sStr="";
    private String eStr="";
    private int selectRow = -1;
    private AlertView rightAlert;

    @Override
    protected void setupUi() {
        setContentView(R.layout.cz_detail);
        setPageTitle("充值明细");

        stime = (TextView)findViewById(R.id.cz_detail_stime);
        etime = (TextView)findViewById(R.id.cz_detail_etime);
        tatle = (TextView)findViewById(R.id.cz_detail_tatle);

        list = (PullToRefreshListView)findViewById(R.id.cz_detail_list);
        adapter = new CZDetailAdapter();
        list.setAdapter(adapter);

        list.getRefreshableView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long
                    id) {

                XNetUtil.APPPrintln("position: "+position);

                selectRow = position - 1;
                alertShow();

                return true;
            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                toInfo(i-1);

            }
        });


        list.setMode(PullToRefreshBase.Mode.BOTH);

        list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                XNetUtil.APPPrintln("onPullDownToRefresh ~~~~~~~~~");
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


        //时间选择器
        datePicker = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
//        Calendar calendar = Calendar.getInstance();
//        datePicker.setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR));//要在setTime 之前才有效果哦

        datePicker.setTime(new Date());
        datePicker.setCyclic(false);
        datePicker.setCancelable(true);
        //时间选择后回调
        datePicker.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                if(isStime)
                {
                    sdate = date;
                    stime.setText(getDate(date));
                }
                else
                {
                    edate = date;
                    etime.setText(getDate(date));
                }
                //tvTime.setText(getTime(date));
            }
        });

    }


    private void alertShow()
    {
        if(dataArr.get(selectRow).getStatus().equals("-1"))
        {
            return;
        }

        rightAlert = new AlertView("记录作废", null, null, null,
                new String[]{"确认", "取消"},
                mContext, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {

                if(position == 0)
                {
                    doDel();
                }

            }
        });

        rightAlert.show();
    }

    private void toInfo(int index)
    {
        Bundle bundle = new Bundle();
        bundle.putString("title","充值明细");
        bundle.putSerializable("model",dataArr.get(index));
        pushVC(CardMoneyDetailVC.class,bundle);
    }

    private void doDel()
    {
        rightAlert.dismissImmediately();
        rightAlert = null;
        XActivityindicator.create(mContext).show();
        MoneyDetailModel model = dataArr.get(selectRow);
        XNetUtil.Handle(APPService.shoptDelValue(model.getId()), "充值记录作废成功", "充值记录作废失败", new XNetUtil.OnHttpResult<Boolean>() {

            @Override
            public void onError(Throwable e) {

                if(XActivityindicator.getHud() != null)
                {
                    XActivityindicator.getHud().dismissImmediately();
                }
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                if(aBoolean)
                {
                    XNotificationCenter.getInstance().postNotice("MondetailDelSuccess",null);
                    end = false;
                    page = 1;
                    getData();
                }
            }
        });

    }

    @Override
    protected void setupData() {

    }

    @Override
    public void rightClick(View v) {
        super.rightClick(v);

    }

    public void chooseStartTime(View v) {
        isStime = true;
        datePicker.show();
    }

    public void chooseEndTime(View v) {
        isStime = false;
        datePicker.show();
    }

    public void doSearch(View v) {

        if(sdate != null)
        {
            sStr = sdate.getTime()/1000+"";
        }

        if(edate != null)
        {
            eStr = edate.getTime()/1000+"";
        }

        XActivityindicator.create(mContext).show();
        end = false;
        page = 1;
        getData();

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

        http = APPService.shoptGetValueList(sid,sStr,eStr, page + "", "20");

        XNetUtil.HandleReturnAll(http, new XNetUtil.OnHttpResult<HttpResult<List<MoneyDetailModel>>>() {

            @Override
            public void onError(Throwable e) {
                list.onRefreshComplete();
                XNetUtil.APPPrintln(e);
                if(XActivityindicator.getHud() != null)
                {
                    XActivityindicator.getHud().dismissImmediately();
                }

            }

            @Override
            public void onSuccess(HttpResult<List<MoneyDetailModel>> models) {

                if(page == 1)
                {
                    dataArr.clear();
                }

                List<MoneyDetailModel> arr = models.getData().getInfo();

                if(arr.size() > 0)
                {
                    dataArr.addAll(arr);
                }

                if(arr.size() == 20)
                {
                    end = false;
                    page += 1;
                }
                else
                {
                    end = true;
                }

                tatle.setText("￥"+models.getData().getSum());

                adapter.notifyDataSetChanged();
                list.onRefreshComplete();

                if(XActivityindicator.getHud() != null)
                {
                    XActivityindicator.getHud().dismissImmediately();
                }
            }
        });

    }


    /**
     * 定义ListView适配器MainListViewAdapter
     */
    class CZDetailAdapter extends BaseAdapter {

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
                        R.layout.cz_detail_cell, null);

                // 实例化一个封装类ListItemView，并实例化它的两个域
                listItemView = new ListItemView();
                listItemView.name = (TextView) convertView
                        .findViewById(R.id.cz_detail_cell_name);
                listItemView.tel = (TextView) convertView
                        .findViewById(R.id.cz_detail_cell_tel);
                listItemView.time = (TextView) convertView
                        .findViewById(R.id.cz_detail_cell_time);
                listItemView.user = (TextView) convertView
                        .findViewById(R.id.cz_detail_cell_user);
                listItemView.num = (TextView) convertView
                        .findViewById(R.id.cz_detail_cell_num);
                listItemView.icon = (ImageView) convertView
                        .findViewById(R.id.cz_detail_cell_fei);

                // 将ListItemView对象传递给convertView
                convertView.setTag(listItemView);
            } else {
                // 从converView中获取ListItemView对象
                listItemView = (ListItemView) convertView.getTag();
            }

            // 获取到mList中指定索引位置的资源
            String name = (String) dataArr.get(position).getTruename();
            String tel = (String) dataArr.get(position).getMobile();
            String time = (String) dataArr.get(position).getCreate_time();
            String user = (String) dataArr.get(position).getOpername();
            String num = (String) dataArr.get(position).getMoney();


            listItemView.name.setText(name);
            listItemView.tel.setText(tel);
            listItemView.time.setText(time);
            listItemView.user.setText(user);
            listItemView.num.setText("￥"+num);

            if(dataArr.get(position).getStatus().equals("-1"))
            {
                int c = Color.parseColor("#dcdcdc");

                listItemView.name.setTextColor(c);
                listItemView.tel.setTextColor(c);
                listItemView.time.setTextColor(c);
                listItemView.user.setTextColor(c);
                listItemView.num.setTextColor(c);

                listItemView.icon.setVisibility(View.VISIBLE);

            }
            else
            {
                int c = Color.parseColor("#333333");
                listItemView.name.setTextColor(c);
                listItemView.tel.setTextColor(Color.parseColor("#999999"));
                listItemView.time.setTextColor(c);
                listItemView.user.setTextColor(c);
                listItemView.num.setTextColor(c);

                listItemView.icon.setVisibility(View.GONE);
            }


            // 返回convertView对象
            return convertView;
        }

    }

    /**
     * 封装两个视图组件的类
     */
    class ListItemView {
        TextView name;
        TextView tel;
        TextView time;
        TextView user;
        TextView num;
        ImageView icon;
    }

}
