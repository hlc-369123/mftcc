/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.common.sigar;

import java.math.BigDecimal;

public class SigarUtil {
    public static Double div(double double1,double double2){
        BigDecimal dec1 = BigDecimal.valueOf(double1);
        BigDecimal dec2 = BigDecimal.valueOf(double2);
        return dec1.divide(dec2,10, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static Double mult(double double1,double double2){
        BigDecimal dec1 = BigDecimal.valueOf(double1);
        BigDecimal dec2 = BigDecimal.valueOf(double2);
        return dec1.multiply(dec2).doubleValue();
    }

    public static Double add(double double1,double double2){
        BigDecimal dec1 = BigDecimal.valueOf(double1);
        BigDecimal dec2 = BigDecimal.valueOf(double2);
        return dec1.add(dec2).doubleValue();
    }

    public static Double sub(double double1,double double2){
        BigDecimal dec1 = BigDecimal.valueOf(double1);
        BigDecimal dec2 = BigDecimal.valueOf(double2);
        return dec1.subtract(dec2).doubleValue();
    }
}
