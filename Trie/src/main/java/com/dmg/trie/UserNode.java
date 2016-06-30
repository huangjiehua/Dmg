/*************************************************************************
	> File Name: UserNode.java
	> Author: 
	> Mail: 
	> Created Time: 2016年06月15日 星期三 16时55分50秒
 ************************************************************************/

package com.dmg.trie;

import java.util.*;

public class UserNode {
    private String uid;
    private String address;
    private int total;
    private int available;
    private int freeze;
    private int total_invest;
    private int status;
    private int experience;
    
    public UserNode(String _uid, String _address, int _total, int _available, int _freeze, int _total_invest, int _status, int _experience) {
        uid = _uid;
        address = _address;
        total = _total;
        available = _available;
        freeze = _freeze;
        total_invest = _total_invest;
        status = _status;
        experience = _experience;
    }

    public void setUid(String _uid) {
        uid = _uid;
    }

    public String getUid() {
        return uid;
    }
    
    public void setAddress(String _address) {
        address = _address;
    }

    public String getAddress() {
        return address;
    }
}
