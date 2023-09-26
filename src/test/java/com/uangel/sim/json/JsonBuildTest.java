package com.uangel.sim.json;

import com.uangel.sim.scenario.Scenario;
import com.uangel.sim.scenario.ScenarioBuilder;
import com.uangel.sim.scenario.nodes.FieldNode;
import com.uangel.sim.scenario.nodes.MsgNode;
import com.uangel.sim.scenario.type.FieldType;
import com.uangel.sim.util.JsonUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author dajin kim
 */
public class JsonBuildTest {

    @Test
    public void buildJsonTest() {

        JSONObject data = new JSONObject();

        data.put("sessionKey", "GW01_20180929191521983_001");
        data.put("dtmfLength", 10);
        data.put("longTest", 5L);
        data.put("bargeIn", false);

        System.out.println(data);
        System.out.println(data.toJSONString());

    }

    @Test
    public void buildJsonFieldArr() {
        JSONObject data = new JSONObject();

        data.put("sessionKey", "GW01_20180929191521983_001");
        data.put("type", "MESSAGE");
        data.put("sttType", "1416523158");
        data.put("ttsType", "rsk");
        data.put("ttsVoiceActor", "14");

        JSONArray messages = new JSONArray();
        JSONObject message = new JSONObject();
        message.put("seq", 1);
        message.put("message", "메시지 입니다.");
        message.put("bargeIn", false);
        messages.add(message);

        data.put("messages", messages);

        System.out.println(data.toJSONString());
        System.out.println(JsonUtil.buildPretty(data));
    }


    @Test
    public void buildJsonStrArr() {
        JSONObject data = new JSONObject();

        data.put("sessionKey", "GW01_20180929191521983_001");
        data.put("type", "MESSAGE");
        data.put("sttType", "1416523158");
        data.put("ttsType", "rsk");
        data.put("ttsVoiceActor", "14");

        String message = "[\n" +
                "    {\n" +
                "      \"bargeIn\": false,\n" +
                "      \"message\": \"메시지 입니다.\",\n" +
                "      \"seq\": 1\n" +
                "    }\n" +
                "  ]";

        //message = "[{\"bargeIn\":false,\"message\":\"메시지 입니다.\",\"seq\":1}]";

        JSONParser parser = new JSONParser();
        JSONArray messages = null;
        try {
            messages = (JSONArray) parser.parse(message);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        data.put("messages", messages);

        System.out.println(data.toJSONString());
        System.out.println(JsonUtil.buildPretty(data));
    }

    @Test
    public void addJsonArrayTest() {
        JSONObject data = new JSONObject();

        data.put("sessionKey", "GW01_20180929191521983_001");
        data.put("type", "MESSAGE");
        data.put("sttType", "1416523158");
        data.put("ttsType", "rsk");
        data.put("ttsVoiceActor", "14");

        String message = "[{\"bargeIn\":false,\"message\":\"메시지 입니다.\",\"seq\":1}]";

        JsonUtil.addJsonArray(data, "messages", message);

        System.out.println(data.toJSONString());
        System.out.println(JsonUtil.buildPretty(data));
    }

    @Test
    public void addJsonArrayWithFieldNode() throws IOException, SAXException {
        String scenarioPath = "./src/test/resources/scenario/easy_cms_basic.xml";
        Scenario scenario = ScenarioBuilder.fromXMLFileName(scenarioPath);

        List<MsgNode> msgNodes = scenario.getMsgNodes();
        List<FieldNode> fieldNodes = msgNodes.get(0).getBodyNode().getFieldNodes();
        System.out.println("Body Field Node List : " + fieldNodes);

        for (FieldNode field : fieldNodes) {
            if (FieldType.ARRAY.equals(field.getType())) {
                String name = field.getName();
                String value = field.getValue();

                System.out.println("Array Field Name : " + name);
                System.out.println("Array Field Value : " + value);

                JSONObject data = new JSONObject();

                JsonUtil.addJsonArray(data, name, value);
                System.out.println("Json Result : " + JsonUtil.buildPretty(data));

                // “ ” 따옴표 포함된 문자열 JSONParser.parse 호출 시 에러 발생
/*                for (int i =0; i < value.length(); i++) {
                    System.out.println("[Idx:" + i + "] (" + value.charAt(i) + ")");
                }

                JSONParser parser = new JSONParser();
                Object result = parser.parse(value);
                System.out.println(result);*/
            }
        }
    }

    @Test
    public void buildJsonMsgTest() throws IOException, SAXException {
        String scenarioPath = "./src/test/resources/scenario/aica_basic.xml";
        Scenario scenario = ScenarioBuilder.fromXMLFileName(scenarioPath);

        int idx = 0;
        assert scenario != null;
        for (MsgNode msgNode : scenario.getMsgNodes()) {
            System.out.println("<< " + msgNode.getUri() + " >>");
            List<FieldNode> fieldNodes = msgNode.getBodyNode().getFieldNodes();
            System.out.println(idx + ") Body Field Node List : " + fieldNodes);

            JSONObject body = JsonUtil.buildJsonMsg(msgNode.getBodyNode(), Collections.emptyMap(), null);
            System.out.println("Result : " + JsonUtil.buildPretty(body));
            idx++;
        }
    }

    @Test
    public void buildJsonObjbyStr() {
        String json = "            {\n" +
                "                \"seq\": 1,\n" +
                "                \"message\": \"시작 메시지 입니다.\",\n" +
                "                \"bargeIn\": false\n" +
                "            }";

        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(json);

            // 변환된 JSONObject 사용 예시
            int seq = (int) (long) jsonObject.get("seq");
            String message = (String) jsonObject.get("message");
            boolean bargeIn = (boolean) jsonObject.get("bargeIn");

            System.out.println("seq: " + seq);
            System.out.println("message: " + message);
            System.out.println("bargeIn: " + bargeIn);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
