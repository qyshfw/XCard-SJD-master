package com.com.x.huiyuan;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.com.x.AppModel.CardTypeModel;
import com.com.x.AppModel.UserModel;
import com.com.x.card.CZManagerVC;
import com.com.x.card.CardManageVC;
import com.com.x.card.XFManageVC;
import com.example.x.xcard.ApplicationClass;
import com.example.x.xcard.BaseActivity;
import com.example.x.xcard.R;
import com.x.custom.DensityUtil;
import com.x.custom.XNetUtil;

import java.util.ArrayList;
import java.util.List;

import static com.example.x.xcard.ApplicationClass.APPService;

/**
 * Created by X on 16/9/6.
 */
public class HYUserInfoVC extends BaseActivity {

    private TextView name;
    private TextView tel;

    private ListView list;
    private CardManageAdapter adapter;
    private List<CardTypeModel> dataArr = new ArrayList<>();
    String sid = ApplicationClass.APPDataCache.User.getShopid();

    UserModel model;

    @Override
    protected void setupUi() {
        setContentView(R.layout.hy_userinfo);
        setPageTitle("会员详情");

        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null && bundle.containsKey("model"))
        {
            model = (UserModel) bundle.getSerializable("model");
        }

        name = (TextView)findViewById(R.id.hy_userinfo_name);
        tel = (TextView)findViewById(R.id.hy_userinfo_tel);

        name.setText(model.getTruename());
        tel.setText(model.getMobile());

        list = (ListView)findViewById(R.id.hy_userinfo_list);
        adapter = new CardManageAdapter();

        list.setAdapter(adapter);

        getData();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            toInfo(position);

            }
        });

    }

    private void toInfo(int index)
    {
        CardTypeModel cm = dataArr.get(index);

        Bundle bundle = new Bundle();
        bundle.putString("uid",model.getUid());
        bundle.putString("cid",cm.getMcardid());

        pushVC(HYRecardMainVC.class,bundle);
    }

    private void getData() {


        XNetUtil.Handle(APPService.hykGetShopCardY(sid, model.getUid()), new XNetUtil.OnHttpResult<List<CardTypeModel>>() {

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
    protected void setupData() {

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
            HYUserInfoVC.ListItemView listItemView;

            // 初始化item view
            if (convertView == null) {
                // 通过LayoutInflater将xml中定义的视图实例化到一个View中
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.card_manage_cell, null);

                // 实例化一个封装类ListItemView，并实例化它的两个域
                listItemView = new HYUserInfoVC.ListItemView();
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
                listItemView = (HYUserInfoVC.ListItemView) convertView.getTag();
            }

            // 获取到mList中指定索引位置的资源
            //int img = (int) dataArr.get(position).
            String name = (String) dataArr.get(position).getType();
            String info = (String) dataArr.get(position).getMcardid();
            String color = (String) dataArr.get(position).getColor();


            // 将资源传递给ListItemView的两个域对象
            //listItemView.header.setImageResource(img);
            //listItemView.imageView.setImageDrawable(img);
            listItemView.name.setText(name);
            listItemView.info.setText("NO."+info);
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
