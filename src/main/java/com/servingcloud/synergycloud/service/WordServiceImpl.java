package com.servingcloud.synergycloud.service;

import com.alibaba.fastjson.JSON;
import com.servingcloud.synergycloud.service.dto.InterFaceInfo;
import com.servingcloud.synergycloud.service.dto.RequestInfo;
import com.servingcloud.synergycloud.service.dto.ResponseInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Created by XiuYin.Cui on 2018/1/12.
 */
@Service
public class WordServiceImpl {

    @Autowired
    private RestTemplate restTemplate;


    public List<InterFaceInfo> tableList(String swaggerUrl) {
        Map<String, Object> map = restTemplate.getForObject(swaggerUrl, Map.class);
        Map<String, Object> definitionsMap = (Map<String, Object>) map.get("definitions");
        List<InterFaceInfo> list = new LinkedList();
        //得到host，并添加上http 或 https
        String host = StringUtils.substringBefore(swaggerUrl, ":") + String.valueOf(map.get("host"));
        //解析paths
        LinkedHashMap<String, LinkedHashMap> paths = (LinkedHashMap) map.get("paths");
        if (paths != null) {
            Iterator<Map.Entry<String, LinkedHashMap>> it = paths.entrySet().iterator();
            while (it.hasNext()) {
                InterFaceInfo table = new InterFaceInfo();
                List<RequestInfo> requestList = new LinkedList<>();
                List<ResponseInfo> responseList = new LinkedList<>();
                // 请求参数格式，类似于 multipart/form-data
                String requestForm = "";
                // 请求参数格式，类似于 multipart/form-data
                String responseForm = "";
                // 请求方式，类似为 get,post,delete,put 这样
                String requestType = "";
                String url; // 请求路径
                String title; // 大标题（类说明）
                String tag; // 小标题 （方法说明）
                String description; //接口描述

                Map.Entry<String, LinkedHashMap> path = it.next();
                url = path.getKey();

                LinkedHashMap<String, LinkedHashMap> value = path.getValue();
                Set<String> requestTypes = value.keySet();
                for (String str : requestTypes) {
                    requestType += str + ",";
                }

                Iterator<Map.Entry<String, LinkedHashMap>> it2 = value.entrySet().iterator();
                // 不管有几种请求方式，都只解析第一种
                Map.Entry<String, LinkedHashMap> firstRequestType = it2.next();
                LinkedHashMap content = firstRequestType.getValue();
                title = String.valueOf(((List) content.get("tags")).get(0));
                description = String.valueOf(content.get("description"));
                List<String> consumes = (List) content.get("consumes");
                if (consumes != null && consumes.size() > 0) {
                    for (String consume : consumes) {
                        requestForm += consume + ",";
                    }
                }
                List<String> produces = (List) content.get("produces");
                if (produces != null && produces.size() > 0) {
                    for (String produce : produces) {
                        responseForm += produce + ",";
                    }
                }

                tag = String.valueOf(content.get("summary"));
                //请求体
                List parameters = (ArrayList) content.get("parameters");
                if (parameters != null && parameters.size() > 0) {
                    for (int i = 0; i < parameters.size(); i++) {
                        RequestInfo request = new RequestInfo();
                        LinkedHashMap<String, Object> param = (LinkedHashMap) parameters.get(i);
                        request.setName(String.valueOf(param.get("name")));
                        request.setType(param.get("type") == null ? "Object" : param.get("type").toString());
                        request.setParamType(String.valueOf(param.get("in")));
                        request.setRequire((Boolean) param.get("required"));
                        request.setRemark(String.valueOf(param.get("description")));
                        requestList.add(request);
                        if (request.getType().equals("Object")) {
                            String reqName = "";
                            if (((Map) param.get("schema")).get("$ref") != null) {
                                reqName = ((Map) param.get("schema")).get("$ref").toString();
                            }
                            reqName = reqName.replace("#/definitions/", "");
                            LinkedHashMap reqBody = new LinkedHashMap();
                            if (definitionsMap.get(reqName) != null) {
                                if (((Map) definitionsMap.get(reqName)).get("properties") != null) {
                                    reqBody = (LinkedHashMap) ((Map) definitionsMap.get(reqName)).get("properties");
                                }
                            }
                            Set<String> keySet = reqBody.keySet();
                            for (String key : keySet) {
                                Map temp = (Map) reqBody.get(key);
                                try {
                                    RequestInfo sonRequest = new RequestInfo();
                                    sonRequest.setName(key);
                                    sonRequest.setType(temp.get("type").toString());
                                    String sonRequestDescription = temp.get("description") != null ? temp.get("description").toString() : "";
                                    sonRequest.setRemark(sonRequestDescription);
                                    requestList.add(sonRequest);
                                } catch (Exception e) {
                                    System.out.println(temp);
                                }
                            }
                        }
                    }
                }
                //返回体
                LinkedHashMap<String, Object> responses = (LinkedHashMap) content.get("responses");
                Iterator<Map.Entry<String, Object>> it3 = responses.entrySet().iterator();
                String responseJson = "";
                while (it3.hasNext()) {
                    ResponseInfo response = new ResponseInfo();
                    Map.Entry<String, Object> entry = it3.next();
                    // 状态码 200 201 401 403 404 这样
                    String statusCode = entry.getKey();
                    LinkedHashMap<String, Object> statusCodeInfo = (LinkedHashMap) entry.getValue();

                    boolean isArray = "array".equals(((Map) statusCodeInfo.get("schema")).get("type"));
                    String responseBodyName = "";
                    if (isArray) {
                        responseBodyName = (String) ((Map) ((Map) statusCodeInfo.get("schema")).get("items")).get("$ref");
                    } else {
                        responseBodyName = (String) ((Map) statusCodeInfo.get("schema")).get("$ref");
                    }
                    if (responseBodyName != null) {
                        responseBodyName = responseBodyName.replace("#/definitions/", "");
                        LinkedHashMap reqBody = (LinkedHashMap) ((Map) definitionsMap.get(responseBodyName)).get("properties");
                        if (reqBody == null) {
                            continue;
                        }
                        Set<String> keySet = reqBody.keySet();
                        Map responseFieldMap = new HashMap();
                        for (String key : keySet) {
                            Map temp = (Map) reqBody.get(key);
                            ResponseInfo sonRequest = new ResponseInfo();
                            sonRequest.setName(key);
                            if (temp.get("type") != null) {
                                sonRequest.setDescription(temp.get("type").toString());
                            }
                            String sonRequestDescription = temp.get("description") != null ? temp.get("description").toString() : "";
                            sonRequest.setRemark(sonRequestDescription);
                            responseList.add(sonRequest);
                            responseFieldMap.put(sonRequest.getName(), sonRequest.getDescription());
                        }
                        Map responseMap = new HashMap();
                        responseMap.put("code", 200);
                        responseMap.put("msg", "msg");
                        if (isArray) {
                            List arrayList = new ArrayList();
                            arrayList.add(responseFieldMap);
                            responseMap.put("data", arrayList);
                            responseJson = JSON.toJSONString(responseMap);
                        } else {
                            responseMap.put("data", responseFieldMap);
                            responseJson = JSON.toJSONString(responseMap);
                        }
                    } else {
                        Map responseMap = new HashMap();
                        responseMap.put("code", 200);
                        responseMap.put("msg", "msg");
                        switch (((Map) statusCodeInfo.get("schema")).get("type").toString()) {
                            case "string":
                                responseMap.put("data", "string");
                                break;
                            case "integer":
                                responseMap.put("data", 0);
                                break;
                            case "number":
                                responseMap.put("data", 0.0);
                                break;
                            case "boolean":
                                responseMap.put("data", true);
                                break;
                            default:
                                responseMap.put("data", null);
                        }
                        responseJson = JSON.toJSONString(responseMap);
                    }
                    String statusDescription = (String) statusCodeInfo.get("description");

                    response.setName(statusCode);
                    response.setDescription(statusDescription);
                    response.setRemark(null);
                    responseList.add(response);
                }

                // 模拟一次HTTP请求,封装请求体和返回体
                // 得到请求方式
                String restType = firstRequestType.getKey();
                Map<String, Object> paramMap = ParamMap(requestList);
                String buildUrl = buildUrl(host + url, requestList);

                //封装Table
                table.setTitle(title);
                table.setUrl(url);
                table.setTag(tag);
                description = (description == null || description.equals("null")) ? tag : description;
                table.setDescription(description);
                table.setRequestForm(StringUtils.removeEnd(requestForm, ","));
                table.setResponseForm(StringUtils.removeEnd(responseForm, ","));
                table.setRequestType(StringUtils.removeEnd(requestType, ","));
                table.setRequestList(requestList);
                table.setResponseList(responseList);
                table.setRequestParam(JSON.toJSONString(paramMap));
//                table.setResponseParam(doRestRequest(restType, buildUrl, paramMap));
                table.setResponseParam(responseJson);
                list.add(table);
            }
        }
        return list;
    }

    /**
     * 重新构建url
     *
     * @param url
     * @param requestList
     * @return etc:http://localhost:8080/rest/delete?uuid={uuid}
     */
    private String buildUrl(String url, List<RequestInfo> requestList) {
        String param = "";
        if (requestList != null && requestList.size() > 0) {
            for (RequestInfo request : requestList) {
                String name = request.getName();
                param += name + "={" + name + "}&";
            }
        }
        if (StringUtils.isNotEmpty(param)) {
            url += "?" + StringUtils.removeEnd(param, "&");
        }
        return url;

    }

    /**
     * 发送一个 Restful 请求
     *
     * @param restType "get", "head", "post", "put", "delete", "options", "patch"
     * @param url      资源地址
     * @param paramMap 参数
     * @return
     */
    private String doRestRequest(String restType, String url, Map<String, Object> paramMap) {
        Object object = null;
        try {
            switch (restType) {
                case "get":
                    object = restTemplate.getForObject(url, Object.class, paramMap);
                    break;
                case "post":
                    object = restTemplate.postForObject(url, null, Object.class, paramMap);
                    break;
                case "put":
                    restTemplate.put(url, null, paramMap);
                    break;
                case "head":
                    HttpHeaders httpHeaders = restTemplate.headForHeaders(url, paramMap);
                    return String.valueOf(httpHeaders);
                case "delete":
                    restTemplate.delete(url, paramMap);
                    break;
                case "options":
                    Set<HttpMethod> httpMethods = restTemplate.optionsForAllow(url, paramMap);
                    return String.valueOf(httpMethods);
                case "patch":
                    object = restTemplate.execute(url, HttpMethod.PATCH, null, null, paramMap);
                    break;
                case "trace":
                    object = restTemplate.execute(url, HttpMethod.TRACE, null, null, paramMap);
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            // 无法使用 restTemplate 发送的请求，返回""
            // ex.printStackTrace();
            return "";
        }
        return String.valueOf(object);
    }

    /**
     * 封装post请求体
     *
     * @param list
     * @return
     */
    private Map<String, Object> ParamMap(List<RequestInfo> list) {
        Map<String, Object> map = new HashMap<>(8);
        if (list != null && list.size() > 0) {
            for (RequestInfo request : list) {
                String name = request.getName();
                String type = request.getType();
                switch (type) {
                    case "string":
                        map.put(name, "string");
                        break;
                    case "integer":
                        map.put(name, 0);
                        break;
                    case "number":
                        map.put(name, 0.0);
                        break;
                    case "boolean":
                        map.put(name, true);
                    default:
                        if (request.getParamType() != null && request.getParamType().equals("body")) {
                            continue;
                        }
                        map.put(name, null);
                        break;
                }
            }
        }
        return map;
    }
}
