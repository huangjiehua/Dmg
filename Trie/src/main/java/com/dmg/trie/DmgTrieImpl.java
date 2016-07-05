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
import com.dmg.util.Utils;
import com.dmg.util.Value;
import java.util.*;

import static com.dmg.util.ByteUtil.EMPTY_BYTE_ARRAY;
import static com.dmg.util.CompactEncoder.binToNibbles;
import static com.dmg.crypto.HashUtil.EMPTY_TRIE_HASH;

/**
 * 
 * @author jiehua
 * Description: This Class is uesd for Dmgblockchain, and the trie is a key-value storage.
 * The key is a 32-byte String, which can be divided into two subkeys used for subtrie. 
 * There are 7 node-types totally, and different node-types have different starters, 
 * which is the first byte in the key.
 * 	usernode---"1**************0000000000000000"
 * 	computenode---"2***************0000000000000000"
 * 	thirdpartynode---"3***************0000000000000000"
 * 	projectnode---"4***************0000000000000000"
 * 	contractnode---"4***************5***************"
 * 	tablenode---"6***************0000000000000000"
 * 	rownode---"6***************7****************"
 * 
 */

public class DmgTrieImpl {
    private static final Logger logger = LoggerFactory.getLogger("dmgtrie");
    private static HashMapDB mockDb = new HashMapDB();
    
    /**
     * Description: Retrieve a value from a key as String, and 32 means the length of key.
     * Every Value in the trie is some kind of jsonstring. "field" is one key in the jsonstring. 
     */
    public static String get32(TrieImpl trie, String key, String field) {
        return get32(trie, key.getBytes(), field);
    }


    public static String get32(TrieImpl trie, byte[] key, String field) {
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
                        return "";
                    }
                    JSONObject jo = JSONObject.fromObject(new String(rlpdata));
                    return jo.getString(field);
                }
            case '2':
                {
                    for(int i = 0; i < 16; i++){
                        subkey[i] = key[i];
                    }
                    rlpdata = trie.get(subkey);
                    if(new String(rlpdata) == ""){
                        return "";
                    }
                    JSONObject jo = JSONObject.fromObject(new String(rlpdata));
                    return jo.getString(field);   
                
                }
            case '3':
                {
                    for(int i = 0; i < 16; i++){
                        subkey[i] = key[i];
                    }
                    rlpdata = trie.get(subkey);
                    if(new String(rlpdata) == ""){
                        return "";
                    }
                    JSONObject jo = JSONObject.fromObject(new String(rlpdata));
                    return jo.getString(field);   
                
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
                            Value val = Value.fromRlpEncoded(Utils.hexStringToBytes(jo.getString("root")));
                            TrieImpl subtrie = new TrieImpl(mockDb, val.asObj());
                            subtrie.setCache(trie.getCache());
                            rlpdata = subtrie.get(subkey);
                            if(new String(rlpdata)==""){
                                return "";
                            }
                            jo = JSONObject.fromObject(new String(rlpdata));
                            return jo.getString(field);   

                        }
                        else if(subkey[0] == '0'){
                            JSONObject jo = JSONObject.fromObject(new String(rlpdata));
                            return jo.getString(field);   
                
                        }
                        else System.out.println("The key is wrong.");
                        break;
                    }
                    else return "";
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
                            Value val = Value.fromRlpEncoded(Utils.hexStringToBytes(jo.getString("root")));
                            TrieImpl subtrie = new TrieImpl(mockDb, val.asObj());
                            subtrie.setCache(trie.getCache());
                            rlpdata = subtrie.get(subkey);
                            if(new String(rlpdata)==""){
                                return "";
                            }
                            jo = JSONObject.fromObject(new String(rlpdata));
                            return jo.getString(field);   

                        }
                        else if(subkey[0] == '0'){
                            JSONObject jo = JSONObject.fromObject(new String(rlpdata));
                            return jo.getString(field);   
                
                        }
                        else System.out.println("The key is wrong.");
                        break;
                    }
                    else return "";
                }
                
            default: System.out.println("wrong key"); 
        }
        return "";
    }

    /*
     * Description: Getting the jsonstring  of key in the trie or "";
     */
    public static String get32(TrieImpl trie, String key) {
        return get32(trie, key.getBytes());
    }


    public static String get32(TrieImpl trie, byte[] key) {
        //byte[] k = binToNibbles(key);
        byte[] subkey = new byte[16];
        byte[] rlpdata ;
        switch(key[0]) {
            case '1':
                {
                    for(int i = 0; i < 16; i++){
                        subkey[i] = key[i];
                    }
                    return new String(trie.get(subkey));
                }
            case '2':
                {
                    for(int i = 0; i < 16; i++){
                        subkey[i] = key[i];
                    }
                    return new String(trie.get(subkey));
                }
            case '3':
                {
                    for(int i = 0; i < 16; i++){
                        subkey[i] = key[i];
                    }
                    return new String(trie.get(subkey));
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
                            Value val = Value.fromRlpEncoded(Utils.hexStringToBytes(jo.getString("root")));
                            TrieImpl subtrie = new TrieImpl(mockDb, val.asObj());
                            subtrie.setCache(trie.getCache());
                            rlpdata = subtrie.get(subkey);
                            return new String(rlpdata);
                        }
                        else if(subkey[0] == '0'){
                        	return new String(rlpdata);
                        }
                        else System.out.println("The key is wrong.");
                        break;
                    }
                    else return "";
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
                            Value val = Value.fromRlpEncoded(Utils.hexStringToBytes(jo.getString("root")));
                            TrieImpl subtrie = new TrieImpl(mockDb, val.asObj());
                            subtrie.setCache(trie.getCache());
                            rlpdata = subtrie.get(subkey);
                            return new String(rlpdata);
                        }
                        else if(subkey[0] == '0'){
                        	return new String(rlpdata);
                        }
                        else System.out.println("The key is wrong.");
                        break;
                    }
                    else return "";
                }
            default: System.out.println("wrong key"); 
        }
        return "";
    }

    /**
     * Insert key/value pair into trie.
     */
    public static void update32(TrieImpl trie, String key, String field, String value) {
        update32(trie, key.getBytes(), field, value);
    }

 
    public static void update32(TrieImpl trie, byte[] key, String field , String value) {
        byte[] subkey1 = new byte[16];
        byte[] subkey2 = new byte[16];
        byte[] rlpdata;
        byte[] rlpdata1;
        switch(key[0]) {
            case '1':
                {
                    for(int i = 0; i < 16; i++){
                        subkey1[i] = key[i];
                    }
                    rlpdata = trie.get(subkey1);
                    if(new String(rlpdata)!=""){
                        JSONObject jo = JSONObject.fromObject(new String(rlpdata));
                        jo = JSONObject.fromObject(new String(rlpdata));
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("uid", jo.getString("uid"));
                        map.put("address", jo.getString("address"));
                        map.put("total", jo.getString("total"));
                        map.put("available", jo.getString("available"));
                        map.put("freeze", jo.getString("freeze"));
                        map.put("total_invest", jo.getString("total_invest"));
                        map.put("status", jo.getString("status"));
                        map.put("experience", jo.getString("experience"));
                        map.put(field, value);
                        jo = JSONObject.fromObject(map);
                        trie.update(subkey1, jo.toString().getBytes());    
                        break;
                    }
                }
            case '2':
                {
                    for(int i = 0; i < 16; i++){
                        subkey1[i] = key[i];
                    }
                    rlpdata = trie.get(subkey1);
                    if(new String(rlpdata)!=""){
                        JSONObject jo = JSONObject.fromObject(new String(rlpdata));
                        jo = JSONObject.fromObject(new String(rlpdata));
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("uid", jo.getString("uid"));
                        map.put("address", jo.getString("address"));
                        map.put("reputation", jo.getString("reputation"));
                        map.put("status", jo.getString("status"));
                        map.put(field, value);
                        jo = JSONObject.fromObject(map);
                        trie.update(subkey1, jo.toString().getBytes());    
                        break;
                    }
                }
            case '3':
                {
                    for(int i = 0; i < 16; i++){
                        subkey1[i] = key[i];
                    }
                    rlpdata = trie.get(subkey1);
                    if(new String(rlpdata)!=""){
                        JSONObject jo = JSONObject.fromObject(new String(rlpdata));
                        jo = JSONObject.fromObject(new String(rlpdata));
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("uid", jo.getString("uid"));
                        map.put("address", jo.getString("address"));
                        map.put("username", jo.getString("username"));
                        map.put(field, value);
                        jo = JSONObject.fromObject(map);
                        trie.update(subkey1, jo.toString().getBytes());    
                        break;
                    }
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
                            Value val = Value.fromRlpEncoded(Utils.hexStringToBytes(jo.getString("root")));
                            TrieImpl subtrie = new TrieImpl(mockDb, val.asObj());
                            subtrie.setCache(trie.getCache());
                            rlpdata1 = subtrie.get(subkey2);
                            if(new String(rlpdata1)!=""){
                                jo = JSONObject.fromObject(new String(rlpdata1));
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("storage", jo.getString("storage"));
                                map.put("code", jo.getString("code"));
                                map.put(field, value);
                                jo = JSONObject.fromObject(map);
                                subtrie.update(subkey2, jo.toString().getBytes());
                                trie.setCache(subtrie.getCache());
                                val = new Value(subtrie.getRoot());
                                jo = JSONObject.fromObject(new String(rlpdata));
                                map = new HashMap<String, String>();
                                map.put("root", Utils.bytesToHexString(val.encode()));
                                map.put("storage", jo.getString("storage"));
                                map.put("code", jo.getString("code"));
                                jo = JSONObject.fromObject(map);
                                trie.update(subkey1, jo.toString().getBytes());    
                            }
                        }
                        else if(subkey2[0] == '0'){
                            JSONObject jo = JSONObject.fromObject(new String(rlpdata));
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("root", jo.getString("root"));
                            map.put("storage", jo.getString("storage"));
                            map.put("code", jo.getString("code"));
                            map.put(field, value);
                            jo = JSONObject.fromObject(map);
                            trie.update(subkey1, jo.toString().getBytes());    
                        }
                        else System.out.println("The key is wrong.");
                    }
                    break;
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
                            Value val = Value.fromRlpEncoded(Utils.hexStringToBytes(jo.getString("root")));
                            TrieImpl subtrie = new TrieImpl(mockDb, val.asObj());
                            subtrie.setCache(trie.getCache());
                            rlpdata1 = subtrie.get(subkey2);
                            if(new String(rlpdata1)!=""){
                                jo = JSONObject.fromObject(new String(rlpdata1));
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("storage", jo.getString("storage"));
                                map.put(field, value);
                                jo = JSONObject.fromObject(map);
                                subtrie.update(subkey2, jo.toString().getBytes());
                                trie.setCache(subtrie.getCache());
                                val = new Value(subtrie.getRoot());
                                jo = JSONObject.fromObject(new String(rlpdata));
                                map = new HashMap<String, String>();
                                map.put("root", Utils.bytesToHexString(val.encode()));
                                map.put("storage", jo.getString("storage"));
                                jo = JSONObject.fromObject(map);
                                trie.update(subkey1, jo.toString().getBytes());    
                            }
                        }
                        else if(subkey2[0] == '0'){
                            JSONObject jo = JSONObject.fromObject(new String(rlpdata));
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("root", jo.getString("root"));
                            map.put("storage", jo.getString("storage"));
                            map.put(field, value);
                            jo = JSONObject.fromObject(map);
                            trie.update(subkey1, jo.toString().getBytes());    
                        }
                        else System.out.println("The key is wrong.");
                    }
                    break;
                }
                
            default: System.out.println("wrong key"); 
        }
    }
    public static void update32(TrieImpl trie, String key, String value) {
        update32(trie, key.getBytes(), value.getBytes());
    }

 
    public static void update32(TrieImpl trie, byte[] key, byte[] value) {
        byte[] subkey1 = new byte[16];
        byte[] subkey2 = new byte[16];
        byte[] rlpdata ;
        byte[] rlpdata1;
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
                    rlpdata = trie.get(subkey1);
                    if(new String(rlpdata) != ""){
                        for(int i = 16; i < 32;i++){
                            subkey2[i-16] = key[i];
                        }
                        if (subkey2[0] == '5') {
                            JSONObject jo = JSONObject.fromObject(new String(rlpdata));
                            Value val = Value.fromRlpEncoded(Utils.hexStringToBytes(jo.getString("root")));
                            TrieImpl subtrie = new TrieImpl(mockDb, val.asObj());
                            subtrie.setCache(trie.getCache());
                            subtrie.update(subkey2, value);
                            trie.setCache(subtrie.getCache());
                            val = new Value(subtrie.getRoot());
                            HashMap map = new HashMap<String, String>();
                            map.put("root", Utils.bytesToHexString(val.encode()));
                            map.put("storage", jo.getString("storage"));
                            map.put("code", jo.getString("code"));
                            jo = JSONObject.fromObject(map);
                            trie.update(subkey1, jo.toString().getBytes());    
                        }
                        else if(subkey2[0] == '0'){
                        	trie.update(subkey1, value);
                        }
                        else System.out.println("The key is wrong.");
                    }
                    else trie.update(subkey1, value);
                    break;
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
                            Value val = Value.fromRlpEncoded(Utils.hexStringToBytes(jo.getString("root")));
                            TrieImpl subtrie = new TrieImpl(mockDb, val.asObj());
                            subtrie.setCache(trie.getCache());
                            subtrie.update(subkey2, value);
                            trie.setCache(subtrie.getCache());
                            val = new Value(subtrie.getRoot());
                            HashMap map = new HashMap<String, String>();
                            map.put("root", Utils.bytesToHexString(val.encode()));
                            map.put("storage", jo.getString("storage"));
                            jo = JSONObject.fromObject(map);
                            trie.update(subkey1, jo.toString().getBytes());    
                        }
                        else if(subkey2[0] == '0'){
                        	trie.update(subkey1, value);
                        }
                        else System.out.println("The key is wrong.");
                    }
                    else trie.update(subkey1, value);
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

