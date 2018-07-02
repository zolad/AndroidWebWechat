

package com.heimz.androidwebwechatlib.bean.Resp;

import java.util.List;

import com.heimz.androidwebwechatlib.bean.BaseResponseEntity;
import com.heimz.androidwebwechatlib.bean.ContactEntity;

public class GetContactResp {
    public BaseResponseEntity BaseResponse;
    public int MemberCount;
    public List<ContactEntity> MemberList;

    public GetContactResp() {
        super();
    }
}

