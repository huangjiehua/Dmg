/*************************************************************************
	> File Name: DmgTrieImpl.java
	> Author: 
	> Mail: 
	> Created Time: 2016年06月15日 星期三 16时10分28秒
 ************************************************************************/

package com.dmg.trie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.util.encoders.Hex;

import com.dmg.datasource.KeyValueDataSource;
import com.dmg.datasource.LevelDbDataSource;
import com.dmg.util.RLP;
import com.dmg.util.Value;

import static com.dmg.util.ByteUtil.EMPTY_BYTE_ARRAY;
import static com.dmg.util.CompactEncoder.binToNibbles;

public class DmgTrieImpl {
    private static final Logger logger = LoggerFactory.getLogger("dmgtrie");

    
    /**
     * Retrieve a value from a key as String, and 32 means the length of key.
     */
    public static byte[] get32(TrieImpl trie, String key, String field) {
        return get32(trie, key.getBytes(), field);
    }


    public static byte[] get32(TrieImpl trie, byte[] key, String field) {
        //byte[] k = binToNibbles(key);
        byte[] subkey = new byte[16];
        byte[] rlpdata = new byte[16];
        switch(key[0]) {
            case '1':
                {
                    for(int i = 0; i < 16; i++){
                        subkey[i] = key[i];
                    }
                    return trie.get(subkey);
                }
                break;
            case '2':
                {
                    for(int i = 0; i < 16; i++){
                        subkey[i] = key[i];
                    }
                    return trie.get(subkey);
                }
                break;
            case '3':
                {
                    for(int i = 0; i < 16; i++){
                        subkey[i] = key[i];
                    }
                    return trie.get(subkey);
                }
                break;
            case '4':
                {
                    for(int i = 0; i < 16; i++){
                        subkey[i] = key[i];
                    }
                    return trie.get(subkey);
                    
                    for(int i = 16; i < 32;i++){
                        subkey[i-16] = key[i];
                    }
                    if (subkey[0] == '5') {
                        KeyValueDataSource levelDb = new LevelDbDataSource("triedb");
                        //levelDb.init();
                        Trie subtrie = new TrieImpl(levelDb, val.asObj());
                        rlpdata = subtrie.get(subkey);
                        Value val1 = Value.fromRlpEncoded(rlpdata);
                        System.out.println("result = "+val1.asInt());
                    }
                    else if(subkey[0] != '0'){
                        System.out.println("The key is wrong.");
                    }
                }
                break;
            case '6':
                {
                    for(int i = 0; i < 16; i++){
                        subkey[i] = key[i];
                    }
                    return trie.get(subkey);
                
                    for(int i = 16; i < 32;i++){
                        subkey[i-16] = key[i];
                    }
                    if (subkey[0] == '7') {
                    	KeyValueDataSource levelDb = new LevelDbDataSource("triedb");
                        //levelDb.init();
                        Trie subtrie = new TrieImpl(levelDb, val.asObj());
                        rlpdata = subtrie.get(subkey);
                        Value val1 = Value.fromRlpEncoded(rlpdata);
                        System.out.println("result = "+val1.asInt());
                    }
                    else if(subkey[0] != '0'){
                        System.out.println("The key is wrong");
                    }
                }
                break;
            default: System.out.println("wrong key"); 
        }
        return rlpdata;
    }

    /**
     * Insert key/value pair into trie.
     */
    public static void update32(TrieImpl trie, String key, String field, String value) {
        update32(trie, key.getBytes(), field, value);
    }

 
    public static void update32(TrieImpl trie, byte[] key, String field ,String value) {
        //byte[] k = binToNibbles(key);
        byte[] subkey1 = new byte[16];
        byte[] subkey2 = new byte[16];
        byte[] rlpdata = new byte[16];
        switch(key[0]) {
            case '1':
                {
                    for(int i = 0; i < 16; i++){
                        subkey1[i] = key[i];
                    }
                    trie.update(subkey1, value);
                }
                break;
            case '2':
                {
                	for(int i = 0; i < 16; i++){
                        subkey1[i] = key[i];
                    }
                    trie.update(subkey1, value);
                }
                break;
            case '3':
                {
                	for(int i = 0; i < 16; i++){
                        subkey1[i] = key[i];
                    }
                    trie.update(subkey1, value);
                }
                break;
            case '4':
                {
                	for(int i = 0; i < 16; i++){
                        subkey1[i] = key[i];
                    }

                    for(int i = 16; i < 32;i++){
                        subkey2[i-16] = key[i];
                    }
                    if (subkey2[0] == '5') {
                        rlpdata = trie.get(subkey1);
                        Value val = Value.fromRlpEncoded(rlpdata);
                        KeyValueDataSource levelDb = new LevelDbDataSource("triedb");
                        TrieImpl subtrie = new TrieImpl(levelDb, val.asObj());
                        subtrie.update(subkey2, value);
                        val = new Value(subtrie.getRoot());
                        rlpdata = val.encode();
                        trie.update(subkey1, rlpdata);
                        rlpdata = subtrie.get(subkey2);
                        val = Value.fromRlpEncoded(rlpdata);
                        System.out.println("cur result = "+val.asInt());
                    }
                    else if(subkey2[0] == '0') {
                        trie.update(subkey1, value);       
                    }
                    else {
                        System.out.println("The key is wrong.");
                    }
                }
                break;
            case '6':
                {
                	for(int i = 0; i < 16; i++){
                        subkey1[i] = key[i];
                    }

                    for(int i = 16; i < 32;i++){
                        subkey2[i-16] = key[i];
                    }
                    if (subkey2[0] == '7') {
                        rlpdata = trie.get(subkey1);
                        Value val = Value.fromRlpEncoded(rlpdata);
                        KeyValueDataSource levelDb = new LevelDbDataSource("triedb");
                        TrieImpl subtrie = new TrieImpl(levelDb, val.asObj());
                        subtrie.update(subkey2, value);
                        val = new Value(subtrie.getRoot());
                        rlpdata = val.encode();
                        trie.update(subkey1, rlpdata);
                        rlpdata = subtrie.get(subkey2);
                        val = Value.fromRlpEncoded(rlpdata);
                        System.out.println("cur result = "+val.asInt());
                    }
                    else if(subkey2[0] == '0') {
                        trie.update(subkey1, value);
                    }
                    else {
                        System.out.println("The key is wrong.");
                    }
                }
                break;
            default: System.out.println("wrong key"); 
        }
    }

    /**
     * Delete a key/value pair from the trie.
     */
    public static void delete32(TrieImpl trie, String key) {
        update32(trie, key.getBytes(), "test", EMPTY_BYTE_ARRAY);
    }

    public static void delete32(TrieImpl trie, byte[] key) {
        update32(trie, key, "test", EMPTY_BYTE_ARRAY);
    }
    /**
     * Get all key/value pairs from the trie.
     */
    //public byte[] getany(Trie trie) {
    //    return trie.serialize();
    //}
}

