package com.com.x.xiaoxi;

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
import com.com.x.AppModel.GangweiModel;
import com.com.x.AppModel.MessageModel;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.x.xcard.ApplicationClass.APPService;

/**
 * Created by X on 16/9/7.
 */
public class MSGManageVC extends BaseActivity {

    private PullToRefreshListView list;
    private MSGManageAdapter adapter;
    private List<MessageModel> dataArr = new ArrayList<>();

    private int page = 1;
    private boolean end = false;
    String sid = ApplicationClass.APPDataCache.User.getShopid();

    @Override
    protected void setupUi() {
        setContentView(R.layout.msg_manage);
        setPageTitle("消息管理");

        setRightImg(R.drawable.add);
        int p = DensityUtil.dip2px(mContext,7);
        setRightImgPadding(p,p,p,p);

        list = (PullToRefreshListView) findViewById(R.id.msg_manage_list);

        adapter = new MSGManageAdapter();

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


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                toInfo(i-1);
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

        XNotificationCenter.getInstance().addObserver("SendMSGSuccessed", new XNotificationCenter.OnNoticeListener() {

            @Override
            public void OnNotice(Object obj) {

                end = false;
                page = 1;
                getData();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XNotificationCenter.getInstance().removeObserver("SendMSGSuccessed");
    }

    private void showAlert(final int p)
    {
        AlertView alert = new AlertView("提醒", "确定要删除消息?", null, null,
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

        XActivityindicator.setAlert(alert);

        alert.show();
    }

    private void deleHD(final int p)
    {
        XNetUtil.Handle(APPService.shopaDelMessages(dataArr.get(p).getId()), "消息删除成功", "消息删除失败", new XNetUtil.OnHttpResult<Boolean>() {

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

    private void toInfo(int p)
    {
        MessageModel model = dataArr.get(p);

        Bundle bundle = new Bundle();
        bundle.putSerializable("model",model);

        pushVC(MSGInfoVC.class,bundle);

    }

    @Override
    protected void setupData() {

    }

    @Override
    public void rightClick(View v) {
        super.rightClick(v);
        pushVC(MSGSendMSGVC.class);

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

        XNetUtil.Handle(APPService.shopaGetMessagesList(sid, page + "", "20"), new XNetUtil.OnHttpResult<List<MessageModel>>() {
            @Override
            public void onError(Throwable e) {

                XNetUtil.APPPrintln(e);
            }

            @Override
            public void onSuccess(List<MessageModel> models) {

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
    class MSGManageAdapter extends BaseAdapter {

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
                convertView = LayoutInflater.from(MSGManageVC.this).inflate(
                        R.layout.msg_manage_cell, null);

                // 实例化一个封装类ListItemView，并实例化它的两个域
                listItemView = new ListItemView();

                listItemView.title = (TextView) convertView
                        .findViewById(R.id.msg_manage_cell_title);
                listItemView.time = (TextView) convertView
                        .findViewById(R.id.msg_manage_cell_time);

                // 将ListItemView对象传递给convertView
                convertView.setTag(listItemView);
            } else {
                // 从converView中获取ListItemView对象
                listItemView = (ListItemView) convertView.getTag();
            }

            String title = (String) dataArr.get(position).getTitle();
            String time = (String) dataArr.get(position).getCreate_time();

            listItemView.title.setText(title);
            listItemView.time.setText(time);

            // 返回convertView对象
            return convertView;
        }

    }

    /**
     * 封装两个视图组件的类
     */
    class ListItemView {
        TextView title;
        TextView time;
    }

}
