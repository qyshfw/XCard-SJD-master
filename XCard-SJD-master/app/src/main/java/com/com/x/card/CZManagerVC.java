package com.com.x.card;

import android.view.View;
import android.widget.TextView;

import com.com.x.AppModel.ValueSumModel;
import com.example.x.xcard.ApplicationClass;
import com.example.x.xcard.BaseActivity;
import com.example.x.xcard.R;
import com.x.custom.XNetUtil;
import com.x.custom.XNotificationCenter;

import static com.example.x.xcard.ApplicationClass.APPService;

/**
 * Created by X on 16/9/6.
 */
public class CZManagerVC extends BaseActivity  {

    private TextView dtatle;
    private TextView dnum;
    private TextView num7;
    private TextView num30;

    private TextView jidu;
    private TextView year;

    private TextView numall;

    String sid = ApplicationClass.APPDataCache.User.getShopid();

    @Override
    protected void setupUi() {
        setContentView(R.layout.cz_manage);
        setPageTitle("充值管理");
        //消费总额
        dtatle=(TextView)findViewById(R.id.cz_manage_tatle);
        //总额下的消费次数
        dnum=(TextView)findViewById(R.id.cz_manage_daynum);
        //本周充值
        num7=(TextView)findViewById(R.id.cz_manage_7num);
        //本月充值
        num30=(TextView)findViewById(R.id.cz_manage_30num);
        //充值总计
        numall=(TextView)findViewById(R.id.cz_manage_allnum);
        //季度充值
        jidu=(TextView)findViewById(R.id.cz_manage_numjidu);
        //年度充值
        year=(TextView)findViewById(R.id.cz_manage_numyear);
        //获取数据
        getData();

        XNotificationCenter.getInstance().addObserver("MondetailDelSuccess", new XNotificationCenter.OnNoticeListener() {

            @Override
            public void OnNotice(Object obj) {
                getData();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XNotificationCenter.getInstance().removeObserver("MondetailDelSuccess");

    }

    private void getData()
    {
        //获取商家充值统计
        XNetUtil.Handle(APPService.shoptGetValueSum(sid), new XNetUtil.OnHttpResult<ValueSumModel>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(ValueSumModel valueSumModel) {

                if(valueSumModel != null)
                {
                    //设置信息，营业额综合，
                    dtatle.setText("￥"+valueSumModel.getDay());
                    dnum.setText("今日充值次数: "+valueSumModel.getDaycnum()+"次");
                    num7.setText("￥"+valueSumModel.getWeek());
                    num30.setText("￥"+valueSumModel.getMonth());
                    numall.setText("￥"+valueSumModel.getAll());
                    jidu.setText("￥"+valueSumModel.getJidu());
                    year.setText("￥"+valueSumModel.getYear());
                }

            }
        });
    }

    @Override
    protected void setupData() {

    }

    public void toCZDetail(View v)
    {
        pushVC(CZDetailVC.class);
    }

}
