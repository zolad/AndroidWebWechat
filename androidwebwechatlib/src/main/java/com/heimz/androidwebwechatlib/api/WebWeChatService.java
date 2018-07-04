

package com.heimz.androidwebwechatlib.api;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.heimz.androidwebwechatlib.bean.ClientData;
import com.heimz.androidwebwechatlib.bean.OnResponseResult;
import com.heimz.androidwebwechatlib.bean.Resp.BatchGetContactResp;
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
import com.heimz.androidwebwechatlib.bean.Resp.errorlistener.OnErrorListener;
import com.heimz.androidwebwechatlib.bean.Resp.listener.DownLoadAppMsgListener;
import com.heimz.androidwebwechatlib.bean.Resp.listener.GetBatchContactListener;
import com.heimz.androidwebwechatlib.bean.Resp.listener.GetContactListener;
import com.heimz.androidwebwechatlib.bean.Resp.listener.IsPhoneConfirmListener;
import com.heimz.androidwebwechatlib.bean.Resp.listener.LogoutListener;
import com.heimz.androidwebwechatlib.bean.Resp.listener.NewLoginListener;
import com.heimz.androidwebwechatlib.bean.Resp.listener.RedirectListener;
import com.heimz.androidwebwechatlib.bean.Resp.listener.SendMsgListener;
import com.heimz.androidwebwechatlib.bean.Resp.listener.UploadAppMsgListener;
import com.heimz.androidwebwechatlib.bean.Resp.listener.WeChatInitListener;
import com.heimz.androidwebwechatlib.bean.Resp.listener.WeChatStatusNotifyListener;
import com.heimz.androidwebwechatlib.bean.Resp.listener.WeChatSyncCheckListener;
import com.heimz.androidwebwechatlib.bean.Resp.listener.WeChatSyncMsgListener;
import com.heimz.androidwebwechatlib.bean.SyncKeyEntity;
import com.heimz.androidwebwechatlib.http.ByteRequest;
import com.heimz.androidwebwechatlib.http.JsonRequest;
import com.heimz.androidwebwechatlib.http.OkHurlStack;
import com.heimz.androidwebwechatlib.http.PersistentCookieStore;
import com.heimz.androidwebwechatlib.http.UploadAppMsgRequest;
import com.heimz.androidwebwechatlib.util.GsonTool;
import com.heimz.androidwebwechatlib.util.MD5Tool;
import com.heimz.androidwebwechatlib.util.MLog;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


//添加网页微信网络请求的工具类
public class WebWeChatService {
	
	
	public static final String TAG = WebWeChatService.class.getSimpleName();
	public RequestQueue mRequsetQueue;
	public ClientData mClientData;
	// public OkHurlStack mHurlStack;
	public WeChatUrl mUrlInterface;
	private volatile static WebWeChatService instance;
	public PersistentCookieStore mCookieStore;
	//private List mlist;

	public WebWeChatService(Context context) {

		init(context);

		this.mClientData = new ClientData();
		this.mUrlInterface = new WeChatUrl(this.mClientData);

		this.mCookieStore = new PersistentCookieStore(
				ApplicationContext.getContext());
		OkHttpClient mOkHttpClient = new OkHttpClient();
		mOkHttpClient.setCookieHandler(new CookieManager(this.mCookieStore,
				CookiePolicy.ACCEPT_ALL));
		this.mRequsetQueue = Volley.newRequestQueue(ApplicationContext.getContext(),
				new OkHurlStack(mOkHttpClient));
		this.mRequsetQueue.start();
	}

	public static void init(Application context) {
		ApplicationContext.init(context);
	}

	public static void init(Context context) {
		ApplicationContext.init(context.getApplicationContext());
	}
	
	public static WebWeChatService getInstance(Context context) {
		if (instance == null) {
			synchronized (WebWeChatService.class) {
				if (instance == null) {
					instance = new WebWeChatService(context);
				}
			}
		}

		return instance;
	}

	public void addRequest(Request request){
		this.mRequsetQueue.add(request);
	}

	public void cancalRequest(String requestId) {
		this.mRequsetQueue.cancelAll(requestId);
	}

	static PersistentCookieStore getCookieStore(WebWeChatService arg1) {
		return arg1.mCookieStore;
	}

	public void retart() {
		restartQueue();

	}

	public void clearData() {
		this.mCookieStore.clearCookies();
		this.mCookieStore.removeAll();
		this.mClientData.clear();
	}

	public void restartQueue() {
		this.mRequsetQueue.stop();
		this.mRequsetQueue.cancelAll(new RequestQueue.RequestFilter() {

			@Override
			public boolean apply(Request<?> arg3) {
				// TODO Auto-generated method stub
				boolean v0 = arg3.getTag() == null
						|| !arg3.getTag().equals("UN_FILTER") ? true : false;
				return v0;
			}
		});
		this.mRequsetQueue.start();
	}

	public void cancelAllRequest() {

		this.mRequsetQueue.cancelAll(new RequestQueue.RequestFilter() {

			@Override
			public boolean apply(Request<?> arg3) {
				// TODO Auto-generated method stub
				boolean v0 = arg3.getTag() == null
						|| !arg3.getTag().equals("UN_FILTER") ? true : false;
				return v0;
			}
		});

	}

	public String getQRcodePic() {
		return "https://login.weixin.qq.com/qrcode/" + this.mClientData.mUUID;
	}

	public String getSyncKeyString() {
		StringBuilder v2 = new StringBuilder();
		try {
			if (this.mClientData.mSyncKey == null) {
				return v2.toString();
			}

			List<SyncKeyEntity.ListEntity> v3 = this.mClientData.mSyncKey.List;
			int v1;
			for (v1 = 0; v1 < v3.size(); ++v1) {
				if (v1 > 0) {
					v2.append('|');
				}

				v2.append(v3.get(v1).Key).append("_").append(v3.get(v1).Val);
			}
		} catch (Exception v0) {
		}
		return v2.toString();

	}



	public boolean readData() {
		boolean isSuccess = false;
		ObjectInputStream v1 = null;
		File v3 = new File(Environment.getExternalStorageDirectory()
				+ "/webwechat/.backup");
		if (!v3.exists()) {
			return false;
		}
		try {
			v1 = new ObjectInputStream(new FileInputStream(v3));

			mClientData = (ClientData) v1.readObject();
			mUrlInterface = new WeChatUrl(this.mClientData);
			isSuccess = true;

			v1.close();
			v3.delete();
		} catch (Exception v0) {
			MLog.e("readclientdata", v0.toString());

		} finally {

			try {
				v1.close();
				v3.delete();
			} catch (Exception v1_1) {
				MLog.e("readclientdata", v1_1.toString());
			}
		}

		return isSuccess;
	}

	public boolean saveData(String arg6) {

		ObjectOutputStream out = null;
		File path = new File(Environment.getExternalStorageDirectory()
				+ "/webwechat/.backup");
		try {
			if (!path.getParentFile().exists()) {
				if (!path.getParentFile().mkdirs()) {
					return false;
				}
			}

			if (!path.exists()) {
				path.createNewFile();
			}
			out = new ObjectOutputStream(new FileOutputStream(path));
			out.writeObject(mClientData);

		} catch (Exception e) {
			MLog.e("readclientdata", e.toString());
			return false;
		} finally {
			try {
				out.close();
			} catch (Exception e) {
				MLog.e("readclientdata", e.toString());
			}
		}

		return true;

	}

	// getuuid
	public void getNewLoginUuid(OnResponseResult<JsLoginResp> arg5) {
		StringRequest v1 = new StringRequest(
				mUrlInterface.getNewLoginUuid(), new NewLoginListener(
						this, arg5), new OnErrorListener(arg5));
		v1.setShouldCache(false);
		mRequsetQueue.add(v1);
	}

	// IsPhoneConfirm
	public void IsPhoneConfirm(OnResponseResult<LoginResp> arg6) {
		StringRequest v1 = new StringRequest(
				mUrlInterface.IsPhoneConfirm(),
				new IsPhoneConfirmListener(this, arg6), new OnErrorListener(
						arg6));
		v1.setShouldCache(false);
		v1.setRetryPolicy(new DefaultRetryPolicy(60000, 1, 1f));
		mRequsetQueue.add(v1);
	}

	// Redirect
	public void Redirect(OnResponseResult<NewLoginPageResp> arg5) {

		// client.restartQueue();

		StringRequest v1 = new StringRequest(mClientData.mRedirectUrl,
				new RedirectListener(this, arg5), new OnErrorListener(arg5));
		v1.setShouldCache(false);
		v1.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1f));
		mRequsetQueue.add(v1);
	}

	// WeChatInit
	public void WeChatInit(OnResponseResult<InitResp> arg7) {
		// MLog.d("jsonrequest",urlInterface.WebWxInitInform()
		// +"\n"+urlInterface.getIdentifyParams().toString());

		JsonRequest v0 = new JsonRequest(Method.POST,
				mUrlInterface.WebWxInitInform(),
				mUrlInterface.getIdentifyParams(),
				new WeChatInitListener(this, arg7), new OnErrorListener(arg7));
		v0.setShouldCache(false);
		v0.setRetryPolicy(new DefaultRetryPolicy(15000, 1, 1f));
		v0.setTag("webwxinit");
		mRequsetQueue.add(v0);
	}

	// getBatchContact
	public void GetBatchContact(String[] groupUserNameArray,
			OnResponseResult<BatchGetContactResp> arg9) {
		String v3 = mUrlInterface.getBatchContact();
		JSONObject v1 = mUrlInterface.getIdentifyParams();
		try {
			v1.put("Count", groupUserNameArray.length);
			JSONArray v4 = new JSONArray();
			int v0_1;
			for (v0_1 = 0; v0_1 < groupUserNameArray.length; ++v0_1) {
				JSONObject v5 = new JSONObject();
				v5.put("UserName", groupUserNameArray[v0_1]);
				v5.put("ChatRoomId", "");
				v5.put("EncryChatRoomId", "");
				v4.put(v5);
			}

			v1.put("List", v4);
		} catch (Exception v0) {
			MLog.e(TAG, "WebWeChatService::webwxbatchgetcontact, make entity Failed");
			return;
		}

		JsonRequest v0_2 = new JsonRequest(Method.POST, v3, v1,
				new GetBatchContactListener(this, arg9), new OnErrorListener(
						arg9));
		v0_2.setShouldCache(true);
		v0_2.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1f));
		mRequsetQueue.add(v0_2);

	}

	public void GetContact(OnResponseResult<GetContactResp> arg7) {
		JsonRequest v0 = new JsonRequest(Method.GET,
				mUrlInterface.getContact(), (String) null,
				new GetContactListener(this, arg7), new OnErrorListener(arg7));
		v0.setShouldCache(false);
		v0.setRetryPolicy(new DefaultRetryPolicy(20000, 1, 1f));
		mRequsetQueue.add(v0);
	}

	public void WxStatusNotify(String UserName,OnResponseResult<StatusNotifyResp> arg7
			) {
		String v2 = mUrlInterface.WxStatusNotify();
		JSONObject v3 = mUrlInterface.getIdentifyParams();
		try {
			// Contact v0_1 = ax.a().b();
			v3.put("ClientMsgId", System.currentTimeMillis());
			v3.put("Code", 3);
			v3.put("FromUserName", UserName);
			v3.put("ToUserName", UserName);
		} catch (Exception v0) {
			MLog.e("make entity Failed",v0.toString());
			// return;
		}

		JsonRequest v0_2 = new JsonRequest(Method.POST, v2, v3,
				new WeChatStatusNotifyListener(this, arg7),
				new OnErrorListener(arg7));
		v0_2.setShouldCache(false);
		mRequsetQueue.add(v0_2);
	}

	public void SyncCheck(OnResponseResult<SyncCheckResp> arg7) {
		mRequsetQueue.cancelAll("synccheck");

		StringRequest v0 = new StringRequest(Method.GET,
				mUrlInterface.SyncCheck(getSyncKeyString(),0), new WeChatSyncCheckListener(this, arg7),
				new OnErrorListener(arg7)) {

			public Priority getPriority() {
				return Priority.HIGH;
			}
		};
		v0.setShouldCache(false);
		v0.setRetryPolicy(new DefaultRetryPolicy(60000, 1, 1f));
		v0.setTag("synccheck");
		mRequsetQueue.add(v0);
	}

	public void SyncMsg(OnResponseResult<WxSyncResp> arg8) {
		mRequsetQueue.cancelAll("syncmsg");
		String v3 = mUrlInterface.SyncMsg();
		JSONObject v4 = mUrlInterface.getIdentifyParams();
		try {
			v4.put("SyncKey",
					new JSONObject(GsonTool.getInstance().toJson(mClientData.mSyncKey)));
			v4.put("rr", mUrlInterface.getNOTTimeStamp());
		} catch (Exception v0) {
			MLog.e("WebWeChatService::webwxsync", "make entity Failed");
			// return;
		}
		MLog.d("syncmsg", "url:" + v3);
		MLog.d("syncmsg", "data:" + v4.toString());
		JsonRequest v0_1 = new JsonRequest(Method.POST, v3, v4,
				new WeChatSyncMsgListener(this, arg8), new OnErrorListener(
						arg8));
		v0_1.setShouldCache(false);
		v0_1.setTag("syncmsg");
		mRequsetQueue.add(v0_1);
	}

	public void Logout(int type,OnResponseResult<LogoutResp> arg9) {

		cancelAllRequest();

		String v2 = mUrlInterface.Logout(type);
		StringBuilder v3 = new StringBuilder();
		v3.append("sid=").append(mClientData.mWxSid);
		v3.append("&uin=").append(mClientData.mWxUin);
		StringBuilder v4 = new StringBuilder();
		int v1;
		for (v1 = 0; v1 < WebWeChatService.getCookieStore(this).getCookies()
				.size(); ++v1) {
			Object v0 = WebWeChatService.getCookieStore(this).getCookies()
					.get(v1);
			v4.append(((HttpCookie) v0).getName() + "="
					+ ((HttpCookie) v0).getValue());
			if (v1 != WebWeChatService.getCookieStore(this).getCookies().size() - 1) {
				v4.append("; ");
			}
		}

		HashMap<String, String> v0_1 = new HashMap<String, String>();
		v0_1.put("Cookie", v4.toString());

		ByteRequest v0_2 = new ByteRequest(Method.POST, v2, v3.toString(),
				new LogoutListener(this, arg9), new OnErrorListener(arg9));

		v0_2.setShouldCache(false);
		mRequsetQueue.add(v0_2);
	}

	

	// 发送文字
	public void SendTextMsg(String fromUserName, String toUserName, String content,
			OnResponseResult<SendAppMsgResp> resp) {
		String v2 = mUrlInterface.SendTextMsg();
		JSONObject v4 = null;
		try {

			long time = System.currentTimeMillis();

			String msgcontent = "{\"Scene\":0," + "\"BaseRequest\":{\"Uin\":"
					+ mClientData.mWxUin
					+ ",\"Sid\":\""
					+ mClientData.mWxSid
					+ "\",\"Skey\":\""
					+ mClientData.mWxSkey
					+ "\",\"DeviceID\":\"e823469202135602\"},\"Msg\":{"
					+ "\"Content\":"+content+",\"FromUserName\":\""
					+ fromUserName + "\",\"ToUserName\":\"" + toUserName
					+ "\",\"LocalID\":\"" + time + "\",\"ClientMsgId\":\""
					+ time + "\"}}";
	
			v4 = new JSONObject(msgcontent);

		} catch (Exception v0) {
			MLog.e("sendtextmsg", "make entity Failed");
			return;
		}
		

		
		JsonRequest v0_1 = new JsonRequest(Method.POST, v2, v4,
				new SendMsgListener(this, resp), new OnErrorListener(resp));
		v0_1.setShouldCache(false);

		v0_1.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1f));
		mRequsetQueue.add(v0_1);
		
		

	}

//	// 发送图片
//	public void SendImgMsg() {
//
//	}


	// 上传音频文件
	public boolean UploadAudioFile(
			File file, String FromUserName, String ToUserName,
			OnResponseResult<UploadFileResp> onresult) {

		String v3 = mUrlInterface.upLoadAppMsg();
		ByteArrayOutputStream v6 = new ByteArrayOutputStream();

		StringBuilder v0 = new StringBuilder();

		String boundary = "---------------------------acebdf13572468";
		String freFix = "--";
		String newLine = "\r\n";

		SimpleDateFormat sdf = new SimpleDateFormat(
				"EEE MMM dd yyyy HH:mm:ss 'GMT'Z", Locale.ENGLISH);
		String timeStr = sdf.format(new Date(file.lastModified()));
	

		try {
			String deviceId = TextUtils.isEmpty(mClientData.mDeviceId) ? mUrlInterface
					.getRandomNumString(15) : mClientData.mDeviceId;

			v0.append(freFix + boundary).append(newLine);
			v0.append("Content-Disposition: form-data; name=\"id\"");
			v0.append(newLine).append(newLine);
			v0.append("WU_FILE_").append(ApplicationContext.FileIndex)
					.append(newLine);
			;

			v0.append(freFix + boundary).append(newLine);
			v0.append("Content-Disposition: form-data; name=\"name\"");
			v0.append(newLine).append(newLine);
			v0.append(file.getName()).append(newLine);
			;

			v0.append(freFix + boundary).append(newLine);
			v0.append("Content-Disposition: form-data; name=\"type\"");
			v0.append(newLine).append(newLine);
			v0.append("audio/mpeg").append(newLine); // audio/mpeg
														// //application/octet-stream

			v0.append(freFix + boundary).append(newLine);
			v0.append("Content-Disposition: form-data; name=\"lastModifiedDate\"");
			v0.append(newLine).append(newLine);
			v0.append(timeStr).append(newLine);

			v0.append(freFix + boundary).append(newLine);
			v0.append("Content-Disposition: form-data; name=\"size\"");
			v0.append(newLine).append(newLine);
			v0.append(file.length()).append(newLine);

			//
			v0.append(freFix + boundary).append(newLine);
			v0.append("Content-Disposition: form-data; name=\"mediatype\"");
			v0.append(newLine).append(newLine);
			v0.append("doc").append(newLine);

			//
			v0.append(freFix + boundary).append(newLine);
			v0.append("Content-Disposition: form-data; name=\"uploadmediarequest\"");
			v0.append(newLine).append(newLine);
			v0.append(
					"{\"UploadType\":2,\"BaseRequest\":{\"Uin\":"
							+ mClientData.mWxUin
							+ ",\"Sid\":\""
							+ mClientData.mWxSid
							+ "\",\"Skey\":\"@"
							+ mClientData.mWxSkey
							+ "\",\"DeviceID\":\"e823469202135602\"},\"ClientMediaId\":"
							+ System.currentTimeMillis() + ",\"TotalLen\":"
							+ file.length() + ",\"StartPos\":0,\"DataLen\":"
							+ file.length()
							+ ",\"MediaType\":4,\"FromUserName\":\""
							+ FromUserName + "\",\"ToUserName\":\""
							+ ToUserName + "\",\"FileMd5\":\"" + MD5Tool.getFileMD5(file)
							+ "\"}").append(newLine);
			// ** ,\"FileMd5\":"+fileMd5+"} **
			// Long.parseLong(clientData.mWxUin));
			// v0.put("Sid", clientData.mWxSid);
			// v0.put("Skey", clientData.mWxSkey);

			//
			v0.append(freFix + boundary).append(newLine);
			v0.append("Content-Disposition: form-data; name=\"webwx_data_ticket\"");
			v0.append(newLine).append(newLine);
			v0.append(mCookieStore.getWebwxDataTicketFromCookie())
					.append(newLine);

			//
			v0.append(freFix + boundary).append(newLine);
			v0.append("Content-Disposition: form-data; name=\"pass_ticket\"");
			v0.append(newLine).append(newLine);
			v0.append(mClientData.mPassTicket).append(newLine);

			//
			v0.append(freFix + boundary).append(newLine);
			v0.append("Content-Disposition: form-data; name=\"filename\"; filename=\""
					+ file.getName() + "\"");
			v0.append(newLine);
			v0.append("Content-Type: audio/mpeg");
			v0.append(newLine).append(newLine);

			v6.write(v0.toString().getBytes("UTF-8"));
			
			
			
			
			FileInputStream v0_2 = new FileInputStream(file);
			byte[] v2 = new byte[2048];
			while (true) {
				int v4 = v0_2.read(v2);
				if (v4 <= 0) {
					break;
				}

				v6.write(v2, 0, v4);
			}

			v0_2.close();
			v0 = new StringBuilder();
			v0.append(newLine);
			v0.append("-----------------------------acebdf13572468--\r\n\r\n");
			v6.write(v0.toString().getBytes("UTF-8"));

			// Iterator<Map.Entry<String, String>>
			// iter=client.mCookieStore.iterator();
			// while(iter.hasNext()){
			// Map.Entry<String, String> me=iter.next();
			// System.out.println(me.getKey()+ " "+me.getValue());
			// }
			//
			// client.mCookieStore.getCookies().t

			// MLog.d("uploadappmsg",client.mCookieStore.getWebwxDataTicketFromCookie());

			MLog.d("uploadappmsg", new String(v6.toByteArray()));
		} catch (Exception v0_1) {
			MLog.e(TAG, v0_1.toString());

			v0_1.printStackTrace();
			return false;
		}

		int s = Method.OPTIONS;

		UploadAppMsgRequest v0_1 = new UploadAppMsgRequest(Method.POST, v3,
				new UploadAppMsgListener(this, onresult),
				new OnErrorListener(onresult), v6);
		v0_1.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1f));
		mRequsetQueue.add(v0_1);

		return true;
	}

	public void SendAppMsg(String fromUserName, String toUserName, String mediaId,
			String FileName, long FileSize,
			OnResponseResult<SendAppMsgResp> resp) {

		String v3 = mUrlInterface.SendAppMsg();
		JSONObject v4 = null;// urlInterface.getIdentifyParams();

		JSONObject msg = new JSONObject();
		String msgstr;

		try {

			long time = System.currentTimeMillis();

			String msgcontent = "{\"Scene\":0," + "\"BaseRequest\":{\"Uin\":"
					+ mClientData.mWxUin
					+ ",\"Sid\":\""
					+ mClientData.mWxSid
					+ "\",\"Skey\":\""
					+ mClientData.mWxSkey
					+ "\",\"DeviceID\":\"e823469202135602\"},\"Msg\":{\"Type\":6,"
					+ "\"Content\":\"<appmsg appid=\'wx782c26e4c19acffb\' sdkver=\'\'><title>"
					+ FileName
					+ "</title><des></des><action></action><type>6</type><content></content>"
					+ "<url></url><lowurl></lowurl><appattach><totallen>"
					+ FileSize
					+ "</totallen><attachid>"
					+ mediaId
					+ "</attachid><fileext>mp3</fileext></appattach><extinfo></extinfo></appmsg>\",\"FromUserName\":\""
					+ fromUserName + "\",\"ToUserName\":\"" + toUserName
					+ "\",\"LocalID\":\"" + time + "\",\"ClientMsgId\":\""
					+ time + "\"}}";
	
			v4 = new JSONObject(msgcontent);

			msgstr = msgcontent;
			MLog.d("sendappmsg", msgstr.toString());
		} catch (Exception v0) {
			MLog.e("sendappmsg", "make entity Failed");
			return;
		}

		JsonRequest v0_1 = new JsonRequest(Method.POST, v3, msgstr,
				new SendMsgListener(this, resp), new OnErrorListener(resp));
		v0_1.setShouldCache(false);

		v0_1.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1f));
		mRequsetQueue.add(v0_1);

	}

	
	public void DownLoadAppMsg(String msgId, String attachid, String filename,
			String fromUserName, 
			OnResponseResult<DownLoadFileResp> resp) {

		String v3 = mUrlInterface.DonwloadAppMsg(attachid, filename,
				mCookieStore.getWebwxDataTicketFromCookie());

		ByteRequest v0_1 = new ByteRequest(Method.GET, v3, null,
				new DownLoadAppMsgListener(this, resp, msgId, fromUserName,
						null, null,filename), new OnErrorListener(
						resp));
		v0_1.setShouldCache(true);
		mRequsetQueue.add(v0_1);
	}
	
	public void DownLoadAppMsg(String msgId, String attachid, String filename,
			String fromUserName, String MemberUserName, String memberNickName,
			OnResponseResult<DownLoadFileResp> resp) {

		String v3 = mUrlInterface.DonwloadAppMsg(attachid, filename,
				mCookieStore.getWebwxDataTicketFromCookie());

		ByteRequest v0_1 = new ByteRequest(Method.GET, v3, null,
				new DownLoadAppMsgListener(this, resp, msgId, fromUserName,
						MemberUserName, memberNickName,filename), new OnErrorListener(
						resp));
		v0_1.setShouldCache(true);
		mRequsetQueue.add(v0_1);
	}
	
	
	public void GetVoice(String msgId, String fromUserName,
			OnResponseResult<DownLoadFileResp> resp) {
		String v3 = mUrlInterface.getVoice(msgId);
		ByteRequest v0_1 = new ByteRequest(Method.GET, v3, null,
				new DownLoadAppMsgListener(this, resp, msgId, fromUserName,
						null, null,"voice.mp3"), new OnErrorListener(
						resp));
		v0_1.setShouldCache(true);
		mRequsetQueue.add(v0_1);
	}

	public void GetVoice(String msgId, String fromUserName,
			String MemberUserName, String memberNickName,
			OnResponseResult<DownLoadFileResp> resp) {
		String v3 = mUrlInterface.getVoice(msgId);
		ByteRequest v0_1 = new ByteRequest(Method.GET, v3, null,
				new DownLoadAppMsgListener(this, resp, msgId, fromUserName,
						MemberUserName, memberNickName,"voice.mp3"), new OnErrorListener(
						resp));
		v0_1.setShouldCache(true);
		mRequsetQueue.add(v0_1);
	}

//	public void UploadImg() {
//
//	}

}
