package com.dmg.transaction;

import java.io.Serializable;
//import java.sql.Date;
import java.util.Date;


public class Transaction {

	private String block_hash;
	private String type;
	private String timestamp;
	private int sequence;
	private String sign;
	private String version;
	private String extra;
	private String data;
	
	public Transaction(String _block_hash, String _type, String _timestamp, 
			int _sequence, String _sign, String _version, String _extra, String _data) {
		block_hash = _block_hash;
		type = _type;
		timestamp = _timestamp;
		sequence = _sequence;
		sign = _sign;
		version = _version;
		extra = _extra;
		data = _data;
	}
	
    public String getData() {
    	return data;
    }
}
