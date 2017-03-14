package com.sunsulei.download.util;

import org.junit.Test;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Created by ex_sunsulei on 2017/2/16.
 */
public class ThunderUtil {
    public static String thunder2Default(String thunder) {
        String thunderSubString = thunder.substring(10);
        try {
            String url = new String(Base64.getDecoder().decode(thunderSubString), "gbk");
            url = url.substring(2, url.length() - 2);
            return url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "http://www.baidu.com";
    }

    @Test
    public void thunder() {
        String a = "";
        String thunder = "thunder://QUFlZDJrOi8vfGZpbGV8JWUzJTgwJTkwbG9sJWU3JTk0JWI1JWU1JWJkJWIxJWU1JWE0JWE5JWU1JWEwJTgyd3d3LmxvbGR5dHQuY29tJWUzJTgwJTkxJUU1JUE1JTg3JUU1JUJDJTgyJUU1JThEJTlBJUU1JUEzJUFCLkhEVFMlRTQlQjglQUQlRTglOEIlQjElRTUlOEYlOEMlRTUlQUQlOTcubXA0fDE5Mjg1Mzg1NzZ8Njg3QzZGRjFFOTM0NEJBQUVDQzM0OUU4REUyMTQ0ODh8aD1LN1NUWktUWlE3MkdWSEhFN0VCUE1NWlhDNkdLTEZNNnwvWlo=";
        System.out.println(thunder2Default(thunder));

    }

    @Test
    public void bbb() throws Exception {
        String ed2k = "ed2k://|file|%e3%80%90lol%e7%94%b5%e5%bd%b1%e5%a4%a9%e5%a0%82www.loldytt.com%e3%80%91%E5%A5%87%E5%BC%82%E5%8D%9A%E5%A3%AB.HDTS%E4%B8%AD%E8%8B%B1%E5%8F%8C%E5%AD%97.mp4|1928538576|687C6FF1E9344BAAECC349E8DE214488|h=K7STZKTZQ72GVHHE7EBPMMZXC6GKLFM6|/";
        String gbk = URLDecoder.decode(ed2k, "gbk");
        String utf8 = URLDecoder.decode(ed2k, "utf8");
        System.out.println(gbk);
        System.out.println(utf8);
        List list = new ArrayList();
    }



}
