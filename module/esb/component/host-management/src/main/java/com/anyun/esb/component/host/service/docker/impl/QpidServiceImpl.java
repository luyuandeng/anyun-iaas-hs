package com.anyun.esb.component.host.service.docker.impl;

import com.anyun.cloud.dto.QpidQueueDto;
import com.anyun.cloud.param.QpidQueueParam;
import com.anyun.esb.component.host.service.docker.QpidService;
import com.google.common.collect.Maps;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zy on 17-7-17.
 */
public class QpidServiceImpl implements QpidService {
    private final static Logger LOGGER = LoggerFactory.getLogger(QpidServiceImpl.class);

    @Override
    public List<QpidQueueDto> getQueuesFromQpid(QpidQueueParam param) {
        String usernamePassword=param.getUsername()+":"+param.getPassword();
        String encoded=new String(Base64.encodeBase64(usernamePassword.getBytes()));
        StringBuffer params=new StringBuffer("select=");
        String uri="http://"+param.getIp()+":"+param.getPort()+param.getAddress()+"?"+params.append(param.getParams());
        try {
            HttpGet get=new HttpGet(uri);
            get.setHeader("Authorization","Basic"+" "+encoded);
            get.setHeader("Content-Type", "application/json; charset=utf-8");

            CloseableHttpClient httpClient= HttpClients.createDefault();
            HttpResponse response=httpClient.execute(get);
            if (response.getStatusLine().getStatusCode()==200){
                HttpEntity entity=response.getEntity();
                String queues = EntityUtils.toString(entity,"utf-8");

                JSONObject jsonObject=JSONObject.fromObject(queues);
                String headers=jsonObject.getString("headers");
                String results=jsonObject.getString("results");
                JSONArray headersArray=JSONArray.fromObject(headers);
                List<List> newList=new ArrayList();
                List list=JSONArray.fromObject(results);
                List<Map<String,Object>> mapList=new ArrayList<Map<String, Object>>();
                List<QpidQueueDto> entityList=new ArrayList<QpidQueueDto>();
                for (int i=0;i<list.size();i++){
                    String ss=list.get(i).toString();
                    List tempList=JSONArray.fromObject(ss);
                    newList.add(tempList);
                }
                for (int j=0;j<newList.size();j++){
                    Map<String,Object> map= Maps.newHashMap();
                    for (int i=0;i<newList.get(j).size();i++){
                        map.put(headersArray.get(i).toString(),newList.get(j).get(i));
                    }
                    mapList.add(map);
                }
                for (int i=0;i<mapList.size();i++){
                    QpidQueueDto qpidQueueDto=new QpidQueueDto();
                    qpidQueueDto.setId(mapList.get(i).get("id").toString());
                    qpidQueueDto.setName(mapList.get(i).get("name").toString());
                    qpidQueueDto.setType(mapList.get(i).get("type").toString());
                    qpidQueueDto.setQueueDethMessage(mapList.get(i).get("queueDepthMessages").toString());
                    entityList.add(qpidQueueDto);
                }
                return entityList;
            }else {
                LOGGER.debug("身份认证失败");
            }
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
