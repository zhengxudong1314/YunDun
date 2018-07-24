package com.dahua.searchandwarn.service;

public interface IMqttMsgListener {

    void onMqttReceive(String msg, String topicName);
    void onMqttException();
    
}
