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
import static com.dmg.crypto.HashUtil.EMPTY_TRIE_HASH;

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
            case '2':
                {
                    for(int i = 0; i < 16; i++){
                        subkey[i] = key[i];
                    }
                    return trie.get(subkey);
                }
            case '3':
                {
                    for(int i = 0; i < 16; i++){
                        subkey[i] = key[i];
                    }
                    return trie.get(subkey);
                }
            case '4':
                {
                    for(int i = 0; i < 16; i++){
                        subkey[i] = key[i];
                    }
                    Value val = Value.fromRlpEncoded(trie.get(subkey));
                    
                    for(int i = 16; i < 32;i++){
                        subkey[i-16] = key[i];
                    }
                    if (subkey[0] == '5') {
                        KeyValueDataSource levelDb = new LevelDbDataSource("triedb");
                        TrieImpl subtrie = new TrieImpl(levelDb, val.asObj());
                        subtrie.setCache(trie.getCache());
                        rlpdata = subtrie.get(subkey);
                    }
                    else if(subkey[0] != '0'){
                        System.out.println("The key is wrong.");
                    }
                    break;
                }
            case '6':
                {
                    for(int i = 0; i < 16; i++){
                        subkey[i] = key[i];
                    }
                    Value val = Value.fromRlpEncoded(trie.get(subkey));
                   
                    for(int i = 16; i < 32;i++){
                        subkey[i-16] = key[i];
                    }
                    if (subkey[0] == '7') {
                    	KeyValueDataSource levelDb = new LevelDbDataSource("triedb");
                        TrieImpl subtrie = new TrieImpl(levelDb, val.asObj());
                        subtrie.setCache(trie.getCache());
                        rlpdata = subtrie.get(subkey);
                    }
                    else if(subkey[0] != '0'){
                        System.out.println("The key is wrong");
                    }
                    break;
                }
                
            default: System.out.println("wrong key"); 
        }
        return rlpdata;
    }

    /**
     * Insert key/value pair into trie.
     */
    public static void update32(TrieImpl trie, String key, String field, String value) {
        update32(trie, key.getBytes(), field, value.getBytes());
    }

 
    public static void update32(TrieImpl trie, byte[] key, String field ,byte[] value) {
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
                    break;
                }
            case '2':
                {
                	for(int i = 0; i < 16; i++){
                        subkey1[i] = key[i];
                    }
                    trie.update(subkey1, value);
                    break;
                }
            case '3':
                {
                	for(int i = 0; i < 16; i++){
                        subkey1[i] = key[i];
                    }
                    trie.update(subkey1, value);
                    break;
                }
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
                        subtrie.setCache(trie.getCache());
                        subtrie.update(subkey2, value);
                        val = new Value(subtrie.getRoot());
                        rlpdata = val.encode();
                        trie.update(subkey1, rlpdata);
                    }
                    else if(subkey2[0] == '0') {
                        trie.update(subkey1, value);       
                    }
                    else {
                        System.out.println("The key is wrong.");
                    }
                    break;
                }
                
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
                        subtrie.setCache(trie.getCache());
                        subtrie.update(subkey2, value);
                        val = new Value(subtrie.getRoot());
                        rlpdata = val.encode();
                        trie.update(subkey1, rlpdata);
                    }
                    else if(subkey2[0] == '0') {
                        trie.update(subkey1, value);       
                    }
                    else {
                        System.out.println("The key is wrong.");
                    }
                    break;
                }
                
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

