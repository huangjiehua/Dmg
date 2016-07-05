
import com.dmg.trie.Trie;
import com.dmg.trie.TrieImpl;
import com.dmg.util.Value;
import com.dmg.trie.DmgTrieImpl;
import com.dmg.datasource.KeyValueDataSource;
import com.dmg.datasource.LevelDbDataSource;
import com.dmg.datasource.HashMapDB;

import java.util.*;
import org.apache.log4j.BasicConfigurator;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class Main {
	
	public static HashMapDB mockDb = new HashMapDB();
	
	public static void apply_transactions(TrieImpl trie, String data) {
		JSONObject jo = JSONObject.fromObject(data);
        Map<String, String> map = new HashMap<String, String>();
        map.put("method", jo.getString("method"));
        map.put("parameter", jo.getString("parameter"));
        switch(map.get("method")) {
        case "Mount":
        {
        	//new contract_trie
        	TrieImpl newtrie = new TrieImpl(mockDb);
        	Value val = new Value(newtrie.getRoot());
        	//project environment
        	JSONObject jo1 = JSONObject.fromObject(jo.getString("parameter"));
        	HashMap<String, String> map1 = new HashMap<String, String>();
        	map1.put("project_user", jo1.getString("userID"));
            map1.put("money", jo1.getString("money"));
            map1.put("invest_money", "0");
            map1.put("interest_period", jo1.getString("interest_period"));
            map1.put("create_time", jo1.getString("create_time"));
            map1.put("status", "1");
            jo = JSONObject.fromObject(map1);
            HashMap<String, String> map2 = new HashMap<String, String>();
            map2.put("ID", jo1.getString("project_ID"));
            map2.put("storage", jo.toString());
            map2.put("code", jo1.getString("code"));
            jo = JSONObject.fromObject(map2);
            DmgTrieImpl.update32(trie, jo1.getString("project_ID"), jo.toString());
            break;
        }
        case "Purchase":
        {
        	JSONObject jo1 = JSONObject.fromObject(jo.getString("parameter"));
        	JSONObject jo2 = JSONObject.fromObject(DmgTrieImpl.get32(trie, jo1.getString("projectID")));
        	//jo2 includes code and storage
        	//execute(trie, jo2, jo1);
        }
        case "Payback":
        {
        	//walkthrought all contracts;
        }
        case "OrderInsert":
        {
        	JSONObject jo1 = JSONObject.fromObject(jo.getString("parameter"));
        	//Suppose the table exit
        	String tablekey = "";
        	String key = tablekey + jo1.getString("id");
        	DmgTrieImpl.update32(trie, key, jo1.toString());
        }
        case "Charge":
        {
    	    JSONObject jo1 = JSONObject.fromObject(jo.getString("parameter"));
    	    //Suppose the table exit
    	    String tablekey = "";
    	    String key = tablekey + jo1.getString("id");
    	    DmgTrieImpl.update32(trie, key, jo1.toString());
        }
        case "WithdrawInsert":
        {
        	JSONObject jo1 = JSONObject.fromObject(jo.getString("parameter"));
        	//Suppose the table exit
        	String tablekey = "";
        	String key = tablekey + jo1.getString("id");
        	DmgTrieImpl.update32(trie, key, jo1.toString());
        }
        }
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BasicConfigurator.configure();
				
		//KeyValueDataSource levelDb = new LevelDbDataSource("triedb");
		//levelDb.init();
		//Trie myTrie = new TrieImpl(levelDb);
		
		//Simulate the state_root stored in the block.
        TrieImpl trie1 = new TrieImpl(mockDb);
        trie1.sync();
        Value val1 = new Value(trie1.getRoot());
        byte[] pre_state_root = val1.encode();
        
        //Simulate the new block.
        Value val2 = Value.fromRlpEncoded(pre_state_root);
        TrieImpl trie2 = new TrieImpl(mockDb, val2.asObj());
        //Simulate the Mount transaction's data
        Map<String, String> map = new HashMap<String, String>();
        map.put("projectID", "40000000000000000000150000000000000001");
        map.put("userID", "10000000000000010000000000000000");
        map.put("money", "1000000");
        map.put("code", "code");
        map.put("sign", "sign");
        map.put("create_time", "0");
        JSONObject jo = JSONObject.fromObject(map);  
        map = new HashMap<String, String>();
        map.put("method", "Mount");
        map.put("parameter", jo.toString());
        jo = JSONObject.fromObject(map);
        apply_transactions(trie2, jo.toString());
        //Simulate the Purchase transaction
        map = new HashMap<String, String>();
        map.put("userID", "10000000000000010000000000000000");
        map.put("amount", "1000");
        map.put("invest_time", "0");
        map.put("interest_time", "0");
        map.put("capital", "10");
        map.put("cycle", "1");
        map.put("return_amount", "10");
        map.put("interest", "0.1");
        map.put("status", "1");
        jo = JSONObject.fromObject(map);  
        map = new HashMap<String, String>();
        map.put("method", "Purchase");
        map.put("parameter", jo.toString());
        jo = JSONObject.fromObject(map);
        apply_transactions(trie2, jo.toString());
      //Simulate the Payback transaction 
        map = new HashMap<String, String>();
        map.put("method", "Payback");
        map.put("parameter", "");
        jo = JSONObject.fromObject(map);
        apply_transactions(trie2, jo.toString());
      //Simulate the OrderInsert transaction
        map = new HashMap<String, String>();
        map.put("id", "1");
        map.put("userID", "10000000000000010000000000000000");
        map.put("order_id", "1");
        map.put("money", "100");
        map.put("fee", "0.2");
        map.put("payment_platform", "zhifubao");
        map.put("bank", "guangda");
        map.put("status", "0");
        map.put("remark", "haha");
        map.put("add_time", "0");
        map.put("do_order_time", "0");
        jo = JSONObject.fromObject(map);  
        map = new HashMap<String, String>();
        map.put("method", "OrderInsert");
        map.put("parameter", jo.toString());
        jo = JSONObject.fromObject(map);
        apply_transactions(trie2, jo.toString());
        //Simulate the Charge transaction
        map = new HashMap<String, String>();
        map.put("order_id", "1");
        map.put("status", "1");
        map.put("remark", "1");
        map.put("finish_time", "100");
        jo = JSONObject.fromObject(map);  
        map = new HashMap<String, String>();
        map.put("method", "Charge");
        map.put("parameter", jo.toString());
        jo = JSONObject.fromObject(map);
        apply_transactions(trie2, jo.toString());
        //Simulate the WithdrawInsert transaction
        map = new HashMap<String, String>();
        map.put("id", "1");
        map.put("userID", "1");
        map.put("bank_name", "1");
        map.put("bank_card", "100");
        map.put("money", "100");
        map.put("fee", "0.1");
        map.put("add_time", "0");
        map.put("status", "1");
        map.put("sign", "sign");
        jo = JSONObject.fromObject(map);  
        map = new HashMap<String, String>();
        map.put("method", "Charge");
        map.put("parameter", jo.toString());
        jo = JSONObject.fromObject(map);
        apply_transactions(trie2, jo.toString());
        if(new String(DmgTrieImpl.get32(trie2, "40000000000000000000150000000000000001", "code")).equals("code")) System.out.println("success\n");
        //else System.out.println("fail\n");
		//trie1.sync();
		mockDb.close();
	}

}
