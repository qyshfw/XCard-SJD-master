package com.com.x.yuangong;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.com.x.AppModel.GangweiModel;
import com.com.x.AppModel.PowerModel;
import com.example.x.xcard.ApplicationClass;
import com.example.x.xcard.BaseActivity;
import com.example.x.xcard.R;
import com.x.custom.XNetUtil;
import com.x.custom.XNotificationCenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.x.xcard.ApplicationClass.APPService;

/**
 * Created by X on 16/9/3.
 */
public class PowerManageVC extends BaseActivity {

    String sid = ApplicationClass.APPDataCache.User.getShopid();
    private GangweiModel gwModel = new GangweiModel();

    private ListView list;
    private PowerManageAdapter adapter;
    private List<PowerModel> dataArr = new ArrayList<>();

    private List<String> powarr = new ArrayList<>();

    public GangweiModel getGwModel() {
        return gwModel;
    }

    public void setGwModel(GangweiModel gwModel) {
        this.gwModel = gwModel;

        XNetUtil.APPPrintln(gwModel.toString());
        XNetUtil.APPPrintln(gwModel.getPower());

        powarr = Arrays.asList(gwModel.getPower().split(","));

    }

    @Override
    protected void setupUi() {

        setContentView(R.layout.power_manage);
        setPageTitle("权限管理");

        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null && bundle.containsKey("model"))
        {
            setGwModel((GangweiModel) bundle.getSerializable("model"));
        }
        getData();

        list = (ListView) findViewById(R.id.power_manage_list);
        // 获取MainListAdapter对象
        adapter = new PowerManageAdapter();
        // 将MainListAdapter对象传递给ListView视图
        list.setAdapter(adapter);
    }

    @Override
    protected void setupData() {

    }

    public void btnClick(View v)
    {
        if(gwModel.getId() == null || dataArr == null || dataArr.size() == 0)
        {
            return;
        }
        String power = "";
        for(PowerModel m : dataArr)
        {
            if(m.getChecked())
            {
                if(power.equals(""))
                {
                    power += m.getId();
                }
                else
                {
                    power += ","+m.getId();
                }
            }
        }

        XNetUtil.Handle(APPService.powerUpdateJobPower(sid, gwModel.getId(), power), "权限修改成功", "权限修改失败", new XNetUtil.OnHttpResult<Boolean>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(Boolean aBoolean) {

                if(aBoolean)
                {
                    XNotificationCenter.getInstance().postNotice("GWPowerUpdateSuccess",null);
                }

            }
        });


    }


    private void getData() {

        XNetUtil.Handle(APPService.settingGetShopPower(), new XNetUtil.OnHttpResult<List<PowerModel>>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(List<PowerModel> powerModels) {

                for(PowerModel m : powerModels)
                {
                    m.setChecked(powarr.contains(m.getId()));
                }

                dataArr = powerModels;
                adapter.notifyDataSetChanged();
            }
        });
    }


/**
 * 定义ListView适配器MainListViewAdapter
 */
class PowerManageAdapter extends BaseAdapter {

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ListItemView listItemView;

        // 初始化item view
        if (convertView == null) {
            // 通过LayoutInflater将xml中定义的视图实例化到一个View中
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.power_manage_cell, null);

            // 实例化一个封装类ListItemView，并实例化它的两个域
            listItemView = new ListItemView();
            listItemView.title = (TextView) convertView
                    .findViewById(R.id.power_manage_cell_title);
            listItemView.checkBox = (CheckBox) convertView
                    .findViewById(R.id.power_manage_cell_checkbox);

            // 将ListItemView对象传递给convertView
            convertView.setTag(listItemView);
        } else {
            // 从converView中获取ListItemView对象
            listItemView = (ListItemView) convertView.getTag();
        }

        // 获取到mList中指定索引位置的资源
        listItemView.checkBox.setTag(position);

        listItemView.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                System.out.println("compoundButton.tag: "+compoundButton.getTag()+" b: "+b);

                dataArr.get(position).setChecked(b);

                adapter.notifyDataSetChanged();
            }
        });



        String title = (String) dataArr.get(position).getName();
        boolean checked = (boolean)dataArr.get(position).getChecked();
        listItemView.title.setText(title);

        listItemView.checkBox.setChecked(checked);

        // 返回convertView对象
        return convertView;
    }

}

/**
 * 封装两个视图组件的类
 */
class ListItemView {
    TextView title;
    CheckBox checkBox;
}

}
