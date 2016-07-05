/*************************************************************************
	> File Name: JsonTest.java
	> Author: 
	> Mail: 
	> Created Time: 2016年07月01日 星期五 11时20分10秒
 ************************************************************************/
package com.dmg.util;

import com.dmg.trie.*;
import org.junit.Ignore;
import org.junit.Test;
import net.sf.json.JSONObject;
import java.util.*;
import static org.junit.Assert.*;


public class JsonTest{

    @Test
    public void TestJsonMap() { 
        // JSON格式数据解析对象
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("name", "Alexia");
        map1.put("sex", "female");
        map1.put("age", "23");
    
        // 将Map转换为JSONArray数据
        JSONObject ja = JSONObject.fromObject(map1);
        JSONObject jb = JSONObject.fromObject(ja.toString());
        assertEquals("Alexia", jb.getString("name"));
        assertEquals("female", jb.getString("sex"));
        assertEquals("23", jb.getString("age"));
    }

}
