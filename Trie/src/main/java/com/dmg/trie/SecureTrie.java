package com.dmg.trie;

import com.dmg.datasource.KeyValueDataSource;

import static com.dmg.crypto.HashUtil.sha3;
import static com.dmg.util.ByteUtil.EMPTY_BYTE_ARRAY;

public class SecureTrie extends TrieImpl implements Trie {

    public SecureTrie(KeyValueDataSource db) {
        this(db, "");
    }

    public SecureTrie(KeyValueDataSource db, Object root) {
        super(db, root);
    }

    @Override
    public byte[] get(byte[] key) {
        return super.get(sha3(key));
    }

    @Override
    public void update(byte[] key, byte[] value) {
        super.update(sha3(key), value);
    }

    @Override
    public void delete(byte[] key) {
        this.update(key, EMPTY_BYTE_ARRAY);
    }

    @Override
    public SecureTrie clone() {
        this.getCache();
        this.getRoot();

        return null;
    }
}
