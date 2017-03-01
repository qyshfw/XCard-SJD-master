package com.com.x.yuangong;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.com.x.AppModel.YuangongModel;
import com.example.x.xcard.ApplicationClass;
import com.example.x.xcard.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.x.custom.XActivityindicator;
import com.x.custom.XEasyList;
import com.x.custom.XHorizontalBaseFragment;
import com.x.custom.XNetUtil;
import com.x.custom.XNotificationCenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import static com.example.x.xcard.ApplicationClass.APPService;

/**
 * Created by X on 16/9/2.
 */
public class YGManageLeft extends XHorizontalBaseFragment
{
    private PullToRefreshListView list;
    private Context context;
    private int page = 1;
    private boolean end = false;
    private  YGManageYGAdapter adapter;
    private List<YuangongModel> dataArr = new ArrayList<>();
    String sid = ApplicationClass.APPDataCache.User.getShopid();

    @Override
    protected void lazyLoad() {

        System.out.println("LeftFragment--->lazyLoad !!!");

        // 获取MainListAdapter对象
        adapter = new YGManageYGAdapter();

        if(list != null)
        {
            list.setAdapter(adapter);
        }

        getData();

    }

    /** Acitivity要实现这个接口，这样Fragment和Activity就可以共享事件触发的资源了 */
    public interface MyListener
    {
        public void showMessage(int index);
    }

    private MyListener myListener;

    @Override
    public void onDestroy() {
        super.onDestroy();

        XNotificationCenter.getInstance().removeObserver("AddYGSuccess");

    }

    @Override
    public void onAttach(Context context) {
        if (context instanceof MyListener) {
            myListener = (MyListener) context;
        }

        this.context = context;
        System.out.println("LeftFragment--->onAttach");


        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        System.out.println("LeftFragment--->onCreate");

        XNotificationCenter.getInstance().addObserver("AddYGSuccess", new XNotificationCenter.OnNoticeListener() {
            @Override
            public void OnNotice(Object obj) {
                page = 1;
                end = false;
                getData();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        System.out.println("LeftFragment--->onCreateView");
        View v = inflater.inflate(R.layout.yg_manage_yg, container, false);
        list = (PullToRefreshListView) v.findViewById(R.id.yg_manage_yg_list);


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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                showAlert(position-1);
            }
        });



        return v;
    }

    private void showAlert(final int index) {
        AlertView Alert = new AlertView("删除员工", null, null, null,
                new String[]{"删除", "取消"},
                context, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    doDel(index);
                }
            }
        });

        XActivityindicator.setAlert(Alert);

        Alert.show();
    }

    private void doDel(int index)
    {
        String id = dataArr.get(index).getId();
        XNetUtil.Handle(APPService.powerDelShopWorker(id), "员工删除成功", "员工删除失败", new XNetUtil.OnHttpResult<Boolean>() {

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
    public void onResume()
    {
        super.onResume();
        System.out.println("LeftFragment--->onResume");
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

        XNetUtil.Handle(APPService.powerGetShopWorker(sid, page + "", "20"), new XNetUtil.OnHttpResult<List<YuangongModel>>() {
            @Override
            public void onError(Throwable e) {

                XNetUtil.APPPrintln(e);
            }

            @Override
            public void onSuccess(List<YuangongModel> yuangongModels) {

                if(page == 1)
                {
                    dataArr.clear();
                }

                if(yuangongModels.size() > 0)
                {
                    dataArr.addAll(yuangongModels);
                }

                if(yuangongModels.size() == 20)
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
    class YGManageYGAdapter extends BaseAdapter {

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
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.yg_manage_yg_cell, null);

                // 实例化一个封装类ListItemView，并实例化它的两个域
                listItemView = new ListItemView();
                listItemView.header = (ImageView) convertView
                        .findViewById(R.id.yg_manage_cell_img);
                listItemView.name = (TextView) convertView
                        .findViewById(R.id.yg_manage_cell_title);
                listItemView.tel = (TextView) convertView
                        .findViewById(R.id.yg_manage_cell_tel);

                // 将ListItemView对象传递给convertView
                convertView.setTag(listItemView);
            } else {
                // 从converView中获取ListItemView对象
                listItemView = (ListItemView) convertView.getTag();
            }

            // 获取到mList中指定索引位置的资源
            //int img = (int) dataArr.get(position).
            String name = (String) dataArr.get(position).getTruename();
            String tel = (String) dataArr.get(position).getMobile();

            // 将资源传递给ListItemView的两个域对象
            //listItemView.header.setImageResource(img);
            //listItemView.imageView.setImageDrawable(img);
            listItemView.name.setText(name);
            listItemView.tel.setText(tel);

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
        RadioButton checkBox;
    }

}
