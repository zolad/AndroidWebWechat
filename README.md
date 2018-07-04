# AndroidWebWechat
AndroidWebWechat
==============
### 安卓版网页微信接口

Usage
==============

获取单例
```java

WebWeChatService mWebWeChatService = WebWeChatService.getInstance(context);

```
WebWeChatService提供的方法有:

（1）获取登录用的uuid
getNewLoginUuid((OnResponseResult<JsLoginResp> arg)

（2）获取登录需要扫描的二维码的图片
getQRcodePic()

（3）获取手机是否点击确定登录
IsPhoneConfirm(OnResponseResult<LoginResp> arg)

（4）登录
NewLogin(OnResponseResult<NewLoginPageResp> arg)

（5）网页微信初始化
WeChatInit(OnResponseResult<InitResp> arg)

（6）提醒手机端网页微信已登录
WxStatusNotify(String UserName,OnResponseResult<StatusNotifyResp> arg)

（7）获取联系人列表
GetContact(OnResponseResult<GetContactResp> arg)

（8）获取群详情
GetBatchContact(String[] groupUserNameArray,OnResponseResult<BatchGetContactResp> arg)
  
（9）获取好友头像链接链接
getUserHeadImage(String HeadImgUrl)

（10）获取群用户头像
getUserHeadImage(String username,String encrychatroomid)

（11）检查是否有新消息或信息状态刷新
SyncCheck(OnResponseResult<SyncCheckResp> arg)

（12）获取新消息
SyncMsg(OnResponseResult<WxSyncResp> arg)

（13）发送文字消息
SendTextMsg(String fromUserName, String toUserName, String content,
			OnResponseResult<SendAppMsgResp> resp)

（14）上传文件
UploadFile(File file, String FromUserName, String ToUserName,OnResponseResult<UploadFileResp> resp)

（15）发送文件
SendAppMsg(String fromUserName, String toUserName, String mediaId,String FileName, long FileSize,OnResponseResult<SendAppMsgResp> resp)

（16）下载文件
DownLoadAppMsg(String msgId, String attachid, String filename,String fromUserName, OnResponseResult<DownLoadFileResp> resp)

（17）获取语音消息的文件
GetVoice(String msgId, String fromUserName,OnResponseResult<DownLoadFileResp> resp)

（18）退出登录
Logout(int type,OnResponseResult<LogoutResp> arg)





