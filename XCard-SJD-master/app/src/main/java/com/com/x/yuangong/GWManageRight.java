package com.com.x.yuangong;

import android.content.Context;
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
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.com.x.AppModel.GangweiModel;
import com.com.x.AppModel.YuangongModel;
import com.example.x.xcard.ApplicationClass;
import com.example.x.xcard.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.x.custom.XActivityindicator;
import com.x.custom.XHorizontalBaseFragment;
import com.x.custom.XNetUtil;
import com.x.custom.XNotificationCenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.x.xcard.ApplicationClass.APPService;

/**
 * Created by X on 16/9/3.
 */
public class GWManageRight extends XHorizontalBaseFragment
{
    private PullToRefreshListView list;
    private GWManageYGAdapter adapter;

    public List<GangweiModel> dataArr = new ArrayList<>();
    private int page = 1;
    private boolean end = false;
    String sid = ApplicationClass.APPDataCache.User.getShopid();

    private Context context;

    public int selectIndex = 0;

    public void refresh()
    {
        adapter.notifyDataSetChanged();
    }

    public void addNew(String name)
    {
        XNetUtil.Handle(APPService.powerAddShopJob(sid, name), "岗位添加成功", "岗位添加失败", new XNetUtil.OnHttpResult<Boolean>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(Boolean aBoolean) {

                if(aBoolean)
                {
                    page = 1;
                    end = false;
                    getData();

                }

            }
        });
    }

    public void update(final String name)
    {

        String id = dataArr.get(selectIndex).getId();
        XNetUtil.Handle(APPService.powerUpdateShopJob(id, name), "岗位修改成功", "岗位修改失败", new XNetUtil.OnHttpResult<Boolean>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(Boolean aBoolean) {

                if(aBoolean)
                {
                    dataArr.get(selectIndex).setName(name);
                    adapter.notifyDataSetChanged();
                }

            }
        });
    }

    @Override
    protected void lazyLoad() {

        System.out.println("RightFragment--->lazyLoad !!!");

        // 获取MainListAdapter对象
        adapter = new GWManageYGAdapter();
        // 将MainListAdapter对象传递给ListView视图

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

    public void setMyListener(MyListener m)
    {
        myListener = m;
    }


    private void rightAlertShow()
    {
        AlertView rightAlert = new AlertView(null, null, null, null,
                new String[]{"修改岗位名称", "修改岗位权限", "删除岗位","取消"},
                context, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                System.out.println("点击了: "+position);
                if(myListener != null)
                {
                    myListener.showMessage(position);
                }
                if(position == 2)
                {
                    doDel(selectIndex);
                }
            }
        });

        XActivityindicator.setAlert(rightAlert);

        rightAlert.show();
    }

    private void doDel(int index)
    {
        String id = dataArr.get(index).getId();
        XNetUtil.Handle(APPService.powerDelShopJob(id), "岗位删除成功", "岗位删除失败", new XNetUtil.OnHttpResult<Boolean>() {

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
    public void onAttach(Context context) {
        if (context instanceof MyListener) {
            myListener = (MyListener) context;
        }

        this.context = context;
        System.out.println("RightFragment--->onAttach");


        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        XNotificationCenter.getInstance().removeObserver("GWPowerUpdateSuccess");
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        System.out.println("RightFragment--->onCreate");

        XNotificationCenter.getInstance().addObserver("GWPowerUpdateSuccess", new XNotificationCenter.OnNoticeListener() {
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
        System.out.println("RightFragment--->onCreateView");
        View v = inflater.inflate(R.layout.yg_manage_gw, container, false);
        list = (PullToRefreshListView) v.findViewById(R.id.yg_manage_gw_list);

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

                selectIndex = i-1;
                rightAlertShow();

            }
        });

        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        System.out.println("RightFragment--->onResume");
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

        XNetUtil.Handle(APPService.powerGetShopJob(sid, page + "", "20"), new XNetUtil.OnHttpResult<List<GangweiModel>>() {
            @Override
            public void onError(Throwable e) {

                XNetUtil.APPPrintln(e);
            }

            @Override
            public void onSuccess(List<GangweiModel> models) {

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
     class GWManageYGAdapter extends BaseAdapter {

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
                        R.layout.yg_manage_gw_cell, null);

                // 实例化一个封装类ListItemView，并实例化它的两个域
                listItemView = new ListItemView();
                listItemView.title = (TextView) convertView
                        .findViewById(R.id.yg_manage_gw_cell_title);

                // 将ListItemView对象传递给convertView
                convertView.setTag(listItemView);
            } else {
                // 从converView中获取ListItemView对象
                listItemView = (ListItemView) convertView.getTag();
            }

            // 获取到mList中指定索引位置的资源
            String title = (String) dataArr.get(position).getName();

            listItemView.title.setText(title);

            // 返回convertView对象
            return convertView;
        }

    }

    /**
     * 封装两个视图组件的类
     */
    class ListItemView {
        TextView title;
    }

}
