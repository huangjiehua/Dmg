package com.dmg.transaction;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dmg.datasource.HashMapDB;
import com.dmg.trie.*;
import com.dmg.util.Utils;
import com.dmg.util.Value;
import com.dmg.vm.MyClassLoader;

import net.sf.json.JSONObject;


public class Compute {
	private static final Logger logger = LoggerFactory.getLogger("transaction");
	public static HashMapDB mockDb = new HashMapDB();
	
    public static void apply_transactions(TrieImpl trie, List<Transaction> txs) {
    	Iterator it=txs.iterator();
    	//hasNext是取值取的是当前值.他的运算过程是判断下个是否有值如果有继续.
    	while(it.hasNext()){
    	    execute(trie, ((Transaction)it.next()).getData());
    	}
    }
    
    private static void execute(TrieImpl trie, String tx) { 	
       	JSONObject jo = JSONObject.fromObject(tx);
        Map<String, String> map = new HashMap<String, String>();
        map.put("method", jo.getString("method"));
        map.put("parameter", jo.getString("parameter"));
        switch(map.get("method")) {
        case "Mount":
        {
        	//transaction_data
        	jo = JSONObject.fromObject(map.get("parameter"));
        	//environment
            HashMap<String, String> map2 = new HashMap<String, String>();
        	map2.put("projectID", jo.getString("projectID"));
        	map2.put("contract_code", jo.getString("contract_code"));
            map2.put("money", jo.getString("money"));
            map2.put("invest_money", jo.getString("invest_money"));
            map2.put("interest_period", jo.getString("interest_period"));
            map2.put("create_time", jo.getString("create_time"));
            map2.put("status", jo.getString("status"));
            JSONObject ja = JSONObject.fromObject(map2);		
	        //execute the code
			byte[] bt = Utils.hexStringToBytes(jo.getString("project_code"));
			int count = bt.length;
			System.out.println("count:"+count);
			try {
				MyClassLoader loader = new MyClassLoader();
			    Class clazz = loader.defineMyClass( bt, 0, count);
			    System.out.println(clazz.getCanonicalName());
                Constructor<?> cs[] =clazz.getConstructors();
	            Object o= cs[0].newInstance(trie, ja.toString(), jo.toString());
	            clazz.getMethod("initialize", null).invoke(o, null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        break;
        }
        case "Purchase":
        {
        	//transaction_data
        	jo = JSONObject.fromObject(map.get("parameter"));
        	//environment
        	String projectID = jo.getString("contractID").substring(0, 16)+"0000000000000000";
        	System.out.println("projectID:"+projectID);
        	JSONObject jb = JSONObject.fromObject(DmgTrieImpl.get32(trie, projectID, "storage"));
            HashMap<String, String> map2 = new HashMap<String, String>();
        	map2.put("projectID", jb.getString("projectID"));
        	map2.put("contract_code", jb.getString("contract_code"));
            map2.put("money", jb.getString("money"));
            map2.put("invest_money", jb.getString("invest_money"));
            map2.put("interest_period", jb.getString("interest_period"));
            map2.put("create_time", jb.getString("create_time"));
            map2.put("status", jb.getString("status"));
            JSONObject ja = JSONObject.fromObject(map2);		
	        //execute the code
			byte[] bt = Utils.hexStringToBytes(DmgTrieImpl.get32(trie, projectID, "code"));
			int count = bt.length;
			System.out.println("count:"+count);
			try {
				MyClassLoader loader = new MyClassLoader();
			    Class clazz = loader.defineMyClass( bt, 0, count);
			    System.out.println(clazz.getCanonicalName());
                Constructor<?> cs[] =clazz.getConstructors();
	            Object o= cs[0].newInstance(trie, ja.toString(), jo.toString());
	            clazz.getMethod("purchase", null).invoke(o, null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            break;
        }
        case "Payback":
        {
        	//transaction_data
        	jo = JSONObject.fromObject(map.get("parameter"));
            String contractID = jo.getString("contractID");
	        //execute the code
			byte[] bt = Utils.hexStringToBytes(DmgTrieImpl.get32(trie, contractID, "code"));
			int count = bt.length;
			System.out.println("count:"+count);
			try {
				MyClassLoader loader = new MyClassLoader();
			    Class clazz = loader.defineMyClass( bt, 0, count);
			    System.out.println(clazz.getCanonicalName());
		        Object o= clazz.newInstance();
	            clazz.getMethod("payback", null).invoke(o, null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        break;
        }
        case "OrderInsert":
        {
          	JSONObject jo1 = JSONObject.fromObject(jo.getString("parameter"));
           	HashMap<String, String> map2 = new HashMap<String, String>();
           	map2.put("storage", jo1.toString());
           	jo = JSONObject.fromObject(map2);
           	DmgTrieImpl.update32(trie, jo1.getString("id"), jo.toString());
           	break;
        }
        case "Charge":
        {
          	JSONObject jo1 = JSONObject.fromObject(jo.getString("parameter"));
            HashMap<String, String> map2 = new HashMap<String, String>();
            map2.put("storage", jo1.toString());
            jo = JSONObject.fromObject(map2);
            DmgTrieImpl.update32(trie, jo1.getString("id"), jo.toString());
            break;
        }
        case "WithdrawInsert":
        {
          	JSONObject jo1 = JSONObject.fromObject(jo.getString("parameter"));
           	HashMap<String, String> map2 = new HashMap<String, String>();
           	map2.put("storage", jo1.toString());
           	jo = JSONObject.fromObject(map2);
           	DmgTrieImpl.update32(trie, jo1.getString("id"), jo.toString());
           	break;
        }
        default:
           	System.out.println("wrong method name");
        }
    }
}
