package com.myname.quickindex;

import android.text.TextUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

/**
 * 根据汉字获取汉字的拼音
 */

public class PinYinUtil  {
    /**
     * 获取汉字的拼音
     * @param chinese
     * @return
     */
    public static String getPinYin(String chinese){
        if(TextUtils.isEmpty(chinese))return null;

        //拼音转换的格式化,主要控制字母的大小写，以及是否需要声调
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);//设置大写字母
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//不需要声调

        //ps:由于不支持对多个汉字进行获取，所以要将字符串转为字符数组，对单个汉字进行获取
        //最后，将每个字的拼音拼接起来，就是所有汉字的拼音
        StringBuilder builder = new StringBuilder();

        char[] chars = chinese.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];

            //1.要进行过滤空格,选择忽略
            if(Character.isWhitespace(c)){
                continue;
            }

            //2.要判断是否是中文，粗略的判断一下：由于一个汉字2个字节，
            //一个字节范围是-128~127,因此汉字肯定大于127
            if(c > 127){
                //有可能是汉字,就利用pinyin4j进行获取
                try {
                    //由于多音字的存在，所以返回的是数组，比如单：[chan, dan, shan]
                    String[] arr = PinyinHelper.toHanyuPinyinStringArray(c, format);
                    if(arr!=null){
                        //此处只能用第0个，原因：
                        //1.首先大部分汉字只有一个读音，多音字属于少数
                        //2.其次，我们也确实无能为力去判断应该用哪个，要判断一个汉字在一串文字
                        //中的精确读音，至少需要几个技术：a.分词算法 b.非常庞大的分词数据库
                        builder.append(arr[0]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //说明不是正确的汉字，选择忽略
                }
            }else {
                //肯定不是汉字，一般是ASCII码表中的字母，对于这个情况，我们选择
                //直接拼接
                builder.append(c);
            }
        }
        return  builder.toString();
    }
}
