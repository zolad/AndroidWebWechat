package com.heimz.androidwebwechat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.heimz.androidwebwechatlib.api.WebWeChatService;
import com.heimz.androidwebwechatlib.api.WxContants;
import com.heimz.androidwebwechatlib.bean.AddMsgEntity;
import com.heimz.androidwebwechatlib.bean.ContactEntity;
import com.heimz.androidwebwechatlib.bean.OnResponseResult;
import com.heimz.androidwebwechatlib.bean.Resp.DownLoadFileResp;
import com.heimz.androidwebwechatlib.bean.Resp.GetContactResp;
import com.heimz.androidwebwechatlib.bean.Resp.InitResp;
import com.heimz.androidwebwechatlib.bean.Resp.JsLoginResp;
import com.heimz.androidwebwechatlib.bean.Resp.LoginResp;
import com.heimz.androidwebwechatlib.bean.Resp.LogoutResp;
import com.heimz.androidwebwechatlib.bean.Resp.NewLoginPageResp;
import com.heimz.androidwebwechatlib.bean.Resp.SendAppMsgResp;
import com.heimz.androidwebwechatlib.bean.Resp.StatusNotifyResp;
import com.heimz.androidwebwechatlib.bean.Resp.SyncCheckResp;
import com.heimz.androidwebwechatlib.bean.Resp.UploadFileResp;
import com.heimz.androidwebwechatlib.bean.Resp.WxSyncResp;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    RecordTool  mRecordTool;
    // @Bind(R.id.tv_msg)
    TextView texts,bigmsg;
    //@Bind(R.id.img_qrcode)
    Button btn_logout;
    ImageView qrcode;
    ListView listV,listV2;
    ContactAdapter madapter;
    ReceiveContentAdapter madapter2;
    WebWeChatService mWebWeChatService;
    InitResp  initResp ;
    ArrayList<AddMsgEntity> msgdata = new ArrayList<AddMsgEntity>();
    boolean isStop = false;
    private Handler handler = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        List<String> permissionReqlist = new ArrayList<String>();

        if (!PermissionUtil.isGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            permissionReqlist.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        }

        if (!PermissionUtil.isGranted(this, Manifest.permission.RECORD_AUDIO))
            permissionReqlist.add(Manifest.permission.RECORD_AUDIO);




        String[] reqarray = new String[permissionReqlist.size()];

        for (int i = 0; i <permissionReqlist.size(); i++) {
            reqarray[i] =  permissionReqlist.get(i);
        }

        if(reqarray.length>0)
             ActivityCompat.requestPermissions(this, reqarray, 100);


        // ButterKnife.bind(this);

        mWebWeChatService = WebWeChatService.getInstance(MainActivity.this);
        listV2  =(ListView)findViewById(R.id.listV2);
        listV= (ListView)findViewById(R.id.listV);
        texts = (TextView)findViewById(R.id.tv_msg);
        btn_logout = (Button)findViewById(R.id.btn_logout);
        madapter2 =  new ReceiveContentAdapter(MainActivity.this);

        madapter =  new ContactAdapter(MainActivity.this);



        listV.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub

                //UpLoadAppMsg(initResp.ContactList.get(arg2).UserName);

                ShowRecordDialog(initResp.ContactList.get(arg2).UserName);

            }
        });

        listV2.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub

                if(madapter2.getData()!=null&&madapter2.getData().get(arg2)!=null){

                    AddMsgEntity tmp = madapter2.getData().get(arg2);
                    if(tmp.MsgType == 49 && tmp.AppMsgType == 6&&tmp.FileName.endsWith(".mp3")){

                        mWebWeChatService.DownLoadAppMsg(tmp.MsgId, tmp.MediaId, tmp.FileName, tmp.FromUserName,new OnResponseResult<DownLoadFileResp>(){

                            @Override
                            public void onFail(int tag, String msg) {
                                // TODO Auto-generated method stub
                                Toast.makeText(MainActivity.this, "下载文件失败", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onSuccess(DownLoadFileResp result) {
                                // TODO Auto-generated method stub
                                Toast.makeText(MainActivity.this, "下载文件成功 ,开始播放", Toast.LENGTH_LONG).show();

                                mRecordTool.PlayWithFilePath(result.filepath);
                            }

                        });
                    }else if(tmp.MsgType == WxContants.MSGTYPE_VOICE){
                        mWebWeChatService.GetVoice(tmp.MsgId, tmp.FromUserName,new OnResponseResult<DownLoadFileResp>(){

                            @Override
                            public void onFail(int tag, String msg) {
                                // TODO Auto-generated method stub
                                Toast.makeText(MainActivity.this, "下载文件失败", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onSuccess(DownLoadFileResp result) {
                                // TODO Auto-generated method stub
                                Toast.makeText(MainActivity.this, "下载文件成功 ,开始播放", Toast.LENGTH_LONG).show();

                                mRecordTool.PlayWithFilePath(result.filepath);
                            }

                        });
                    }


                }

            }
        });






        btn_logout.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Logout(0);



            }
        });
        bigmsg= (TextView)findViewById(R.id.tv_bigmsg);
        qrcode = (ImageView)findViewById(R.id.img_qrcode);
        long value  = ~System.currentTimeMillis();
        long unsignedValue = value & Integer.MAX_VALUE;
        unsignedValue |= 0x80000000L;

        bigmsg.setText(unsignedValue+" "+value);




        GetUUid();


        mRecordTool =  new RecordTool(this);

    }

    public void GetUUid(){
        handler.removeCallbacksAndMessages(0);

        mWebWeChatService.cancelAllRequest();
        mWebWeChatService.clearData();

        mWebWeChatService.getNewLoginUuid(new OnResponseResult<JsLoginResp>() {

            @Override
            public void onSuccess(JsLoginResp result) {
                // TODO Auto-generated method stub
                ImageLoader.getInstance().displayImage(mWebWeChatService.getQRcodePic(), qrcode);
                texts.setText("uuid   "+result.window_QRLogin_uuid);
                handler.postDelayed(task,2000);//延迟调用

            }

            @Override
            public void onFail(int tag, String msg) {
                // TODO Auto-generated method stub

            }
        });
    }


    public void Redirect(){

        mWebWeChatService.Redirect(new OnResponseResult<NewLoginPageResp>() {

            @Override
            public void onSuccess(NewLoginPageResp result) {
                // TODO Auto-generated method stub
                texts.setText("pass_ticket "+result.pass_ticket);
                if(result.ret == 0){

                    initWeChat();

                }else{

                    texts.setText("redirect fail -1");
                }

            }



            @Override
            public void onFail(int tag, String msg) {
                // TODO Auto-generated method stub
                //WebWeChatService.getInstance().mClientData.mRedirectUrl
                texts.setText("redirect fail "+tag+" "+msg);
            }
        });
    }


    private void initWeChat() {
        // TODO Auto-generated method stub
        mWebWeChatService.WeChatInit(new OnResponseResult<InitResp>() {

            @Override
            public void onSuccess(InitResp result) {
                // TODO Auto-generated method stub
                //ImageLoader.getInstance().displayImage(result.User.HeadImgUrl, qrcode);

                //byte[] imageAsBytes = Base64.decode(result.User.HeadImgUrl, 0);
                //  qrcode.setImageBitmap(BitmapFactory.decodeByteArray(
                //                imageAsBytes, 0, imageAsBytes.length));

                if(result.BaseResponse.Ret==0){

                    initResp = result;

                    bigmsg.setText(JSON.toJSONString(result.User).toString()+" \n"+
                            result.User.HeadImgUrl

                    );
                    texts.setText(result.User.NickName+" ");
                    //ApplicationContext.initResult = result;





                    madapter.setData(result.ContactList);
                    //listV.addItemDecoration(new DividerItemDecoration(this,
                    //		DividerItemDecoration.VERTICAL_LIST));
                    //	listV.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    listV.setAdapter(madapter);
                    madapter.notifyDataSetChanged();

                    openStatusNotify(result.User.UserName);

                    SyncCheck();
                    //getContact();

                }else{
                    bigmsg.setText(JSON.toJSONString(result.BaseResponse));
                    texts.setText("初始化失败，可能是上一次登录没有退出");
                }
                //    GsonTool.getInstance().toJson(result).toString());
            }

            @Override
            public void onFail(int tag, String msg) {
                // TODO Auto-generated method stub
                texts.setText("wx init fail "+tag+" "+msg);
                //initWeChat() ;
            }
        });
    }



    public void Quit(){

        if(madapter!=null&&madapter.getData()!=null){
            Toast.makeText(this, "微信已退出", Toast.LENGTH_LONG).show();

            madapter.getData().clear();
            madapter.notifyDataSetChanged();
        }
        mWebWeChatService.clearData();
        mWebWeChatService.restartQueue();
        GetUUid();

        //finish();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        handler.removeCallbacksAndMessages(0);

    }


    //同步检查，获取是否有新消息和状态信息变更
    public void SyncCheck(){

        mWebWeChatService.SyncCheck(new OnResponseResult<SyncCheckResp>() {

            @Override
            public void onSuccess(SyncCheckResp result) {
                // TODO Auto-generated method stub
                switch(result.retcode){

                    case 0:
                        if(result.selector > 0)//大于0代表有新消息或者状态变更
                        {
                            texts.setText("有新消息");

                            SyncMsg();

                        }else if (result.selector == 0){
                            texts.setText("没新消息");

                            SyncCheck();


                        }else{

                            //目前为止没见过返回小于0的，以防万一重新检查
                            SyncCheck();
                        }



                        break;

                    case 1101: //被踢下线

                        Quit();
                        break;
                    default : //非0代表有错误，执行退出操作

                        Quit();
                        break;

                }
            }

            @Override
            public void onFail(int tag, String msg) {
                // TODO Auto-generated method stub
                texts.setText("检查更新信息失败");

                SyncCheck();

            }
        });



    }


    //获取新消息或状态变更详情
    private void SyncMsg() {
        // TODO Auto-generated method stub
        texts.setText("开始获取新信息");
        mWebWeChatService.SyncMsg(new OnResponseResult<WxSyncResp>() {

            @Override
            public void onSuccess(WxSyncResp result) {
                // TODO Auto-generated method stub
                //bigmsg.setText(JSON.toJSONString(result.AddMsgList).toString());

                //ArrayList<AddMsgEntity> data = new ArrayList<AddMsgEntity>();
                bigmsg.setText("获取信息成功");

                if(result.AddMsgCount>0){




                    for(AddMsgEntity entity:result.AddMsgList){
                        Log.e("syncmsg",entity.MsgType+" "+entity.SubMsgType+" "+entity.FileName);
                        //这里自己过滤一遍消息
                        //更多数值的意思看WXContants类
                        //WxContants.MSGTYPE_APP 文件消息
                        if((entity.MsgType == WxContants.MSGTYPE_TEXT|| entity.MsgType == WxContants.MSGTYPE_IMAGE||
                                entity.MsgType == WxContants.MSGTYPE_VIDEO	 || entity.MsgType == WxContants.MSGTYPE_VOICE
                                || entity.MsgType == WxContants.MSGTYPE_APP
                        )&&entity.SubMsgType == 0)
                            msgdata.add(0,entity);
                    }

                    madapter2.setData(msgdata);
                    listV2.setAdapter(madapter2);
                    madapter2.notifyDataSetChanged();

                }

                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        SyncCheck();
                    }
                }, 1000);

            }

            @Override
            public void onFail(int tag, String msg) {
                // TODO Auto-generated method stub
                texts.setText("获取更新信息失败");

                SyncCheck();

            }
        });
    }


    //获取联系人
    public void  getContact(){

        mWebWeChatService.GetContact(new OnResponseResult<GetContactResp>() {

            @Override
            public void onSuccess(GetContactResp result) {
                // TODO Auto-generated method stub
                //ImageLoader.getInstance().displayImage(result.User.HeadImgUrl, qrcode);

                //byte[] imageAsBytes = Base64.decode(result.User.HeadImgUrl, 0);
                //  qrcode.setImageBitmap(BitmapFactory.decodeByteArray(
                //                imageAsBytes, 0, imageAsBytes.length));

                if(result.BaseResponse.Ret==0){

                    bigmsg.setText(JSON.toJSONString(result.MemberList).toString());

                    madapter =  new ContactAdapter(MainActivity.this);

                    ArrayList<ContactEntity> tlist = new ArrayList<ContactEntity>();

                    for(ContactEntity tmps:result.MemberList){

                        if(tmps!=null&&tmps.ContactFlag == 2){
                            tlist.add(tmps);
                        }
                    }

                    madapter.setData(tlist);
                    //listV.addItemDecoration(new DividerItemDecoration(this,
                    //		DividerItemDecoration.VERTICAL_LIST));
                    //	listV.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    listV.setAdapter(madapter);
                    madapter.notifyDataSetChanged();

                }else{
                    bigmsg.setText(JSON.toJSONString(result.BaseResponse));
                    texts.setText("获取联系人失败");
                }
                //    GsonTool.getInstance().toJson(result).toString());
            }

            @Override
            public void onFail(int tag, String msg) {
                // TODO Auto-generated method stub
                texts.setText("get contact fail "+tag+" "+msg);
            }
        });




    }


    //用于提醒手机端网页端已登录
    public void openStatusNotify(String userName){

        mWebWeChatService.WxStatusNotify(userName,new OnResponseResult<StatusNotifyResp>() {

            @Override
            public void onSuccess(StatusNotifyResp result) {
                // TODO Auto-generated method stub
                texts.setText("开启消息通知成功");
            }

            @Override
            public void onFail(int tag, String msg) {
                // TODO Auto-generated method stub
                texts.setText("开启消息通知失败");
            }
        });
    }

    /**
     * type
     * 0 主动退出
     * 1 被踢掉线后要调一次退出
     * */
    public void Logout(int type){

        mWebWeChatService.Logout(type,new OnResponseResult<LogoutResp>() {

            @Override
            public void onSuccess(LogoutResp result) {
                // TODO Auto-generated method stub
                //texts.setText("登出成功");
                Toast.makeText(MainActivity.this, "退出登录成功", Toast.LENGTH_SHORT).show();

                Quit();
            }

            @Override
            public void onFail(int tag, String msg) {
                // TODO Auto-generated method stub
                texts.setText("登出失败");
            }
        });

    }


    //上传音频文件
    public void UpLoadAudioFile(final String ToUserName){

        //String filePath = Environment.getExternalStorageDirectory().toString() + File.separator + "test.mp3";
        String filePath = mRecordTool.mAudioFile.getAbsolutePath();
        final File file = new File(filePath);

        if(file.length() == 0||file.length() == 105){
            //当录音文件大小为0或105时代表录音失败，没录成功
            Toast.makeText(MainActivity.this, "录音失败，请重试!", Toast.LENGTH_SHORT).show();
            return;
        }

        mWebWeChatService.UploadAudioFile(file,initResp.User.UserName,ToUserName,new OnResponseResult<UploadFileResp>() {

            @Override
            public void onSuccess(UploadFileResp result) {
                // TODO Auto-generated method stub
                bigmsg.setText(JSON.toJSONString(result));
                Toast.makeText(MainActivity.this, "上传文件成功",Toast.LENGTH_SHORT).show();
                SendAppMsg(result.MediaId,ToUserName,"MyVoice.mp3",file.length());
            }

            @Override
            public void onFail(int tag, String msg) {
                // TODO Auto-generated method stub
                bigmsg.setText("上传文件失败 "+msg);
                Toast.makeText(MainActivity.this, "上传文件失败 ",  Toast.LENGTH_SHORT).show();
            }
        });

    }

    //发送APP消息

    public void SendAppMsg(String mediaId,String ToUserName,String FileName,long FileSize){



        mWebWeChatService.SendAppMsg(initResp.User.UserName, ToUserName, mediaId, FileName,FileSize, new OnResponseResult<SendAppMsgResp>() {

            @Override
            public void onSuccess(SendAppMsgResp result) {
                // TODO Auto-generated method stub
                Toast.makeText(MainActivity.this, "发送文件成功 "+result.MsgID,  Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(int tag, String msg) {
                // TODO Auto-generated method stub
                Toast.makeText(MainActivity.this, "发送文件失败 ",  Toast.LENGTH_SHORT).show();
            }
        });
    }



    AlertDialog alertDialog;
    private void ShowRecordDialog(final String userName){



        final String items[]={"开始录音","停止","播放"};
        //dialog参数设置
        AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("请录制你想发送的语音"); //设置标题
        //设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
        builder.setItems(items,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // dialog.dismiss();
                // Toast.makeText(MainActivity.this, items[which], Toast.LENGTH_SHORT).show();
                preventDismissDialog();
                if(which == 0){
                    mRecordTool.StartRecord();
                }else if(which == 1){
                    mRecordTool.StopRecord();
                }else if(which == 2){
                    mRecordTool.Play();
                }


            }
        });
        builder.setPositiveButton("发送",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismissDialog();
                if(mRecordTool.isRecording == false && mRecordTool.mAudioFile!=null)
                    UpLoadAudioFile(userName);
                // Toast.makeText(MainActivity.this, "确定", Toast.LENGTH_SHORT).show();
            }
        });


        alertDialog =  builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();



    }


    /**
     * 关闭对话框
     */
    private void dismissDialog() {
        try {
            Field field = alertDialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(alertDialog, true);
        } catch (Exception e) {
        }
        alertDialog.dismiss();
    }

    /**
     * 通过反射 阻止关闭对话框
     */
    private void preventDismissDialog() {
        try {
            Field field = alertDialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            //设置mShowing值，欺骗android系统
            field.set(alertDialog, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Runnable task =new Runnable() {
        public void run() {


            mWebWeChatService.IsPhoneConfirm(new OnResponseResult<LoginResp>() {

                @Override
                public void onSuccess(LoginResp result) {
                    // TODO Auto-generated method stub
                    texts.setText(result.window_code+"   "+result.window_redirect_uri);
                    ;
                    if(result.window_user_avatar!=null){

                        String  picData = result.window_user_avatar.substring(result.window_user_avatar.indexOf(",") + 1);
                        //ImageLoader.getInstance().displayImage(result.window_user_avatar, qrcode);

                        byte[] imageAsBytes = Base64.decode(picData.getBytes(), 0);
                        qrcode.setImageBitmap(BitmapFactory.decodeByteArray(
                                imageAsBytes, 0, imageAsBytes.length));
                    }
                    if(result.window_code == 201)
                        handler.postDelayed(task,2000);//延迟调用
                    else if(result.window_code == 408){

                        GetUUid();

                    }
                    else if(result.window_code == 200){

                        Redirect();

                    }

                }

                @Override
                public void onFail(int tag, String msg) {
                    // TODO Auto-generated method stub
                    handler.postDelayed(task,2000);//延迟调用
                }
            });
        }
    };
}

