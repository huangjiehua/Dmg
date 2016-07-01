/*************************************************************************
	> File Name: JsonTest.java
	> Author: 
	> Mail: 
	> Created Time: 2016年07月01日 星期五 11时20分10秒
 ************************************************************************/
package com.dmg.util;

import com.dmg.trie.*;
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

    @Ignore
    @Test
    public void TetsJsonObject() {
        UserNode  n = new UserNode("0001", "dog", 0, 0, 0, 0, 0, 0);
        JSONObject jsonObject1 = JSONObject.fromObject(n);  
        JSONObject jsonObject2 = JSONObject.fromObject(jsonObject1.toString());  
        Object usernode = JSONObject.toBean( jsonObject1  );  
        assertEquals( jsonObject2.get( "_uid"  ), n.getUid());  
        assertEquals( jsonObject2.get( "_address"  ), n.getAddress());  
    }
}
