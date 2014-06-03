package org.tang.exam.utils;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Comparator;

import org.tang.exam.entity.ChatMsgEntity;

public class Comparators implements Comparator{

	@Override
	public int compare(Object ob1, Object ob2) {
		Collator collator = Collator.getInstance(); //调入这个是解决中文排序问题 
		ChatMsgEntity c1 = (ChatMsgEntity) ob1 ;
		ChatMsgEntity c2 = (ChatMsgEntity) ob2 ;
        String createTime1 = (String) c1.getCreateTime();
        String createTime2 = (String) c2.getCreateTime(); 
        CollationKey key1 = collator.getCollationKey(createTime1.toLowerCase()); 
        CollationKey key2 = collator.getCollationKey(createTime2.toLowerCase()); 
        return key1.compareTo(key2); 
	}
	
}
