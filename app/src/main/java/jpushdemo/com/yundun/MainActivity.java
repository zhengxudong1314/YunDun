package jpushdemo.com.yundun;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;

import com.dahua.searchandwarn.modules.facesearching.activity.SW_FaceSearchingActivity;
import com.dahua.searchandwarn.modules.warning.activity.SW_SearchActivity;
import com.dahua.searchandwarn.modules.warning.activity.SW_WarningAccpetActivity;
import com.dahua.searchandwarn.utils.LogUtils;
import com.dahua.searchandwarn.utils.Utils;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.internal.MemoryPersistence;

public class MainActivity extends AppCompatActivity {
    //tcp://172.6.3.111:1883
    public static final String HOST = "tcp://10.23.10.35:1883";
    public static final String TOPIC = "DAHUA";
    private String clientid;
    private String userName = "admin";
    private String passWord = "admin";
    private MqttClient client;
    private MqttConnectOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bt4 = (Button) findViewById(R.id.bt4);
        Button bt3 = (Button) findViewById(R.id.bt3);
        Button bt2 = (Button) findViewById(R.id.bt2);
        Button bt1 = (Button) findViewById(R.id.bt1);
        Utils.init(this.getApplication());
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SW_SearchActivity.class));
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

        //start();
        //MqttUtils.connectMqtt(this);
        /*Intent intent = new Intent(this, UnReadService.class);
        startService(intent);*/
    }


    public void getNotification() {
        PendingIntent contentIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, SW_WarningAccpetActivity.class), 0);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(com.dahua.searchandwarn.R.drawable.test1)
                .setContentTitle("这是标题")
                .setContentText("这是内容")
                .setContentIntent(contentIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    private void start() {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            clientid = telephonyManager.getDeviceId();
            client = new MqttClient(HOST, clientid, new MemoryPersistence());
            // MQTT的连接设置
            options = new MqttConnectOptions();
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            // 设置连接的用户名
            options.setUserName(userName);
            // 设置连接的密码
            options.setPassword(passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(20);
            // 设置回调

           /* MqttTopic topic = client.getTopic(TOPIC);
            //setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
            options.setWill(topic, "close".getBytes(), 2, true);*/
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        client.connect(options);
                        //订阅消息
                        int[] Qos = {1};
                        String[] topic1 = {TOPIC};
                        client.subscribe(topic1, Qos);
                        client.setCallback(new MqttCallback() {
                            @Override
                            public void connectionLost(Throwable throwable) {
                                LogUtils.e("连接断开，可以做重连");
                                try {
                                    client.connect(options);
                                } catch (MqttException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void messageArrived(MqttTopic mqttTopic, MqttMessage mqttMessage) throws Exception {

                                getNotification();
                                LogUtils.e("接收消息主题 :" + mqttTopic.getName());
                                LogUtils.e("接收消息Qos  :" + mqttMessage.getQos());
                                LogUtils.e("接收消息内容  :" + mqttMessage.getPayload());

                            }

                            @Override
                            public void deliveryComplete(MqttDeliveryToken mqttDeliveryToken) {
                                LogUtils.e("deliveryComplete---------" + mqttDeliveryToken.isComplete());
                            }
                        });
                    } catch (MqttException e) {
                        LogUtils.e(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
