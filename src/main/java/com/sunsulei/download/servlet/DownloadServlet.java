package com.sunsulei.download.servlet;

import com.sunsulei.download.util.ThunderUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ex_sunsulei on 2017/2/9.
 */
public class DownloadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("---进入下载---");
        String download = request.getParameter("url");
        if(download.contains("thunder")){
            download = ThunderUtil.thunder2Default(download);
        }
        String url = download;
        int thread = Integer.valueOf(request.getParameter("thread"));
        final String location = request.getParameter("location");
        System.out.println("**下载的url ："+url);
        System.out.println("**下载线程数 ："+thread);
        System.out.println("**存放的路径 ："+location);
        URLConnection urlConnection = new URL(url).openConnection();
        Long byteLength = urlConnection.getContentLengthLong();
        double size = (double)byteLength / 1024 / 1024;
        System.out.println("**文件的大小 ："+size +"M");
        System.out.println("**"+url.substring(url.lastIndexOf('/')+1)+"文件总共："+byteLength+"字节---");
        if (byteLength < 1) {
            System.err.println("--下载失败，读不到内容--");
            response.getWriter().write("download failed!");
            return;
        }
        Map<Integer, String> map = getDownloadPart(thread, byteLength);
        for (int i = 1; i <= thread; i++) {
            String part = map.get(i);
            new Thread(() -> download(url,part,location)).start();
        }
        while (true){
            File file = new File(location + url.substring(url.lastIndexOf('/') + 1));
            File tixing = new File(location+"该文件正在下载，请等待!");
            tixing.createNewFile();
            if(file.length() == byteLength){
                tixing.delete();
                File a = new File(location+"下载已完成！该提醒5秒后自动删除!");
                a.createNewFile();
                response.getWriter().write("download complete!");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                a.delete();
                System.out.println("---下载完成---");
                break;
            }

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }


    private void download(String url, String downloadPart,String location) {
        try {
            HttpURLConnection urlConnection =(HttpURLConnection) new URL(url).openConnection();
            urlConnection.setRequestProperty("Range", downloadPart);
            int responseCode = urlConnection.getResponseCode();
            if(responseCode != 206){
                System.out.println(Thread.currentThread().getId()+"该线程下载的任务不是分段下载，跳过该任务，"+url+",链接下载失败！");
            }
            String fileName = url.substring(url.lastIndexOf('/')+1);
            if(fileName.length() >200){
                fileName = "aaa";
            }
            RandomAccessFile randomAccessFile = new RandomAccessFile(location+fileName,"rwd");
            String start = downloadPart.split("=|-")[1];
            randomAccessFile.seek(Long.valueOf(start));
            InputStream inputStream = urlConnection.getInputStream();
            byte [] bs = new byte[1024 * 1024];
            int len;

            while((len = inputStream.read(bs))!= -1){
                randomAccessFile.write(bs,0,len);
            }
            randomAccessFile.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Map<Integer, String> getDownloadPart(int thread, Long byteLength) {
        Map<Integer, String> map = new HashMap<>();
        if(byteLength < thread){
            map.put(1,"0-"+(byteLength-1));
            return map;
        }
        long everyPart = byteLength / thread;
        long start = 0;
        if (byteLength % thread == 0) {
            for (int i = 1; i <= thread; i++) {
                long end = everyPart * i - 1;
                map.put(i, "bytes="+start + "-" + end);
                start = end + 1;
            }
        } else {
            long yushu = byteLength % thread;
            for (int i = 1; i <= thread; i++) {
                if (yushu != 0) {
                    long end = start + everyPart;
                    map.put(i,"bytes="+ start + "-" + end);//bytes=1-100
                    start = end + 1;
                    yushu--;
                } else {
                    long end = start + everyPart - 1;
                    if (end >= byteLength - 1) {
                        end = byteLength - 1;
                    }
                    map.put(i, "bytes="+start + "-" + end);
                    start = end + 1;
                }
            }
        }
        return map;
    }
}
