package com.dmg.vm;

import com.dmg.datasource.KeyValueDataSource;
import com.dmg.datasource.LevelDbDataSource;
import com.dmg.datasource.HashMapDB;
import com.dmg.trie.*;
import com.dmg.util.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.util.encoders.Hex;
import net.sf.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.util.*;

import static com.dmg.crypto.HashUtil.EMPTY_TRIE_HASH;
import static org.junit.Assert.*;

public class VMTest {
    private HashMapDB mockDb = new HashMapDB();
	
    @Test
	public void LordCodeTest() {
		//File file = new File(".");
		InputStream input;
		try {			
			input = new FileInputStream("/home/jiehua/workspace/Dmg/src/main/java/com/dmg/vm/Programmer.class");
			byte[] result = new byte[1024];
		    int count = input.read(result);
		    byte[] other = new byte[count];
		    System.arraycopy(result, 0, other, 0, count);
		    System.out.println("count-length:"+count);
			String code = Utils.bytesToHexString(other);
			byte[] bt = Utils.hexStringToBytes(code);
			int count1 = bt.length;
			System.out.println("count-length:"+count1);
			MyClassLoader loader = new MyClassLoader();
			Class clazz = loader.defineMyClass( bt, 0, count1);
			System.out.println(clazz.getCanonicalName());
	        Object o= clazz.newInstance();
	        clazz.getMethod("setName", String.class).invoke(o, "wangfangdagg");
	        clazz.getMethod("getName", null).invoke(o, null);
	        input = new FileInputStream("/home/jiehua/workspace/Dmg/src/main/java/com/dmg/vm/Programmer.class");
	        count = input.read(result);
		    System.out.println("count-length-re:"+count);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void LordCodeFromTrieTest() {
		InputStream input;
		try {			
			input = new FileInputStream("/home/jiehua/workspace/Dmg/src/main/java/com/dmg/vm/Programmer.class");
			byte[] result = new byte[1024];
		    int count = input.read(result);
		    byte[] other = new byte[count];
		    System.arraycopy(result, 0, other, 0, count);
			String code = Utils.bytesToHexString(other);
			//Simulate the trie
			TrieImpl trie = new TrieImpl(mockDb);
			//new contract_trie
        	TrieImpl newtrie = new TrieImpl(mockDb);
        	Value val = new Value(newtrie.getRoot());
        	//project environment
        	HashMap<String, String> map1 = new HashMap<String, String>();
        	map1.put("projectID", "40000000000000010000000000000000");
        	map1.put("project_user", "10000000000000010000000000000000");
            map1.put("money", "1");
            map1.put("invest_money", "0");
            map1.put("interest_period", "1");
            map1.put("create_time", "0");
            map1.put("status", "1");
            JSONObject jo = JSONObject.fromObject(map1);
            HashMap<String, String> map2 = new HashMap<String, String>();
            map2.put("root", Utils.bytesToHexString(val.encode()));
            map2.put("storage", jo.toString());
            map2.put("code", code);
            jo = JSONObject.fromObject(map2);
            DmgTrieImpl.update32(trie, "40000000000000010000000000000000", jo.toString());
			
	        //execute the code
			byte[] bt = Utils.hexStringToBytes(DmgTrieImpl.get32(trie,"40000000000000010000000000000000", "code"));
			int count1 = bt.length;
			MyClassLoader loader = new MyClassLoader();
			Class clazz = loader.defineMyClass( bt, 0, count1);
			System.out.println(clazz.getCanonicalName());
	        Object o= clazz.newInstance();
	        clazz.getMethod("setName", String.class).invoke(o, "dmg");
	        clazz.getMethod("getName", null).invoke(o, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void RealTest() {
		InputStream input;
		InputStream input1;
		try {			
			input = new FileInputStream("/home/jiehua/workspace/Dmg/bin/com/dmg/vm/Project.class");
			byte[] result = new byte[10240];
		    int count = input.read(result);
		    byte[] other = new byte[count];
		    System.out.println("count:"+count);
		    System.arraycopy(result, 0, other, 0, count);
			String project_code = Utils.bytesToHexString(other);
			input1 = new FileInputStream("/home/jiehua/workspace/Dmg/bin/com/dmg/vm/Contract.class");
			byte[] result1 = new byte[10240];
		    int count1 = input1.read(result1);
		    byte[] other1 = new byte[count1];
		    System.out.println("count1:"+count1);
		    System.arraycopy(result1, 0, other1, 0, count1);
			String contract_code = Utils.bytesToHexString(other1);
			//System.out.println("code"+code);
			//Simulate the trie
			TrieImpl trie = new TrieImpl(mockDb);
			//new contract_trie
        	TrieImpl newtrie = new TrieImpl(mockDb);
        	Value val = new Value(newtrie.getRoot());
        	//transaction
        	HashMap<String, String> map1 = new HashMap<String, String>();
        	map1.put("projectID", "40000000000000010000000000000000");
        	map1.put("project_user", "10000000000000010000000000000000");
        	map1.put("contract_code", contract_code);
        	map1.put("project_code", project_code);
            map1.put("money", "1");
            map1.put("invest_money", "0");
            map1.put("interest_period", "1");
            map1.put("create_time", "0");
            map1.put("status", "1");
            JSONObject jo = JSONObject.fromObject(map1);
            
            //environment
            HashMap<String, String> map2 = new HashMap<String, String>();
        	map2.put("projectID", map1.get("projectID"));
        	map2.put("project_user", map1.get("project_user"));
        	map2.put("contract_code", map1.get("contract_code"));
            map2.put("money", map1.get("money"));
            map2.put("invest_money", map1.get("invest_money"));
            map2.put("interest_period", map1.get("interest_period"));
            map2.put("create_time", map1.get("create_time"));
            map2.put("status", map1.get("status"));
            JSONObject ja = JSONObject.fromObject(map2);
			
	        //execute the code
			byte[] bt = Utils.hexStringToBytes(jo.getString("project_code"));
			//System.out.println("code-re"+DmgTrieImpl.get32(trie,"40000000000000010000000000000000", "code"));
			int count2 = bt.length;
			System.out.println("count2:"+count2);
			MyClassLoader loader = new MyClassLoader();
			Class clazz = loader.defineMyClass( result, 0, count);
			System.out.println(clazz.getCanonicalName());
            Constructor<?> cs[] =clazz.getConstructors();
	        Object o= cs[0].newInstance(trie, ja.toString(), jo.toString());
	        clazz.getMethod("initialize", null).invoke(o, null);
	        System.out.println("initial:"+DmgTrieImpl.get32(trie,"40000000000000010000000000000000", "storage"));
	        
	        map1 = new HashMap<String, String>();
	        map1.put("userID", "10000000000000010000000000000000");
	        map1.put("contractID", "40000000000000015000000000000001");
	        map1.put("amount", "1000");
	        map1.put("invest_time", "0");
	        map1.put("interest_time", "0");
	        map1.put("capital", "10");
	        map1.put("cycle", "1");
	        map1.put("return_amount", "10");
	        map1.put("interest", "0.1");
	        map1.put("status", "1");
	        map1.put("code", "code");
	        jo = JSONObject.fromObject(map1);  
	        o= cs[0].newInstance(trie, ja.toString(), jo.toString());
	        clazz.getMethod("purchase", null).invoke(o, null);
	        
	        System.out.println("purchase:"+DmgTrieImpl.get32(trie,"40000000000000015000000000000001", "storage"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
