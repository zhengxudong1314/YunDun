package com.dahua.searchandwarn.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 作用：
 * 作者： 郑旭东
 * 日期：2018/5/29
 */

public class TwoPointUtils {
    /**
     * double转String,保留小数点后两位
     * @param num
     * @return
     */
    public static String doubleToString(double num){
        //使用0.00不足位补0，#.##仅保留有效位
        BigDecimal bd = new BigDecimal(num);
        BigDecimal bigDecimal = bd.setScale(2, RoundingMode.DOWN);

        return bigDecimal.toString();
        //return String.format("%.2f",num-0.005);
    }
}
