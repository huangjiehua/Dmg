
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
	
	public static void apply_transactions(Trie trie, String data) {
		JSONObject jo = JSONObject.fromObject(data);
        Map<String, String> map = new HashMap<String, String>();
        map.put("method", jo.getString("Mount"));
        map.put("parameter", jo.getString("parameter"));
        switch(map.get("method")) {
        case "Mount":break;
        case "Purchase":break;
        case "Payback":break;
        case "OrderInsert":break;
        case "Charge":break;
        case "withdrawInsert":break;
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
        //Simulate the transaction's data
        Map<String, String> map = new HashMap<String, String>();
        map.put("projectID", "40000000000000000000150000000000000001");
        map.put("userID", "10000000000000010000000000000000");
        map.put("code", "code");
        map.put("sign", "sign");
        JSONObject jo = JSONObject.fromObject(map);  
        map = new HashMap<String, String>();
        map.put("method", "Mount");
        map.put("parameter", jo.toString());
        jo = JSONObject.fromObject(map);
        apply_transactions(trie2, jo.toString());
        if(new String(DmgTrieImpl.get32(trie2, "40000000000000000000150000000000000001", "code")).equals("code")) System.out.println("success\n");
        //else System.out.println("fail\n");
		//trie1.sync();
		mockDb.close();
	}

}
