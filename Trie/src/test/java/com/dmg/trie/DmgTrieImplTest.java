/*************************************************************************
	> File Name: DmgTrieImplTest.java
	> Author: 
	> Mail: 
	> Created Time: 2016年06月28日 星期二 21时26分56秒
 ************************************************************************/

package com.dmg.trie;

import com.dmg.datasource.KeyValueDataSource;
import com.dmg.datasource.LevelDbDataSource;
import com.dmg.datasource.HashMapDB;
import com.dmg.util.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.util.encoders.Hex;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static com.dmg.crypto.HashUtil.EMPTY_TRIE_HASH;
import static org.junit.Assert.*;


public class DmgTrieImplTest {

    private static final Logger logger = LoggerFactory.getLogger("test");

    private static String LONG_STRING = "1234567890abcdefghijklmnopqrstuvwxxzABCEFGHIJKLMNOPQRSTUVWXYZ";
    private static String ROOT_HASH_EMPTY = Hex.toHexString(EMPTY_TRIE_HASH);

    private static String testkey1 = "10000000000000010000000000000000";
    private static String testkey2 = "20000000000000010000000000000000";
    private static String testkey3 = "30000000000000010000000000000000";
    private static String testkey4 = "40000000000000010000000000000000";
    private static String testkey5 = "40000000000000015000000000000001";
    private static String testkey6 = "60000000000000010000000000000000";
    private static String testkey7 = "60000000000000017000000000000001";
    private static String testkey8 = "5000000000000001";
    private static String testkey9 = "7000000000000001";

    private static String dog = "dog";
    private static String cat = "cat";
    private static String test = "test";

    private KeyValueDataSource levelDb = new LevelDbDataSource("triedb");
    private HashMapDB mockDb = new HashMapDB();
//      A: [ '', '', '', '', B, '', '', '', C, '', '', '', '', '', '', '', '' ]
//      B: [ '\x00\x6f', D ]
//      D: [ '', '', '', '', '', '', E, '', '', '', '', '', '', '', '', '', 'verb' ]
//      E: [ '\x17', F ]
//      F: [ '', '', '', '', '', '', G, '', '', '', '', '', '', '', '', '', 'puppy' ]
//      G: [ '\x35', 'coin' ]
//      C: [ '\x20\x6f\x72\x73\x65', 'stallion' ]

    @After
    public void closelevelDb() throws IOException {
        levelDb.close();
    }

    @Test
    public void testEmptyKey() {
        TrieImpl trie = new TrieImpl(levelDb);

        DmgTrieImpl.update32(trie, testkey1, dog);
        assertEquals(dog, DmgTrieImpl.get32(trie, testkey1));
    }

    @Test
    public void testInsertShortString() {
        TrieImpl trie = new TrieImpl(levelDb);

        DmgTrieImpl.update32(trie, testkey1, dog);
        assertEquals(dog, DmgTrieImpl.get32(trie, testkey1));
    }

    @Test
    public void testInsertLongString() {
        TrieImpl trie = new TrieImpl(levelDb);

        DmgTrieImpl.update32(trie, testkey1, LONG_STRING);
        assertEquals(LONG_STRING, DmgTrieImpl.get32(trie, testkey1));
    }

    @Test
    public void testInsertMultipleItems1() {
        TrieImpl trie = new TrieImpl(levelDb);
        DmgTrieImpl.update32(trie, testkey1, dog);
        assertEquals(dog, DmgTrieImpl.get32(trie, testkey1));

        DmgTrieImpl.update32(trie, testkey2, cat);
        assertEquals(cat, DmgTrieImpl.get32(trie, testkey2));

        // Test if everything is still there
        assertEquals(dog, DmgTrieImpl.get32(trie, testkey1));
        assertEquals(cat, DmgTrieImpl.get32(trie, testkey2));
    }

    @Test
    public void testInsertMultipleItems2() {
        TrieImpl trie = new TrieImpl(levelDb);

        DmgTrieImpl.update32(trie, testkey1, dog);
        assertEquals(dog, DmgTrieImpl.get32(trie, testkey1));

        DmgTrieImpl.update32(trie, testkey2, cat);
        assertEquals(cat, DmgTrieImpl.get32(trie, testkey2));

        DmgTrieImpl.update32(trie, testkey3, LONG_STRING);
        assertEquals(LONG_STRING, DmgTrieImpl.get32(trie, testkey3));


        // Test if everything is still there
        assertEquals(dog, DmgTrieImpl.get32(trie, testkey1));
        assertEquals(cat, DmgTrieImpl.get32(trie, testkey2));
        assertEquals(LONG_STRING, DmgTrieImpl.get32(trie, testkey3));
    }

    @Test
    public void testUpdateShortToShortString() {
        TrieImpl trie = new TrieImpl(levelDb);

        DmgTrieImpl.update32(trie, testkey1, dog);
        assertEquals(dog, DmgTrieImpl.get32(trie, testkey1));

        DmgTrieImpl.update32(trie, testkey1, dog + "1");
        assertEquals(dog + "1", DmgTrieImpl.get32(trie, testkey1));
    }

    @Test
    public void testUpdateLongToLongString() {
        TrieImpl trie = new TrieImpl(levelDb);
        DmgTrieImpl.update32(trie, testkey1, LONG_STRING);
        assertEquals(LONG_STRING, DmgTrieImpl.get32(trie, testkey1));
        DmgTrieImpl.update32(trie, testkey1, LONG_STRING + "1");
        assertEquals(LONG_STRING + "1", DmgTrieImpl.get32(trie, testkey1));
    }

    @Test
    public void testUpdateShortToLongString() {
        TrieImpl trie = new TrieImpl(levelDb);

        DmgTrieImpl.update32(trie, testkey1, dog);
        assertEquals(dog, DmgTrieImpl.get32(trie, testkey1));

        DmgTrieImpl.update32(trie, testkey1, LONG_STRING + "1");
        assertEquals(LONG_STRING + "1", DmgTrieImpl.get32(trie, testkey1));
    }

    @Test
    public void testupdateLongToShortString() {
        TrieImpl trie = new TrieImpl(levelDb);

        DmgTrieImpl.update32(trie, testkey1, LONG_STRING);
        assertEquals(LONG_STRING, DmgTrieImpl.get32(trie, testkey1));

        DmgTrieImpl.update32(trie, testkey1, dog + "1");
        assertEquals(dog + "1", DmgTrieImpl.get32(trie, testkey1));
    }

    @Test
    public void testDeleteShortString1() {
        TrieImpl trie = new TrieImpl(levelDb);

        DmgTrieImpl.update32(trie, testkey1, dog);
        assertEquals(dog, DmgTrieImpl.get32(trie, testkey1));
        byte[] ROOT_HASH_BEFORE = trie.getRootHash();

        DmgTrieImpl.update32(trie, testkey2, cat);
        assertEquals(cat, DmgTrieImpl.get32(trie, testkey2));
        byte[] ROOT_HASH_AFTER = trie.getRootHash();

        DmgTrieImpl.delete32(trie, testkey2);
        assertEquals("", DmgTrieImpl.get32(trie, testkey2));
        assertEquals(Hex.toHexString(ROOT_HASH_BEFORE), Hex.toHexString(trie.getRootHash()));
    }

    @Test
    public void testDeleteLongString1() {
        TrieImpl trie = new TrieImpl(levelDb);

        DmgTrieImpl.update32(trie, testkey1, LONG_STRING);
        assertEquals(LONG_STRING, DmgTrieImpl.get32(trie, testkey1));
        byte[] ROOT_HASH_BEFORE = trie.getRootHash();

        DmgTrieImpl.update32(trie, testkey2, LONG_STRING);
        assertEquals(LONG_STRING, DmgTrieImpl.get32(trie, testkey2));

        DmgTrieImpl.delete32(trie, testkey2);
        assertEquals("", DmgTrieImpl.get32(trie, testkey2));
        assertEquals(Hex.toHexString(ROOT_HASH_BEFORE), Hex.toHexString(trie.getRootHash()));
    }


    @Test
    public void testDeleteAll() {
        TrieImpl trie = new TrieImpl(levelDb);
        assertEquals(ROOT_HASH_EMPTY, Hex.toHexString(trie.getRootHash()));

        DmgTrieImpl.update32(trie, testkey1, dog);
        DmgTrieImpl.update32(trie, testkey2, cat);
        DmgTrieImpl.update32(trie, testkey3, LONG_STRING);
    
        DmgTrieImpl.delete32(trie, testkey1);
        DmgTrieImpl.delete32(trie, testkey2);
        DmgTrieImpl.delete32(trie, testkey3);
        assertEquals(ROOT_HASH_EMPTY, Hex.toHexString(trie.getRootHash()));
    }

    @Test
    public void testTrieEquals() {
        TrieImpl trie1 = new TrieImpl(levelDb);
        TrieImpl trie2 = new TrieImpl(levelDb);

        DmgTrieImpl.update32(trie1, testkey1, LONG_STRING);
        DmgTrieImpl.update32(trie2, testkey1, LONG_STRING);
        assertTrue("Expected tries to be equal", trie1.equals(trie2));
        assertEquals(Hex.toHexString(trie1.getRootHash()), Hex.toHexString(trie2.getRootHash()));
       
        DmgTrieImpl.update32(trie1, testkey2, LONG_STRING);
        DmgTrieImpl.update32(trie2, testkey3, LONG_STRING);
        assertFalse("Expected tries not to be equal", trie1.equals(trie2));
        assertNotEquals(Hex.toHexString(trie1.getRootHash()), Hex.toHexString(trie2.getRootHash()));
    }

    @Test
    public void TestTrieDirtyTracking() {
        TrieImpl trie = new TrieImpl(mockDb);
        DmgTrieImpl.update32(trie, testkey1, LONG_STRING);
        assertTrue("Expected trie to be dirty", trie.getCache().isDirty());

        trie.sync();
        assertFalse("Expected trie not to be dirty", trie.getCache().isDirty());

        DmgTrieImpl.update32(trie, testkey2, LONG_STRING);
        trie.getCache().undo();
        assertFalse("Expected trie not to be dirty", trie.getCache().isDirty());
    }

    @Test
    public void TestTrieReset() {
        TrieImpl trie = new TrieImpl(levelDb);

        DmgTrieImpl.update32(trie, testkey1, LONG_STRING);
        assertNotEquals("Expected cached nodes", 0, trie.getCache().getNodes().size());

        trie.getCache().undo();

        assertEquals("Expected no nodes after undo", 0, trie.getCache().getNodes().size());
    }

    @Test
    public void testTrieCopy() {
        TrieImpl trie = new TrieImpl(levelDb);
        DmgTrieImpl.update32(trie, testkey1, "reindeer");
        TrieImpl trie2 = trie.copy();
        assertNotEquals(trie.hashCode(), trie2.hashCode()); // avoid possibility that its just a reference copy
        assertEquals(Hex.toHexString(trie.getRootHash()), Hex.toHexString(trie2.getRootHash()));
        assertTrue(trie.equals(trie2));
    }

    @Test
    public void testTrieUndo() {
        TrieImpl trie = new TrieImpl(levelDb);
        DmgTrieImpl.update32(trie, testkey1, "reindeer");
        trie.sync();
        byte[] ROOT_HASH_BEFORE = trie.getRootHash();
        DmgTrieImpl.update32(trie, testkey2, "puppy");

        trie.undo();
        assertEquals(Hex.toHexString(ROOT_HASH_BEFORE), Hex.toHexString(trie.getRootHash()));
    }

    @Test
    public void TestTrieRootCopy() {
        //store root as rlpdata, and then use it for building trie.
        TrieImpl trie = new TrieImpl(mockDb);
        trie.update(testkey1, dog);
        Value val = new Value(trie.getRoot());
        trie.update(testkey2.getBytes(), val.encode());
        trie.sync();
        Value val1 = Value.fromRlpEncoded(trie.get(testkey2));
        TrieImpl trie1 = new TrieImpl(mockDb, val1.asObj());
        assertEquals(dog, new String(trie1.get(testkey1)));
    }


    @Test
    public void TestDelete1() {
        TrieImpl trie = new TrieImpl(levelDb);
        DmgTrieImpl.update32(trie, testkey1, dog);
        assertEquals(dog, new String(DmgTrieImpl.get32(trie, testkey1)));
        DmgTrieImpl.delete32(trie, testkey1);
        assertNotEquals(dog, new String(DmgTrieImpl.get32(trie, testkey1)));
    }

    @Test
    public void testGetFromRootNode() {
        TrieImpl trie1 = new TrieImpl(mockDb);
        trie1.update(cat, dog);
        trie1.sync();
        Value val = new Value(trie1.getRoot());
        String root = Utils.bytesToHexString(val.encode());
        Value val1 = Value.fromRlpEncoded(Utils.hexStringToBytes(root));
        TrieImpl trie2 = new TrieImpl(mockDb, val.asObj());
        assertEquals(dog, new String(trie2.get(cat)));
    }

    @Test
    public void testEmptyNode() {
        TrieImpl trie = new TrieImpl(mockDb);
        assertEquals("", new String(trie.get("huangjiehua")));
        //assertEquals("".getBytes(), trie.get("huangjiehua"));
    }

    @Test
    public void TestObject_field1() {
        TrieImpl trie = new TrieImpl(mockDb);
        Map<String, String> map = new HashMap<String, String>();
        map.put("uid", "123");
        map.put("address", "123");
        map.put("total", "100");
        map.put("available", "100");
        map.put("freeze", "0");
        map.put("total_invest", "3");
        map.put("status", "0");
        map.put("experience", "1");
        JSONObject jo = JSONObject.fromObject(map);
        DmgTrieImpl.update32(trie, testkey1, jo.toString());
        JSONObject ja = JSONObject.fromObject(DmgTrieImpl.get32(trie, testkey1));
        assertEquals(jo.getString("uid"), ja.getString("uid"));
        assertEquals(jo.getString("address"), ja.getString("address"));
        assertEquals(jo.getString("total"), ja.getString("total"));
        assertEquals(jo.getString("available"), ja.getString("available"));
        assertEquals(jo.getString("available"), ja.getString("available"));
        assertEquals(jo.getString("freeze"), ja.getString("freeze"));
        assertEquals(jo.getString("total_invest"), ja.getString("total_invest"));
        assertEquals(jo.getString("status"), ja.getString("status"));
        assertEquals(jo.getString("experience"), ja.getString("experience"));

    }
    @Test
    public void TestObject_field2() {
        TrieImpl trie = new TrieImpl(mockDb);
        Map<String, String> map = new HashMap<String, String>();
        map.put("uid", "123");
        map.put("address", "123");
        map.put("total", "100");
        map.put("available", "100");
        map.put("freeze", "0");
        map.put("total_invest", "3");
        map.put("status", "0");
        map.put("experience", "1");
        JSONObject jo = JSONObject.fromObject(map);
        DmgTrieImpl.update32(trie, testkey1, jo.toString());
        DmgTrieImpl.update32(trie, testkey1, "experience", "2");
        JSONObject ja = JSONObject.fromObject(DmgTrieImpl.get32(trie, testkey1));
        assertEquals(jo.getString("uid"), ja.getString("uid"));
        assertEquals(jo.getString("address"), ja.getString("address"));
        assertEquals(jo.getString("total"), ja.getString("total"));
        assertEquals(jo.getString("available"), ja.getString("available"));
        assertEquals(jo.getString("available"), ja.getString("available"));
        assertEquals(jo.getString("freeze"), ja.getString("freeze"));
        assertEquals(jo.getString("total_invest"), ja.getString("total_invest"));
        assertEquals(jo.getString("status"), ja.getString("status"));
        assertEquals("2", ja.getString("experience"));
    }
    @Test
    public void TestObject_field3() {
        TrieImpl trie = new TrieImpl(mockDb);
        Map<String, String> map = new HashMap<String, String>();
        map.put("uid", "123");
        map.put("address", "123");
        map.put("reputation", "100");
        map.put("status", "0");
        JSONObject jo = JSONObject.fromObject(map);
        DmgTrieImpl.update32(trie, testkey2, jo.toString());
        JSONObject ja = JSONObject.fromObject(DmgTrieImpl.get32(trie, testkey2));
        assertEquals(jo.getString("uid"), ja.getString("uid"));
        assertEquals(jo.getString("address"), ja.getString("address"));
        assertEquals(jo.getString("reputation"), ja.getString("reputation"));
        assertEquals(jo.getString("status"), ja.getString("status"));
    }
    @Test
    public void TestObject_field4() {
        TrieImpl trie = new TrieImpl(mockDb);
        Map<String, String> map = new HashMap<String, String>();
        map.put("uid", "123");
        map.put("address", "123");
        map.put("reputation", "100");
        map.put("status", "0");
        JSONObject jo = JSONObject.fromObject(map);
        DmgTrieImpl.update32(trie, testkey2, jo.toString());
        DmgTrieImpl.update32(trie, testkey2, "uid", "1234");
        JSONObject ja = JSONObject.fromObject(DmgTrieImpl.get32(trie, testkey2));
        assertEquals("1234", ja.getString("uid"));
        assertEquals(jo.getString("address"), ja.getString("address"));
        assertEquals(jo.getString("reputation"), ja.getString("reputation"));
        assertEquals(jo.getString("status"), ja.getString("status"));
    }
    @Test
    public void TestObject_field5() {
        TrieImpl trie = new TrieImpl(mockDb);
        Map<String, String> map = new HashMap<String, String>();
        map.put("uid", "123");
        map.put("address", "123");
        map.put("username", "0");
        JSONObject jo = JSONObject.fromObject(map);
        DmgTrieImpl.update32(trie, testkey3, jo.toString());
        JSONObject ja = JSONObject.fromObject(DmgTrieImpl.get32(trie, testkey3));
        assertEquals(jo.getString("uid"), ja.getString("uid"));
        assertEquals(jo.getString("address"), ja.getString("address"));
        assertEquals(jo.getString("username"), ja.getString("username"));
    }
    @Test
    public void TestObject_field6() {
        TrieImpl trie = new TrieImpl(mockDb);
        Map<String, String> map = new HashMap<String, String>();
        map.put("uid", "123");
        map.put("address", "123");
        map.put("username", "100");
        JSONObject jo = JSONObject.fromObject(map);
        DmgTrieImpl.update32(trie, testkey3, jo.toString());
        DmgTrieImpl.update32(trie, testkey3, "uid", "1234");
        JSONObject ja = JSONObject.fromObject(DmgTrieImpl.get32(trie, testkey3));
        assertEquals("1234", ja.getString("uid"));
        assertEquals(jo.getString("address"), ja.getString("address"));
        assertEquals(jo.getString("username"), ja.getString("username"));
    }
    @Test
    public void TestObject_field7() {
        TrieImpl trie = new TrieImpl(mockDb);
        Map<String, String> map = new HashMap<String, String>();
        map.put("root", "123");
        map.put("storage", "123");
        map.put("code", "0");
        JSONObject jo = JSONObject.fromObject(map);
        DmgTrieImpl.update32(trie, testkey4, jo.toString());
        JSONObject ja = JSONObject.fromObject(DmgTrieImpl.get32(trie, testkey4));
        assertEquals(jo.getString("root"), ja.getString("root"));
        assertEquals(jo.getString("storage"), ja.getString("storage"));
        assertEquals(jo.getString("code"), ja.getString("code"));
    }
    @Test
    public void TestObject_field8() {
        TrieImpl trie = new TrieImpl(mockDb);
        Map<String, String> map = new HashMap<String, String>();
        map.put("root", "123");
        map.put("storage", "123");
        map.put("code", "100");
        JSONObject jo = JSONObject.fromObject(map);
        DmgTrieImpl.update32(trie, testkey4, jo.toString());
        DmgTrieImpl.update32(trie, testkey4, "root", "1234");
        JSONObject ja = JSONObject.fromObject(DmgTrieImpl.get32(trie, testkey4));
        assertEquals("1234", ja.getString("root"));
        assertEquals(jo.getString("storage"), ja.getString("storage"));
        assertEquals(jo.getString("code"), ja.getString("code"));
    }
    @Test
    public void TestObject_field9() {
        TrieImpl trie = new TrieImpl(mockDb);
        Map<String, String> map = new HashMap<String, String>();
        map.put("root", "123");
        map.put("storage", "123");
        JSONObject jo = JSONObject.fromObject(map);
        DmgTrieImpl.update32(trie, testkey6, jo.toString());
        JSONObject ja = JSONObject.fromObject(DmgTrieImpl.get32(trie, testkey6));
        assertEquals(jo.getString("root"), ja.getString("root"));
        assertEquals(jo.getString("storage"), ja.getString("storage"));
    }
    @Test
    public void TestObject_field10() {
        TrieImpl trie = new TrieImpl(mockDb);
        Map<String, String> map = new HashMap<String, String>();
        map.put("root", "123");
        map.put("storage", "123");
        JSONObject jo = JSONObject.fromObject(map);
        DmgTrieImpl.update32(trie, testkey6, jo.toString());
        //DmgTrieImpl.update32(trie, testkey6, "storage", "1234");
        JSONObject ja = JSONObject.fromObject(DmgTrieImpl.get32(trie, testkey6));
        assertEquals(jo.getString("root"), ja.getString("root"));
        assertEquals("123", ja.getString("storage"));
    }
    @Test
    public void TestObject_field11() throws UnsupportedEncodingException {
        TrieImpl trie = new TrieImpl(mockDb);
        trie.update(testkey8, dog);
        Value val = new Value(trie.getRoot());
        Map<String, String> map = new HashMap<String, String>();
        map.put("root", Utils.bytesToHexString(val.encode()));
        map.put("storage", "123");
        map.put("code", "0");
        JSONObject jo = JSONObject.fromObject(map);
        TrieImpl trie1 = new TrieImpl(mockDb);
        trie1.setCache(trie.getCache());
        DmgTrieImpl.update32(trie1, testkey4, jo.toString());
        JSONObject ja = JSONObject.fromObject(new String(DmgTrieImpl.get32(trie1, testkey4)));
        assertEquals(jo.getString("root"), ja.getString("root"));
        val = Value.fromRlpEncoded(Utils.hexStringToBytes(jo.getString("root")));
        TrieImpl trie2 = new TrieImpl(mockDb, val.asObj());
        trie2.setCache(trie1.getCache());
        //assertEquals(trie.getRootHash(), trie2.getRootHash());
        assertEquals(dog, new String(trie2.get(testkey8)));
        assertEquals(dog, new String(DmgTrieImpl.get32(trie1, testkey5)));
    }
    @Test
    public void TestObject_field12() throws UnsupportedEncodingException {
        TrieImpl trie = new TrieImpl(mockDb);
        trie.update(testkey9, dog);
        Value val = new Value(trie.getRoot());
        Map<String, String> map = new HashMap<String, String>();
        map.put("root", Utils.bytesToHexString(val.encode()));
        map.put("storage", "123");
        JSONObject jo = JSONObject.fromObject(map);
        TrieImpl trie1 = new TrieImpl(mockDb);
        trie1.setCache(trie.getCache());
        DmgTrieImpl.update32(trie1, testkey6, jo.toString());
        JSONObject ja = JSONObject.fromObject(new String(DmgTrieImpl.get32(trie1, testkey6)));
        assertEquals(jo.getString("root"), ja.getString("root"));
        val = Value.fromRlpEncoded(Utils.hexStringToBytes(jo.getString("root")));
        TrieImpl trie2 = new TrieImpl(mockDb, val.asObj());
        trie2.setCache(trie1.getCache());
        //assertEquals(trie.getRootHash(), trie2.getRootHash());
        assertEquals(dog, new String(trie2.get(testkey9)));
        assertEquals(dog, new String(DmgTrieImpl.get32(trie1, testkey7)));
    }
    @Test
    public void TestObject_field13() throws UnsupportedEncodingException {
        TrieImpl trie = new TrieImpl(mockDb);
        Value val = new Value(trie.getRoot());
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("root", Utils.bytesToHexString(val.encode()));
        map.put("storage", "123");
        map.put("code", "code");
        JSONObject jo = JSONObject.fromObject(map);
        DmgTrieImpl.update32(trie, testkey4, jo.toString());
        map = new HashMap<String, String>();
        map.put("storage", "123");
        map.put("code", "code");
        jo = JSONObject.fromObject(map);
        DmgTrieImpl.update32(trie, testkey5, jo.toString());
        assertEquals("123", DmgTrieImpl.get32(trie, testkey5, "storage"));
    }
    @Test
    public void TestObject_field14() throws UnsupportedEncodingException {
        TrieImpl trie = new TrieImpl(mockDb);
        Value val = new Value(trie.getRoot());
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("root", Utils.bytesToHexString(val.encode()));
        map.put("storage", "123");
        JSONObject jo = JSONObject.fromObject(map);
        DmgTrieImpl.update32(trie, testkey6, jo.toString());
        map = new HashMap<String, String>();
        map.put("storage", "123");
        jo = JSONObject.fromObject(map);
        DmgTrieImpl.update32(trie, testkey7, jo.toString());
        assertEquals("123", DmgTrieImpl.get32(trie, testkey7, "storage"));
    }
    @Test
    public void TestObject_field15() throws UnsupportedEncodingException {
        TrieImpl trie = new TrieImpl(mockDb);
        Value val = new Value(trie.getRoot());
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("root", Utils.bytesToHexString(val.encode()));
        map.put("storage", "123");
        map.put("code", "code");
        JSONObject jo = JSONObject.fromObject(map);
        DmgTrieImpl.update32(trie, testkey4, jo.toString());
        assertEquals("code", DmgTrieImpl.get32(trie, testkey4, "code"));
    }
    @Test
    public void TestObject_field16() throws UnsupportedEncodingException {
        TrieImpl trie = new TrieImpl(mockDb);
        Value val = new Value(trie.getRoot());
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("root", Utils.bytesToHexString(val.encode()));
        map.put("storage", "123");
        JSONObject jo = JSONObject.fromObject(map);
        DmgTrieImpl.update32(trie, testkey6, jo.toString());
        assertEquals("123", DmgTrieImpl.get32(trie, testkey6, "storage"));
    }
    @Ignore
    @Test
    public void TestRootHash() {
    	TrieImpl trie = new TrieImpl(levelDb);
    	trie.update(cat, dog);
    	trie.sync();
    	System.out.println(trie.getRootHash());
    	System.out.println(levelDb.get(trie.getRootHash()));
    }

}
