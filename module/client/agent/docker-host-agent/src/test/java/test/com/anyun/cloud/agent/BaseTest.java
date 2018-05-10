package test.com.anyun.cloud.agent;

import com.anyun.cloud.api.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Assert;

/**
 * Created by twitchgg on 16-8-3.
 */
public class BaseTest extends Assert {
    protected void printResponse(Response<?> response) {
        System.out.println(toJson(response));
    }

    protected static String toJson(Object obj) {
        try {
            GsonBuilder gb = new GsonBuilder();
            gb.disableHtmlEscaping();
            gb.setPrettyPrinting();
            Gson gson = gb.create();
            return gson.toJson(obj);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
