package com.uangel.sim.json;

import com.uangel.sim.util.JsonUtil;
import org.junit.Test;

/**
 * @author dajin kim
 */
public class JsonUtilTest {

    @Test
    public void getAllJsonFieldsTest() {
        String message = "[\n" +
                "    {\n" +
                "      \"bargeIn\": false,\n" +
                "      \"message\": \"메시지 입니다.\",\n" +
                "      \"seq\": 1\n" +
                "    }\n" +
                "  ]";

        //message = "[{\"bargeIn\":false,\"message\":\"메시지 입니다.\",\"seq\":1}]";

        System.out.println(JsonUtil.getAllJsonFields(message));

    }
}
