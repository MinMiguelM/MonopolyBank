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
public class ObjectRequest implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 1. Pagar 
     * 2. Peaje / premio / pago / Lottery
     * 3. Game over
     */
    private int operation;
    
    /**
     * id from the map
     * Bank is id = -1
     */
    private int toPlayer;
    private int value;
    
    public ObjectRequest(){
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public int getToPlayer() {
        return toPlayer;
    }

    public void setToPlayer(int toPlayer) {
        this.toPlayer = toPlayer;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
