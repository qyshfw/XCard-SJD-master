package com.x.custom;

import android.widget.Toast;

import com.com.x.AppModel.HttpResult;
import com.example.x.xcard.ApplicationClass;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by X on 2016/10/1.
 */

public class XNetUtil {
   //接口
    public interface OnHttpResult<T>
    {
        //Http 的返回结果。
        void onError(Throwable e);
        void onSuccess(T t);
    }

    final static public <T> void APPPrintln(T t)
    {
        System.out.println(t);
    }

    public static <T> void Handle(Observable<HttpResult<T>> obj,Subscriber<T> res) {
//Observer模式的意图：在对象的内部状态发生改变时，自动通知外部对象响应；Observable，可被观察的，被观察类，
        obj
                //subscribeOn()改变调用它之前代码的线程
                /**
                 * 如果你想给Observable操作符链添加多线程功能，
                 * 你可以指定操作符（或者特定的Observable）在特定的调度器(Scheduler)上执行。
                 */

                .subscribeOn(Schedulers.newThread())
                //observeOn()改变调用它之后代码的线程
                .observeOn(AndroidSchedulers.mainThread())
                //将查询的网络结果放入map中。
                .map(new HttpResultFunc<T>())
                .subscribe(res);

    }

    public static <T> void HandleReturnAll(Observable<HttpResult<T>> obj,final OnHttpResult<HttpResult<T>> res) {
        obj
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpResult<T>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        res.onError(e);
                    }

                    @Override
                    public void onNext(HttpResult<T> tHttpResult) {
                        res.onSuccess(tHttpResult);
                    }
                });

    }


    public static <T> void Handle(Observable<HttpResult<T>> obj,final OnHttpResult<T> res) {
        obj
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new HttpResultFunc<T>())
                .subscribe(new Subscriber<T>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        res.onError(e);
                    }

                    @Override
                    public void onNext(T t) {
                        res.onSuccess(t);
                    }
                });

    }

  //构建了一个要传递的信息。
    public static <T> void Handle(Observable<HttpResult<T>> obj,String success,String fail,final OnHttpResult<Boolean> res) {

        obj
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new HttpResultFuncBool<T>(success,fail))
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        Toast.makeText(ApplicationClass.context, e.toString(), Toast.LENGTH_LONG).show();
                        res.onError(e);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        res.onSuccess(aBoolean);
                    }
                });

    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型；
     *           得到反馈值后。出现对话框。
     */
    private static class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            if (httpResult.getRet() != 200) {
                XActivityindicator.create(ApplicationClass.context).showErrorWithStatus("数据加载失败!");
            }
            else
            {
                if(httpResult.getData().getCode() != 0)
                {
                    String msg = httpResult.getData().getMsg();
                    msg = msg.length() == 0 ? "数据加载失败!" : msg;
                    XActivityindicator.create(ApplicationClass.context).showErrorWithStatus(msg);
                }
            }

            return httpResult.getData().getInfo();
        }
    }

    private static class HttpResultFuncBool<T> implements Func1<HttpResult<T>, Boolean> {

        private String success = "";
        private String fail = "";

        public HttpResultFuncBool(String s,String f) {

            this.success = s;
            this.fail = f;
        }

        @Override
        public Boolean call(HttpResult<T> httpResult) {
               //网络连接失败。
            if (httpResult.getRet() != 200) {
                //调用X指示器中的create方法、
                XActivityindicator.create(ApplicationClass.context).showErrorWithStatus(fail);

                return false;
            }
            else
            {
                if(httpResult.getData().getCode() != 0)
                {
                    //字数不符合要求
                    String msg = httpResult.getData().getMsg();
                    msg = msg.length() == 0 ? fail : msg;
                    XActivityindicator.create(ApplicationClass.context).showErrorWithStatus(msg);
                    return false;
                }
            }

            if(success != null)
            {
                XActivityindicator.create(ApplicationClass.context).showSuccessWithStatus(success);
            }

            return true;
        }
    }


}
