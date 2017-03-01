package com.com.x.card;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.com.x.AppModel.CardTypeModel;
import com.com.x.AppModel.UserModel;
import com.example.x.xcard.ApplicationClass;
import com.example.x.xcard.BaseActivity;
import com.example.x.xcard.R;
import com.x.custom.XActivityindicator;
import com.x.custom.XNetUtil;
import com.x.custom.XNotificationCenter;

import java.util.ArrayList;
import java.util.List;

import static com.example.x.xcard.ApplicationClass.APPService;

/**
 * Created by X on 16/9/4.
 */
public class ChooseCardTypeVC extends BaseActivity {

    private ListView list;
    //卡类的适配器
    private CardTypeAdapter adapter = new CardTypeAdapter();
    private List<CardTypeModel> dataArr = new ArrayList<>();
    //获取APP数据的缓存，该商铺的代号
    String sid = ApplicationClass.APPDataCache.User.getShopid();
    //获取用户的ID；
    String uid = ApplicationClass.APPDataCache.User.getUid();
    //弹出提示框
    private AlertView alert;
    //用户的相关变量。
    private UserModel user;
    private List<CardTypeModel> userCards;
    private List<String> idArr = new ArrayList<>();
    @Override
    //刷新UI
    protected void setupUi() {
        setContentView(R.layout.card_choosetype);
        setPageTitle("卡选择");
        //获取bundle中的数据
        Bundle bundle = this.getIntent().getExtras();
        //判断bundle中的数据
        if (bundle != null && bundle.containsKey("model"))
        {
            //序列化，获取其中的数据
            user = (UserModel)bundle.getSerializable("model");
        }
           //网络访问后将设置数据参数，新创建的一个CardTypeModel,
        XNetUtil.Handle(APPService.hykGetShopCardY(sid, user.getUid()), new XNetUtil.OnHttpResult<List<CardTypeModel>>() {
            @Override
            public void onError(Throwable e) {

            }
            //卡的相关信息判断。
            @Override
            public void onSuccess(List<CardTypeModel> cardTypeModels) {

                if(cardTypeModels.size() > 0)
                {
                    for(CardTypeModel m:cardTypeModels)
                    {
                        //卡的ID加入Arr数组
                        idArr.add(m.getCardid());
                    }
                    //实例化
                    userCards = cardTypeModels;
                    //适配器通知数据改变
                    adapter.notifyDataSetChanged();
                }

            }
        });
         //实例化
        list = (ListView)findViewById(R.id.card_type_list);
         //设置适配器
        list.setAdapter(adapter);
        //成功添加信息。
        getData();
        XNotificationCenter.getInstance().addObserver("CardListChagendSuccess", new XNotificationCenter.OnNoticeListener() {

            @Override
            public void OnNotice(Object obj) {
                getData();
            }
        });


         //子项的点击事件监听
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                openCard(position);

            }
        });


    }

    @Override
    protected void setupData() {

    }

    public void openCard(final int p)
    {
        //卡片信息的实例化
        final CardTypeModel m = dataArr.get(p);
        if(idArr.contains(m.getId()))
        {
            return;
        }
        //提示框
        alert = new AlertView("确定要开通会员卡?", null, null, null,
                new String[]{"取消","确定"},
                mContext, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if(position == 1)
                {
                     //网络访问后获得用户的信息
                        XNetUtil.Handle(APPService.hykAddCard(user.getUid(), user.getUsername(), m.getId()), "领取会员卡成功", "领取会员卡失败", new XNetUtil.OnHttpResult<Boolean>() {
                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onSuccess(Boolean aBoolean) {

                            if(aBoolean)
                            {
                               //获得卡的id
                                idArr.add(m.getId());
                                adapter.notifyDataSetChanged();

                            }
                        }
                    });
                }

            }
        });
        alert.show();

        XActivityindicator.setAlert(alert);


    }


    private void getData() {

        //获取商铺的相关信息。
        XNetUtil.Handle(APPService.hykGetShopCard(sid, uid), new XNetUtil.OnHttpResult<List<CardTypeModel>>() {

            @Override
            public void onError(Throwable e) {
                XNetUtil.APPPrintln(e);
            }
         //成功添加信息
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


    /**
     * 定义ListView适配器MainListViewAdapter
     */
    class CardTypeAdapter extends BaseAdapter {

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
            ChooseCardTypeVC.ListItemView listItemView;

            // 初始化item view
            if (convertView == null) {
                // 通过LayoutInflater将xml中定义的视图实例化到一个View中
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.card_manage_cell, null);

                // 实例化一个封装类ListItemView，并实例化它的两个域
                listItemView = new ChooseCardTypeVC.ListItemView();
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
                listItemView = (ChooseCardTypeVC.ListItemView) convertView.getTag();
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
            listItemView.info.setTextColor(Color.parseColor("#ff7e00"));

            if(idArr.contains(dataArr.get(position).getId()))
            {
                listItemView.info.setText("已领取");
            }
            else
            {
                listItemView.info.setText("");
            }

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