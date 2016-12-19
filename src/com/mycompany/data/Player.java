/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.data;

import java.io.Serializable;

/**
 *
 * @author ASUS
 */
public class Player implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String ip;
    private Integer money;
    private int prison;

    public int getPrison() {
        return prison;
    }

    public void setPrison(int prison) {
        this.prison = prison;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
