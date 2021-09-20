package com.example.myapplication;

import android.content.ContentValues;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import static com.example.myapplication.MainActivity.user_id;
import static com.example.myapplication.MainActivity.password;


public class RequestHttpURLConnection {
    public String request(String _url, ContentValues _params){

        // HttpURLConnection 참조 변수
        HttpURLConnection urlConn = null;
        // url 뒤에 붙여서 보낼 파라미터
        StringBuffer sbParams = new StringBuffer();

        // StringBuffer에 파라미터 연결
        if(_params == null) // ( 보낼 데이터 없으면 파라미터 비움 )
            sbParams.append("user_id="+user_id+"&password="+password);
        else{ // 보낼 데이터 있으면 파라미터 채움
            boolean isAnd = false;
            String key;
            String value;

            for(Map.Entry<String, Object> parameter : _params.valueSet()){
                key = parameter.getKey();
                value = parameter.getValue().toString();

                // 파라미터가 두 개 이상이면 사이에 & 붙임
                if (isAnd)
                    sbParams.append("&");
                sbParams.append(key).append("=").append(value);

                // 파라미터가 2개 이상이면 isAnd를 true로 바꾸고 다음 루프부터 & 붙임
                if(true)
                    if(_params.size()>=2)
                        isAnd = true;
            }
        }

        // HttpURLConnection을 통해 web의 데이터 가져오기
        try{
            URL url = new URL(_url);
            urlConn = (HttpURLConnection) url.openConnection();

            //  urlConn 설정.
            urlConn.setConnectTimeout(15000);
            urlConn.setReadTimeout(5000);
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestMethod("POST"); // URL 요청에 대한 메소드 설정 : POST.
            urlConn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
            urlConn.setRequestProperty("Context_Type", "application/x-www-form-urlencode");
            urlConn.setRequestProperty("apikey", "MQb3K4IM.vNWw7r8kOK4ndnIY1agOqZMqZw8KBDI4"); // ""안에 apikey를 입력

            //  parameter 전달 및 데이터 읽어오기.
            String strParams = sbParams.toString(); //sbParams에 정리한 파라미터들을 스트링으로 저장. 예)id=id1&pw=123;
            OutputStream os = urlConn.getOutputStream();
            os.write(strParams.getBytes(StandardCharsets.UTF_8)); // 출력 스트림에 출력.
            os.flush(); // 출력 스트림을 플러시(비운다)하고 버퍼링 된 모든 출력 바이트를 강제 실행.
            os.close(); // 출력 스트림을 닫고 모든 시스템 자원을 해제.

            //  연결 요청 확인.
            // 실패 시 null을 리턴하고 메서드를 종료.
            if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;

            // 읽어온 결과물 리턴.
            // 요청한 URL의 출력물을 BufferedReader로 받는다.
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), StandardCharsets.UTF_8));

            // 출력물의 라인과 그 합에 대한 변수.
            String line;
            String page = "";

            // 라인을 받아와 합친다.
            while ((line = reader.readLine()) != null){
                page += line;
            }
            return page;

        } catch (MalformedURLException e) { // for URL.
            e.printStackTrace();
        } catch (IOException e) { // for openConnection().
            e.printStackTrace();
        } finally {
            if (urlConn != null)
                urlConn.disconnect();
        }
        return null;
        }
    }
