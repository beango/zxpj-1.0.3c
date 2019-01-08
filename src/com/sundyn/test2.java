package com.sundyn;

import com.sundyn.action.FormFile;
import com.sundyn.cer.CertifacateGenerate;
import com.sundyn.util.BaseCert;
import com.xuan.xutils.http.HttpUtils;
import org.apache.logging.log4j.Level;
import org.junit.Test;

import java.io.*;
import java.net.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.*;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class test2 {
    @Test
    public void testverify(){
        String r = HttpUtils.doPost("http://10.168.13.108/fsWebServiceWebHall/evaluate/sendEvaluate.action",
                "{\"evaluateLevel\":\"1\",\"evaluateContent\":\"很满意\",\"transitionId\":\"8b0427c241d44e7b8c128fd33b8ddc3c\",\"officeId\":\"2\",\"evaluateUrlAddr\":\"/download/recorder/10D07AEA2A44-2018-11-14-16-58-24.jpg\",\"operatorId\":\"100023\",\"counterId\":\"10D07AEA2A44\"}");
        System.out.println(r);
    }

    public String doPost(String urlStr, String strInfo) {
        String reStr="";
        try {
            URL url = new URL(urlStr);
            URLConnection con = url.openConnection();
            con.setDoOutput(true);
            //con.setRequestProperty("Pragma:", "no-cache");
            con.setRequestProperty("Cache-Control", "no-cache");
            con.setRequestProperty("Content-Type", "text/xml");
            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
            out.write(new String(strInfo.getBytes("utf-8")));
            out.flush();
            out.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
            String line = "";
            for (line = br.readLine(); line != null; line = br.readLine()) {
                reStr += line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reStr;
    }

}
