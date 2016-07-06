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
		System.out.println(environment.getString("project_user"));
	}

	public void payback() {
		System.out.println(transaction.getString("money"));
	}
}
