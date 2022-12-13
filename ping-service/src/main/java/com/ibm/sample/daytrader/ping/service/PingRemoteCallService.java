package com.ibm.sample.daytrader.ping.service;

import com.ibm.sample.daytrader.ping.beans.PingGatewayBean;
import com.ibm.sample.daytrader.ping.utils.Log;

import javax.annotation.Resource;

public class PingRemoteCallService extends BaseRemoteCallService {

    @Resource
    private PingGatewayBean pingGatewayBean;

    public Boolean registerOneUser() throws Exception {
        try {
            return pingGatewayBean.pingRegisterOneUser();
        }
        catch (Exception e) {
            Log.trace(e.getMessage());
        }
        return false;
    }
}
