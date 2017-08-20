package cn.edu.nuc.androidlab.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dreamY on 2017/7/20.
 */

public abstract class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";
    protected  Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(getResourcesLayout(),container,false);
        unbinder=ButterKnife.bind(this,mView);
        Log.i(TAG, "onCreateView: "+getResourcesLayout());
        init();
        logic();
        return mView;
    }

    protected abstract void init();

    protected abstract int getResourcesLayout();

    protected abstract void logic();

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: "+getResourcesLayout());
        unbinder.unbind();
    }
}
