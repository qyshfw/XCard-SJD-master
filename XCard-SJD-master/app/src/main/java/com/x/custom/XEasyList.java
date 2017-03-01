package com.x.custom;

import android.widget.BaseAdapter;

import com.com.x.AppModel.HttpResult;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by X on 2016/10/6.
 */

public class XEasyList<T> {

    private WeakReference<PullToRefreshListView> list;
    private WeakReference<BaseAdapter> adapter;
    private List<T> dataArr = new ArrayList<>();

    //private Observable<HttpResult<List<T>>> http;

    public XEasyList(PullToRefreshListView list, BaseAdapter adapter, Observable<HttpResult<List<T>>> http) {
        this.list = new WeakReference<PullToRefreshListView>(list);
        this.adapter = new WeakReference<BaseAdapter>(adapter);
        //this.http = http;
    }
}
