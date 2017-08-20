package cn.edu.nuc.androidlab.common;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;

import cn.edu.nuc.androidlab.common.bean.LU;
import cn.edu.nuc.androidlab.common.bean.Live;
import cn.edu.nuc.androidlab.common.bean.UserInfo;

/**
 * BaseApplication
 *
 * Created by MurphySL on 2017/8/12.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if(BuildConfig.DEBUG){
            ARouter.openLog();
            ARouter.openDebug();
            ARouter.printStackTrace();
        }
        ARouter.init(this);

        AVObject.registerSubclass(UserInfo.class);
        AVObject.registerSubclass(Live.class);
        AVObject.registerSubclass(LU.class);

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this, "O5aEuqARNjtbvT2tGTW23bB5-gzGzoHsz", "XMaxhc0a9L5cDOIAXrBeqoS8");
        // LeanCloud 调试日志
        AVOSCloud.setDebugLogEnabled(true);
    }
}
