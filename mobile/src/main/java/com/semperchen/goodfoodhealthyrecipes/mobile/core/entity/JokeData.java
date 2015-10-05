package com.semperchen.goodfoodhealthyrecipes.mobile.core.entity;

import java.util.List;

public class JokeData {
	public String status;       //返回状态，六个0表示成功 
	public String desc;		  //返回结果描述，六个0表示成功
	public List<Joke> detail;	  //具体笑话列表，是一个数组
	public class Joke{
		public String id;		  //数据库自增id，没有任何意义
		public String xhid;	  //笑话id，判断笑话新旧用的
		public String author;	  //笑话作者
		public String content;  //笑话内容
		public String picUrl;	  //笑话的图片（如果有）
		public String status;	  //笑话状态（能返回的都是1）
	
		public String toString() {
			return "[id:"+id+",xhid:"+xhid+",author:"+author+",content:"+content+",picUrl:"+picUrl+",status:"+status+"]";
		}
	}
}
