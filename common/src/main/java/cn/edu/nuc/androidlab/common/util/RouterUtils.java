package cn.edu.nuc.androidlab.common.util;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * RouterUtils
 *
 * Created by MurphySL on 2017/8/12.
 */

public class RouterUtils {

    public static Object navigation(String path){
        return ARouter.getInstance().build(path).navigation();
    }
}
