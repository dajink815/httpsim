package com.uangel.sim.scenario.handler;

import com.uangel.sim.command.CliInfo;
import com.uangel.sim.scenario.Scenario;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author dajin kim
 */
@Slf4j
public class KeywordMapper {

    private static final Pattern keyPattern = Pattern.compile("\\[(.*?)\\]");
    private static final String LAST = "last_";

    private final CliInfo cliInfo;

    public KeywordMapper(CliInfo cliInfo) {
        this.cliInfo = cliInfo;
    }

    public String replaceKeyword(String keyword, Map<String, String> fieldsMap) {
        String before = keyword;
        Matcher m = keyPattern.matcher(keyword);

        // [] 포함돼 있는 모든 단어 처리
        while (m.find()) {
            String result = getValue(m.group(1), fieldsMap);   // 중괄호 제외한 값
            if (result != null) keyword = keyword.replace(m.group(0), result);
        }
        if (!before.equals(keyword)) log.debug("ReplaceKeyword ({} -> {})", before, keyword);
        return keyword;
    }

    private String getValue(String keyword, Map<String, String> fieldsMap) {
        try {
            switch (keyword.toLowerCase()) {
                case "host" :
                    return cliInfo.getHttpIp();
                case "port" :
                    return String.valueOf(cliInfo.getHttpPort());
                case "tid" :
                case "transactionid":
                    return UUID.randomUUID().toString();
                default:
                    break;
            }

            if (keyword.startsWith(LAST)) {
                String fieldName = keyword.substring(LAST.length());
                return fieldsMap.get(fieldName);
            }

        } catch (Exception e) {
            log.error("KeywordMapper.getValue.Exception ", e);
        }
        return null;
    }

}
