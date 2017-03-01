package com.example.x.xcard;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.com.x.AppModel.BannerModel;
import com.com.x.card.CZMainVC;
import com.com.x.card.CZManagerVC;
import com.com.x.card.CardManageVC;
import com.com.x.card.MakeCardVC;
import com.com.x.card.XFManageVC;
import com.com.x.huiyuan.HYManageVC;
import com.com.x.huodong.HDManageVC;
import com.com.x.user.APPConfig;
import com.com.x.user.LoginVC;
import com.com.x.user.ShopSetupVC;
import com.com.x.user.SystemMsg;
import com.com.x.xiaoxi.MSGManageVC;
import com.com.x.yuangong.YGManageMainVC;
import com.x.custom.DensityUtil;
import com.x.custom.XActivityindicator;
import com.x.custom.XGridView;
import com.x.custom.XNetUtil;
import com.x.custom.XNotificationCenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.x.xcard.ApplicationClass.APPDataCache;
import static com.example.x.xcard.ApplicationClass.APPService;
//继承于基类
public class MainActivity extends BaseActivity {

    private XGridView gview;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;

    List<BannerModel> bannerArr = new ArrayList<BannerModel>();

    private ConvenientBanner convenientBanner;//顶部广告栏控件
    private List<String> networkImages = new ArrayList<String>();

    // 图片封装为一个数组
    private int[] icon = {R.drawable.index_icon01, R.drawable.index_icon02,
            R.drawable.index_icon03, R.drawable.index_icon04, R.drawable.index_icon05,
            R.drawable.index_icon06, R.drawable.index_icon07, R.drawable.index_icon08,
            R.drawable.index_icon10, R.drawable.index_icon09, R.drawable.index_icon11, R.color.white};
    private String[] iconName = {"会员管理", "充值管理", "消费管理", "活动管理", "消息管理", "卡类管理", "店铺设置",
            "员工管理", "系统公告", "设置", "更多", ""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //WindowManager.LayoutParams 是 WindowManager 接口的嵌套类；继承于 ViewGroup.LayoutParams
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        getWindow().setAttributes(params);
        setContentView(R.layout.activity_main);
        doHideBackBtn();
        //setRightImg(R.drawable.user_head);
        gview = (XGridView) findViewById(R.id.homt_gview);
        //不可滑动
        gview.setScrollEnable(false);
         //设置点击事件。
        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                /**
                 * 前8个item，每点击一次都判断一次是否登录。
                 *   if(i<8)
                 {
                 //检查是否登录
                 if(!checkIsLogin())
                 {
                 return;
                 }
                 else
                 {
                 //判断权限
                 if(!CheckUserPower(i+4+"")){return;}
                 }
                 }
                 */

               // 各个item 的跳转
                switch (i) {
                    case 0://会员管理
                        pushVC(HYManageVC.class);
                        break;
                    case 1://充值管理
                        pushVC(CZManagerVC.class);
                        break;
                    case 2://消费管理
                        pushVC(XFManageVC.class);
                        break;
                    case 3://活动管理
                        pushVC(HDManageVC.class);
                        break;
                    case 4://消息管理
                        pushVC(MSGManageVC.class);
                        break;
                    case 5://卡类管理
                        pushVC(CardManageVC.class);
                        break;
                    case 6://店铺设置
                        pushVC(ShopSetupVC.class);
                        break;
                    case 7://员工管理
                        pushVC(YGManageMainVC.class);
                        break;
                    case 8://系统公告
                        pushVC(SystemMsg.class);
                        break;

                    case 9://设置
                        pushVC(APPConfig.class);

                        break;

                    default:
                        break;
                }

            }
        });


        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
        getData();
        //新建适配器
        String[] from = {"image", "text"};
        int[] to = {R.id.image, R.id.text};
        sim_adapter = new SimpleAdapter(this, data_list, R.layout.home_item_cell, from, to);
        //配置适配器
        gview.setAdapter(sim_adapter);
        //顶部循环开源库
        convenientBanner = (ConvenientBanner) findViewById(R.id.convenientBanner);
        convenientBanner.setPageIndicator(new int[]{R.drawable.banner_dot_default, R.drawable.banner_dot_selected})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);


        ViewGroup.LayoutParams layoutParams = convenientBanner.getLayoutParams();

        int w = ApplicationClass.SW - DensityUtil.dip2px(mContext,28);
        int h = (int)(w * 7 / 16.0);

        layoutParams.height = h;
        convenientBanner.setLayoutParams(layoutParams);


        getBanner();
        //滑动监听。
        convenientBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                System.out.println("!!! Banner选择position: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        convenientBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                System.out.println("~~~ Banner点击position: " + position);
            }
        });
        //获得实例
        XNotificationCenter.getInstance().addObserver("ShowAccountLogout", new XNotificationCenter.OnNoticeListener() {
            @Override
            public void OnNotice(Object obj) {
                showAccountLogout();
            }
        });

    }

    private void showAccountLogout()
    {
        AlertView Alert = new AlertView("提醒", "您的账户已在其他设备登录", null, null,
                new String[]{"确定"},
                mContext, AlertView.Style.Alert, new com.bigkoo.alertview.OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0) {

                }
            }
        });

        XActivityindicator.setAlert(Alert);
        Alert.show();
        //数据缓存
        APPDataCache.User.unRegistNotice();
        APPDataCache.User.reSet();
    }

    private void getBanner() {

        XNetUtil.Handle(APPService.getBanner(), new XNetUtil.OnHttpResult<List<BannerModel>>() {
            @Override
            public void onError(Throwable e) {

                XNetUtil.APPPrintln(e);

            }

            @Override
            public void onSuccess(List<BannerModel> bannerModels) {
                //广告位的轮播
                bannerArr = bannerModels;
                for(BannerModel model:bannerModels)
                {
                    System.out.println(model.toString());
                    networkImages.add(model.getPicurl());
                }
                convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
                    @Override
                    public NetworkImageHolderView createHolder() {
                        return new NetworkImageHolderView();
                    }
                }, networkImages);


            }
        });




    }

    // 开始自动翻页
    @Override
    protected void onResume() {
        super.onResume();
        //开始自动翻页
        //convenientBanner.startTurning(5000);
          //没有登录的时候显示
        if(APPDataCache.User.getUid().equals(""))
        {
            setPageTitle("怀府网");
        }
        else
        //有数据缓存的时候，取得店铺名。
        {
            setPageTitle(APPDataCache.User.getShopname());

        }
         //数据请求
        APPDataCache.User.requestPower();
    }

    // 停止自动翻页
    @Override
    protected void onPause() {
        super.onPause();
        //停止翻页
        convenientBanner.stopTurning();
    }

    public List<Map<String, Object>> getData() {
        //cion和iconName的长度是相同的，这里任选其一都可以
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            data_list.add(map);
        }

        return data_list;
    }

    @Override
    public void rightClick(View v) {
        System.out.println("点击右侧菜单~~~~~~~~");
        presentVC(LoginVC.class);
    }

    public void toBanKa(View v) {
        //进入办卡界面
        //判断登录情况以及用户权限。
        if(!checkIsLogin() || !CheckUserPower("1"))
        {
            return;
        }
        //传递键值对，
        Bundle bundle = new Bundle();
        bundle.putString("title", "办卡");
        pushVC(MakeCardVC.class, bundle);

    }

    public void toXiaoFei(View v) {
        //进入消费界面
        if(!checkIsLogin() || !CheckUserPower("2"))
        {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("title", "消费");
        pushVC(CZMainVC.class, bundle);
    }

    public void toChongZhi(View v) {
//充值界面
        if(!checkIsLogin() || !CheckUserPower("3"))
        {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("title", "充值");

        pushVC(CZMainVC.class, bundle);
    }


    public void btnClick(View v) {
        pushVC(TestVC.class);
    }

    @Override
    protected void setupData() {

    }

    @Override
    protected void setupUi() {

    }

    @Override
    public void onCreateCustomToolBar(Toolbar toolbar) {
        super.onCreateCustomToolBar(toolbar);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
