/*************************************************************************
	> File Name: DmgTrieImpl.java
	> Author: 
	> Mail: 
	> Created Time: 2016年06月15日 星期三 16时10分28秒
 ************************************************************************/

package org.dmg.trie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static com.dmg.util.ByteUtil.EMPTY_BYTE_ARRAY;
import static com.dmg.util.CompactEncoder.binToNibbles;

public class DmgTrieImpl {
    private static final Logger logger = LoggerFactory.getLogger("dmgtrie");

    /**
     * Retrieve a value from a key as String.
     */
    public byte[] get64(TrieImpl trie, String key, String field) {
        return this.get(trie, key.getBytes(), field);
    }

    @Override
    public byte[] get64(TrieImpl trie, byte[] key, String field) {
        byte[] k = binToNibbles(key);
        byte[] subkey;
        byte[] rlpdata;
        switch(k[0]) {
            case 1:
                {
                    for(int i = 0; i < 32; i++){
                        subkey[i] = key[i];
                    }
                    rlpdata = trie.get(subkey);
                }
                break;
            case 2:
                {
                    for(int i = 0; i < 32; i++){
                        subkey[i] = key[i];
                    }
                    rlpdata = trie.get(subkey);
                }
                break;
            case 3:
                {
                    for(int i = 0; i < 32; i++){
                        subkey[i] = key[i];
                    }
                    rlpdata = trie.get(subkey);
                }
                break;
            case 4:
                {
                    for(int i = 0; i < 32; i++){
                        subkey[i] = key[i];
                    }
                    rlpdata = trie.get(subkey);
                    int flat = 1;
                    for(int i = 32; i < 64;i++){
                        subkey[i-32] = key[i];
                        if (subkey[i-32] != 0)
                            flat = 0;
                    }
                    if (flat == 1) {

                    }
                    else {
                        TrieImpl subtrie = TrieImpl();
                        rlpdata = subtrie.get(subkey);
                    }
                }
                break;
            case 6:
                {
                    for(int i = 0; i < 32; i++){
                        subkey[i] = key[i];
                    }
                    rlpdata = trie.get(subkey);
                    int flat = 1;
                    for(int i = 32; i < 64;i++){
                        subkey[i-32] = key[i];
                        if (subkey[i-32] != 0)
                            flat = 0;
                    }
                    if (flat == 1) {

                    }
                    else {
                        TrieImpl subtrie = TrieImpl();
                        rlpdata = subtrie.get(subkey);
                    }
                }
                break;
            default: print("wrong key"); 
        }
    }

    /**
     * Insert key/value pair into trie.
     */
    public void update64(TrieImpl trie, String key, String field, String value) {
        this.update(trie, key, field, value)
    }

    @Override
    public void update64(TrieImpl trie, byte[] key, String field ,byte[] value) {
        byte[] k = binToNibbles(key);
        byte[] subkey;
        byte[] rlpdata;
        switch(k[0]) {
            case 1:
                {
                    for(int i = 0; i < 32; i++){
                        subkey[i] = key[i];
                    }
                    rlpdata = trie.get(subkey);
                }
                break;
            case 2:
                {
                    for(int i = 0; i < 32; i++){
                        subkey[i] = key[i];
                    }
                    rlpdata = trie.get(subkey);
                }
                break;
            case 3:
                {
                    for(int i = 0; i < 32; i++){
                        subkey[i] = key[i];
                    }
                    rlpdata = trie.get(subkey);
                }
                break;
            case 4:
                {
                    for(int i = 0; i < 32; i++){
                        subkey[i] = key[i];
                    }
                    rlpdata = trie.get(subkey);
                    int flat = 1;
                    for(int i = 32; i < 64;i++){
                        subkey[i-32] = key[i];
                        if (subkey[i-32] != 0)
                            flat = 0;
                    }
                    if (flat == 1) {

                    }
                    else {
                        TrieImpl subtrie = TrieImpl();
                        rlpdata = subtrie.get(subkey);
                    }
                }
                break;
            case 6:
                {
                    for(int i = 0; i < 32; i++){
                        subkey[i] = key[i];
                    }
                    rlpdata = trie.get(subkey);
                    int flat = 1;
                    for(int i = 32; i < 64;i++){
                        subkey[i-32] = key[i];
                        if (subkey[i-32] != 0)
                            flat = 0;
                    }
                    if (flat == 1) {

                    }
                    else {
                        TrieImpl subtrie = TrieImpl();
                        rlpdata = subtrie.get(subkey);
                    }
                }
                break;
            default: print("wrong key"); 
        }
    }

    /**
     * Delete a key/value pair from the trie.
     */
    public void delete64(TrieImpl trie, String key) {
        this.update(trie, key.getBytes(), EMPTY_BYTE_ARRAY);
    }

    @Override
    public void delete64(TrieImpl trie, byte[] key) {

    }

    /**
     * Get all key/value pairs from the trie.
     */
    public byte[] getany(TrieImpl trie) {
        
    }
}

