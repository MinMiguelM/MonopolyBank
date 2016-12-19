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
     * 4. Compra
     * 5. Devolución
     * 6. Actualizacion de ticket
     */
    private int operation;
    
    /**
     * id from the map
     * Bank is id = -1
     */
    private int toPlayer;
    private String value;
    private Object object;
    private int fromPlayer;
    
    public ObjectRequest(){
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getFromPlayer() {
        return fromPlayer;
    }

    public void setFromPlayer(int fromPlayer) {
        this.fromPlayer = fromPlayer;
    }
}
