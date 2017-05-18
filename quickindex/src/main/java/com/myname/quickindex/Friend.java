package com.myname.quickindex;

/**
 * Created by Administrator on 2017/1/23.
 */

public class Friend implements Comparable<Friend>{
    public String pinYin;
    public  String name;
    public Friend(String name){
        this.name = name;
        this.pinYin = PinYinUtil.getPinYin(name);
    }

    @Override
    public int compareTo(Friend friend) {
        return this.pinYin.compareTo(friend.pinYin);
    }
}
