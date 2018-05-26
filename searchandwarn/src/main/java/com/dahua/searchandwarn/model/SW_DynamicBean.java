package com.dahua.searchandwarn.model;

import java.io.Serializable;
import java.util.List;

/**
 * 创建： ZXD
 * 日期 2018/5/15
 * 功能：
 */

public class SW_DynamicBean {

    /**
     * data : [{"originPlace":"","sex":"0","factoryName":"海康","devY":29.7858914081,"faceId":"500318732878172814620180504192421010001","devName":"D1区3栋单元大厅入口","deviceId":"5003187328781728146","devX":106.649517417,"source_image1":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180504/1f1800ed-2da1-4160-8dc1-3c364527f0ea.jpg","source_image2":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180504/9594d1da-8f70-4b46-905b-adf7ab02a5db.jpg","faceTime":"2018-05-04 19:24:21.0","obj_right":"0","similarity":"69.13521575927734","obj_bottom":"0","idCardCode":"","name":"","household":"","obj_left":"0","obj_top":"0","age":"0"},{"originPlace":"","sex":"1","factoryName":"海康","devY":29.785083978634,"faceId":"500318732878172816520180501210745010001","devName":"A1区10栋单元大厅入口","deviceId":"5003187328781728165","devX":106.64982823469,"source_image1":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180501/a0277b5a-a182-4c81-80b8-28ff3d0ab50d.jpg","source_image2":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180501/bce38bcf-73fd-44c6-b19e-d049c8b4ff2f.jpg","faceTime":"2018-05-01 21:07:45.0","obj_right":"193","similarity":"68.63227844238281","obj_bottom":"191","idCardCode":"","name":"","household":"","obj_left":"35","obj_top":"33","age":"31"},{"originPlace":"","sex":"1","factoryName":"海康","devY":29.7839150384,"faceId":"50031800100835520180504111042010001","devName":"C1区3栋单元大厅出口","deviceId":"500318001008355","devX":106.6490292549,"source_image1":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180504/3928c7ea-875a-435c-85ce-a14f073398b1.jpg","source_image2":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180504/c1768d99-3608-4442-a706-20b5323cbd0c.jpg","faceTime":"2018-05-04 11:10:42.0","obj_right":"304","similarity":"68.61271667480469","obj_bottom":"304","idCardCode":"","name":"","household":"","obj_left":"66","obj_top":"68","age":"36"},{"originPlace":"","sex":"0","factoryName":"海康","devY":29.7867806453,"faceId":"50031800100839820180505130636010001","devName":"D1区6栋单元大厅出口","deviceId":"500318001008398","devX":106.6504669189,"source_image1":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180505/80b46ee9-6d86-43b3-9e0a-874700dbbbc5.jpg","source_image2":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180505/5898680c-dbfe-42ad-8727-ce71aafc2b3b.jpg","faceTime":"2018-05-05 13:06:36.0","obj_right":"341","similarity":"68.37818145751953","obj_bottom":"328","idCardCode":"","name":"","household":"","obj_left":"55","obj_top":"42","age":"38"},{"originPlace":"","sex":"1","factoryName":"海康","devY":29.784450977307,"faceId":"500318732878172816620180512125349010001","devName":"A1区12栋单元大厅出口","deviceId":"5003187328781728166","devX":106.64913086035,"source_image1":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180512/83d53670-049f-419c-b772-7efacf881e24.jpg","source_image2":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180512/c065c115-6b24-49ec-bb45-1b5c746b19f8.jpg","faceTime":"2018-05-12 12:53:49.0","obj_right":"280","similarity":"68.35066986083984","obj_bottom":"304","idCardCode":"","name":"","household":"","obj_left":"26","obj_top":"57","age":"44"},{"originPlace":"","sex":"0","factoryName":"海康","devY":29.5830303509,"faceId":"5001129001132000000320180506155051010001","devName":"花卉园地铁站1B出口围墙上","deviceId":"50011290011320000003","devX":106.5142267942,"source_image1":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180506/e8cc7bfe-b785-4b7a-bec0-07f883f5f089.jpg","source_image2":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180506/f7a29657-5c06-4d44-8249-dbc4e055b7d0.jpg","faceTime":"2018-05-06 15:50:51.0","obj_right":"240","similarity":"68.24028778076172","obj_bottom":"219","idCardCode":"","name":"","household":"","obj_left":"42","obj_top":"21","age":"57"},{"originPlace":"","sex":"0","factoryName":"海康","devY":29.7865641563,"faceId":"50031800100838520180502071657010001","devName":"D1区2栋单元大厅出口","deviceId":"500318001008385","devX":106.6502818465,"source_image1":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180502/6c042f43-8488-4ab9-84fa-d4e1ffce2c6d.jpg","source_image2":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180502/3e233aee-fbb4-4ea9-9eec-7ba27407174b.jpg","faceTime":"2018-05-02 07:16:57.0","obj_right":"353","similarity":"67.98143005371094","obj_bottom":"345","idCardCode":"","name":"","household":"","obj_left":"65","obj_top":"57","age":"52"},{"originPlace":"","sex":"0","factoryName":"海康","devY":29.7858914081,"faceId":"500318732878172814620180503182222010001","devName":"D1区3栋单元大厅入口","deviceId":"5003187328781728146","devX":106.649517417,"source_image1":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180503/7d84e0c2-fd1f-481e-879f-bdd2ebdcbfaa.jpg","source_image2":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180503/5f3462dc-d37b-47e4-9a06-68e3b824aab3.jpg","faceTime":"2018-05-03 18:22:22.0","obj_right":"218","similarity":"67.90884399414063","obj_bottom":"227","idCardCode":"","name":"","household":"","obj_left":"30","obj_top":"39","age":"37"},{"originPlace":"","sex":"0","factoryName":"海康","devY":29.7858914081,"faceId":"500318732878172814620180503193750010001","devName":"D1区3栋单元大厅入口","deviceId":"5003187328781728146","devX":106.649517417,"source_image1":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180503/7d798dac-bc3e-46b4-a27e-bddf1b9d39fb.jpg","source_image2":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180503/b59213d4-b690-4406-801d-b7b94b05240b.jpg","faceTime":"2018-05-03 19:37:50.0","obj_right":"270","similarity":"67.90152740478516","obj_bottom":"288","idCardCode":"","name":"","household":"","obj_left":"0","obj_top":"21","age":"26"},{"originPlace":"","sex":"0","factoryName":"海康","devY":29.785083978634,"faceId":"50031800100820220180506085945010001","devName":"A1区10栋消防应急门出口","deviceId":"500318001008202","devX":106.64982823469,"source_image1":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180506/32030883-6be0-4ebb-b54c-8afb641ff25d.jpg","source_image2":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180506/c3fcc607-ebb9-4195-bdbb-ae31e378918a.jpg","faceTime":"2018-05-06 08:59:45.0","obj_right":"222","similarity":"67.85394287109375","obj_bottom":"227","idCardCode":"","name":"","household":"","obj_left":"36","obj_top":"41","age":"20"},{"originPlace":"","sex":"0","factoryName":"海康","devY":29.785083978634,"faceId":"50031800100820220180503071635010001","devName":"A1区10栋消防应急门出口","deviceId":"500318001008202","devX":106.64982823469,"source_image1":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180503/6553b4f0-ed4c-4ed0-aa90-0752fedcde48.jpg","source_image2":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180503/9095d2e5-de73-4040-be22-3d5d2b0751d3.jpg","faceTime":"2018-05-03 07:16:35.0","obj_right":"196","similarity":"67.84516906738281","obj_bottom":"197","idCardCode":"","name":"","household":"","obj_left":"40","obj_top":"41","age":"21"},{"originPlace":"","sex":"0","factoryName":"海康","devY":29.580187504,"faceId":"500318732878172817020180510101057010001","devName":"花卉园西路56号背后4单元入口","deviceId":"5003187328781728170","devX":106.511606276,"source_image1":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180510/d99d857e-eefe-4b0e-9d61-471a6660662e.jpg","source_image2":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180510/a5d9c822-5640-4e36-bc01-443141033ddb.jpg","faceTime":"2018-05-10 10:10:57.0","obj_right":"456","similarity":"67.64288330078125","obj_bottom":"449","idCardCode":"","name":"","household":"","obj_left":"66","obj_top":"59","age":"40"},{"originPlace":"","sex":"0","factoryName":"海康","devY":29.7858914081,"faceId":"500318732878172814820180514092124010001","devName":"D1区3栋消防应急门出口-2","deviceId":"5003187328781728148","devX":106.649517417,"source_image1":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180514/637fba85-9e9a-439a-838f-cfa4b6347f52.jpg","source_image2":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180514/1197cd2f-5ed8-4639-ae73-bc534b70d110.jpg","faceTime":"2018-05-14 09:21:24.0","obj_right":"385","similarity":"67.60077667236328","obj_bottom":"349","idCardCode":"","name":"","household":"","obj_left":"75","obj_top":"39","age":"61"},{"originPlace":"","sex":"0","factoryName":"海康","devY":29.783817975993,"faceId":"50031800100817820180514074713010001","devName":"A1区2栋消防应急门出口","deviceId":"500318001008178","devX":106.65020910835,"source_image1":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180514/9be83d41-f875-4fff-9d62-86016b77bf99.jpg","source_image2":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180514/f49d0ae7-5c49-41e0-a084-16165ef1f471.jpg","faceTime":"2018-05-14 07:47:13.0","obj_right":"210","similarity":"67.52381134033203","obj_bottom":"214","idCardCode":"","name":"","household":"","obj_left":"46","obj_top":"50","age":"31"},{"originPlace":"","sex":"0","factoryName":"海康","devY":29.788581579188,"faceId":"50031800100842520180501235758010001","devName":"D4区1栋单元大厅入口","deviceId":"500318001008425","devX":106.64246825315,"source_image1":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180501/b6e37ffd-75dd-405d-b043-69ef694b30dd.jpg","source_image2":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180501/bc414a94-7a9b-4060-b5da-f075644600f0.jpg","faceTime":"2018-05-01 23:57:58.0","obj_right":"257","similarity":"67.52008819580078","obj_bottom":"237","idCardCode":"","name":"","household":"","obj_left":"59","obj_top":"39","age":"22"},{"originPlace":"","sex":"1","factoryName":"海康","devY":29.7858914081,"faceId":"500318732878172814620180503171348010001","devName":"D1区3栋单元大厅入口","deviceId":"5003187328781728146","devX":106.649517417,"source_image1":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180503/cfadf724-6d08-41f2-9e3b-ed7b8ea6ee9d.jpg","source_image2":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180503/7693a0e9-ed2a-4315-b636-9a55858b8b1b.jpg","faceTime":"2018-05-03 17:13:48.0","obj_right":"348","similarity":"67.50934600830078","obj_bottom":"318","idCardCode":"","name":"","household":"","obj_left":"94","obj_top":"64","age":"43"},{"originPlace":"","sex":"1","factoryName":"海康","devY":29.5801067368,"faceId":"500318732878172817120180501111817010001","devName":"花卉园西路56号背后4单元出口","deviceId":"5003187328781728171","devX":106.5117165819,"source_image1":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180501/7e15a8e4-1863-417e-bebe-8e6a3c512dcd.jpg","source_image2":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180501/c544cd6e-e4f9-45e6-bb15-d3908247be82.jpg","faceTime":"2018-05-01 11:18:17.0","obj_right":"236","similarity":"67.49767303466797","obj_bottom":"230","idCardCode":"","name":"","household":"","obj_left":"40","obj_top":"34","age":"42"},{"originPlace":"","sex":"0","factoryName":"海康","devY":29.5830303509,"faceId":"5001129001132000000320180507113639010001","devName":"花卉园地铁站1B出口围墙上","deviceId":"50011290011320000003","devX":106.5142267942,"source_image1":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180507/39175486-ece7-4edd-8804-126cdc3cd378.jpg","source_image2":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180507/2f2c3cf7-0f9c-489a-b5ac-056da91c3bfb.jpg","faceTime":"2018-05-07 11:36:39.0","obj_right":"475","similarity":"67.42022705078125","obj_bottom":"441","idCardCode":"","name":"","household":"","obj_left":"111","obj_top":"77","age":"45"},{"originPlace":"","sex":"1","factoryName":"海康","devY":29.783753602963,"faceId":"50031800100818020180514102035010001","devName":"A1区3栋消防应急门出口","deviceId":"500318001008180","devX":106.64969948866,"source_image1":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180514/2786c8f9-8762-4b87-bf7d-cf8224bbd2df.jpg","source_image2":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180514/f9f0a849-015f-4d88-a796-ab0ee69f8de7.jpg","faceTime":"2018-05-14 10:20:35.0","obj_right":"365","similarity":"67.38884735107422","obj_bottom":"331","idCardCode":"","name":"","household":"","obj_left":"101","obj_top":"67","age":"42"},{"originPlace":"","sex":"0","factoryName":"海康","devY":29.791113584498,"faceId":"500318732878172813820180512210532010001","devName":"D4区6栋单元大厅入口","deviceId":"5003187328781728138","devX":106.64394883253,"source_image1":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180512/abdd11bc-eb57-4525-a627-8319a8ba8314.jpg","source_image2":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180512/8c068a6f-3e0c-44a7-8672-f34946ed59c5.jpg","faceTime":"2018-05-12 21:05:32.0","obj_right":"386","similarity":"67.24622344970703","obj_bottom":"361","idCardCode":"","name":"","household":"","obj_left":"72","obj_top":"47","age":"36"},{"originPlace":"","sex":"0","factoryName":"海康","devY":29.7865641563,"faceId":"50031800100838520180510155216010001","devName":"D1区2栋单元大厅出口","deviceId":"500318001008385","devX":106.6502818465,"source_image1":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180510/c817083e-ac45-41df-a0bb-9ef114bcb24d.jpg","source_image2":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180510/bf6b4e0a-dc03-4bc6-8d12-d2012ce2a9c8.jpg","faceTime":"2018-05-10 15:52:16.0","obj_right":"305","similarity":"67.21353912353516","obj_bottom":"307","idCardCode":"","name":"","household":"","obj_left":"63","obj_top":"65","age":"46"},{"originPlace":"","sex":"1","factoryName":"海康","devY":29.785083978634,"faceId":"50031800100820220180507062121010001","devName":"A1区10栋消防应急门出口","deviceId":"500318001008202","devX":106.64982823469,"source_image1":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180507/b239dc3a-a2bd-4a45-a1bb-2a8ec74847ce.jpg","source_image2":"http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180507/a030564c-116c-40c3-8f9e-18db4cff38d2.jpg","faceTime":"2018-05-07 06:21:21.0","obj_right":"244","similarity":"67.21328735351563","obj_bottom":"227","idCardCode":"","name":"","household":"","obj_left":"50","obj_top":"33","age":"24"}]
     * retCode : 0
     * message : 动态比对成功
     */

    private int retCode;
    private String message;
    private List<DataBean> data;

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * originPlace :
         * sex : 0
         * factoryName : 海康
         * devY : 29.7858914081
         * faceId : 500318732878172814620180504192421010001
         * devName : D1区3栋单元大厅入口
         * deviceId : 5003187328781728146
         * devX : 106.649517417
         * source_image1 : http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180504/1f1800ed-2da1-4160-8dc1-3c364527f0ea.jpg
         * source_image2 : http://10.23.10.21:8080/cloud-hz-controller/resource/getImage?imagePath=host00/face/20180504/9594d1da-8f70-4b46-905b-adf7ab02a5db.jpg
         * faceTime : 2018-05-04 19:24:21.0
         * obj_right : 0
         * similarity : 69.13521575927734
         * obj_bottom : 0
         * idCardCode :
         * name :
         * household :
         * obj_left : 0
         * obj_top : 0
         * age : 0
         */

        private String originPlace;
        private String sex;
        private String factoryName;
        private double devY;
        private String faceId;
        private String devName;
        private String deviceId;
        private double devX;
        private String source_image1;
        private String source_image2;
        private String faceTime;
        private String obj_right;
        private String similarity;
        private String obj_bottom;
        private String idCardCode;
        private String name;
        private String household;
        private String obj_left;
        private String obj_top;
        private String age;

        public String getOriginPlace() {
            return originPlace;
        }

        public void setOriginPlace(String originPlace) {
            this.originPlace = originPlace;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getFactoryName() {
            return factoryName;
        }

        public void setFactoryName(String factoryName) {
            this.factoryName = factoryName;
        }

        public double getDevY() {
            return devY;
        }

        public void setDevY(double devY) {
            this.devY = devY;
        }

        public String getFaceId() {
            return faceId;
        }

        public void setFaceId(String faceId) {
            this.faceId = faceId;
        }

        public String getDevName() {
            return devName;
        }

        public void setDevName(String devName) {
            this.devName = devName;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public double getDevX() {
            return devX;
        }

        public void setDevX(double devX) {
            this.devX = devX;
        }

        public String getSource_image1() {
            return source_image1;
        }

        public void setSource_image1(String source_image1) {
            this.source_image1 = source_image1;
        }

        public String getSource_image2() {
            return source_image2;
        }

        public void setSource_image2(String source_image2) {
            this.source_image2 = source_image2;
        }

        public String getFaceTime() {
            return faceTime;
        }

        public void setFaceTime(String faceTime) {
            this.faceTime = faceTime;
        }

        public String getObj_right() {
            return obj_right;
        }

        public void setObj_right(String obj_right) {
            this.obj_right = obj_right;
        }

        public String getSimilarity() {
            return similarity;
        }

        public void setSimilarity(String similarity) {
            this.similarity = similarity;
        }

        public String getObj_bottom() {
            return obj_bottom;
        }

        public void setObj_bottom(String obj_bottom) {
            this.obj_bottom = obj_bottom;
        }

        public String getIdCardCode() {
            return idCardCode;
        }

        public void setIdCardCode(String idCardCode) {
            this.idCardCode = idCardCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHousehold() {
            return household;
        }

        public void setHousehold(String household) {
            this.household = household;
        }

        public String getObj_left() {
            return obj_left;
        }

        public void setObj_left(String obj_left) {
            this.obj_left = obj_left;
        }

        public String getObj_top() {
            return obj_top;
        }

        public void setObj_top(String obj_top) {
            this.obj_top = obj_top;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }
    }
}
