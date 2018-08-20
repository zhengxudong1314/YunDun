package jpushdemo.com.yundun;

import android.app.Application;
import android.content.Intent;

import com.dahua.searchandwarn.base.UnReadService;
import com.dahua.searchandwarn.utils.Utils;

/**
 * 创建： ZXD
 * 日期 2018/6/11
 * 功能：
 */

public class MyApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(this, UnReadService.class);
        startService(intent);
        Utils.init(this, "61.128.209.66");
    }
}
