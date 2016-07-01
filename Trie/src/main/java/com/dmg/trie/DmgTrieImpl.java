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
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.dmg.datasource.KeyValueDataSource;
import com.dmg.datasource.LevelDbDataSource;
import com.dmg.datasource.HashMapDB;
import com.dmg.util.RLP;
import com.dmg.util.Value;
import java.util.*;

import static com.dmg.util.ByteUtil.EMPTY_BYTE_ARRAY;
import static com.dmg.util.CompactEncoder.binToNibbles;
import static com.dmg.crypto.HashUtil.EMPTY_TRIE_HASH;

public class DmgTrieImpl {
    private static final Logger logger = LoggerFactory.getLogger("dmgtrie");
    private HashMapDB mockDb = new HashMapDB("triedb");
    
    /**
     * Retrieve a value from a key as String, and 32 means the length of key.
     */
    public static byte[] get32(TrieImpl trie, String key, String field) {
        return get32(trie, key.getBytes(), field);
    }


    public static byte[] get32(TrieImpl trie, byte[] key, String field) {
        byte[] subkey = new byte[16];
        byte[] rlpdata ;
        switch(key[0]) {
            case '1':
                {
                    for(int i = 0; i < 16; i++){
                        subkey[i] = key[i];
                    }
                    rlpdata = trie.get(subkey);
                    if(new String(rlpdata) == ""){
                        return rlpdata;
                    }
                    JSONObject jo = JSONObject.fromObject(new String(rlpdata));
                    return jo.getString(field).getBytes();
                }
            case '2':
                {
                    for(int i = 0; i < 16; i++){
                        subkey[i] = key[i];
                    }
                    rlpdata = trie.get(subkey);
                    if(new String(rlpdata) == ""){
                        return rlpdata;
                    }
                    JSONObject jo = JSONObject.fromObject(new String(rlpdata));
                    return jo.getString(field).getBytes();   
                
                }
            case '3':
                {
                    for(int i = 0; i < 16; i++){
                        subkey[i] = key[i];
                    }
                    rlpdata = trie.get(subkey);
                    if(new String(rlpdata) == ""){
                        return rlpdata;
                    }
                    JSONObject jo = JSONObject.fromObject(new String(rlpdata));
                    return jo.getString(field).getBytes();   
                
                }
            case '4':
                {
                    for(int i = 0; i < 16; i++){
                        subkey[i] = key[i];
                    }
                    rlpdata = trie.get(subkey);
                    if(new String(rlpdata) != ""){
                        for(int i = 16; i < 32;i++){
                            subkey[i-16] = key[i];
                        }
                        if (subkey[0] == '5') {
                            JSONObject jo = JSONObject.fromObject(new String(rlpdata));
                            Value val = Value.fromRlpEncoded(jo.getString("root").getBytes());
                            TrieImpl subtrie = new TrieImpl(mockDb, val.asObj());
                            subtrie.setCache(trie.getCache());
                            rlpdata = subtrie.get(subkey);
                            if(new String(rlpdata)==""){
                                return rlpdata;
                            }
                            jo = JSONObject.fromObject(new String(rlpdata));
                            return jo.getString(field).getBytes();   

                        }
                        else if(subkey[0] == '0'){
                            if(new String(rlpdata)==""){
                                return rlpdata;
                            }
                            JSONObject jo = JSONObject.fromObject(new String(rlpdata));
                            return jo.getString(field).getBytes();   
                
                        }
                        System.out.println("The key is wrong.");
                        break;
                    }
                }
            case '6':
                {
                    for(int i = 0; i < 16; i++){
                        subkey[i] = key[i];
                    }
                    rlpdata = trie.get(subkey);
                    if(new String(rlpdata) != ""){
                        for(int i = 16; i < 32;i++){
                            subkey[i-16] = key[i];
                        }
                        if (subkey[0] == '7') {
                            JSONObject jo = JSONObject.fromObject(new String(rlpdata));
                            Value val = Value.fromRlpEncoded(jo.getString("root").getBytes());
                            TrieImpl subtrie = new TrieImpl(mockDb, val.asObj());
                            subtrie.setCache(trie.getCache());
                            rlpdata = subtrie.get(subkey);
                            if(new String(rlpdata)==""){
                                return rlpdata;
                            }
                            jo = JSONObject.fromObject(new String(rlpdata));
                            return jo.getString(field).getBytes();   

                        }
                        else if(subkey[0] == '0'){
                            if(new String(rlpdata)==""){
                                return rlpdata;
                            }
                            JSONObject jo = JSONObject.fromObject(new String(rlpdata));
                            return jo.getString(field).getBytes();   
                
                        }
                        System.out.println("The key is wrong.");
                        break;
                    }
                }
                
            default: System.out.println("wrong key"); 
        }
        return "".getBytes();
    }

    public static byte[] get32(TrieImpl trie, String key) {
        return get32(trie, key.getBytes());
    }


    public static byte[] get32(TrieImpl trie, byte[] key) {
        //byte[] k = binToNibbles(key);
        byte[] subkey = new byte[16];
        byte[] rlpdata ;
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
                    rlpdata = trie.get(subkey);
                    if(new String(rlpdata) != ""){
                        for(int i = 16; i < 32;i++){
                            subkey[i-16] = key[i];
                        }
                        if (subkey[0] == '5') {
                            JSONObject jo = JSONObject.fromObject(new String(rlpdata));
                            Value val = Value.fromRlpEncoded(jo.getString("root").getBytes());
                            TrieImpl subtrie = new TrieImpl(mockDb, val.asObj());
                            subtrie.setCache(trie.getCache());
                            rlpdata = subtrie.get(subkey);
                        }
                        else if(subkey[0] != '0'){
                            System.out.println("The key is wrong.");
                        }
                        return rlpdata;
                        break;
                    }
                }
            case '6':
                {
                    for(int i = 0; i < 16; i++){
                        subkey[i] = key[i];
                    }
                    rlpdata = trie.get(subkey);
                    if(new String(rlpdata) != ""){
                        for(int i = 16; i < 32;i++){
                            subkey[i-16] = key[i];
                        }
                        if (subkey[0] == '7') {
                            JSONObject jo = JSONObject.fromObject(new String(rlpdata));
                            Value val = Value.fromRlpEncoded(jo.getString("root").getBytes());
                            TrieImpl subtrie = new TrieImpl(mockDb, val.asObj());
                            subtrie.setCache(trie.getCache());
                            rlpdata = subtrie.get(subkey);
                        }
                        else if(subkey[0] != '0'){
                            System.out.println("The key is wrong.");
                        }
                        return rlpdata;
                        break;
                    }
                }
                
            default: System.out.println("wrong key"); 
        }
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
        byte[] rlpdata ;
        switch(key[0]) {
            case '1':
                {
                    for(int i = 0; i < 16; i++){
                        subkey1[i] = key[i];
                    }
                    rlpdata = trie.update(subkey1, value);
                    if(new String(rlpdata)==""){
                        return rlpdata;
                    }
                    JSONObject jo = JSONObject.fromObject(new String(rlpdata));
                    jo = JSONObject.fromObject(new String(rlpdata));
                    Map<String, String> map = new HashMap<String, String>();
                    p.put("uid", jo.getString("uid"));
                    map.put("address", jo.getString("address"));
                    map.put("total", jo.getString("total"));
                    map.put("available", jo.getString("available"));
                    map.put("freeze", jo.getString("freeze"));
                    map.put("total_invest", jo.getString("total_invest"));
                    map.put("status", jo.getString("status"));
                    map.put("experience", jo.getString("experience"));
                    map.put(field, jo.getString(field));
                    jo = JSONObject.fromObject(map);
                    trie.update(subkey1, jo.toString().getBytes());    
                    break;
                }
            case '2':
                {
                    for(int i = 0; i < 16; i++){
                        subkey1[i] = key[i];
                    }
                    rlpdata = trie.update(subkey1, value);
                    if(new String(rlpdata)==""){
                        return rlpdata;
                    }
                    JSONObject jo = JSONObject.fromObject(new String(rlpdata));
                    jo = JSONObject.fromObject(new String(rlpdata));
                    Map<String, String> map = new HashMap<String, String>();
                    p.put("uid", jo.getString("uid"));
                    map.put("address", jo.getString("address"));
                    map.put("reputation", jo.getString("reputation"));
                    map.put("status", jo.getString("status"));
                    map.put(field, jo.getString(field));
                    jo = JSONObject.fromObject(map);
                    trie.update(subkey1, jo.toString().getBytes());    
                    break;
                }
            case '3':
                {
                    for(int i = 0; i < 16; i++){
                        subkey1[i] = key[i];
                    }
                    rlpdata = trie.update(subkey1, value);
                    if(new String(rlpdata)==""){
                        return rlpdata;
                    }
                    JSONObject jo = JSONObject.fromObject(new String(rlpdata));
                    jo = JSONObject.fromObject(new String(rlpdata));
                    Map<String, String> map = new HashMap<String, String>();
                    p.put("uid", jo.getString("uid"));
                    map.put("address", jo.getString("address"));
                    map.put("username", jo.getString("username"));
                    map.put(field, jo.getString(field));
                    jo = JSONObject.fromObject(map);
                    trie.update(subkey1, jo.toString().getBytes());    
                    break;
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
                    rlpdata = trie.get(subkey1);
                    if(new String(rlpdata) != ""){
                        for(int i = 16; i < 32;i++){
                            subkey2[i-16] = key[i];
                        }
                        if (subkey2[0] == '5') {
                            JSONObject jo = JSONObject.fromObject(new String(rlpdata));
                            Value val = Value.fromRlpEncoded(jo.getString("root").getBytes());
                            TrieImpl subtrie = new TrieImpl(mockDb, val.asObj());
                            subtrie.setCache(trie.getCache());
                            rlpdata = subtrie.get(subkey);
                            if(new String(rlpdata)==""){
                                return rlpdata;
                            }
                            jo = JSONObject.fromObject(new String(rlpdata));
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("storage", jo.getString("storage"));
                            map.put("code", jo.getString("code"));
                            map.put(field, jo.getString(field));
                            jo = JSONObject.fromObject(map);
                            subtrie.update(subkey2, jo.toString().getBytes());

                            
                        }
                        else if(subkey2[0] == '0'){
                            if(new String(rlpdata)==""){
                                return rlpdata;
                            }
                            JSONObject jo = JSONObject.fromObject(new String(rlpdata));
                            jo = JSONObject.fromObject(new String(rlpdata));
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("root", jo.getString("root"));
                            map.put("storage", jo.getString("storage"));
                            map.put("code", jo.getString("code"));
                            map.put(field, jo.getString(field));
                            jo = JSONObject.fromObject(map);
                            trie.update(subkey1, jo.toString().getBytes());    
                
                        }
                        System.out.println("The key is wrong.");
                        break;

                    }
                }
                
            case '6':
                {
                    for(int i = 0; i < 16; i++){
                        subkey1[i] = key[i];
                    }
                    rlpdata = trie.get(subkey1);
                    if(new String(rlpdata) != ""){
                        for(int i = 16; i < 32;i++){
                            subkey2[i-16] = key[i];
                        }
                        if (subkey2[0] == '7') {
                            JSONObject jo = JSONObject.fromObject(new String(rlpdata));
                            Value val = Value.fromRlpEncoded(jo.getString("root").getBytes());
                            TrieImpl subtrie = new TrieImpl(mockDb, val.asObj());
                            subtrie.setCache(trie.getCache());
                            rlpdata = subtrie.get(subkey);
                            if(new String(rlpdata)==""){
                                return rlpdata;
                            }
                            jo = JSONObject.fromObject(new String(rlpdata));
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("storage", jo.getString("storage"));
                            map.put(field, jo.getString(field));
                            jo = JSONObject.fromObject(map);
                            subtrie.update(subkey2, jo.toString().getBytes());

                            
                        }
                        else if(subkey2[0] == '0'){
                            if(new String(rlpdata)==""){
                                return rlpdata;
                            }
                            JSONObject jo = JSONObject.fromObject(new String(rlpdata));
                            jo = JSONObject.fromObject(new String(rlpdata));
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("root", jo.getString("root"));
                            map.put("storage", jo.getString("storage"));
                            map.put(field, jo.getString(field));
                            jo = JSONObject.fromObject(map);
                            trie.update(subkey1, jo.toString().getBytes());    
                
                        }
                        System.out.println("The key is wrong.");
                        break;

                    }    
                }
                
            default: System.out.println("wrong key"); 
        }
    }
    public static void update32(TrieImpl trie, String key, String value) {
        update32(trie, key.getBytes(), value.getBytes());
    }

 
    public static void update32(TrieImpl trie, byte[] key, byte[] value) {
        //byte[] k = binToNibbles(key);
        byte[] subkey1 = new byte[16];
        byte[] subkey2 = new byte[16];
        byte[] rlpdata ;
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
                        TrieImpl subtrie = new TrieImpl(mockDb, val.asObj());
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
                        TrieImpl subtrie = new TrieImpl(mockDb, val.asObj());
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
        delete32(trie, key.getBytes());
    }

    public static void delete32(TrieImpl trie, byte[] key) {
        update32(trie, key, EMPTY_BYTE_ARRAY);
    }
    /**
     * Get all key/value pairs from the trie.
     */
    //public byte[] getany(Trie trie) {
    //    return trie.serialize();
    //}
}

