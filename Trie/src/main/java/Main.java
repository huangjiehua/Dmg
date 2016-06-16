
import com.dmg.trie.Trie;
import com.dmg.trie.TrieImpl;

import org.apache.log4j.BasicConfigurator;

import com.dmg.datasource.KeyValueDataSource;
import com.dmg.datasource.LevelDbDataSource;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BasicConfigurator.configure();
				
		KeyValueDataSource levelDb = new LevelDbDataSource("triedb");
		levelDb.init();
		Trie myTrie = new TrieImpl(levelDb);
		
		myTrie.update("0x010x010x02".getBytes(), "HelloWorld".getBytes());
		
		myTrie.sync();
		System.out.println(myTrie.getRootHash().toString());
		System.out.println(levelDb.get("a5fc59427869fca71397f6a1b153465b14ad12b4f9f9956b283fad42731c3f92".getBytes()));
		levelDb.close();
	}

}
