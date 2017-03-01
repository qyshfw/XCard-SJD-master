package com.example.x.xcard;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.robin.lazy.cache.CacheLoaderManager;
import com.robin.lazy.cache.disk.naming.HashCodeFileNameGenerator;
import com.x.custom.ServicesAPI;
import com.x.custom.XNetUtil;
import com.x.custom.XNotificationCenter;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApplicationClass extends Application {

	public static int stateBarHeight = 0;
	public static int navBarHeight = 0;

	public static int SW = 0;
	public static int SH = 0;

	public static Context context;

	public static Retrofit retrofit;

	public static ServicesAPI APPService;

	static public DataCache APPDataCache;

	private List<WeakReference<Activity>> vcArrs = new ArrayList<>();

	/**
	 * 创建全局变量 全局变量一般都比较倾向于创建一个单独的数据类文件，并使用static静态变量
	 * 
	 * 这里使用了在Application中添加数据的方法实现全局变量
	 * 注意在AndroidManifest.xml中的Application节点添加android:name=".MyApplication"属性
	 * 
	 */
	//设置窗口的参数
	private WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

	public WindowManager.LayoutParams getMywmParams() {return wmParams;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
         //getApplicationContext() 返回应用的上下文，生命周期是整个应用，应用摧毁它才摧毁
      // Activity.this的context 返回当前activity的上下文，属于activity ，activity 摧毁他就摧毁
		registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
			//新创建应用周期的回调；
			@Override
			public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
				XNetUtil.APPPrintln("onActivityCreated: "+activity);

				context = activity;
			}

			@Override
			public void onActivityStarted(Activity activity) {

			}

			@Override
			public void onActivityResumed(Activity activity) {
				context = activity;
				//弱引用。
				WeakReference<Activity> item = new WeakReference<Activity>(activity);
				vcArrs.add(item);
			}

			@Override
			public void onActivityPaused(Activity activity) {

			}

			@Override
			public void onActivityStopped(Activity activity) {

			}

			@Override
			public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

			}

			@Override
			public void onActivityDestroyed(Activity activity) {

			}

		});
		//缓存的相关处理。getInstance()是实例化
		CacheLoaderManager.getInstance().init(this, new HashCodeFileNameGenerator(), 1024 * 1024 * 64, 200, 50);
		//App数据的缓存
		APPDataCache = new DataCache();
		//云推送
		initCloudChannel(this);
		//网络图片初始化
		initImageLoader();
		// DisplayMetics 类：Andorid.util 包下的DisplayMetrics 类提供了一种关于显示的通用信息，如显示大小，分辨率和字体。
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        //获取屏幕的宽度
		SW = displayMetrics.widthPixels;
		//获取屏幕的高度
		SH = displayMetrics.heightPixels;

		//建立一个client对象
		OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
			@Override
			public Response intercept(Chain chain) throws IOException {
        //创建一个Request
				Request request = chain.request().newBuilder()
						.addHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36")
						.addHeader("Content-Type","text/plain; charset=utf-8")
						.addHeader("Accept","*/*")
						.addHeader("Accept-Encoding","gzip, deflate, sdch")
						.build();
				XNetUtil.APPPrintln("URL: "+request.url().toString());
				if(request.body() != null)
				{
					XNetUtil.APPPrintln("Body: "+request.body().toString());
				}

				Response response = chain.proceed(request);
				return response;

			}
		}).build();

//网络请求库retrofit
		retrofit = new Retrofit.Builder()
				.baseUrl(ServicesAPI.APPUrl)
				.addConverterFactory(GsonConverterFactory.create())
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.callFactory(client)
				.build();

		APPService = retrofit.create(ServicesAPI.class);

		XNotificationCenter.getInstance().addObserver("AccountLogout", new XNotificationCenter.OnNoticeListener() {

			@Override
			public void OnNotice(Object obj) {

				for(WeakReference<Activity> item : vcArrs)
				{
					if(item.get() != null)
					{
						if(item.get() instanceof MainActivity)
						{
							continue;
						}
						item.get().finish();
					}
				}
//观察者模式，一般getInstance()是作为单例模式获取实例或者抽象类获得子类的规范方法名，postNotice为发送消息。
				XNotificationCenter.getInstance().postNotice("ShowAccountLogout",null);

			}
		});

		System.out.println("================init============");
	}

	//初始化网络图片缓存库
	private void initImageLoader() {
		//网络图片例子,结合常用的图片缓存库UIL,你可以根据自己需求自己换其他网络图片库
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
				//设置图片为null或者是错误的时候显示的图片。
				showImageForEmptyUri(R.drawable.app_default)
				//设置下载的图片是否在缓存的内存中；设置下载的图片是否缓存在SD卡中。
				.cacheInMemory(true).cacheOnDisk(true).build();
                //图片初始化。
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).defaultDisplayImageOptions(defaultOptions)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(config);

	}


	/**
	 * 初始化云推送通道
	 * @param applicationContext
	 */
	private void initCloudChannel(Context applicationContext) {
		PushServiceFactory.init(applicationContext);

		CloudPushService pushService = PushServiceFactory.getCloudPushService();

		pushService.register(applicationContext, new CommonCallback() {
			@Override
			public void onSuccess(String response) {
				XNetUtil.APPPrintln("init cloudchannel success");
				//APPDataCache.User.registNotice();
			}
			@Override
			public void onFailed(String errorCode, String errorMessage) {
				XNetUtil.APPPrintln("init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
			}
		});
	}

}
