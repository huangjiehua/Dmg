
import com.dmg.trie.Trie;
import com.dmg.trie.TrieImpl;
import com.dmg.util.Utils;
import com.dmg.util.Value;
import com.dmg.trie.DmgTrieImpl;
import com.dmg.datasource.KeyValueDataSource;
import com.dmg.datasource.LevelDbDataSource;
import com.dmg.datasource.HashMapDB;

import java.util.*;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class Main {
	private static final Logger logger = LoggerFactory.getLogger("transaction");
	
	public static HashMapDB mockDb = new HashMapDB();
	
	public static void apply_transactions(TrieImpl trie, String data) {
		JSONObject jo = JSONObject.fromObject(data);
        Map<String, String> map = new HashMap<String, String>();
        map.put("method", jo.getString("method"));
        map.put("parameter", jo.getString("parameter"));
        switch(map.get("method")) {
        case "Mount":
        {
        	//This part is completed by project_code, and this is the simulation.
        	//new contract_trie
        	TrieImpl newtrie = new TrieImpl(mockDb);
        	Value val = new Value(newtrie.getRoot());
        	//project environment
        	JSONObject jo1 = JSONObject.fromObject(jo.getString("parameter"));
        	HashMap<String, String> map1 = new HashMap<String, String>();
        	map1.put("projectID", jo1.getString("projectID"));
        	map1.put("project_user", jo1.getString("userID"));
            map1.put("money", jo1.getString("money"));
            map1.put("invest_money", "0");
            map1.put("interest_period", jo1.getString("interest_period"));
            map1.put("create_time", jo1.getString("create_time"));
            map1.put("status", "1");
            jo = JSONObject.fromObject(map1);
            HashMap<String, String> map2 = new HashMap<String, String>();
            map2.put("root", Utils.bytesToHexString(val.encode()));
            map2.put("storage", jo.toString());
            map2.put("code", jo1.getString("code"));
            jo = JSONObject.fromObject(map2);
            DmgTrieImpl.update32(trie, jo1.getString("projectID"), jo.toString());
            break;
        }
        case "Purchase":
        {
        	JSONObject jo1 = JSONObject.fromObject(jo.getString("parameter"));
        	/*
        	 * JSONObject jo2 = JSONObject.fromObject(DmgTrieImpl.get32(trie, jo1.getString("projectID")));
        	 * jo2 includes code and storage
        	 * execute(trie, jo2, jo1);
        	 * But we simulate the project_code's work---purchase
        	 */

        	HashMap<String, String> map1 = new HashMap<String, String>();
        	map1.put("userID", jo1.getString("userID"));
            map1.put("contractID", jo1.getString("contractID"));
            map1.put("amount", jo1.getString("amount"));
            map1.put("invest_time", jo1.getString("invest_time"));
            map1.put("interest_time", jo1.getString("interest_time"));
            map1.put("capital", jo1.getString("capital"));
            map1.put("cycle", jo1.getString("cycle"));
            map1.put("return_amount", jo1.getString("return_amount"));
            map1.put("interest", jo1.getString("interest"));
            map1.put("status", jo1.getString("status"));
            jo = JSONObject.fromObject(map1);
            HashMap<String, String> map2 = new HashMap<String, String>();
            map2.put("storage", jo.toString());
            map2.put("code", jo1.getString("code"));
            jo = JSONObject.fromObject(map2);
            DmgTrieImpl.update32(trie, jo1.getString("contractID"), jo.toString());
            break;
        }
        case "Payback":
        {
        	System.out.println("walkthrought all contracts");
        	break;
        }
        case "OrderInsert":
        {
        	JSONObject jo1 = JSONObject.fromObject(jo.getString("parameter"));
        	//Suppose the table exist
        	TrieImpl newtrie = new TrieImpl(mockDb);
        	Value val = new Value(newtrie.getRoot());
            HashMap<String, String> map1 = new HashMap<String, String>();
            map1.put("root", Utils.bytesToHexString(val.encode()));
            map1.put("storage", "1");
            jo = JSONObject.fromObject(map1);
        	String tablekey = "60000000000000010000000000000000";
        	DmgTrieImpl.update32(trie, tablekey, jo.toString());
        	HashMap<String, String> map2 = new HashMap<String, String>();
        	map2.put("storage", jo1.toString());
        	jo = JSONObject.fromObject(map2);
        	DmgTrieImpl.update32(trie, jo1.getString("id"), jo.toString());
        	break;
        }
        case "Charge":
        {
        	JSONObject jo1 = JSONObject.fromObject(jo.getString("parameter"));
        	//Suppose the table exist
        	TrieImpl newtrie = new TrieImpl(mockDb);
        	Value val = new Value(newtrie.getRoot());
            HashMap<String, String> map1 = new HashMap<String, String>();
            map1.put("root", Utils.bytesToHexString(val.encode()));
            map1.put("storage", "1");
            jo = JSONObject.fromObject(map1);
        	String tablekey = "60000000000000020000000000000000";
        	DmgTrieImpl.update32(trie, tablekey, jo.toString());
        	HashMap<String, String> map2 = new HashMap<String, String>();
        	map2.put("storage", jo1.toString());
        	jo = JSONObject.fromObject(map2);
        	DmgTrieImpl.update32(trie, jo1.getString("id"), jo.toString());
        	break;
        }
        case "WithdrawInsert":
        {
        	JSONObject jo1 = JSONObject.fromObject(jo.getString("parameter"));
        	//Suppose the table exist
        	TrieImpl newtrie = new TrieImpl(mockDb);
        	Value val = new Value(newtrie.getRoot());
            HashMap<String, String> map1 = new HashMap<String, String>();
            map1.put("root", Utils.bytesToHexString(val.encode()));
            map1.put("storage", "1");
            jo = JSONObject.fromObject(map1);
        	String tablekey = "60000000000000030000000000000000";
        	DmgTrieImpl.update32(trie, tablekey, jo.toString());
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//BasicConfigurator.configure();

		//KeyValueDataSource levelDb = new LevelDbDataSource("triedb");
		//levelDb.init();
		//Trie myTrie = new TrieImpl(levelDb);
		
		//Simulate the state_root stored in the block.
        TrieImpl trie1 = new TrieImpl(mockDb);
        Value val1 = new Value(trie1.getRoot());
        byte[] pre_state_root = val1.encode();
        //Simulate the new block.
        Value val2 = Value.fromRlpEncoded(pre_state_root);
        TrieImpl trie2 = new TrieImpl(mockDb, val2.asObj());
        //Simulate the Mount transaction's data
        Map<String, String> map = new HashMap<String, String>();
        map.put("projectID", "40000000000000010000000000000000");
        map.put("userID", "10000000000000010000000000000000");
        map.put("money", "1000000");
        map.put("code", "code");
        map.put("sign", "sign");
        map.put("create_time", "0");
        map.put("interest_period", "1");
        JSONObject jo = JSONObject.fromObject(map);  
        map = new HashMap<String, String>();
        map.put("method", "Mount");
        map.put("parameter", jo.toString());
        jo = JSONObject.fromObject(map);
        apply_transactions(trie2, jo.toString());
        System.out.println(DmgTrieImpl.get32(trie2, "40000000000000010000000000000000", "storage"));
        //Simulate the Purchase transaction
        map = new HashMap<String, String>();
        map.put("userID", "10000000000000010000000000000000");
        map.put("contractID", "40000000000000015000000000000001");
        map.put("amount", "1000");
        map.put("invest_time", "0");
        map.put("interest_time", "0");
        map.put("capital", "10");
        map.put("cycle", "1");
        map.put("return_amount", "10");
        map.put("interest", "0.1");
        map.put("status", "1");
        map.put("code", "code");
        jo = JSONObject.fromObject(map);  
        map = new HashMap<String, String>();
        map.put("method", "Purchase");
        map.put("parameter", jo.toString());
        jo = JSONObject.fromObject(map);
        apply_transactions(trie2, jo.toString());
        System.out.println(DmgTrieImpl.get32(trie2, "40000000000000015000000000000001", "storage"));
        //Simulate the Payback transaction 
        map = new HashMap<String, String>();
        map.put("method", "Payback");
        map.put("parameter", "");
        jo = JSONObject.fromObject(map);
        apply_transactions(trie2, jo.toString());
        //Simulate the OrderInsert transaction
        map = new HashMap<String, String>();
        map.put("id", "60000000000000017000000000000001");
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
        System.out.println(DmgTrieImpl.get32(trie2, "60000000000000017000000000000001", "storage"));
        //Simulate the Charge transaction
        map = new HashMap<String, String>();
        map.put("id", "60000000000000027000000000000001");
        map.put("status", "1");
        map.put("remark", "1");
        map.put("finish_time", "100");
        jo = JSONObject.fromObject(map);  
        map = new HashMap<String, String>();
        map.put("method", "Charge");
        map.put("parameter", jo.toString());
        jo = JSONObject.fromObject(map);
        apply_transactions(trie2, jo.toString());
        System.out.println(DmgTrieImpl.get32(trie2, "60000000000000027000000000000001", "storage"));
        //Simulate the WithdrawInsert transaction
        map = new HashMap<String, String>();
        map.put("id", "60000000000000037000000000000001");
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
        map.put("method", "WithdrawInsert");
        map.put("parameter", jo.toString());
        jo = JSONObject.fromObject(map);
        apply_transactions(trie2, jo.toString());
        System.out.println(DmgTrieImpl.get32(trie2, "60000000000000037000000000000001", "storage"));

		trie2.sync();
		mockDb.close();
	}

}
