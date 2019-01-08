package com.sundyn.quartz;

import com.sundyn.service.DeptService;
import com.sundyn.service.EmployeeService;
import com.sundyn.util.SundynSet;
import com.sundyn.vo.EmployeeVo;
import com.xuan.xutils.http.HttpUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.jdom.JDOMException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.context.ContextLoader;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QuartzJob {
    public static org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger();
    @Resource
    private DeptService deptService;
    @Resource
    private EmployeeService employeeService;
    /*
    同步机构
     */
    public void syncDept()
    {
        System.out.println("同步部门数据！！！");
        String inteurl = "";
        ServletContext context = ContextLoader.getCurrentWebApplicationContext().getServletContext();
        final String path = context.getRealPath("/");
        try {
            SundynSet set = SundynSet.getInstance(path);
            inteurl = set.getM_nanhai().get("evaluateurl");
            System.out.println("Quartz的任务调度！！！" + inteurl);
        } catch (JDOMException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String data = "  {\n" +
                "  \"r_code\": \"200\",\n" +
                "  \"r_msg\": \"成功响应\",\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"officeId\": \"100011\",\n" +
                "      \"officeName\": \"桂城街道社会工作局\",\n" +
                "      \"officePhone\": \"0757-86798923\",\n" +
                "      \"officeAddress\": \"桂城街道行政服务中心综合窗口\",\n" +
                "      \"officeCode\": \"075720213\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"officeId\": \"100013\",\n" +
                "      \"officeName\": \"桂城街道教育局\",\n" +
                "      \"officePhone\": \"0757-81289271\",\n" +
                "      \"officeAddress\": \"桂城街道行政服务中心综合\",\n" +
                "      \"officeCode\": \"075720198\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        data = HttpUtils.get(inteurl + "/evaluate/searchAllOperator.action?methodType=3",null).getResultStr();
        logger.info(data);

        if (data!=null){
            JSONObject jsonObject = new JSONObject(data);

            System.out.println("返回：" + jsonObject);
            if (jsonObject==null){
                logger.error("searchAllOperator接口异常：返回数据转换为json失败！");
                return;
            }
            if (!jsonObject.getString("r_code").equals("200")){
                logger.error("searchAllOperator接口异常：返回数据返回码为"+jsonObject.getString("r_code")+"！");
                return;
            }
            JSONArray jsonData = jsonObject.getJSONArray("data");
            if (jsonData==null){
                logger.error("searchAllOperator接口异常：返回data数据为空！");
                return;
            }
            for (int i=0; i<jsonData.length(); i++){
                JSONObject j = (JSONObject)jsonData.get(i);
                /*logger.info("officeName:" + j.optString("officeName")
                        + "," +"officePhone:" + j.optString("officePhone") + ","
                        + "," +"officeId:" + j.optString("officeId") + ","
                        + "," +"officeCode:" + j.optString("officeCode") + ","
                        + "," +"officeAddress:" + j.optString("officeAddress"));*/
                String mac = j.optString("officeId");
                String name = j.optString("officeName");
                String ext1 = j.optString("officeCode");
                String ext2 = j.optString("officePhone");
                String ext3 = j.optString("officeAddress");
                deptService.SyncDept(null, mac, name, 1, ext1, ext2, ext3);

                syncCounter(mac);
            }
        }
        else{
            logger.error("searchAllOperator接口异常：返回数据为空！");
        }
    }

    /*
    同步窗口
     */
    public void syncCounter(String mac)
    {
        System.out.println("同步窗口数据！！！");
        ServletContext context = ContextLoader.getCurrentWebApplicationContext().getServletContext();
        final String path = context.getRealPath("/");
        String inteurl = "";
        try {
            SundynSet set = SundynSet.getInstance(path);
            inteurl = set.getM_nanhai().get("evaluateurl");
            System.out.println("Quartz的任务调度！！！" + inteurl);
        } catch (JDOMException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String data = "{\n" +
                "  \"r_code\": \"200\",\n" +
                "  \"r_msg\": \"成功响应\",\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"counter_code\": \"44060500000001Window0102\",\n" +
                "      \"hall_code\": \"44060500000001\",\n" +
                "      \"cklc\": \"01\",\n" +
                "      \"cklx\": \"7\",\n" +
                "      \"counter_name\": \"注册许可综合窗\"\n" +
                "    }, {\n" +
                "      \"counter_code\": \"44060500000001Window0103\",\n" +
                "      \"hall_code\": \"44060500000001\",\n" +
                "      \"cklc\": \"01\",\n" +
                "      \"cklx\": \"7\",\n" +
                "      \"counter_name\": \"注册许可综合窗\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        data = HttpUtils.get(inteurl + "/evaluate/searchAllOperator.action?methodType=2&officeId=" + mac,null).getResultStr();
        logger.info(data);
        if (data!=null){
            JSONObject jsonObject = new JSONObject(data);

            System.out.println("返回：" + jsonObject);
            if (jsonObject==null){
                logger.error("searchAllOperator接口异常：返回数据转换为json失败！");
                return;
            }
            if (!jsonObject.getString("r_code").equals("200")){
                logger.error("searchAllOperator接口异常：返回数据返回码为"+jsonObject.getString("r_code")+"！");
                return;
            }
            JSONArray jsonData = jsonObject.getJSONArray("data");
            if (jsonData==null){
                logger.error("searchAllOperator接口异常：返回data数据为空！");
                return;
            }
            for (int i=0; i<jsonData.length(); i++){
                JSONObject j = (JSONObject)jsonData.get(i);
                /*logger.info("counter_code:" + j.optString("counter_code")
                        + "," +"hall_code:" + j.optString("hall_code") + ","
                        + "," +"cklc:" + j.optString("cklc") + ","
                        + "," +"ckby:" + j.optString("ckby") + ","
                        + "," +"counter_name:" + j.optString("counter_name")
                        + "," +"fathermac:" + mac);*/
                String cmac = j.optString("counter_code");
                String name = j.optString("counter_name");
                String ext1 = j.optString("hall_code");
                String ext2 = j.optString("cklc");
                String ext3 = j.optString("ckby");
                deptService.SyncDept(mac, cmac, name, 0, ext1, ext2, ext3);
            }
        }
        else{
            logger.error("searchAllOperator接口异常：返回数据为空！");
        }
    }

    /*
   同步人员
    */
    public void syncEmployee()
    {
        System.out.println("同步人员数据！！！");
        ServletContext context = ContextLoader.getCurrentWebApplicationContext().getServletContext();
        final String path = context.getRealPath("/");
        String inteurl = "";
        try {
            SundynSet set = SundynSet.getInstance(path);
            inteurl = set.getM_nanhai().get("evaluateurl");
            System.out.println("Quartz的任务调度接口:" + inteurl);
        } catch (JDOMException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String data = "{\n" +
                "  \"r_code\": \"200\",\n" +
                "  \"r_msg\": \"成功响应\",\n" +
                "  \"data\": [{\n" +
                "      \"operatorId\": \"qzh_wy\",\n" +
                "      \"operatorName\": \"王燕\",\n" +
                "      \"officeId\": \"100013\",\n" +
                "      \"officeName\": \"桂城街道教育局\",\n" +
                "      \"officeAddress\": \"行政服务中心社会事务办窗口\",\n" +
                "      \"townId\": \"0000\",\n" +
                "      \"townName\": \"南海区\",\n" +
                "      \"counterId\": \"44060500000001Window0103\",\n" +
                "      \"counterName\": \"注册许可综合窗\"\n" +
                "    } ]\n" +
                "  }";
        data = HttpUtils.get(inteurl + "/evaluate/searchAllOperator.action?methodType=1",null).getResultStr();
        logger.info(data);
        if (data!=null){
            JSONObject jsonObject = new JSONObject(data);

            System.out.println("返回：" + jsonObject);
            if (jsonObject==null){
                logger.error("searchAllOperator接口异常：返回数据转换为json失败！");
                return;
            }
            if (!jsonObject.getString("r_code").equals("200")){
                logger.error("searchAllOperator接口异常：返回数据返回码为"+jsonObject.getString("r_code")+"！");
                return;
            }
            JSONArray jsonData = jsonObject.getJSONArray("data");
            if (jsonData==null){
                logger.error("searchAllOperator接口异常：返回data数据为空！");
                return;
            }
            for (int i=0; i<jsonData.length(); i++){
                JSONObject j = (JSONObject)jsonData.get(i);
                /*logger.info("operatorId:" + j.optString("operatorId")
                        + "," +"operatorName:" + j.optString("operatorName") + ","
                        + "," +"officeId:" + j.optString("officeId") + ","
                        + "," +"officeName:" + j.optString("officeName") + ","
                        + "," +"counterName:" + j.optString("counterName"));*/
                String operatorId = j.optString("operatorId");
                String name = j.optString("operatorName");
                String officeId = j.optString("officeId");
                String mac = j.optString("officeId");
                String ext3 = j.optString("ckby");
                //deptService.SyncDept(mac, cmac, name, 0, ext1, ext2, ext3);
                Map deptData = this.deptService.findByMac(mac);
                if (null!=deptData){
                    final EmployeeVo employeeVo = new EmployeeVo();
                    employeeVo.setName(name);
                    employeeVo.setJob_desc("");
                    employeeVo.setSex("1");
                    employeeVo.setCardnum(operatorId);
                    employeeVo.setPhone("");
                    employeeVo.setDeptid(Integer.valueOf(deptData.get("id").toString()));
                    employeeVo.setPassWord("49BA59ABBE56E057");


                    employeeService.addEmployee(employeeVo);
                }
            }
        }
        else{
            logger.error("searchAllOperator接口异常：返回数据为空！");
        }
    }

    private void getrst(int retcode, String msg, String id){
        JSONObject o = new JSONObject();
        HashMap jsonData = new HashMap();
        jsonData.put("code", retcode);
        Map context = new HashMap();
        if (StringUtils.isNotBlank(id))
            context.put("id",id);
        context.put("msg", msg);
        jsonData.put("context", context);
    }
}
