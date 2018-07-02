package com.heimz.androidwebwechat;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.heimz.androidwebwechatlib.api.WxContants;
import com.heimz.androidwebwechatlib.bean.AddMsgEntity;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.ArrayList;

public class ReceiveContentAdapter  extends BaseAdapter {

    private ArrayList<AddMsgEntity> data;
    Context context;

    public ReceiveContentAdapter(Context context) {

        this.context = context;

    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder viewHolder;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.msgitem,null);
            viewHolder=new ViewHolder();
            viewHolder.tv = (TextView) convertView.findViewById(R.id.tv);


            convertView.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder) convertView.getTag();
        }

        AddMsgEntity tmp = data.get(position);

        String contentstr = "";
        if(tmp.FromUserName.startsWith("@@")){  //群消息

            contentstr = StringEscapeUtils.unescapeHtml(tmp.Content).replaceAll("<br/>", "");
        }else{

            contentstr = StringEscapeUtils.unescapeHtml(tmp.Content).replaceAll("<br/>", "");
        }
        switch(tmp.MsgType){
            case WxContants.MSGTYPE_TEXT:
                viewHolder.tv.setText(tmp.Content);
                break;

            case WxContants.MSGTYPE_APP:

                if(tmp.AppMsgType == 6 && tmp.FileName.startsWith("cldvoice")){
                    try {
                        viewHolder.tv.setText("凯立德语音消息，点击播放:"+tmp.FileName);
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        viewHolder.tv.setText("凯立德语音消息，点击播放");
                        e1.printStackTrace();
                    }
                }else{
                    viewHolder.tv.setText("app文件消息");//+StringEscapeUtils.unescapeHtml(tmp.Content));
                }
                break;
            case WxContants.MSGTYPE_IMAGE:
                try {
                    viewHolder.tv.setText("有一条图片消息:"+tmp.FileName);
                    //new String(tmp.Content.getBytes("utf-8"),"utf-8"));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    viewHolder.tv.setText("有一条图片消息");
                    e.printStackTrace();
                }
                break;
            case WxContants.MSGTYPE_EMOTICON:
                viewHolder.tv.setText("有一条颜文字消息");
                break;
            case WxContants.MSGTYPE_VOICE:
                viewHolder.tv.setText("有一条语音消息,点击播放");
                break;
            case WxContants.MSGTYPE_VIDEO:
                viewHolder.tv.setText("有一条视频消息");
                break;
            default:
                viewHolder.tv.setText(contentstr);
                break;

        }

        return convertView;
    }


    class ViewHolder
    {

        TextView tv;
//       ImageView im;

//        public MyViewHolder(View view)
//        {
//            super(view);
//            tv = (TextView) view.findViewById(R.id.tv);
//            im = (ImageView) view.findViewById(R.id.im);
//        }
    }

    public ArrayList<AddMsgEntity> getData() {
        return data;
    }

    public void setData(ArrayList<AddMsgEntity> data) {
        this.data = data;
    }

}
