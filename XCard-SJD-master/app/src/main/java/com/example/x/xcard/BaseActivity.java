package com.example.x.xcard;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.com.x.user.LoginVC;
import com.x.custom.XNetUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.example.x.xcard.ApplicationClass.APPDataCache;

/**
 * 所有界面的基类
 *
 * @author wangtao 11-13
 *
 */
public abstract class BaseActivity extends AppCompatActivity implements
		Properties {

	protected Context mContext;
	/** 操作缓存 */
	protected static SaveCache mMaveCache;
    //标题栏
	private ToolBarHelper mToolBarHelper ;
	public Toolbar toolbar ;

	private TextView pageTitle;
	private ImageView backBtn;
	private ImageView rightImg;
	private TextView rightTxt;

	private boolean isPush = true;

	public View decorView;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		mContext = this;
		//初始化UI
		setupUi();
		//数据初始化
		setupData();
		//新页面接收数据
		Bundle bundle = this.getIntent().getExtras();
		//bundle非空，并且bundle里含有键值isPush
		if (bundle != null && bundle.containsKey("isPush"))
		{
			isPush = bundle.getBoolean("isPush");
		}

	}

	/**
	 * 初始化ui
	 */
	protected abstract void setupUi();

	/**
	 *
	 */
	protected abstract void setupData();

	/**
	 * 根据界面名称打印日志
	 *
	 * @param message
	 */
	protected void doLogMsg(String message) {
		if (IS_SHOW_DEBUG) {
			Log.i(getLocalClassName(), "" + message);
		}
	};

	/**
	 * 短消息提示
	 *
	 * @param msg
	 */
	public void doShowToast(String msg) {
		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 长消息提示
	 *
	 * @param msg
	 */
	public void doShowToastLong(String msg) {
		Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
	}

	/**
	 * 长消息提示
	 *
	 * @param msg
	 */
	public void doShowToastLong(int msg) {
		Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!isActive)
		{
			isActive = true;
			APPDataCache.User.checkToken();
			XNetUtil.APPPrintln("APP is BecomeActive!!!!!!");
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void doShowMesage(String msg, boolean isActivityRun) {
		// new AlertDialog.Builder(this).setMessage(msg).setTitle("温馨提示")
		// .setCancelable(true)
		// .setNegativeButton("确定", new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface arg0, int arg1) {
		// arg0.dismiss();
		// }
		// }).create().show();
		if (!isActivityRun) {
			return;
		}
		new AlertDialog.Builder(mContext).setMessage(msg).setTitle("温馨提示")
				.setNegativeButton("确定", null).show();
	}

	public void doShowMesage(String msg) {
		doShowMesage(msg, true);
	}

	public static void doShowMesage(Activity c, String msg) {
		new AlertDialog.Builder(c).setMessage(msg).setTitle("温馨提示")
				.setNegativeButton("确定", null).show();
	}
    //数据保存到文件中
	public static void doSaveDataToFile(Context context, Object obj, String key) {
		if (!IS_SHOW_DEBUG) {
			return;
		}
		if (obj == null) {
			return;
		}
		//SD卡数据
		SaveSdcardData save = new SaveSdcardData(context, "log");
		File dir = new File(save.getParentPath());
		if (!dir.exists()) {
			dir.mkdirs();
		}

		File file = new File(save.getParentPath() + "/" + key + ".txt");
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file);
			out.write(new String(obj + "").getBytes("utf-8"));
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 启动另外一个界面通过push动画
	 *
	 * @param activity
	 */
	public void pushVC(Class activity) {

		if(!CountTime.isBeyoundTime("启动界面", 300)){
			return;
		}

		Intent intentActive = new Intent(this, activity);

		//用Bundle携带数据
		Bundle bundle=new Bundle();
		//放入键值队
		bundle.putBoolean("isPush", true);
		intentActive.putExtras(bundle);

		startActivity(intentActive);
		//这个函数有两个参数，一个参数是第一个activity进入时的动画，另外一个参数则是第二个activity退出时的动画。
		overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
	}


	/**
	 * 启动另外一个界面通过push动画
	 *
	 * @param activity
	 * @param bundle
	 */
	public void pushVC(Class activity,Bundle bundle) {

		if(!CountTime.isBeyoundTime("启动界面", 300)){
			return;
		}

		Intent intentActive = new Intent(this, activity);
		//用Bundle携带数据
		bundle.putBoolean("isPush", true);
		intentActive.putExtras(bundle);
		startActivity(intentActive);
		overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
	}



	/**
	 * 启动另外一个界面通过present动画
	 *
	 * @param activity
	 */
	public void presentVC(Class activity) {

		if(!CountTime.isBeyoundTime("启动界面", 300)){
			return;
		}

		Intent intentActive = new Intent(this, activity);
		//用Bundle携带数据
		Bundle bundle=new Bundle();
		bundle.putBoolean("isPush", false);
		intentActive.putExtras(bundle);

		startActivity(intentActive);

		overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
	}

	/**
	 * 启动另外一个界面通过present动画
	 *
	 * @param activity
	 * @param bundle
	 */
	public void presentVC(Class activity,Bundle bundle) {

		if(!CountTime.isBeyoundTime("启动界面", 300)){
			return;
		}

		Intent intentActive = new Intent(this, activity);
		//用Bundle携带数据
		bundle.putBoolean("isPush", false);
		intentActive.putExtras(bundle);

		startActivity(intentActive);

		overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
	}

//popVC 为点击事件
	public void popVC(View v)

	{
		doPop();
	}

	public void doPop()
	{
		this.finish();
		if(isPush)
		{
			overridePendingTransition(R.anim.pop_left_out,R.anim.pop_left_in);
		}
		else
		{
			overridePendingTransition(R.anim.pop_up_out,R.anim.pop_up_in);
		}
	}

	public void rightClick(View v)
	{
		System.out.println("点击右侧菜单!!!!!!");
	}


	public void setFullScreen()
	{
		decorView.setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
	}

	@Override
	public void setContentView(int layoutResID) {

		decorView = getWindow().getDecorView();

		setFullScreen();

		decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
			@Override
			public void onSystemUiVisibilityChange(int i) {

				if(i == 0)
				{
					decorView.setSystemUiVisibility(
							View.SYSTEM_UI_FLAG_LAYOUT_STABLE
									| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
									| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
									| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

				}
				System.out.println("onSystemUiVisibilityChange: "+i);

			}
		});
        //应用程序窗口
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

		mToolBarHelper = new ToolBarHelper(this,layoutResID) ;
		toolbar = mToolBarHelper.getToolBar() ;
		setContentView(mToolBarHelper.getContentView());
        /*把 toolbar 设置到Activity 中*/
		setSupportActionBar(toolbar);
        /*自定义的一些操作*/
		onCreateCustomToolBar(toolbar) ;
		decorView.setSystemUiVisibility(0);
	}
//
	public void onCreateCustomToolBar(Toolbar toolbar){
		toolbar.setContentInsetsRelative(0,0);
		toolbar.showOverflowMenu() ;
		getLayoutInflater().inflate(R.layout.toobar_button, toolbar) ;
        //布局文件的实例化
		try {
			pageTitle = (TextView) toolbar.findViewById(R.id.APP_Nav_Title);
			backBtn = (ImageView) toolbar.findViewById(R.id.APP_Back_Btn);
			rightImg = (ImageView) toolbar.findViewById(R.id.APP_Right_Img);
			rightTxt = (TextView) toolbar.findViewById(R.id.APP_Right_Txt);
			rightImg.setVisibility(View.GONE);
			rightTxt.setVisibility(View.GONE);
		}
		catch (Exception e)
		{

		}


	}
//设置各个界面的标题栏？
	public void setPageTitle(String txt)
	{
		if(pageTitle != null)
		{
			pageTitle.setText(txt);
		}
	}
//设置图
	public void setRightImg(int id)
	{
		if(rightImg != null)
		{
			rightImg.setImageResource(id);
			rightImg.setVisibility(View.VISIBLE);
			rightTxt.setVisibility(View.GONE);
		}
	}
//设置控件内的间距
	public void setRightImgPadding(int left, int top, int right, int bottom)
	{
		if(rightImg != null)
		{
			rightImg.setPadding(left,top,right,bottom);
		}
	}

	public void setRightTxt(String txt)
	{
		if(rightTxt != null)
		{
			rightTxt.setText(txt);
			rightTxt.setVisibility(View.VISIBLE);
			rightImg.setVisibility(View.GONE);
		}

	}

//设置返回按钮是否可见
	public void doHideBackBtn()
	{
		if(backBtn != null)
		{
			/**
			 * View.VISIBLE--->可见
			 View.INVISIBLE--->不可见，但这个View仍然会占用在xml文件中所分配的布局空间，不重新layout
			 View.GONE---->不可见，但这个View在ViewGroup中不保留位置，会重新layout，不再占用空间，那后面的view就会取代他的位置，
			 */

			backBtn.setVisibility(View.GONE);
		}
	}
// onOptionsItemSelected(MenuItem),回调方法，每当有菜单项被点击时，android就会调用该方法，并传入被点击菜单项。
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home){
			//页面切换效果。
			doPop();
			return false ;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		doPop();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		System.out.println("onWindowFocusChanged !!!!!!");

		if (hasFocus && Build.VERSION.SDK_INT >= 19) {

			decorView.setSystemUiVisibility(
					//全屏模式？？
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE
							| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		}

	}
//检查是否登录
	public boolean checkIsLogin()
	{
		//调用缓存信息。
		XNetUtil.APPPrintln("APPDataCache: "+APPDataCache);
		XNetUtil.APPPrintln("APPDataCache.User: "+APPDataCache.User);

		if(APPDataCache.User.getUid().equals(""))
		{
			//如果缓存信息中没有用户ID，则返回false
			presentVC(LoginVC.class);
			return false;
		}
		return true;
	}
//判断用户权限。
	public boolean CheckUserPower(String str)
	{
		boolean b = APPDataCache.User.getPowerArr().contains(str);

		if(!b)
		{
			doShowToast("您没有该操作权限,无法使用此功能");
		}

		return b;
	}

	private boolean isActive = false;

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		if (!isAppOnForeground()) {
			//app 进入后台
			isActive = false;
			//全局变量isActive = false 记录当前已经进入后台
		}
	}



	/**
	 * 程序是否在前台运行
	 *
	 * @return
	 */
	public boolean isAppOnForeground() {
		// Returns a list of application processes that are running on the
		// device

		ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = getApplicationContext().getPackageName();
        // ActivityManager.RunningAppProcessInfo： 正在运行的进程信息
		List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}

}
