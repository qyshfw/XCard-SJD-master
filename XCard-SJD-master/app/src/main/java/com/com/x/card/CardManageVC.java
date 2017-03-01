package com.com.x.card;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.com.x.AppModel.CardTypeModel;
import com.com.x.AppModel.YuangongModel;
import com.com.x.yuangong.YGManageLeft;
import com.example.x.xcard.ApplicationClass;
import com.example.x.xcard.BaseActivity;
import com.example.x.xcard.R;
import com.x.custom.DensityUtil;
import com.x.custom.XNetUtil;
import com.x.custom.XNotificationCenter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.example.x.xcard.ApplicationClass.APPService;

/**
 * Created by X on 16/9/8.
 */
public class CardManageVC extends BaseActivity {

    private ListView list;

    private CardManageAdapter adapter;
    private List<CardTypeModel> dataArr = new ArrayList<>();
    String sid = ApplicationClass.APPDataCache.User.getShopid();
    String uid = ApplicationClass.APPDataCache.User.getUid();
    private AlertView alert;
    @Override
    protected void setupUi() {
        setContentView(R.layout.card_manage);
        setPageTitle("卡管理");
        setRightImg(R.drawable.add);
        int p = DensityUtil.dip2px(mContext,7);
        setRightImgPadding(p,p,p,p);

        list = (ListView)findViewById(R.id.card_manage_list);
        adapter = new CardManageAdapter();

        list.setAdapter(adapter);

        getData();

        XNotificationCenter.getInstance().addObserver("CardListChagendSuccess", new XNotificationCenter.OnNoticeListener() {

            @Override
            public void OnNotice(Object obj) {
                getData();
            }
        });



        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                doChoose(position);

            }
        });



    }

    @Override
    protected void setupData() {

    }


    private void getData() {


        XNetUtil.Handle(APPService.hykGetShopCard(sid, uid), new XNetUtil.OnHttpResult<List<CardTypeModel>>() {

            @Override
            public void onError(Throwable e) {
                XNetUtil.APPPrintln(e);
            }

            @Override
            public void onSuccess(List<CardTypeModel> cardTypeModels) {
                if(cardTypeModels.size()>0)
                {
                    dataArr = cardTypeModels;
                    adapter.notifyDataSetChanged();
                }
            }
        });


    }


    @Override
    public void rightClick(View v) {
        super.rightClick(v);

        pushVC(CardAddVC.class);
    }

    public void doChoose(final int p)
    {
        alert = new AlertView(null, null, null, null,
                new String[]{"作废","编辑","取消"},
                mContext, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                System.out.println("点击了: "+position);

                if(position == 0)
                {

                    deleCard(p);
                }
                else if(position == 1)
                {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("model",dataArr.get(p));
                    pushVC(CardAddVC.class,bundle);
                }

            }
        });

        alert.show();

    }

    private void deleCard(final int p)
    {
        alert.dismissImmediately();
        alert = null;

        XNetUtil.Handle(APPService.shopdDelShopCard(dataArr.get(p).getId()), "会员卡作废成功", "会员卡作废失败", new XNetUtil.OnHttpResult<Boolean>() {

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                if(aBoolean)
                {
                    getData();
                }
            }
        });

    }


    private void hiddenViewByTag(ViewGroup v,String tag)
    {
        for(int i=0;i<v.getChildCount();i++)
        {
            View temp = v.getChildAt(i);

            if(temp.getTag() instanceof  String)
            {
                if(temp.getTag().toString().equals(tag))
                {
                    temp.setVisibility(View.GONE);
                    System.out.println("tag is 充值卡： "+temp);
                }
            }

            if (temp instanceof ViewGroup)
            {
                hiddenViewByTag((ViewGroup)temp,tag);
            }


        }
    }



    /**
     * 定义ListView适配器MainListViewAdapter
     */
    class CardManageAdapter extends BaseAdapter {

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
            CardManageVC.ListItemView listItemView;

            // 初始化item view
            if (convertView == null) {
                // 通过LayoutInflater将xml中定义的视图实例化到一个View中
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.card_manage_cell, null);

                // 实例化一个封装类ListItemView，并实例化它的两个域
                listItemView = new CardManageVC.ListItemView();
                listItemView.img = (ImageView) convertView
                        .findViewById(R.id.card_manage_cell_img);
                listItemView.name = (TextView) convertView
                        .findViewById(R.id.card_manage_cell_name);
                listItemView.info = (TextView) convertView
                        .findViewById(R.id.card_manage_cell_info);

                // 将ListItemView对象传递给convertView
                convertView.setTag(listItemView);
            } else {
                // 从converView中获取ListItemView对象
                listItemView = (CardManageVC.ListItemView) convertView.getTag();
            }

            // 获取到mList中指定索引位置的资源
            //int img = (int) dataArr.get(position).
            String name = (String) dataArr.get(position).getType();
            String info = (String) dataArr.get(position).getInfo();
            String color = (String) dataArr.get(position).getColor();


            // 将资源传递给ListItemView的两个域对象
            //listItemView.header.setImageResource(img);
            //listItemView.imageView.setImageDrawable(img);
            listItemView.name.setText(name);
            listItemView.info.setText(info);
            listItemView.img.setBackgroundColor(Color.parseColor("#"+color));

            // 返回convertView对象
            return convertView;
        }

    }

    /**
     * 封装两个视图组件的类
     */
    class ListItemView {
        ImageView img;
        TextView name;
        TextView info;
    }

}





