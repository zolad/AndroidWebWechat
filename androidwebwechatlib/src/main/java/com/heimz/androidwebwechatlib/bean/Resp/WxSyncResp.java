package com.heimz.androidwebwechatlib.bean.Resp;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.heimz.androidwebwechatlib.bean.AddMsgEntity;
import com.heimz.androidwebwechatlib.bean.BaseResponseEntity;
import com.heimz.androidwebwechatlib.bean.ContactEntity;
import com.heimz.androidwebwechatlib.bean.DelContactEntity;
import com.heimz.androidwebwechatlib.bean.SyncKeyEntity;

public class WxSyncResp {

	public BaseResponseEntity BaseResponse;
	public SyncKeyEntity SyncKey;
	public int AddMsgCount;
	public int ContinueFlag ;
	public int DelContactCount ;
	public int ModContactCount ;
	public int ModChatRoomMemberCount ;
	public String SKey;

	public List<AddMsgEntity> AddMsgList;
	
	

	public List<ContactEntity> ModContactList;
	public List<DelContactEntity> DelContactList;
}
