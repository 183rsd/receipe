//package com.example.myapplication;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.widget.Toast;
//
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//public class RestAPITaskLogin extends AsyncTask<Integer, Void, String> {
//    protected String mURL;
//    protected String id, pwd;
//    Context context;
//
//    public RestAPITaskLogin(String url, String id, String pwd) {
//        this.mURL = url;
//        this.id = id;
//        this.pwd = pwd;
//    }
//    public RestAPITaskLogin(Context context){
//        this.context = context;
//    }
//
//    @Override
//    protected String doInBackground(Integer... params) {
//        try {
//            URL url = new URL(mURL);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setDefaultUseCaches(false);
//            conn.setRequestMethod("POST");
//            conn.setDoInput(true);
//            conn.setDoOutput(true);
//            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
//            conn.addRequestProperty("apikey", "");
//
//            StringBuffer buffer = new StringBuffer();
//            buffer.append("user_id").append("=").append(id).append("&");
//            buffer.append("password").append("=").append(pwd);
//
//            OutputStreamWriter outputStream = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
//            PrintWriter writer = new PrintWriter(outputStream);
//            writer.write(buffer.toString());
//            System.out.println(buffer.toString());
//            writer.flush();
//            writer.close();
//
//            InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
//            BufferedReader reader = new BufferedReader(tmp);
//            StringBuilder builder = new StringBuilder();
//            String str;
//            while ((str = reader.readLine()) != null) {
//                builder.append(str + "\n");
//            }
//            String result = builder.toString();
//            JSONObject jsonObject = new JSONObject(result);
//            JSONObject postObject = jsonObject.getJSONObject("code");
//            String code = postObject.getString("code");
//
//            return code;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "200";
//    }
//
//    @Override
//    protected void onPostExecute(String code) {
//         if (code.equals("200")) {
//            Toast.makeText(getActivity(), "로그인 성공", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(context.getApplicationContext(), MainActivity2.class);
//            context.startActivity(intent);
//        } else if (code.equals("207")) {
//             Toast.makeText(context.getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
//         } else if (code.equals("206")) {
//            Toast.makeText(context.getApplicationContext(), "API Key가 틀렸습니다.", Toast.LENGTH_SHORT).show();
//        } else if (code.equals("500")) {
//            Toast.makeText(context.getApplicationContext(), "API 실행 중 시스템에서 발생한 에러입니다.", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//
//
//}
