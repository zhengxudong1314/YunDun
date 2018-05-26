package jpushdemo.com.yundun;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dahua.searchandwarn.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 作用：
 * 作者： 郑旭东
 * 日期：2018/5/21
 */

public class TwoFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.two, null);
        EventBus.getDefault().register(this);
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onMoon(String str){
        LogUtils.e(str);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
