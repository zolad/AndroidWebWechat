package com.heimz.androidwebwechat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.heimz.androidwebwechatlib.api.WebWeChatService;
import com.heimz.androidwebwechatlib.bean.ContactEntity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

public class ContactAdapter extends BaseAdapter {


    Context context;
    private List<ContactEntity> data;
    Options decodingOptions;
    DisplayImageOptions po;

    public ContactAdapter(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        decodingOptions = new Options();
        DisplayImageOptions m_options =
                new DisplayImageOptions.Builder()
                        //.showImageOnLoading(R.drawable.ic_loading)
                        .decodingOptions(decodingOptions)
                        .imageScaleType(ImageScaleType.NONE)
                        .build();

        po  = new DisplayImageOptions.Builder()
//		 .showImageOnLoading(R.drawable.new_avatar) // 设置图片在下载期间显示的图片
//		 .showImageForEmptyUri(R.drawable.new_avatar)// 设置图片Uri为空或是错误的时候显示的图片
//		 .showImageOnFail(R.drawable.new_avatar) // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
                .decodingOptions(decodingOptions)// 设置图片的解码配置
                // .delayBeforeLoading(int delayInMillis)//int
                // delayInMillis为你设置的下载前的延迟时间
                // 设置图片加入缓存前，对bitmap进行设置
                // .preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(false)// 设置图片在下载前是否重置，复位
                // .displayer(new SimpleBitmapDisplayer())// 是否设置为圆角，弧度为多少
                // .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();// 构建完成
    }

    public List<ContactEntity> getData() {
        return data;
    }

    public void setData(List<ContactEntity> data) {
        this.data = data;
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
            convertView=View.inflate(context, R.layout.item,null);
            viewHolder=new ViewHolder();
            viewHolder.tv = (TextView) convertView.findViewById(R.id.tv);
            viewHolder.im = (ImageView) convertView.findViewById(R.id.im);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder) convertView.getTag();
        }

        ContactEntity tmp  =  data.get(position);

      //  viewHolder.im.setImageResource(context.getResources().getColor(R.color.color_gray));

        viewHolder.tv.setText(data.get(position).NickName+"");
        final String imagepath = "https://" + WebWeChatService.getInstance(context).mClientData.mHost + data.get(position).HeadImgUrl;
        viewHolder.im.setTag(imagepath);
        ImageRequest imageRequest = new ImageRequest(
                imagepath,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {

                        if(viewHolder.im.getTag()==null||viewHolder.im.getTag().equals(imagepath))
                            viewHolder.im.setImageBitmap(response);
                    }
                }, 0, 0, Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //imageView.setImageResource(R.drawable.default_image);
            }
        });

        WebWeChatService.getInstance(context).addRequest(imageRequest);
        //ImageLoader.getInstance().loadImage(uri,



//		displayImage//(uri, imageView, options)   displayImage(
//				(
//				//WebWeChatService.getInstance().urlInterface.getHeadImage(tmp.UserName)
//				"http://" + WebWeChatService.getInstance().mClientData.mHost + data.get(position).HeadImgUrl
//
//				,viewHolder.im,po);

        return convertView;
    }


    class ViewHolder
    {

        TextView tv;
        ImageView im;

//        public MyViewHolder(View view)
//        {
//            super(view);
//            tv = (TextView) view.findViewById(R.id.tv);
//            im = (ImageView) view.findViewById(R.id.im);
//        }
    }

}
