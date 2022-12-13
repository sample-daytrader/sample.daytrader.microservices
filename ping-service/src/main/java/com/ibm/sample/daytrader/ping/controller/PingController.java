package com.ibm.sample.daytrader.ping.controller;

import com.ibm.sample.daytrader.ping.beans.PingGatewayBean;
import com.ibm.sample.daytrader.ping.service.PingRemoteCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class PingController {


    private static PingRemoteCallService remoteCallService = new PingRemoteCallService();

    @RequestMapping(value = "/ping/registerUser", method = RequestMethod.POST)
    public ResponseEntity<Boolean> registerOneUser() throws Exception {
        try {
            Boolean success = remoteCallService.registerOneUser();
            return new ResponseEntity<Boolean>(success, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
