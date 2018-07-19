package jpushdemo.com.yundun;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.dahua.searchandwarn.base.UnReadService;
import com.dahua.searchandwarn.model.SW_AddressTreeBean;
import com.dahua.searchandwarn.model.SW_UnReadNum;
import com.dahua.searchandwarn.modules.facesearching.activity.SW_FaceSearchingActivity;
import com.dahua.searchandwarn.modules.warning.activity.SW_WarningAccpetActivity;
import com.dahua.searchandwarn.utils.Utils;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {
    //tcp://172.6.3.111:1883
    public static final String HOST = "tcp://10.23.10.35:1883";
    public static final String TOPIC = "DAHUA";
    private String clientid;
    private String userName = "admin";
    private String passWord = "admin";
    private MqttClient client;
    private MqttConnectOptions options;
    private Button bt4;
    private RecyclerView rv;
    private CompositeDisposable compositeDisposable;
    Map<String, SW_AddressTreeBean.BaseInfo> dataMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sp = getSharedPreferences("dahua", MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("name", "zxd").commit();
        compositeDisposable = new CompositeDisposable();
        rv = (RecyclerView) findViewById(R.id.rv);
        Utils.init(this.getApplication());
        EventBus.getDefault().register(this);
        bt4 = (Button) findViewById(R.id.bt4);
        Button bt3 = (Button) findViewById(R.id.bt3);
        Button bt2 = (Button) findViewById(R.id.bt2);
        Button bt1 = (Button) findViewById(R.id.bt1);
        Intent intent = new Intent(this, UnReadService.class);
        startService(intent);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SW_WarningAccpetActivity.class));
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SW_FaceSearchingActivity.class));
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
       // getNetData();


    }

    private void filter(SW_AddressTreeBean.BaseInfo bean) {
        if (bean.getChildren() != null) {
            for (SW_AddressTreeBean.BaseInfo baseInfo : bean.getChildren()) {
                baseInfo.setOrg_id(bean.getOrgCode());
                dataMap.put(baseInfo.getOrgCode(), baseInfo);
                if (baseInfo.getChildren() == null || baseInfo.getChildren().size() == 0) {
                    break;
                }
                filter(baseInfo);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onDeed(SW_UnReadNum bean) {
        bt4.setText(bean.getNum() + "");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
        EventBus.getDefault().unregister(this);
    }
}
