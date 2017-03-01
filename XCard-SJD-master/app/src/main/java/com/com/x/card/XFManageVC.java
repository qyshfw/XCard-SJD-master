package com.com.x.card;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.com.x.AppModel.ValueSumModel;
import com.example.x.xcard.ApplicationClass;
import com.example.x.xcard.BaseActivity;
import com.example.x.xcard.R;
import com.x.custom.DensityUtil;
import com.x.custom.XNetUtil;
import com.x.custom.XNotificationCenter;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.LineChartView;

import static com.example.x.xcard.ApplicationClass.APPService;

/**
 * Created by X on 16/9/6.
 */
public class XFManageVC extends BaseActivity {

    private TextView dtatle;
    private TextView num7;
    private TextView num30;
    private TextView numall;
    private TextView jidu;
    private TextView year;
    String sid = ApplicationClass.APPDataCache.User.getShopid();

    @Override
    protected void setupUi() {
        setContentView(R.layout.xf_manage);
        setPageTitle("消费管理");

        dtatle=(TextView)findViewById(R.id.xf_manage_tatle);
        num7=(TextView)findViewById(R.id.xf_manage_num7);
        num30=(TextView)findViewById(R.id.xf_manage_num30);
        numall=(TextView)findViewById(R.id.xf_manage_numall);

        jidu=(TextView)findViewById(R.id.xf_manage_numjidu);
        year=(TextView)findViewById(R.id.xf_manage_numyear);

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
        XNetUtil.Handle(APPService.shoptGetCostSum(sid,"0"), new XNetUtil.OnHttpResult<ValueSumModel>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(ValueSumModel valueSumModel) {

                if(valueSumModel != null)
                {
                    dtatle.setText(valueSumModel.getDaycnum()+"次");
                    num7.setText(valueSumModel.getWeek()+"次");
                    num30.setText(valueSumModel.getMonth()+"次");
                    numall.setText(valueSumModel.getAll()+"次");

                    jidu.setText(valueSumModel.getJidu()+"次");
                    year.setText(valueSumModel.getYear()+"次");
                }

            }
        });
    }

    @Override
    protected void setupData() {

    }

    public void toXFDetail(View v)
    {
        pushVC(XFDetailVC.class);
    }

}
