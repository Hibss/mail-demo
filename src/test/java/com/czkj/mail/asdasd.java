package com.czkj.mail;

import com.google.common.base.Joiner;

/**
 * @Author steven.sheng
 * @Date 2019/10/10/01010:19
 */
public class asdasd {
    public static void main(String[] args) {
        String aa = "A72942K9001021157W00," +
                "51210229B010U7J89194," +
                "97001251N71152S04D99," +
                "1C105A092000219423Q7," +
                "4U1521892E1400A90197," +
                "452908A2101G919D7011," +
                "195217E01294BC017190," +
                "02004PM05O7101192192," +
                "110K91F4295210799N09," +
                "2141U50Z29910928C170," +
                "900A7120171R14296G59," +
                "11192029722504F00WN0";
        System.out.println(aa);
        String[] a = aa.split(",");
        System.out.println(Joiner.on("','").join(a));
    }
}
