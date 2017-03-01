package com.com.x.AppModel;

import com.x.custom.XNetUtil;

import java.lang.reflect.Method;

/**
 * Created by X on 2016/10/2.
 */

public class ModelUtil {


    final static public <T> void reSet(T t) {

        try {

            // getDeclaredMethods得到本类中所有方法
            Method[] studentMethods = t.getClass().getMethods();

            for (Method method : studentMethods) {

                if(!method.getReturnType().equals(void.class) || !method.getName().contains("set"))
                {
                    continue;
                }
                 //获得此类的所有方法
                Class[] paramsType =  method.getParameterTypes();

                if (paramsType.length != 1){continue;}
                //Java的反射机制。
                method.invoke(t,paramsType[0].newInstance());
            }

        }
        catch (Exception e)
        {
            XNetUtil.APPPrintln("Model重设失败: "+e);
        }

    }

}
