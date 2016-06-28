/*************************************************************************
	> File Name: DmgTrieImplTest.java
	> Author: 
	> Mail: 
	> Created Time: 2016年06月28日 星期二 21时26分56秒
 ************************************************************************/

package com.dmg.trie;

import com.dmg.datasource.KeyValueDataSource;
import com.dmg.datasource.LevelDbDataSource;
import com.dmg.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.util.encoders.Hex;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

import static com.dmg.crypto.HashUtil.EMPTY_TRIE_HASH;
import static org.junit.Assert.*;

public class DmgTrieImplTest {

    private static final Logger logger = LoggerFactory.getLogger("test");

    private static String LONG_STRING = "1234567890abcdefghijklmnopqrstuvwxxzABCEFGHIJKLMNOPQRSTUVWXYZ";
    private static String ROOT_HASH_EMPTY = Hex.toHexString(EMPTY_TRIE_HASH);

    private static String testkey1 = "1000000000000001";
    private static String testkey2 = "2000000000000001";
    private static String testkey3 = "3000000000000001";
    private static String testkey4 = "40000000000000010000000000000000";
    private static String testkey5 = "40000000000000015000000000000001";
    private static String testkey6 = "60000000000000010000000000000000";
    private static String testkey7 = "60000000000000017000000000000001";

    private static String dog = "dog";
    private static String cat = "cat";
    private static String test = "test";

    private KeyValueDataSource levelDb = new LevelDbDataSource("triedb");
    private KeyValueDataSource levelDb_2 = new LevelDbDataSource("triedb");
//      ROOT: [ '\x16', A ]
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
        levelDb_2.close();
    }

    @Test
    public void testEmptyKey() {
        TrieImpl trie = new TrieImpl(levelDb);

        trie.update("", dog);
        assertEquals(dog, new String(trie.get("")));
    }

    @Test
    public void testInsertShortString() {
        TrieImpl trie = new TrieImpl(levelDb);

        trie.update(testkey1, dog);
        assertEquals(dog, new String(trie.get(testkey1)));
    }

    @Test
    public void testInsertLongString() {
        TrieImpl trie = new TrieImpl(levelDb);

        trie.update(testkey1, LONG_STRING);
        assertEquals(LONG_STRING, new String(trie.get(testkey1)));
    }

    @Test
    public void testInsertMultipleItems1() {
        TrieImpl trie = new TrieImpl(levelDb);
        trie.update(testkey1, dog);
        assertEquals(dog, new String(trie.get(testkey1)));

        trie.update(testkey2, cat);
        assertEquals(cat, new String(trie.get(testkey2)));

        // Test if everything is still there
        assertEquals(dog, new String(trie.get(testkey1)));
        assertEquals(cat, new String(trie.get(testkey2)));
    }

    @Test
    public void testInsertMultipleItems2() {
        TrieImpl trie = new TrieImpl(levelDb);

        trie.update(testkey1, dog);
        assertEquals(dog, new String(trie.get(testkey1)));

        trie.update(testkey2, cat);
        assertEquals(cat, new String(trie.get(testkey2)));

        trie.update(testkey3, LONG_STRING);
        assertEquals(LONG_STRING, new String(trie.get(testkey3)));


        // Test if everything is still there
        assertEquals(dog, new String(trie.get(testkey1)));
        assertEquals(cat, new String(trie.get(testkey2)));
        assertEquals(LONG_STRING, new String(trie.get(testkey3)));
    }

    @Test
    public void testUpdateShortToShortString() {
        TrieImpl trie = new TrieImpl(levelDb);

        trie.update(testkey1, dog);
        assertEquals(dog, new String(trie.get(testkey1)));

        trie.update(testkey1, dog + "1");
        assertEquals(dog + "1", new String(trie.get(testkey1)));
    }

    @Test
    public void testUpdateLongToLongString() {
        TrieImpl trie = new TrieImpl(levelDb);
        trie.update(testkey1, LONG_STRING);
        assertEquals(LONG_STRING, new String(trie.get(testkey1)));
        trie.update(testkey1, LONG_STRING + "1");
        assertEquals(LONG_STRING + "1", new String(trie.get(testkey1)));
    }

    @Test
    public void testUpdateShortToLongString() {
        TrieImpl trie = new TrieImpl(levelDb);

        trie.update(testkey1, dog);
        assertEquals(dog, new String(trie.get(testkey1)));

        trie.update(testkey1, LONG_STRING + "1");
        assertEquals(LONG_STRING + "1", new String(trie.get(testkey1)));
    }

    @Test
    public void testUpdateLongToShortString() {
        TrieImpl trie = new TrieImpl(levelDb);

        trie.update(testkey1, LONG_STRING);
        assertEquals(LONG_STRING, new String(trie.get(testkey1)));

        trie.update(testkey1, dog + "1");
        assertEquals(dog + "1", new String(trie.get(testkey1)));
    }

    @Test
    public void testDeleteShortString1() {
        TrieImpl trie = new TrieImpl(levelDb);

        trie.update(testkey1, dog);
        assertEquals(dog, new String(trie.get(testkey1)));
        byte[] ROOT_HASH_BEFORE = trie.getRootHash();

        trie.update(testkey2, cat);
        assertEquals(cat, new String(trie.get(testkey2)));
        byte[] ROOT_HASH_AFTER = trie.getRootHash();

        trie.delete(testkey2);
        assertEquals("", new String(trie.get(testkey2)));
        assertEquals(ROOT_HASH_BEFORE, trie.getRootHash());
    }

    @Test
    public void testDeleteLongString1() {
        TrieImpl trie = new TrieImpl(levelDb);

        trie.update(testkey1, LONG_STRING);
        assertEquals(LONG_STRING, new String(trie.get(testkey1)));
        byte[] ROOT_HASH_BEFORE = trie.getRootHash();

        trie.update(testkey2, LONG_STRING);
        assertEquals(LONG_STRING, new String(trie.get(testkey2)));

        trie.delete(testkey2);
        assertEquals("", new String(trie.get(testkey2)));
        assertEquals(ROOT_HASH_BEFORE, trie.getRootHash());
    }

    @Test
    public void testDeleteCompletellyDiferentItems() {
        TrieImpl trie = new TrieImpl(levelDb);

        String val_1 = "2a";
        String val_2 = "09";
        String val_3 = "a9";

        trie.update(Hex.decode(val_1), Hex.decode(val_1));
        trie.update(Hex.decode(val_2), Hex.decode(val_2));

        String root1 = Hex.toHexString(trie.getRootHash());

        trie.update(Hex.decode(val_3), Hex.decode(val_3));
        trie.delete(Hex.decode(val_3));
        String root1_ = Hex.toHexString(trie.getRootHash());

        Assert.assertEquals(root1, root1_);
    }

    @Test
    public void testDeleteAll() {
        TrieImpl trie = new TrieImpl(levelDb);
        assertEquals(ROOT_HASH_EMPTY, Hex.toHexString(trie.getRootHash()));

        trie.update(testkey1, dog);
        trie.update(testkey2, cat);
        trie.update(testkey3, LONG_STRING);
    
        trie.delete(ca);
        trie.delete(cat);
        trie.delete(doge);
        assertEquals(ROOT_HASH_EMPTY, Hex.toHexString(trie.getRootHash()));
    }

    @Test
    public void testTrieEquals() {
        TrieImpl trie1 = new TrieImpl(levelDb);
        TrieImpl trie2 = new TrieImpl(levelDb);

        trie1.update(testkey1, LONG_STRING);
        trie2.update(testkey1, LONG_STRING);
        assertTrue("Expected tries to be equal", trie1.equals(trie2));
        assertEquals(Hex.toHexString(trie1.getRootHash()), Hex.toHexString(trie2.getRootHash()));

        trie1.update(testkey1, LONG_STRING);
        trie2.update(testkey1, LONG_STRING);
        assertFalse("Expected tries not to be equal", trie1.equals(trie2));
        assertNotEquals(Hex.toHexString(trie1.getRootHash()), Hex.toHexString(trie2.getRootHash()));
    }

    @Ignore
    @Test
    public void testTrieSync() {
        TrieImpl trie = new TrieImpl(levelDb);

        trie.update(testkey1, LONG_STRING);
        assertEquals("Expected no data in database", levelDb.getAddedItems(), 0);

        trie.sync();
        assertNotEquals("Expected data to be persisted", levelDb.getAddedItems(), 0);
    }

    @Ignore
    @Test
    public void TestTrieDirtyTracking() {
        TrieImpl trie = new TrieImpl(levelDb);
        trie.update(testkey1, LONG_STRING);
        assertTrue("Expected trie to be dirty", trie.getCache().isDirty());

        trie.sync();
        assertFalse("Expected trie not to be dirty", trie.getCache().isDirty());

        trie.update(testkey2, LONG_STRING);
        trie.getCache().undo();
        assertFalse("Expected trie not to be dirty", trie.getCache().isDirty());
    }

    @Test
    public void TestTrieReset() {
        TrieImpl trie = new TrieImpl(levelDb);

        trie.update(testkey1, LONG_STRING);
        assertNotEquals("Expected cached nodes", 0, trie.getCache().getNodes().size());

        trie.getCache().undo();

        assertEquals("Expected no nodes after undo", 0, trie.getCache().getNodes().size());
    }

    @Test
    public void testTrieCopy() {
        TrieImpl trie = new TrieImpl(levelDb);
        trie.update("doe", "reindeer");
        TrieImpl trie2 = trie.copy();
        assertNotEquals(trie.hashCode(), trie2.hashCode()); // avoid possibility that its just a reference copy
        assertEquals(Hex.toHexString(trie.getRootHash()), Hex.toHexString(trie2.getRootHash()));
        assertTrue(trie.equals(trie2));
    }

    @Test
    public void testTrieUndo() {
        TrieImpl trie = new TrieImpl(levelDb);
        trie.update("doe", "reindeer");
        trie.sync();
        byte[] ROOT_HASH_BEFORE = trie.getRootHash();
        trie.update("dog", "puppy");

        trie.undo();
        assertEquals(ROOT_HASH_BEFORE, trie.getRootHash());
    }
}
