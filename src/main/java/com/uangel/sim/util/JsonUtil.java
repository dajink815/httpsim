package com.uangel.sim.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.uangel.sim.scenario.nodes.BodyNode;
import com.uangel.sim.scenario.nodes.FieldNode;
import com.uangel.sim.scenario.type.FieldType;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author dajin kim
 */
@Slf4j
public class JsonUtil {

    private JsonUtil() {
        // nothing
    }

    // Json Str -> ClassType Object
    public static <T> T parse(String json, Type classType) {
        Gson gson = new Gson();
        return gson.fromJson(json, classType);
    }

    // JsonElement -> ClassType Object
    public static <T> T parse(JsonElement json, Class<T> classType) {
        Gson gson = new Gson();
        return gson.fromJson(json, classType);
    }

    // Object -> Json Str
    public static String build(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    // Object -> Pretty Json Str
    public static String buildPretty(Object obj) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(obj);
    }

    // Json Str -> Pretty Json Str
    public static String buildPretty(String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(JsonParser.parseString(json));
    }

    // Json Str -> JsonElement
    public static JsonElement build(String json) {
        Gson gson = new Gson();
        return gson.toJsonTree(json);
    }

    // Object -> JsonElement
    public static JsonElement build(Object obj, Type objType) {
        Gson gson = new GsonBuilder().create();
        return gson.toJsonTree(obj, objType);
    }

    // Json File -> Key, Value Map
    public static Map<String, String> getAllFileFields(String filePath) {
        Map<String, String> map = new HashMap<>();

        try {
            // jackson objectMapper 객체 생성
            ObjectMapper mapper = new ObjectMapper();

            File file = new File(filePath);
            if (!file.exists() || !file.isFile()) return map;

            // JsonNode 생성 (readTree, readValue)
            JsonNode jsonNode = mapper.readTree(file);
            getAllFieldsMap("", jsonNode, map);
        } catch (IOException e) {
            log.error("JsonUtil.getAllFileFields.Exception [{}]", filePath, e);
        }
        return map;
    }

    // Json Str -> Key, Value Map
    public static Map<String, String> getAllJsonFields(String json) {
        Map<String, String> map = new HashMap<>();

        try {
            // jackson objectMapper 객체 생성
            ObjectMapper mapper = new ObjectMapper();

 /*           MyDto readValue = mapper.readValue(json, MyDto.class);
            log.debug("MyDto : [{}]", readValue);*/

            // JsonNode 생성 (readTree, readValue)
            JsonNode jsonNode = mapper.readTree(json);
            getAllFieldsMap("", jsonNode, map);
        } catch (JsonProcessingException e) {
            log.error("JsonUtil.getAllJsonFields.Exception [{}]", json, e);
        }
        return map;
    }

    private static void getAllFieldsMap(String currentPath, JsonNode jsonNode, Map<String, String> map) {
        if (jsonNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields();

            while (iter.hasNext()) {
                Map.Entry<String, JsonNode> entry = iter.next();
                getAllFieldsMap(entry.getKey(), entry.getValue(), map);
            }
        } else if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            for (int i = 0; i < arrayNode.size(); i++) {
                getAllFieldsMap(currentPath + "[" + i + "]", arrayNode.get(i), map);
            }
        } else if (jsonNode.isValueNode()) {
            ValueNode valueNode = (ValueNode) jsonNode;
            map.put(currentPath, valueNode.asText());
        }
    }

    /**
     * JSONObject 에 Array 타입의 필드 추가
     * @param jsonObj 필드 추가 할 JSON
     * @param fieldName 필드 이름
     * @param arrStr 필드 값
     * */
    public static void addJsonArray(JSONObject jsonObj, String fieldName, String arrStr) {
        try {
            JSONParser parser = new JSONParser();
            JSONArray jsonArr = (JSONArray) parser.parse(arrStr);
            jsonObj.put(fieldName, jsonArr);
        } catch (ParseException e) {
            log.error("JsonUtil.addJsonArray.Exception - {}", arrStr, e);
        }
    }

    public static JSONObject buildJsonMsg(BodyNode bodyNode) {

        List<FieldNode> fieldNodeList = bodyNode.getFieldNodes();

        try {
            JSONObject data = new JSONObject();

            // Struct Node Message build

            for (FieldNode fieldNode : fieldNodeList) {
                String fieldName = fieldNode.getName();
                FieldType type = fieldNode.getType();
                String value = fieldNode.getValue();

                // Keyword
/*                KeywordMapper keywordMapper = scenario.getKeywordMapper();
                value = keywordMapper.replaceKeyword(value, sessionInfo);*/

                if (StringUtil.isNull(value)) continue;

                // Type 별 세팅
                if (FieldType.STR.equals(type)) {
                    data.put(fieldName, value);
                } else if (FieldType.INT.equals(type)) {
                    data.put(fieldName, Integer.parseInt(value));
                } else if (FieldType.LONG.equals(type)) {
                    data.put(fieldName, Long.parseLong(value));
                } else if (FieldType.BOOL.equals(type)) {
                    data.put(fieldName, Boolean.parseBoolean(value));
                } else if (FieldType.ARRAY.equals(type)) {
                    addJsonArray(data, fieldName, value);
                }
            }

            return data;

        } catch (Exception e) {
            log.error("JsonMsgBuilder.getSubMessage.Exception ", e);
        }

        return new JSONObject();
    }
}
