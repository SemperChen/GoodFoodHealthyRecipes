package com.semperchen.goodfoodhealthyrecipes.mobile.core.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by abc on 2015/10/5.
 */
public class IntensionData implements Serializable{
    public  String showapi_res_code;
    public String showapi_res_error;
    public Body showapi_res_body;
    public class Body{
        public String ret_code;
        public Bean pagebean;
        public class Bean{
            public String allNum;              //所有数量
            public String allPages;            //所有页
            public String currentPage;         //当前页
            public String maxResult;           //每页最大数量
            public List<Detail> contentlist;   //条目列表
            public class Detail{
                public String create_time;     //创建时间
                public String hate;             //点踩得数量
                public String love;             //点赞的数量
                public String height;           //图片高度
                public String width;            //图片宽度
                public String id;               //图片id
                public String name;            //作者名称
                public String profile_image; //作者头像
                public String image0;          //0号图，数字越大，尺寸越大
                public String image1;          //1号图
                public String image2;          //2号图
                public String image3;          //3号图
                public String is_gif;          //是否gif
                public String text;            //段子正文
                public String type;            //类型
                public String videotime;      //视频时长
                public String video_uri;       //视频url
                public String voicelength;   //声音文件大小
                public String voicetime;      //声音时长
                public String voiceuri;       //声音url
                public String weixin_url;    //请填写参数描述
            }
        }
    }
}
