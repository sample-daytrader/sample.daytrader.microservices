package com.ibm.sample.daytrader.ping.beans;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.sample.daytrader.ping.entities.AccountDataBean;
import com.ibm.sample.daytrader.ping.utils.Log;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.persistence.Entity;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLEncoder;

import static com.ibm.sample.daytrader.ping.service.BaseRemoteCallService.invokeEndpoint;

@Component
public class PingGatewayBean {

    static String gatewayUrl = System.getenv("DAYTRADER_GATEWAY_SERVICE");


    protected static ObjectMapper mapper = null;

    static
    {
        mapper = new ObjectMapper(); // create once, reuse
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // ignore properties that are not declared
    }

    /**
     * Right after construction, we'll create and populate all the relevant database tables in the services
     */
    @PostConstruct
    public void postConstruct() {
        Log.traceEnter(PingGatewayBean.class.getName() + "::postConstruct()");
        try // create the database
        {
            Log.debug("URL: " + gatewayUrl);
            String url = gatewayUrl + "/admin/recreateDBTables";
            String responseEntity = invokeEndpoint(url, "POST", "");
            Boolean success = mapper.readValue(responseEntity, Boolean.class);
            if (success)
                Log.trace("Successfully reset all DB Tables");
            else
                Log.trace("Failed to reset all DB Tables");
        }
        catch(Throwable t)
        {
            StringWriter sw = new StringWriter();
            t.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.error("recreateDBTables() threw exception " + exceptionAsString);
        }

    }

    /**
     * Right before destruction, we'll reset all the databases
     */
    @PreDestroy
    public void preDestroy() {
        Log.traceEnter(PingGatewayBean.class.getName() + "::preDestroy()");
        return;
    }

    public boolean pingRegisterOneUser() throws Exception {
        Log.traceEnter(PingGatewayBean.class.getName() + "::pingRegisterOneUser()");
        String pingUrl = gatewayUrl+ "/accounts";
        String accountDataAsString = null;
        try{
            Log.trace("Creating a random user.");
            AccountDataBean randomUser = AccountDataBean.getRandomInstance();
            accountDataAsString = mapper.writeValueAsString(randomUser);
            Log.trace("Random user created successfully: \n" + accountDataAsString);
        } catch (Exception e) {
            Log.error("Failed to create a random user... " + e.getMessage());
            System.exit(1);
        }
        Log.traceExit("EXIT: " + PingGatewayBean.class.getName() + "::pingRegisterOneUser()");
        return false;
    }
}
