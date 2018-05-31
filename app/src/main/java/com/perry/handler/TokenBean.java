package com.perry.handler;

/**
 * Author: Pengyu Wang
 * Creation Date: 2018/5/31 11:56
 * Desc:
 */

public class TokenBean {
    private String name;

    public TokenBean() {
    }

    public TokenBean(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TokenBean{" +
                "name='" + name + '\'' +
                '}';
    }
}
