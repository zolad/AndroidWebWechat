

package com.heimz.androidwebwechatlib.bean.Resp;

import java.util.List;

import com.heimz.androidwebwechatlib.bean.BaseResponseEntity;
import com.heimz.androidwebwechatlib.bean.ContactEntity;

public class BatchGetContactResp {
	
	public  BaseResponseEntity BaseResponse;
    public List<ContactEntity> ContactList;
    public int Count;

    public BatchGetContactResp() {
        super();
    }
}

