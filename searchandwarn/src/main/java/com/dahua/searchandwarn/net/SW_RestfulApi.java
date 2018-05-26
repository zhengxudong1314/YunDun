package com.dahua.searchandwarn.net;


import com.dahua.searchandwarn.model.SW_AddressTreeBean;
import com.dahua.searchandwarn.model.SW_DisposeBean;
import com.dahua.searchandwarn.model.SW_DynamicBean;
import com.dahua.searchandwarn.model.SW_FaceCropBean;
import com.dahua.searchandwarn.model.SW_HistoryWarnBean;
import com.dahua.searchandwarn.model.SW_IgnoreBean;
import com.dahua.searchandwarn.model.SW_SingleWarnBean;
import com.dahua.searchandwarn.model.SW_StaticBean;
import com.dahua.searchandwarn.model.SW_UserLoginBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by wukang on 2018/3/9.
 * <p>
 * 数据存储接口
 */
public interface SW_RestfulApi {
    // 10.23.10.35
    // 用户登录 https://www.apiopen.top    application/x-www-form-urlencoded

    // 用户登录
    @POST("/user/login")
    Observable<SW_UserLoginBean> userLogin(@Query("loginAccount") String loginAccount, @Query("loginPassword") String loginPassword);

    // 获取告警Kafka地址

    // 历史预警信息查询
    @POST("/dhapp/alarmInfo/list")
    Observable<SW_HistoryWarnBean> getHistoryWarn(@QueryMap Map<String,String> map);

    //单条预警信息查看
    @POST("/dhapp/alarmInfo/query")
    Observable<SW_SingleWarnBean> getSingleWarn(@Query("alarmId") String alarmId);

    //预警忽略
    @POST("/dhapp/alarmControl/ignore")
    Observable<SW_IgnoreBean> warnIgnore(@Query("alarmId") String alarmId, @Query("ignoreMsg") String ignoreMsg, @Query("operator") String operator);

    //预警处理
    @POST("/dhapp/alarmControl/handle")
    Observable<SW_DisposeBean> warnDispose(@Query("alarmId") String alarmId, @Query("suggestion") String suggestion, @Query("resultType") int resultType, @Query("operator") String operator);

    // 人脸抓拍机组织树
    @POST("/dhapp/deviceResource/tree")
    Observable<SW_AddressTreeBean> getAddressTree();

    // 动态比对
    @FormUrlEncoded
    @POST("/dhapp/faceSearch/getFaceRetrieval")
    Observable<SW_DynamicBean> getDynamicData(@FieldMap  Map<String,String> map);

    // 静态比对
    @FormUrlEncoded
    @POST("/dhapp/staticCompare/start")
    Observable<SW_StaticBean> getStaticData(@FieldMap Map<String,String> map);

    // 人脸裁剪
    @FormUrlEncoded
    @POST("/dhapp/staticlist/structure")
    Observable<SW_FaceCropBean> getFaceCrop(@Field("imageBase64") String imgUrl);

}
