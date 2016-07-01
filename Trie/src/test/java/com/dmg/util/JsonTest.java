/*************************************************************************
	> File Name: JsonTest.java
	> Author: 
	> Mail: 
	> Created Time: 2016年07月01日 星期五 11时20分10秒
 ************************************************************************/
package com.dmg.util;

import com.dmg.datasource.KeyValueDataSource;
import com.dmg.datasource.LevelDbDataSource;
import com.dmg.datasource.HashMapDB;
import com.dmg.util.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.util.encoders.Hex;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

import static com.dmg.crypto.HashUtil.EMPTY_TRIE_HASH;
import static org.junit.Assert.*;
import static com.dmg.util.ByteUtil.wrap;

public class JsonTest{

    @Test
    public void TestJson() { 
        UserNode  n = new UserNode(testkey1, dog, 0, 0, 0, 0, 0, 0);
        // JSON格式数据解析对象
        JSONObject jo = new JSONObject();
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("name", "Alexia");
        map1.put("sex", "female");
        map1.put("age", "23");
    
        // 将Map转换为JSONArray数据
        JSONArray ja1 = JSONArray.fromObject(map1);
        jo.put("map", ja1);
        JSONArray ja = jb.getJSONArray("map");
        for (int i = 0; i < ja.size(); i++) {
            assertEquals("Alexia", ja.getJSONObject(i).getString("name"));
            assertEquals("female", ja.getJSONObject(i).getString("sex"));
            assertEquals("23", ja.getJSONObject(i).getString("age"));
        }
    }
}
