package com.zhuxiaoxue.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.UnsupportedEncodingException;

public class Strings {

    public static String toUTF8(String str){
        if(str != null){
            try {
                return new String(str.getBytes("ISO8859-1"),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("字符串转码异常");
            }
        }
        return "";
    }

    public static String toPinyin(String str){
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        String pingyin = null;
        try {
            pingyin = PinyinHelper.toHanYuPinyinString(str,format,"",true);
            return pingyin;
        } catch (BadHanyuPinyinOutputFormatCombination ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
}
