package com.dmg.transaction;

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
public class TransactionTest {
	public static HashMapDB mockDb = new HashMapDB();
    @Test
    public void TestTransactionList() {
    	TrieImpl trie = new TrieImpl(mockDb);
    	List<Transaction> txs = new ArrayList<Transaction>();
    	//Mount transaction
    	try {
		    InputStream input = new FileInputStream("/home/jiehua/workspace/Dmg/bin/com/dmg/vm/Project.class");
		    byte[] result = new byte[10240];
	        int count = input.read(result);
	        byte[] other = new byte[count];
	        System.arraycopy(result, 0, other, 0, count);
		    String project_code = Utils.bytesToHexString(other);
		    InputStream input1 = new FileInputStream("/home/jiehua/workspace/Dmg/bin/com/dmg/vm/Contract.class");
		    byte[] result1 = new byte[10240];
	        int count1 = input1.read(result1);
	        byte[] other1 = new byte[count1];
	        System.arraycopy(result1, 0, other1, 0, count1);
		    String contract_code = Utils.bytesToHexString(other1);
		
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
            map1 = new HashMap<String, String>();
            map1.put("method", "Mount");
            map1.put("parameter", jo.toString());
            jo = JSONObject.fromObject(map1);
    	    txs.add(new Transaction("1", "1", "1", 1, "1", "1", "1", jo.toString()));
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
    	    //Purchase transaction
    	    HashMap<String, String> map1 = new HashMap<String, String>();
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
            JSONObject jo = JSONObject.fromObject(map1);  
            map1 = new HashMap<String, String>();
            map1.put("method", "Purchase");
            map1.put("parameter", jo.toString());
            jo = JSONObject.fromObject(map1);
    	    txs.add(new Transaction("1", "1", "1", 1, "1", "1", "1", jo.toString()));
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
    	    //Payback transaction
    	    HashMap<String, String> map1 = new HashMap<String, String>();
    	    map1.put("contractID", "40000000000000015000000000000001");
            JSONObject jo = JSONObject.fromObject(map1);  
            map1 = new HashMap<String, String>();
            map1.put("method", "Payback");
            map1.put("parameter", jo.toString());
            jo = JSONObject.fromObject(map1);  
    	    txs.add(new Transaction("1", "1", "1", 1, "1", "1", "1", jo.toString()));
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //Suppose the table exist
        TrieImpl newtrie = new TrieImpl(mockDb);
        Value val = new Value(newtrie.getRoot());
        HashMap<String, String> map1 = new HashMap<String, String>();
        map1.put("root", Utils.bytesToHexString(val.encode()));
        map1.put("storage", "1");
        JSONObject jo = JSONObject.fromObject(map1);
        String tablekey = "60000000000000010000000000000000";
        DmgTrieImpl.update32(trie, tablekey, jo.toString());
    	//OrderInsert transaction
        map1 = new HashMap<String, String>();
        map1.put("id", "60000000000000017000000000000001");
        map1.put("userID", "10000000000000010000000000000000");
        map1.put("order_id", "1");
        map1.put("money", "100");
        map1.put("fee", "0.2");
        map1.put("payment_platform", "zhifubao");
        map1.put("bank", "guangda");
        map1.put("status", "0");
        map1.put("remark", "haha");
        map1.put("add_time", "0");
        map1.put("do_order_time", "0");
        jo = JSONObject.fromObject(map1);  
        map1 = new HashMap<String, String>();
        map1.put("method", "OrderInsert");
        map1.put("parameter", jo.toString());
        jo = JSONObject.fromObject(map1);
    	txs.add(new Transaction("1", "1", "1", 1, "1", "1", "1", jo.toString()));
    	
        //Suppose the table exist
        newtrie = new TrieImpl(mockDb);
        val = new Value(newtrie.getRoot());
        map1 = new HashMap<String, String>();
        map1.put("root", Utils.bytesToHexString(val.encode()));
        map1.put("storage", "1");
        jo = JSONObject.fromObject(map1);
        tablekey = "60000000000000020000000000000000";
        DmgTrieImpl.update32(trie, tablekey, jo.toString());
    	//Charge transaction
        map1 = new HashMap<String, String>();
        map1.put("id", "60000000000000027000000000000001");
        map1.put("status", "1");
        map1.put("remark", "1");
        map1.put("finish_time", "100");
        jo = JSONObject.fromObject(map1);  
        map1 = new HashMap<String, String>();
        map1.put("method", "Charge");
        map1.put("parameter", jo.toString());
        jo = JSONObject.fromObject(map1);
    	txs.add(new Transaction("1", "1", "1", 1, "1", "1", "1", jo.toString()));
    	//Suppose the table exist
        newtrie = new TrieImpl(mockDb);
        val = new Value(newtrie.getRoot());
        map1 = new HashMap<String, String>();
        map1.put("root", Utils.bytesToHexString(val.encode()));
        map1.put("storage", "1");
        jo = JSONObject.fromObject(map1);
        tablekey = "60000000000000030000000000000000";
        DmgTrieImpl.update32(trie, tablekey, jo.toString());
    	//WithdrawInsert transaction
        map1 = new HashMap<String, String>();
        map1.put("id", "60000000000000037000000000000001");
        map1.put("userID", "1");
        map1.put("bank_name", "1");
        map1.put("bank_card", "100");
        map1.put("money", "100");
        map1.put("fee", "0.1");
        map1.put("add_time", "0");
        map1.put("status", "1");
        map1.put("sign", "sign");
        jo = JSONObject.fromObject(map1);  
        map1 = new HashMap<String, String>();
        map1.put("method", "WithdrawInsert");
        map1.put("parameter", jo.toString());
        jo = JSONObject.fromObject(map1);
    	txs.add(new Transaction("1", "1", "1", 1, "1", "1", "1", jo.toString()));
    	Compute.apply_transactions(trie, txs);
    }
}
