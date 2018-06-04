package jpushdemo.com.yundun;

import android.text.TextUtils;

import com.dahua.searchandwarn.model.SW_AddressTreeBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作用：
 * 作者： 郑旭东
 * 日期：2018/6/1
 */

public class GroupTreeManager {

    private static volatile GroupTreeManager mInstance;
    private List<SW_AddressTreeBean.BaseInfo> mAllBaseInfos = new ArrayList<>();
    private Map<String, SW_AddressTreeBean.BaseInfo> mDataMap = new HashMap<>();

    public static GroupTreeManager getInstance(){
        if(mInstance == null){
            synchronized (GroupTreeManager.class){
                if(mInstance == null){
                    mInstance = new GroupTreeManager();
                }
            }
        }
        return mInstance;
    }

    public List<SW_AddressTreeBean.BaseInfo> getAllBaseInfos() {
        return mAllBaseInfos;
    }

    public void setAllBaseInfos(List<SW_AddressTreeBean.BaseInfo> allBaseInfos) {
//        this.mAllBaseInfos = allBaseInfos;
        for (SW_AddressTreeBean.BaseInfo info: allBaseInfos) {
            filter(info);
        }
    }

    private void filter(SW_AddressTreeBean.BaseInfo bean){
        if(TextUtils.isEmpty(bean.getOrg_id())){
            mAllBaseInfos.add(bean);
        }
        if (bean.getChildren() != null) {
            for (SW_AddressTreeBean.BaseInfo baseInfo : bean.getChildren()) {
                baseInfo.setOrg_id(bean.getOrgCode());
                mAllBaseInfos.add(baseInfo);
                mDataMap.put(baseInfo.getOrgCode(), baseInfo);
                if (baseInfo.getChildren() == null || baseInfo.getChildren().size() == 0) {
                    break;
                }
                filter(baseInfo);
            }
        }
    }
}
