

package com.heimz.androidwebwechatlib.bean.Resp;

import java.util.List;

import com.heimz.androidwebwechatlib.bean.BaseResponseEntity;
import com.heimz.androidwebwechatlib.bean.ContactEntity;
import com.heimz.androidwebwechatlib.bean.SyncKeyEntity;

import android.content.Entity;

public class InitResp {
    public BaseResponseEntity BaseResponse;
    public String ChatSet;
    public int ClickReportInterval;
    public long ClientVersion;
    public List<ContactEntity> ContactList;
    public int Count;
    public int GrayScale;
    public int InviteStartCount;
    public int MPSubscribeMsgCount;
    public List<MPSubscribeMsgListEntity> MPSubscribeMsgList;
    public String SKey;
    public SyncKeyEntity SyncKey;
    public long SystemTime;
    public ContactEntity User;

    public InitResp() {
        super();
    }
    
 

    
    public class MPSubscribeMsgListEntity {
        public int MPArticleCount;
        public List<MPArticleListEntity> MPArticleList;
        public String NickName;
        public int Time;
        public String UserName;
        
        
        class MPArticleListEntity {
            public String Cover;
            public String Digest;
            public String Title;
            public String Url;
        }
    }
    
}

