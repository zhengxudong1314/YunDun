package jpushdemo.com.yundun;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * 创建： ZXD
 * 日期 2018/7/25
 * 功能：
 */

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,
                "接收到的Intent的Action为：" + intent.getAction() + "\n 消息内容是：" + intent.getIntExtra("msg",0),
                Toast.LENGTH_LONG).show();
    }
}
