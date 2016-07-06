package com.dmg.vm;

import java.util.HashMap;

import com.dmg.trie.*;
import com.dmg.util.*;
import com.dmg.datasource.*;
import net.sf.json.JSONObject;

public class Project {
	private TrieImpl trie;
	private JSONObject environment;
	private JSONObject transaction;
	public HashMapDB mockDb = new HashMapDB();
	
	public Project(TrieImpl _trie, String _env, String _trans) {
		trie = _trie;
		environment = JSONObject.fromObject(_env);
		transaction = JSONObject.fromObject(_trans);
	}
	public void initialize() {
		//new contract_trie
    	TrieImpl newtrie = new TrieImpl(mockDb);
    	Value val = new Value(newtrie.getRoot());
    	
        HashMap<String, String> map2 = new HashMap<String, String>();
        map2.put("root", Utils.bytesToHexString(val.encode()));
        map2.put("storage", environment.toString());
        map2.put("code", transaction.getString("project_code"));
        JSONObject jo = JSONObject.fromObject(map2);
        DmgTrieImpl.update32(trie, environment.getString("projectID"), jo.toString());

	}

	public void purchase() {
		HashMap<String, String> map1 = new HashMap<String, String>();
    	map1.put("userID", transaction.getString("userID"));
        map1.put("contractID", transaction.getString("contractID"));
        map1.put("amount", transaction.getString("amount"));
        map1.put("invest_time", transaction.getString("invest_time"));
        map1.put("interest_time", transaction.getString("interest_time"));
        map1.put("capital", transaction.getString("capital"));
        map1.put("cycle", transaction.getString("cycle"));
        map1.put("return_amount", transaction.getString("return_amount"));
        map1.put("interest", transaction.getString("interest"));
        map1.put("status", transaction.getString("status"));
        JSONObject jo = JSONObject.fromObject(map1);
        HashMap<String, String> map2 = new HashMap<String, String>();
        map2.put("storage", jo.toString());
        map2.put("code", environment.getString("contract_code"));
        jo = JSONObject.fromObject(map2);
        DmgTrieImpl.update32(trie, transaction.getString("contractID"), jo.toString());
	}
	
}
