package com.semperchen.goodfoodhealthyrecipes.mobile.core.entity;

import java.util.List;

public class JokeData {
	public String status;       //����״̬������0��ʾ�ɹ� 
	public String desc;		  //���ؽ������������0��ʾ�ɹ�
	public List<Joke> detail;	  //����Ц���б���һ������
	public class Joke{
		public String id;		  //���ݿ�����id��û���κ�����
		public String xhid;	  //Ц��id���ж�Ц���¾��õ�
		public String author;	  //Ц������
		public String content;  //Ц������
		public String picUrl;	  //Ц����ͼƬ������У�
		public String status;	  //Ц��״̬���ܷ��صĶ���1��
	
		public String toString() {
			return "[id:"+id+",xhid:"+xhid+",author:"+author+",content:"+content+",picUrl:"+picUrl+",status:"+status+"]";
		}
	}
}
