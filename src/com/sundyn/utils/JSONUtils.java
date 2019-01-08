package com.sundyn.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

public class JSONUtils {
    private final static String regex = "\"([^\\\" ]+?)\":";

    /**
     * 一个方法解析多层json数据  json + 正则 + 递归
     * @param jsonStr
     * @return
     */
    public static Object jsonParse(final String jsonStr) {
        if (jsonStr == null) throw new NullPointerException("JsonString shouldn't be null");
        try {
            if (isJsonObject(jsonStr)) {
                final Pattern pattern = Pattern.compile(regex);
                final Matcher matcher = pattern.matcher(jsonStr);
                final Map<String, Object> map = new HashMap<String, Object>();
                final JSONObject jsonObject = new JSONObject(jsonStr);
//                System.out.println("jsonStr = " + jsonStr);
//                System.out.println("jsonObject = " + jsonObject);
                try {
                    for (; matcher.find(); ) {
                        String groupName = matcher.group(1);
                        Object obj = jsonObject.opt(groupName);
                        if (isJsonObject(obj+"") || isJsonArray(obj+"")) {
                            matcher.region(matcher.end() + (obj+"").replace("\\", "").length(), matcher.regionEnd());
                            map.put(groupName, jsonParse(obj+""));
                        } else {
                            map.put(groupName, obj+"");
                        }
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
                return map;
            } else if (isJsonArray(jsonStr)) {
                List<Object> list = new ArrayList<Object>();
                try {
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Object object = jsonArray.opt(i);
                        list.add(jsonParse(object+""));
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
                return list;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return jsonStr;
    }

    /**
     * To determine whether a string is JsonObject {@link org.json.JSONObject}
     * @param jsonStr {@link java.lang.String}
     * @return boolean
     */
    private static boolean isJsonObject(final String jsonStr) {
        if (jsonStr == null) return false;
        return Pattern.matches("^\\{.*\\}$", jsonStr.trim());
    }

    /**
     * To determine whether a string is JsonArray {@link org.json.JSONArray};
     * @param jsonStr {@link java.lang.String}
     * @return boolean
     */
    private static boolean isJsonArray(final String jsonStr) {
        if (jsonStr == null) return false;
        return Pattern.matches("^\\[.*\\]$", jsonStr.trim());
    }

    /**
     * 将对象封装为json字符串 (json + 递归)
     * @param obj 参数应为{@link java.util.Map} 或者 {@link java.util.List}
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Object jsonEnclose(Object obj) {
        try {
            if (obj instanceof Map) {   //如果是Map则转换为JsonObject
                Map<String, Object> map = (Map<String, Object>)obj;
                Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
                JSONStringer jsonStringer = (JSONStringer) new JSONStringer().object();
                while (iterator.hasNext()) {
                    Map.Entry<String, Object> entry = iterator.next();
                    jsonStringer.key(entry.getKey()).value(jsonEnclose(entry.getValue()));
                }
                JSONObject jsonObject = new JSONObject(new JSONTokener(jsonStringer.endObject().toString()));
                return jsonObject;
            } else if (obj instanceof List) {  //如果是List则转换为JsonArray
                List<Object> list = (List<Object>)obj;
                JSONStringer jsonStringer = (JSONStringer) new JSONStringer().array();
                for (int i = 0; i < list.size(); i++) {
                    jsonStringer.value(jsonEnclose(list.get(i)));
                }
                JSONArray jsonArray = new JSONArray(new JSONTokener(jsonStringer.endArray().toString()));
                return jsonArray;
            } else {
                return obj;
            }
        } catch (Exception e) {
            // TODO: handle exception
            return e.getMessage();
        }
    }


    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        /*
         * 解析时使用示例
         */
        String jsonStr1 = "{\"ContractRoot\":{" +
                "               \"tcpCont\":{" +
                "                   \"ActionCode\":\"1\"," +
                "                   \"Response\":{" +
                "                       \"RspCode\":\"9002\"," +
                "                       \"RspDesc\":\"TransactionID超出长度约束\"," +
                "                       \"RspType\":\"9\"" +
                "                   }," +
                "                   \"RspTime\":\"20170527144416\"," +
                "                   \"TransactionID\":\"74201705281800202798\"" +
                "               }" +
                "           }" +
                "       }";
        String jsonStr2 = "{\"ContractRoot\":\"9002\"," +
                "           \"RspDesc\":\"TransactionID超出长度约束\"," +
                "           \"RspType\":\"9\"," +
                "           \"RspTime\":\"20170527144416\"," +
                "           \"TransactionID\":\"74201705281800202798\"" +
                "           }";
        Object object = jsonParse(jsonStr1);
        Map<String, Object> m = (Map<String, Object>) object;
        System.out.println(m.size());
        Map resultMap = new HashMap();
        boolean flag = true;
        while(flag){
            flag = false;
            if (object instanceof String) {
                System.out.println("string　= " + object.toString());
            } else if (object instanceof Map) {
                HashMap<String, Object> map = (HashMap<String, Object>)object;
                Iterator<Entry<String, Object>>  iterator = map.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry<String, Object> entry = iterator.next();
                    if (entry.getValue() instanceof Map) {
                        object = entry.getValue();
                        flag = true;
                    } else {
                        resultMap.put(entry.getKey(),entry.getValue().toString());
                    }
                }
            } else if (object instanceof List) {//list未测试
                System.out.println("list = " + object.toString());
            }
        }
        System.out.println("resultMap : " + resultMap);
        /**
         * resultMap : {TransactionID=74201705281800202798, RspType=9, RspCode=9002, ActionCode=1, RspTime=20170527144416, RspDesc=TransactionID超出长度约束}
         * resultMap : {TransactionID=74201705281800202798, RspType=9, RspTime=20170527144416, RspDesc=TransactionID超出长度约束, ContractRoot=9002}
         */

        /*
         * 封装时使用示例
         */
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("returnCode", "0");
        map.put("returnMsg", "成功");
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        Map<String, Object> listMap = new HashMap<String, Object>();
        listMap.put("test", "测试");
        listMap.put("fucp", "fucp");
        list.add(listMap);
        list.add(listMap);
        map.put("returnStatus", list);
        System.out.println("map : " + map);
        System.out.println("json : " + jsonEnclose(map).toString());
        /*
         * 封装结果
         * map : {returnCode=0, returnMsg=成功, returnStatus=[{test=测试, fucp=fucp}, {test=测试, fucp=fucp}]}
         * json : {"returnCode":"0","returnMsg":"成功","returnStatus":[{"test":"测试","fucp":"fucp"},{"test":"测试","fucp":"fucp"}]}
         */
    }
}
