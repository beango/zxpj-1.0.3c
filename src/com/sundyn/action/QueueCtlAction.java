package com.sundyn.action;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.sundyn.entity.InteLog;
import com.sundyn.entity.SysDictinfo;
import com.sundyn.service.DeptService;
import com.sundyn.service.ISysDictinfoService;
import com.sundyn.util.socketUdp;
import com.sundyn.utils.JSONUtils;
import com.xuan.xutils.http.HttpResponse;
import com.xuan.xutils.http.HttpUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.springframework.http.MediaType;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.*;

public class QueueCtlAction extends MainAction {
    @Getter
    @Setter
    public Map jsonData;
    @Resource
    private DeptService deptService;
    @Resource
    private ISysDictinfoService dictinfoService;

    String uri = "http://localhost:8088/queueSysinterface.aspx";

    public String queuectl(){
        SysDictinfo dict = dictinfoService.selectOne(new EntityWrapper<SysDictinfo>().where("isEnable=1").and("dictkey='QueueInte'"));

        if (dict!=null){
            System.out.println("排队机接口地址：" + dict.getDictvalue());
            uri = dict.getDictvalue();
        }
        else
        {
            System.out.println("接口参数：" + "null");
        }
        final HttpServletRequest request = ServletActionContext.getRequest();
        String contentType = request.getContentType();
        Scanner scanner = null;
        StringBuilder sb = new StringBuilder();
        System.out.println("contentType：" + contentType);
        try {
            if(MediaType.APPLICATION_JSON_UTF8_VALUE.toLowerCase().equals(contentType) ||
                    MediaType.APPLICATION_JSON_UTF8_VALUE.equals(contentType) ||
                    MediaType.APPLICATION_JSON_UTF8.equals(contentType) ||
                    MediaType.APPLICATION_JSON_VALUE.toLowerCase().equals(contentType) ||
                    MediaType.APPLICATION_JSON_VALUE.equals(contentType) ||
                    MediaType.APPLICATION_FORM_URLENCODED.equals(contentType)){
                scanner = new Scanner(request.getInputStream(),"utf-8");
                while (scanner.hasNextLine()) {
                    sb.append(scanner.nextLine());
                }
                if (sb==null || sb.length()==0){
                    return getrst(500, "参数为空", null);
                }
                System.out.println("sb：" + sb.toString());
            }
            else{
                try {
                    Enumeration<String> names = request.getParameterNames();
                    StringBuilder sbParam = new StringBuilder("");
                    while(names.hasMoreElements()) {
                        String name = names.nextElement();
                        String value = new String(request.getParameter(name).getBytes("iso-8859-1"),"utf-8");
                        sbParam.append("&" + name + "=" + value);
                    }
                    scanner = new Scanner(request.getInputStream(),"utf-8");
                    while (scanner.hasNextLine()) {
                        sb.append(scanner.nextLine());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        String interfacename = req.getString("interfacename");
        ObjectMapper mapper = new ObjectMapper();
        HttpResponse response = null;
        try {
            System.out.println("排队接口请求转发,uri:" + uri + ", 参数：" + sb.toString());
            response = HttpUtils.postJson(uri, sb.toString());
            String result = response.getResultStr();
            System.out.println("结果:" + result);
            Object object = JSONUtils.jsonParse(result);
            jsonData = (Map<String, Object>) object;
            return SUCCESS;
        } catch (Exception e) {
            return getrst(1, "失败", null);
        }
    }

    private String getrst(int retcode, String msg, String id){
        JSONObject o = new JSONObject();
        jsonData = new HashMap();
        jsonData.put("succ", retcode);
        Map context = new HashMap();
        if (StringUtils.isNotBlank(id))
            context.put("id",id);
        context.put("msg", msg);
        jsonData.put("context", context);
        return SUCCESS;
    }
}
