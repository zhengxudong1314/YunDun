package com.dahua.searchandwarn.base;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.dahua.searchandwarn.model.SW_NewMessageBean;
import com.dahua.searchandwarn.model.SW_UnReadNum;
import com.dahua.searchandwarn.modules.warning.activity.SW_WarningDetailsActivity;
import com.dahua.searchandwarn.service.IMqttMsgListener;
import com.dahua.searchandwarn.service.MqttService;
import com.dahua.searchandwarn.utils.LogUtils;
import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 作用：
 * 作者： 郑旭东
 * 日期：2018/5/20
 */

public class MqttUtils implements IMqttMsgListener {
    //tcp://172.6.3.111:1883
    public static final String HOST = "tcp://10.23.10.35:1883";
    public static final String TOPIC = "DAHUA";
    private static String clientid;
    private static String userName = "admin";
    private static String passWord = "admin";
    private static MqttClient client;
    private static MqttConnectOptions options;
    private int num;
    private Context mContext;

    public MqttUtils() {
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMood(SW_UnReadNum num) {
        this.num = num.getNum();
    }

    public void connectMqtt(final Context context, String ip) {
        this.mContext = context;
        MqttService.init(ip, 1883, new String[]{TOPIC}, this);
        MqttService.actionRestart(context);
        /*try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
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

           *//* MqttTopic topic = client.getTopic(TOPIC);
            //setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
            options.setWill(topic, "close".getBytes(), 2, true);*//*
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

                                num++;
                                LogUtils.e("接收消息主题 :"+mqttTopic.getName());
                                LogUtils.e("接收消息Qos  :"+mqttMessage.getQos());
                                String s = new String(mqttMessage.getPayload());
                                LogUtils.e("接收消息  :"+s);
                               *//* Gson gson = new Gson();
                                SW_NewMessageBean sw_newMessageBean = gson.fromJson(s, SW_NewMessageBean.class);
                                String device_name = sw_newMessageBean.getDevice_name();
                                LogUtils.e("接收消息内容  :"+device_name);
                                getNotification(context,mqttTopic.getName(),device_name);
                                SW_NewMessageBean newMessageBean = new SW_NewMessageBean();
                                sw_newMessageBean.setNewMessage(0);
                                EventBus.getDefault().postSticky(newMessageBean);
                                EventBus.getDefault().postSticky(new SW_UnReadNum(num));*//*
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
        }*/
    }

    private static void getNotification(Context context, String topic, String message,String alarmId) {
        Intent intent = new Intent(context, SW_WarningDetailsActivity.class);
        intent.putExtra("alarmId", alarmId);
        PendingIntent contentIntent = PendingIntent.getActivity(
                context, 0,intent , PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(com.dahua.searchandwarn.R.drawable.test1)
                .setContentTitle(topic)
                .setContentText(message)
                .setContentIntent(contentIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), notification);
    }

    @Override
    public void onMqttReceive(String msg, String topicName) {
        LogUtils.e("接收消息内容  :" + msg);
        num++;
        String s = new String(msg);
        Gson gson = new Gson();
        SW_NewMessageBean sw_newMessageBean = gson.fromJson(s, SW_NewMessageBean.class);
        String device_name = sw_newMessageBean.getDevice_name();
        String control_reason = sw_newMessageBean.getControl_reason();
        if (!TextUtils.isEmpty(device_name) && !TextUtils.isEmpty(control_reason)) {
            String appPusher = sw_newMessageBean.getAppPusher();
            String[] split = appPusher.split(",");
            for (int i = 0; i < split.length; i++) {
                if (SW_Constracts.getUserName(mContext).equals(split[i])){
                    getNotification(mContext, device_name, control_reason,sw_newMessageBean.getId());
                }
            }

        }
        SW_NewMessageBean newMessageBean = new SW_NewMessageBean();
        sw_newMessageBean.setNewMessage(0);
        EventBus.getDefault().postSticky(newMessageBean);
        EventBus.getDefault().postSticky(new SW_UnReadNum(num));
}

    @Override
    public void onMqttException() {
        LogUtils.e("MQ异常 :" + "");
    }
}
