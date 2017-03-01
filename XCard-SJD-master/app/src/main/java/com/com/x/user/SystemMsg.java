package com.com.x.user;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.com.x.AppModel.MessageModel;
import com.com.x.xiaoxi.MSGInfoVC;
import com.example.x.xcard.ApplicationClass;
import com.example.x.xcard.BaseActivity;
import com.example.x.xcard.MainActivity;
import com.example.x.xcard.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.x.custom.XGridView;
import com.x.custom.XNetUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.x.xcard.ApplicationClass.APPService;

/**
 * Created by X on 16/9/1.
 */
public class SystemMsg extends BaseActivity {

    private PullToRefreshListView list;
    private SystemMsgAdapter adapter = new SystemMsgAdapter();
    private List<MessageModel> dataArr = new ArrayList<>();

    private int page = 1;
    private boolean end = false;

    @Override
    protected void setupUi() {
        setContentView(R.layout.system_msg);
        setPageTitle("系统公告");

        list = (PullToRefreshListView) findViewById(R.id.system_msg_list);

        adapter = new SystemMsgAdapter();
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

        getData();

    }

    private void toInfo(int p)
    {
        MessageModel model = dataArr.get(p);
        model.setGonggao(true);
        Bundle bundle = new Bundle();
        bundle.putSerializable("model",model);

        pushVC(MSGInfoVC.class,bundle);

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

        XNetUtil.Handle(APPService.hykAddValues(page + "", "20"), new XNetUtil.OnHttpResult<List<MessageModel>>() {
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
    class SystemMsgAdapter extends BaseAdapter {

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
                convertView = LayoutInflater.from(SystemMsg.this).inflate(
                        R.layout.system_msg_cell, null);

                // 实例化一个封装类ListItemView，并实例化它的两个域
                listItemView = new ListItemView();
                listItemView.imageView = (ImageView) convertView
                        .findViewById(R.id.system_msg_cell_img);
                listItemView.title = (TextView) convertView
                        .findViewById(R.id.system_msg_cell_title);
                listItemView.info = (TextView) convertView
                        .findViewById(R.id.system_msg_cell_info);
                listItemView.time = (TextView) convertView
                        .findViewById(R.id.system_msg_cell_time);

                // 将ListItemView对象传递给convertView
                convertView.setTag(listItemView);
            } else {
                // 从converView中获取ListItemView对象
                listItemView = (ListItemView) convertView.getTag();
            }

            // 获取到mList中指定索引位置的资源

            String title = (String) dataArr.get(position).getTitle();
            String info = (String) dataArr.get(position).getDescription();
            String time = (String) dataArr.get(position).getCreate_time();
            // 将资源传递给ListItemView的两个域对象
            //listItemView.imageView.setImageResource(img);
            //listItemView.imageView.setImageDrawable(img);
            listItemView.title.setText(title);
            listItemView.info.setText(info);
            listItemView.time.setText(time);

            // 返回convertView对象
            return convertView;
        }

    }

    /**
     * 封装两个视图组件的类
     */
    class ListItemView {
        ImageView imageView;
        TextView title;
        TextView info;
        TextView time;
    }

}
