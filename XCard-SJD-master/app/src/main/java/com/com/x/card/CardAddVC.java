package com.com.x.card;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.com.x.AppModel.CardTypeModel;
import com.example.x.xcard.ApplicationClass;
import com.example.x.xcard.BaseActivity;
import com.example.x.xcard.R;
import com.x.custom.XActivityindicator;
import com.x.custom.XHorizontalMenu;
import com.x.custom.XNetUtil;
import com.x.custom.XNotificationCenter;

import static com.example.x.xcard.ApplicationClass.APPService;

/**
 * Created by X on 16/9/8.
 */
public class CardAddVC extends BaseActivity {

    private RadioGroup radioGroup;
    private RecyclerView colorList;
    private int selectRadioId;
    private int selectColorRow = 0;
    //用户缓存
    String sid = ApplicationClass.APPDataCache.User.getShopid();
    private EditText mark;

    private CardTypeModel model;

    private String[] colors = {"e49100","446ab4","f2666b","e6bd2c","19ad83"
            ,"1d1e20","c322ec"
    };

    XNetUtil.OnHttpResult<Boolean> result = new XNetUtil.OnHttpResult<Boolean>() {

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onSuccess(Boolean aBoolean) {

            if(aBoolean)
            {
                XNotificationCenter.getInstance().postNotice("CardListChagendSuccess",null);
                XActivityindicator.getHud().setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(SVProgressHUD hud) {
                        doPop();
                    }
                });
            }
        }
    };


private CardAddColorListAdapter adapter;

    @Override
    protected void setupUi() {
        setContentView(R.layout.card_add);



        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null && bundle.containsKey("model"))
        {
            model = (CardTypeModel) bundle.getSerializable("model");
        }

        selectRadioId = R.id.card_add_radio0;
        radioGroup = (RadioGroup)findViewById(R.id.card_add_radioGroup);
        colorList = (RecyclerView)findViewById(R.id.card_add_colorlist);
        mark = (EditText) findViewById(R.id.card_add_info);

        if(model != null)
        {
            setPageTitle("编辑卡片");
            radioGroup.setEnabled(false);

            switch (model.getType())
            {
                case "充值卡":
                    radioGroup.check(R.id.card_add_radio0);
                    break;
                case "计次卡":
                    radioGroup.check(R.id.card_add_radio1);
                    break;
                case "打折卡":
                    radioGroup.check(R.id.card_add_radio2);
                    break;
                case "积分卡":
                    radioGroup.check(R.id.card_add_radio3);
                    break;

                default:
                    break;
            }

            for(int i=0;i<colors.length;i++)
            {
                String c = colors[i];

                if(c.equals(model.getColor()))
                {
                    selectColorRow = i;
                    break;
                }

            }


        }
        else
        {
            setPageTitle("添加卡片");
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                selectRadioId = radioGroup.getCheckedRadioButtonId();
            }
        });

        //设置布局管理器
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this,2);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        colorList.setLayoutManager(linearLayoutManager);

        adapter = new CardAddColorListAdapter(mContext);

        adapter.setOnItemClickLitener(new XHorizontalMenu.OnItemClickLitener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                selectColorRow = position;
                adapter.notifyDataSetChanged();
            }
        });

        colorList.setAdapter(adapter);

    }

    @Override
    protected void setupData() {

    }

    public void submit(View v)
    {
//        充值卡: 2
//        计次卡: 1
//        打折卡: 3
//        积分卡: 4

        String color = colors[selectColorRow];
        String info = mark.getText().toString().trim();

        if(model == null)
        {
            String typeid = "";
            switch (selectRadioId)
            {
                case R.id.card_add_radio0:
                    typeid = "2";
                    break;
                case R.id.card_add_radio1:
                    typeid = "1";
                    break;
                case R.id.card_add_radio2:
                    typeid = "3";
                    break;
                case R.id.card_add_radio3:
                    typeid = "4";
                    break;

                default:
                    break;
            }

            //添加会员卡的接口
            XNetUtil.Handle(APPService.shopdAddShopCard(sid, color, info, typeid), "会员卡添加成功", "会员卡添加失败", result);
        }
        else
        {
            XNetUtil.Handle(APPService.shopdUpdateShopCard(model.getId(), color, info), "会员卡修改成功", "会员卡修改失败", result);
        }

    }


    /**
     * 定义ListView适配器MainListViewAdapter
     */
    private class CardAddColorListAdapter extends RecyclerView.Adapter {

        private XHorizontalMenu.OnItemClickLitener mOnItemClickLitener;

        public void setOnItemClickLitener(XHorizontalMenu.OnItemClickLitener mOnItemClickLitener)
        {
            this.mOnItemClickLitener = mOnItemClickLitener;
        }

        private  class ViewHolder extends RecyclerView.ViewHolder
        {
            public ViewHolder(View arg0)
            {
                super(arg0);
            }
            ImageView bg;
            ImageView color;
        }

        private LayoutInflater mInflater;

        public CardAddColorListAdapter(Context context)
        {
            mInflater = LayoutInflater.from(context);
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = mInflater.inflate(R.layout.card_add_color_cell,
                    parent, false);
            ViewHolder viewHolder = new ViewHolder(view);

            viewHolder.bg = (ImageView) view
                    .findViewById(R.id.card_add_color_cell_bg);
            viewHolder.color = (ImageView) view
                    .findViewById(R.id.card_add_color_cell_img);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder,final int i) {

            ViewHolder viewHolder = ((ViewHolder)holder);

            if(i == selectColorRow)
            {
                viewHolder.bg.setBackgroundResource(R.drawable.card_add_color_shape_selected);
            }
            else
            {
                viewHolder.bg.setBackgroundResource(R.drawable.card_add_color_shape_normal);
            }

            int c = Color.parseColor("#"+colors[i]);
            viewHolder.color.setBackgroundColor(c);
            //System.out.println("item width: "+holder.itemView.getWidth());

            //如果设置了回调，则设置点击事件
            if (mOnItemClickLitener != null)
            {
                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        mOnItemClickLitener.onItemClick(holder.itemView, i);
                    }
                });

            }


        }

        /**
         * 返回item的id
         */
        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public int getItemCount() {
            return colors.length;
        }

    }

}


