package com.sundyn.action;

import com.sundyn.entity.InteLog;
import com.sundyn.service.DeptService;
import com.sundyn.util.ChangeCharset;
import com.sundyn.util.socketUdp;
import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSON;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.xvolks.jnative.util.windows.structures.GUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.*;

public class EvalCtlAction extends MainAction {
    @Getter
    @Setter
    public Map jsonData;
    @Resource
    private DeptService deptService;

    public String evalctl(){
        final HttpServletRequest request = ServletActionContext.getRequest();

        StringBuilder sb = new StringBuilder();
        Scanner scanner = null;
        try {
            String contentType = request.getContentType();
            JSONObject json = null;
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
                addLog(request, sb.toString(), null);
                if (sb==null || sb.length()==0){
                    return getrst(500, "参数为空", null);
                }
                try {
                    JSONObject paramData = new JSONObject(sb.toString());
                    String method = paramData.getString("method");
                    json = new JSONObject(paramData.getString("json"));
                    System.out.println("json; " + json);
                } catch (Exception e) {
                    e.printStackTrace();
                    return getrst(500, "获取参数失败", null);
                }
            } else{
                Enumeration<String> names = request.getParameterNames();
                StringBuilder sbParam = new StringBuilder("");
                while(names.hasMoreElements()) {
                    String name = names.nextElement();
                    String value = new String(request.getParameter(name).getBytes("iso-8859-1"),"utf-8");
                    sbParam.append("&" + name + "=" + value);
                    System.out.println("name:" + name.toString() + "     value:" + value);
                    if (name.equals("json")){
                        json = new JSONObject(value);
                    }
                }
                System.out.println("接口参数2：" + (sbParam.length()==0?"":sbParam.substring(1)));
                addLog(request, sbParam.length()==0?"":sbParam.substring(1), null);
            }
            if (null != json)
            {
                int state = json.getInt("state");
                String centerid = json.optString("centerid");
                String counterid = json.optString("counterid");
                String counterip = json.optString("counterip");
                String serviceper_no = json.optString("serviceper_no");

                if (StringUtils.isBlank(counterip)){
                    Map dept = null;
                    if(StringUtils.isNotBlank(centerid) && StringUtils.isNotBlank(counterid))
                        dept = deptService.findByMac(centerid, counterid);
                    else if (StringUtils.isNotBlank(counterid))
                        dept = deptService.findByMac(counterid);
                    if(dept!=null && dept.get("ipadd") != null && StringUtils.isNotBlank(dept.get("ipadd").toString()))
                        System.out.println("获取关联设备：" + dept.get("ipadd"));
                    else {
                        System.out.println("找不到评价器：" + centerid + ", " + counterid);
                        return getrst(500, "找不到关联评价器", null);
                    }
                    counterip = dept.get("ipadd").toString();
                    HashMap<String, Object> o = new HashMap<>();
                    o.put("countername", dept.get("name").toString());
                }
                switch (state){
                    case 0://0.登录
                        return login(counterip, serviceper_no, null);
                    case 1://1.开始办理，欢迎光临
                        return welcome(counterip);
                    case 2://2.结束办理，评价
                        return apprise(counterip);
                    case 3://3.退出
                        return logout(counterip, serviceper_no);
                    case 4://4.暂停
                        return pause1(counterip);
                    case 5://5.取消暂停
                        return resume(counterip);
                }
            }
            else{
                return getrst(500, "获取json参数失败", null);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return getrst(500, "参数转换失败", null);
        }

        return getrst(200, "成功", "70bfebb7-1594-4376-83ce-e559e5d186be");
    }

    /*
    登录
     */
    private String login(String ipadd, String serviceper_no, HashMap<String, Object> hashMap){
        try {
            final socketUdp s = new socketUdp();
            String reply = s.send(ipadd, 8001, "S04"+serviceper_no+"E");
            if (reply==null || !reply.equals("S04OK"))
                return getrst(500, "评价器未成功", null);
            s.close();
        } catch (SocketException e) {
            e.printStackTrace();
            return getrst(500, "评价器连接失败", null);
        }
        return getrst(200, "成功", null, hashMap);
    }

    /*
    退出
     */
    private String logout(String ipadd, String serviceper_no){
        try {

            final socketUdp s = new socketUdp();
            String reply = s.send(ipadd, 8001, "S05E");
            if (reply==null || !reply.equals("S05OK"))
                return getrst(500, "评价器未成功", null);
            s.close();
        } catch (SocketException e) {
            e.printStackTrace();
            return getrst(500, "评价器连接失败", null);
        }
        return getrst(200, "成功", null);
    }

    /*
    欢迎光临
     */
    private String welcome(String ipadd){
        try {
            final socketUdp s = new socketUdp();
            String reply = s.send(ipadd, 8001, "S01E");//欢迎光临
            if (reply==null || !reply.equals("S01OK"))
                return getrst(500, "评价器未成功", null);
            s.close();
        } catch (SocketException e) {
            e.printStackTrace();
            return getrst(500, "评价器连接失败", null);
        }
        return getrst(200, "成功", null);
    }

    /*
    评价
     */
    private String apprise(String ipadd){
        String guid = String.valueOf(UUID.randomUUID()).replace("-","");
        try {
            final socketUdp s = new socketUdp();
            ChangeCharset test = new ChangeCharset();
            String reply = s.send(ipadd, 8001, test.toISO_8859_1("S02"+guid+"E"));//请评价
            System.out.println("S02"+guid+"E");
            if (reply==null || !reply.equals("S02OK"))
                return getrst(500, "评价器未成功", guid);
            s.close();
        } catch (SocketException e) {
            e.printStackTrace();
            return getrst(500, "评价器连接失败", guid);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return getrst(200, "成功", guid);
    }

    /*
    暂停
     */
    private String pause1(String ipadd){
        try {
            final socketUdp s = new socketUdp();
            String reply = s.send(ipadd, 8001, "S03E");
            if (reply==null || !reply.equals("S03OK"))
                return getrst(500, "评价器未成功", null);
            s.close();
        } catch (SocketException e) {
            e.printStackTrace();
            return getrst(500, "评价器连接失败", null);
        }
        return getrst(200, "成功", null);
    }

    /*
    恢复暂停
     */
    private String resume(String ipadd){
        try {
            final socketUdp s = new socketUdp();
            String reply = s.send(ipadd, 8001, "S06E");//欢迎光临
            if (reply==null || !reply.equals("S06OK"))
                return getrst(500, "评价器未成功", null);
            s.close();
        } catch (SocketException e) {
            e.printStackTrace();
            return getrst(500, "评价器连接失败", null);
        }
        return getrst(200, "成功", null);
    }

    private String getrst(int retcode, String msg, String id){
        return getrst(retcode, msg, id, null);
    }
    private String getrst(int retcode, String msg, String id, HashMap<String, Object> hashMap){
        JSONObject o = new JSONObject();
        jsonData = new HashMap();
        jsonData.put("code", retcode);
        Map context = new HashMap();
        if (StringUtils.isNotBlank(id))
            context.put("id",id);
        context.put("msg", msg);
        if (hashMap!=null){
            context.put("data", hashMap);
        }
        jsonData.put("context", context);
        return SUCCESS;
    }

    private Integer addLog(HttpServletRequest request, String body, String ywlsh) throws UnsupportedEncodingException {
        String url = request.getScheme()+"://"+ request.getServerName()+request.getRequestURI();
        if (null!=request.getQueryString())
            url += "?"+request.getQueryString();

        InteLog log = new InteLog();
        log.setToken(request.getParameter("token"));
        log.setYwlsh(ywlsh);
        log.setInteurl(url);
        log.setIntedata(body);
        log.setReqtime(new Date());

        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) ip = request.getHeader("Proxy-Client-IP");
        if(ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) ip = request.getHeader("WL-Proxy-Client-IP");
        if(ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) ip = request.getRemoteAddr();
        log.setReqip(ip);
        log.setStatus(0);
        log.insert();
        return log.getId();
    }
}
