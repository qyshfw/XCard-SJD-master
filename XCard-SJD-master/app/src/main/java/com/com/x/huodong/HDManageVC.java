package com.com.x.huodong;

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
import com.com.x.AppModel.ActivityModel;
import com.example.x.xcard.ApplicationClass;
import com.example.x.xcard.BaseActivity;
import com.example.x.xcard.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.x.custom.DensityUtil;
import com.x.custom.XNetUtil;
import com.x.custom.XNotificationCenter;

import java.util.ArrayList;
import java.util.List;

import static com.example.x.xcard.ApplicationClass.APPService;

/**
 * Created by X on 16/9/6.
 */
public class HDManageVC extends BaseActivity {

    private PullToRefreshListView list;
    private HDManageAdapter adapter;
    private List<ActivityModel> dataArr = new ArrayList<>();

    private int page = 1;
    private boolean end = false;
    String sid = ApplicationClass.APPDataCache.User.getShopid();

    private AlertView alert;

    @Override
    protected void setupUi() {
        setContentView(R.layout.hd_manage);
        setPageTitle("活动管理");
        setRightImg(R.drawable.add);
        int p = DensityUtil.dip2px(mContext,7);
        setRightImgPadding(p,p,p,p);

        //找到需要下拉的list
        list = (PullToRefreshListView) findViewById(R.id.hd_manage_list);
        adapter = new HDManageAdapter();
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




        list.getRefreshableView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long
                    id) {

                showAlert(position-1);

                return true;
            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                toInfo(i-1);

            }
        });


        getData();

        XNotificationCenter.getInstance().addObserver("CreateHDSuccessed", new XNotificationCenter.OnNoticeListener() {

            @Override
            public void OnNotice(Object obj) {

                end = false;
                page = 1;
                getData();
            }
        });

    }

    private void toInfo(int index)
    {
        Bundle bundle = new Bundle();
        //序列化
        bundle.putSerializable("model",dataArr.get(index));
        //活动详情界面
        pushVC(HDInfoVC.class,bundle);
    }


    private void showAlert(final int p)
    {
        alert = new AlertView("提醒", "确定要作废该活动?", null, null,
                new String[]{"作废","取消"},
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

        XNetUtil.Handle(APPService.shopaDelShopHD(dataArr.get(p).getId()), "活动作废成功", "活动作废失败", new XNetUtil.OnHttpResult<Boolean>() {

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XNotificationCenter.getInstance().removeObserver("CreateHDSuccessed");
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

        XNetUtil.Handle(APPService.shopaGetShopHD(sid, page + "", "20"), new XNetUtil.OnHttpResult<List<ActivityModel>>() {
            @Override
            public void onError(Throwable e) {

                XNetUtil.APPPrintln(e);
            }

            @Override
            public void onSuccess(List<ActivityModel> models) {

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

    @Override
    protected void setupData() {

    }


    @Override
    public void rightClick(View v) {
        super.rightClick(v);
        pushVC(HDCreateVC.class);
    }


    /**
     * 定义ListView适配器MainListViewAdapter
     */
    class HDManageAdapter extends BaseAdapter {

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
            HDManageVC.ListItemView listItemView;

            // 初始化item view
            if (convertView == null) {
                // 通过LayoutInflater将xml中定义的视图实例化到一个View中
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.hd_manage_cell, null);

                // 实例化一个封装类ListItemView，并实例化它的两个域
                listItemView = new HDManageVC.ListItemView();
                listItemView.img = (ImageView) convertView
                        .findViewById(R.id.hd_manage_cell_img);
                listItemView.time = (TextView) convertView
                        .findViewById(R.id.hd_manage_cell_time);
                listItemView.title = (TextView) convertView
                        .findViewById(R.id.hd_manage_cell_title);

                // 将ListItemView对象传递给convertView
                convertView.setTag(listItemView);
            } else {
                // 从converView中获取ListItemView对象
                listItemView = (HDManageVC.ListItemView) convertView.getTag();
            }

            ViewGroup.LayoutParams layoutParams = listItemView.img.getLayoutParams();

            int w = ApplicationClass.SW - DensityUtil.dip2px(mContext,28);
            int h = (int)(w * 7.0 / 16.0);

            layoutParams.height = h;
            listItemView.img.setLayoutParams(layoutParams);

            // 获取到mList中指定索引位置的资源
            String img = dataArr.get(position).getUrl();
            String time = dataArr.get(position).getS_time()+"至"+dataArr.get(position).getE_time();
            String title = dataArr.get(position).getTitle();

            ImageLoader.getInstance().displayImage(img,listItemView.img);

            listItemView.time.setText(time);
            listItemView.title.setText(title);

            // 返回convertView对象
            return convertView;
        }

    }

    /**
     * 封装两个视图组件的类
     */
    class ListItemView {
        ImageView img;
        TextView time;
        TextView title;
    }


}