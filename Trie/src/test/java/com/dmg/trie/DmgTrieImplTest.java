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
import org.junit.After;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.util.encoders.Hex;

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

        DmgTrieImpl.update32(trie, testkey1, "test", dog);
        assertEquals(dog, new String(DmgTrieImpl.get32(trie, testkey1, "test")));
    }

    @Test
    public void testInsertShortString() {
        TrieImpl trie = new TrieImpl(levelDb);

        DmgTrieImpl.update32(trie, testkey1, "test", dog);
        assertEquals(dog, new String(DmgTrieImpl.get32(trie, testkey1, "test")));
    }

    @Test
    public void testInsertLongString() {
        TrieImpl trie = new TrieImpl(levelDb);

        DmgTrieImpl.update32(trie, testkey1, "test", LONG_STRING);
        assertEquals(LONG_STRING, new String(DmgTrieImpl.get32(trie, testkey1, "test")));
    }

    @Test
    public void testInsertMultipleItems1() {
        TrieImpl trie = new TrieImpl(levelDb);
        DmgTrieImpl.update32(trie, testkey1, "test", dog);
        assertEquals(dog, new String(DmgTrieImpl.get32(trie, testkey1, "test")));

        DmgTrieImpl.update32(trie, testkey2, "test", cat);
        assertEquals(cat, new String(DmgTrieImpl.get32(trie, testkey2, "test")));

        // Test if everything is still there
        assertEquals(dog, new String(DmgTrieImpl.get32(trie, testkey1, "test")));
        assertEquals(cat, new String(DmgTrieImpl.get32(trie, testkey2, "test")));
    }

    @Test
    public void testInsertMultipleItems2() {
        TrieImpl trie = new TrieImpl(levelDb);

        DmgTrieImpl.update32(trie, testkey1, "test", dog);
        assertEquals(dog, new String(DmgTrieImpl.get32(trie, testkey1, "test")));

        DmgTrieImpl.update32(trie, testkey2, "test", cat);
        assertEquals(cat, new String(DmgTrieImpl.get32(trie, testkey2, "test")));

        DmgTrieImpl.update32(trie, testkey3, "test", LONG_STRING);
        assertEquals(LONG_STRING, new String(DmgTrieImpl.get32(trie, testkey3, "test")));


        // Test if everything is still there
        assertEquals(dog, new String(DmgTrieImpl.get32(trie, testkey1, "test")));
        assertEquals(cat, new String(DmgTrieImpl.get32(trie, testkey2, "test")));
        assertEquals(LONG_STRING, new String(DmgTrieImpl.get32(trie, testkey3, "test")));
    }

    @Test
    public void testUpdateShortToShortString() {
        TrieImpl trie = new TrieImpl(levelDb);

        DmgTrieImpl.update32(trie, testkey1, "test", dog);
        assertEquals(dog, new String(DmgTrieImpl.get32(trie, testkey1, "test")));

        DmgTrieImpl.update32(trie, testkey1, "test", dog + "1");
        assertEquals(dog + "1", new String(DmgTrieImpl.get32(trie, testkey1, "test")));
    }

    @Test
    public void testUpdateLongToLongString() {
        TrieImpl trie = new TrieImpl(levelDb);
        DmgTrieImpl.update32(trie, testkey1, "test", LONG_STRING);
        assertEquals(LONG_STRING, new String(DmgTrieImpl.get32(trie, testkey1, "test")));
        DmgTrieImpl.update32(trie, testkey1, "test", LONG_STRING + "1");
        assertEquals(LONG_STRING + "1", new String(DmgTrieImpl.get32(trie, testkey1, "test")));
    }

    @Test
    public void testUpdateShortToLongString() {
        TrieImpl trie = new TrieImpl(levelDb);

        DmgTrieImpl.update32(trie, testkey1, "test", dog);
        assertEquals(dog, new String(DmgTrieImpl.get32(trie, testkey1, "test")));

        DmgTrieImpl.update32(trie, testkey1, "test", LONG_STRING + "1");
        assertEquals(LONG_STRING + "1", new String(DmgTrieImpl.get32(trie, testkey1, "test")));
    }

    @Test
    public void testupdateLongToShortString() {
        TrieImpl trie = new TrieImpl(levelDb);

        DmgTrieImpl.update32(trie, testkey1, "test", LONG_STRING);
        assertEquals(LONG_STRING, new String(DmgTrieImpl.get32(trie, testkey1, "test")));

        DmgTrieImpl.update32(trie, testkey1, "test", dog + "1");
        assertEquals(dog + "1", new String(DmgTrieImpl.get32(trie, testkey1, "test")));
    }

    @Test
    public void testDeleteShortString1() {
        TrieImpl trie = new TrieImpl(levelDb);

        DmgTrieImpl.update32(trie, testkey1, "test", dog);
        assertEquals(dog, new String(DmgTrieImpl.get32(trie, testkey1, "test")));
        byte[] ROOT_HASH_BEFORE = trie.getRootHash();

        DmgTrieImpl.update32(trie, testkey2, "test", cat);
        assertEquals(cat, new String(DmgTrieImpl.get32(trie, testkey2, "test")));
        byte[] ROOT_HASH_AFTER = trie.getRootHash();

        DmgTrieImpl.delete32(trie, testkey2);
        assertEquals("", new String(DmgTrieImpl.get32(trie, testkey2, "test")));
        assertEquals(Hex.toHexString(ROOT_HASH_BEFORE), Hex.toHexString(trie.getRootHash()));
    }

    @Test
    public void testDeleteLongString1() {
        TrieImpl trie = new TrieImpl(levelDb);

        DmgTrieImpl.update32(trie, testkey1, "test", LONG_STRING);
        assertEquals(LONG_STRING, new String(DmgTrieImpl.get32(trie, testkey1, "test")));
        byte[] ROOT_HASH_BEFORE = trie.getRootHash();

        DmgTrieImpl.update32(trie, testkey2, "test", LONG_STRING);
        assertEquals(LONG_STRING, new String(DmgTrieImpl.get32(trie, testkey2, "test")));

        DmgTrieImpl.delete32(trie, testkey2);
        assertEquals("", new String(DmgTrieImpl.get32(trie, testkey2, "field")));
        assertEquals(Hex.toHexString(ROOT_HASH_BEFORE), Hex.toHexString(trie.getRootHash()));
    }

    @Test
    public void testDeleteCompletellyDiferentItems() {
        TrieImpl trie = new TrieImpl(levelDb);

        String val_1 = "2a";
        String val_2 = "09";
        String val_3 = "a9";

        DmgTrieImpl.update32(trie, Hex.decode(val_1), "test", Hex.decode(val_1));
        DmgTrieImpl.update32(trie, Hex.decode(val_2), "test", Hex.decode(val_2));

        String root1 = Hex.toHexString(trie.getRootHash());

        DmgTrieImpl.update32(trie, Hex.decode(val_3), "test", Hex.decode(val_3));
        DmgTrieImpl.delete32(trie, Hex.decode(val_3));
        String root1_ = Hex.toHexString(trie.getRootHash());

        Assert.assertEquals(root1, root1_);
    }

    @Test
    public void testDeleteAll() {
        TrieImpl trie = new TrieImpl(levelDb);
        assertEquals(ROOT_HASH_EMPTY, Hex.toHexString(trie.getRootHash()));

        DmgTrieImpl.update32(trie, testkey1, "test", dog);
        DmgTrieImpl.update32(trie, testkey2, "test", cat);
        DmgTrieImpl.update32(trie, testkey3, "test", LONG_STRING);
    
        DmgTrieImpl.delete32(trie, testkey1);
        DmgTrieImpl.delete32(trie, testkey2);
        DmgTrieImpl.delete32(trie, testkey3);
        assertEquals(ROOT_HASH_EMPTY, Hex.toHexString(trie.getRootHash()));
    }

    @Test
    public void testTrieEquals() {
        TrieImpl trie1 = new TrieImpl(levelDb);
        TrieImpl trie2 = new TrieImpl(levelDb);

        DmgTrieImpl.update32(trie1, testkey1, "test", LONG_STRING);
        DmgTrieImpl.update32(trie2, testkey1, "test", LONG_STRING);
        assertTrue("Expected tries to be equal", trie1.equals(trie2));
        assertEquals(Hex.toHexString(trie1.getRootHash()), Hex.toHexString(trie2.getRootHash()));

        DmgTrieImpl.update32(trie1, testkey2, "test", LONG_STRING);
        DmgTrieImpl.update32(trie2, testkey3, "test", LONG_STRING);
        assertFalse("Expected tries not to be equal", trie1.equals(trie2));
        assertNotEquals(Hex.toHexString(trie1.getRootHash()), Hex.toHexString(trie2.getRootHash()));
    }

    @Ignore
    @Test
    public void TestTrieDirtyTracking() {
        TrieImpl trie = new TrieImpl(levelDb);
        DmgTrieImpl.update32(trie, testkey1, "test", LONG_STRING);
        assertTrue("Expected trie to be dirty", trie.getCache().isDirty());

        trie.sync();
        assertFalse("Expected trie not to be dirty", trie.getCache().isDirty());

        DmgTrieImpl.update32(trie, testkey2, "test", LONG_STRING);
        trie.getCache().undo();
        assertFalse("Expected trie not to be dirty", trie.getCache().isDirty());
    }

    @Test
    public void TestTrieReset() {
        TrieImpl trie = new TrieImpl(levelDb);

        DmgTrieImpl.update32(trie, testkey1, "test", LONG_STRING);
        assertNotEquals("Expected cached nodes", 0, trie.getCache().getNodes().size());

        trie.getCache().undo();

        assertEquals("Expected no nodes after undo", 0, trie.getCache().getNodes().size());
    }

    @Test
    public void testTrieCopy() {
        TrieImpl trie = new TrieImpl(levelDb);
        DmgTrieImpl.update32(trie, "doe", "test", "reindeer");
        TrieImpl trie2 = trie.copy();
        assertNotEquals(trie.hashCode(), trie2.hashCode()); // avoid possibility that its just a reference copy
        assertEquals(Hex.toHexString(trie.getRootHash()), Hex.toHexString(trie2.getRootHash()));
        assertTrue(trie.equals(trie2));
    }

    @Test
    public void testTrieUndo() {
        TrieImpl trie = new TrieImpl(levelDb);
        DmgTrieImpl.update32(trie, "doe", "test", "reindeer");
        trie.sync();
        byte[] ROOT_HASH_BEFORE = trie.getRootHash();
        DmgTrieImpl.update32(trie, "dog", "test", "puppy");

        trie.undo();
        assertEquals(Hex.toHexString(ROOT_HASH_BEFORE), Hex.toHexString(trie.getRootHash()));
    }

    @Test
    public void TestTrieRootCopy() {
        //store root as rlpdata, and then use it for building trie.
        TrieImpl trie = new TrieImpl(levelDb);
        trie.update(testkey1, dog);
        Value val = new Value(trie.getRoot());
        trie.update(testkey2.getBytes(), val.encode());
        //trie.sync();
        Value val1 = Value.fromRlpEncoded(trie.get(testkey2));
        TrieImpl trie1 = new TrieImpl(levelDb, val1.asObj());
        trie1.setCache(trie.getCache());
        assertEquals(dog, new String(trie1.get(testkey1)));
    }

    @Test
    public void TestSubTrie1() {
        TrieImpl subtrie = new TrieImpl(levelDb);
        subtrie.update(testkey8, dog);
        Value val = new Value(subtrie.getRoot());
        TrieImpl trie = new TrieImpl(levelDb);
        DmgTrieImpl.update32(trie, testkey4.getBytes(), "test", val.encode());
        assertEquals(dog, new String(DmgTrieImpl.get32(trie, testkey5, "test")));
    }

    @Test
    public void TestSubTrie2() {
        TrieImpl subtrie = new TrieImpl(levelDb);
        subtrie.update(testkey9, dog);
        Value val = new Value(subtrie.getRoot());
        TrieImpl trie = new TrieImpl(levelDb);
        DmgTrieImpl.update32(trie, testkey6.getBytes(), "test", val.encode());
        assertEquals(dog, new String(DmgTrieImpl.get32(trie, testkey7, "test")));
    }
    
    @Test
    public void TestSubTrie1_update() {
        TrieImpl subtrie = new TrieImpl(levelDb);
        subtrie.update(testkey8, dog);
        Value val = new Value(subtrie.getRoot());
        TrieImpl trie = new TrieImpl(levelDb);
        DmgTrieImpl.update32(trie, testkey4.getBytes(), "test", val.encode());
        assertEquals(dog, new String(DmgTrieImpl.get32(trie, testkey5, "test")));
        DmgTrieImpl.update32(trie, testkey5, "test", cat);
        assertEquals(cat, new String(DmgTrieImpl.get32(trie, testkey5, "test")));
    }

    @Test
    public void TestSubTrie2_update() {
        TrieImpl subtrie = new TrieImpl(levelDb);
        subtrie.update(testkey9, dog);
        Value val = new Value(subtrie.getRoot());
        TrieImpl trie = new TrieImpl(levelDb);
        DmgTrieImpl.update32(trie, testkey6.getBytes(), "test", val.encode());
        assertEquals(dog, new String(DmgTrieImpl.get32(trie, testkey7, "test")));
        DmgTrieImpl.update32(trie, testkey7, "test", cat);
        assertEquals(cat, new String(DmgTrieImpl.get32(trie, testkey7, "test")));
    }

    @Test
    public void NewProject() {
        TrieImpl subtrie = new TrieImpl(levelDb);
        Value val = new Value(subtrie.getRoot());
        TrieImpl trie = new TrieImpl(levelDb);
        DmgTrieImpl.update32(trie, testkey4.getBytes(), "test", val.encode());
        DmgTrieImpl.update32(trie, testkey5, "test", dog);
        assertEquals(dog, new String(DmgTrieImpl.get32(trie, testkey5, "test")));
    }
}
