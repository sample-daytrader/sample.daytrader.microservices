/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.ibm.sample.daytrader.web;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

import com.ibm.sample.daytrader.core.direct.CookieUtils;
import com.ibm.sample.daytrader.core.direct.TradeJDBCDirect;
import com.ibm.sample.daytrader.utils.Log;
import com.ibm.sample.daytrader.utils.TradeConfig;

import java.io.IOException;


/**
 * 
 * TradeAppServlet provides the standard web interface to Trade and can be
 * accessed with the Go Trade! link. Driving benchmark load using this interface
 * requires a sophisticated web load generator that is capable of filling HTML
 * forms and posting dynamic data.
 */


@WebServlet("/app")
public class TradeAppServlet extends HttpServlet {

    /**
     * Servlet initialization method.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        java.util.Enumeration en = config.getInitParameterNames();
        while (en.hasMoreElements()) {
            String parm = (String) en.nextElement();
            String value = config.getInitParameter(parm);
            TradeConfig.setConfigParam(parm, value);
        }
        try {
            if (TradeConfig.runTimeMode == TradeConfig.JDBC) {
                TradeJDBCDirect.init();
            } else if (TradeConfig.runTimeMode == TradeConfig.JPA) {
                TradeJDBCDirect.init();
                
            } else {
                TradeJDBCDirect.init();
            }
        } catch (Exception e) {
            Log.error(e, "TradeAppServlet:init -- Error initializing TradeDirect");
        }
    }

    /**
     * Returns a string that contains information about TradeScenarioServlet
     * 
     * @return The servlet information
     */
    public java.lang.String getServletInfo() {
        return "TradeAppServlet provides the standard web interface to Trade";
    }

    /**
     * Process incoming HTTP GET requests
     * 
     * @param request
     *            Object that encapsulates the request to the servlet
     * @param response
     *            Object that encapsulates the response from the servlet
     */
    public void doGet(javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response)
            throws ServletException, IOException {
        performTask(request, response);
    }

    /**
     * Process incoming HTTP POST requests
     * 
     * @param request
     *            Object that encapsulates the request to the servlet
     * @param response
     *            Object that encapsulates the response from the servlet
     */
    public void doPost(javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response)
            throws ServletException, IOException {
        performTask(request, response);
    }

    /**
     * Main service method for TradeAppServlet
     * 
     * @param request
     *            Object that encapsulates the request to the servlet
     * @param response
     *            Object that encapsulates the response from the servlet
     */
    public void performTask(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException 
    {

        resp.setContentType("text/html");
        TradeServletAction tsAction = new TradeServletAction();

        String action = req.getParameter("action");
        
        String userID = null;
       
        ServletContext ctx = getServletConfig().getServletContext();
       
        if (action == null) 
        {
            tsAction.doWelcome(ctx, req, resp, "");
            return;
        } 
        else if (action.equals("login")) 
        {            
            userID = req.getParameter("uid");
            String passwd = req.getParameter("passwd");
            try 
            {
                tsAction.doLogin(ctx, req, resp, userID, passwd);
            } 
            catch (ServletException se) 
            {
            	Log.error("TradeAppServlet#performTask(" + action + ") - " + userID, se ); 
                tsAction.doWelcome(ctx, req, resp, se.getMessage());
            }
            return;
        } 
        else if (action.equals("register")) 
        {
            userID = req.getParameter("user id");
            String passwd = req.getParameter("passwd");
            String cpasswd = req.getParameter("confirm passwd");
            String fullname = req.getParameter("Full Name");
            String ccn = req.getParameter("Credit Card Number");
            String money = req.getParameter("money");
            String email = req.getParameter("email");
            String smail = req.getParameter("snail mail");
            tsAction.doRegister(ctx, req, resp, userID, passwd, cpasswd, fullname, ccn, money, email, smail);    
            return;
        }
        
        // Use cookies instead of session attributes
        userID = CookieUtils.getCookieValue(req.getCookies(),"uidBean");
		Log.debug("TradeAppServlet#performTask(" + action + ") - getCookie(uidBean): " + userID ); 
      	 		
        // The rest of the operations require the user to be logged in -
        // Get the Session and validate the user. It the user isn't in
        // the then return to the login page; otherwise continue
        if (userID == null) 
        {
            Log.error("TradeAppServlet#performTask() - User Not Logged in");
            tsAction.doWelcome(ctx, req, resp, "User Not Logged in");
            return;
        }     
        
        if (action.equals("quotes")) 
        {
            String symbols = req.getParameter("symbols");
            tsAction.doQuotes(ctx, req, resp, userID, symbols);
        } 
        else if (action.equals("buy")) 
        {           	
            String symbol = req.getParameter("symbol");
            String quantity = req.getParameter("quantity");
            tsAction.doBuy(ctx, req, resp, userID, symbol, quantity);
        } 
        else if (action.equals("sell")) 
        {   	
            int holdingID = Integer.parseInt(req.getParameter("holdingID"));
            tsAction.doSell(ctx, req, resp, userID, new Integer(holdingID));
        } 
        else if (action.equals("portfolio") || action.equals("portfolioNoEdge")) 
        {           	
            tsAction.doPortfolio(ctx, req, resp, userID, "Portfolio as of " + new java.util.Date());
        } 
        else if (action.equals("logout")) 
        {
            tsAction.doLogout(ctx, req, resp, userID);
        } 
        else if (action.equals("home")) 
        {	
            tsAction.doHome(ctx, req, resp, userID, "Ready to Trade");
        } 
        else if (action.equals("account")) 
        { 	
            tsAction.doAccount(ctx, req, resp, userID, "");
        } 
        else if (action.equals("update_profile")) 
        {           	
            String password = req.getParameter("password");
            String cpassword = req.getParameter("cpassword");
            String fullName = req.getParameter("fullname");
            String address = req.getParameter("address");
            String creditcard = req.getParameter("creditcard");
            String email = req.getParameter("email");
            tsAction.doAccountUpdate(ctx, req, resp, userID,
                    password == null ? "" : password.trim(),
                    cpassword == null ? "" : cpassword.trim(),
                    fullName == null ? "" : fullName.trim(),
                    address == null ? "" : address.trim(),
                    creditcard == null ? "" : creditcard.trim(),
                    email == null ? "" : email.trim());
        } 
        else 
        {  	            	
            Log.error("TradeAppServlet: Invalid Action=" + action);
            tsAction.doWelcome(ctx, req, resp, "TradeAppServlet: Invalid Action" + action);
        }         	

    }

}