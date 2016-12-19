/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 *
 * @author ASUS
 */
public class Ticket implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private Map<Integer,Player> players;
    private Map<Integer,Property> propertiesAvailable;
    private Map<Integer,Property> propertiesSold;
    
    public Ticket(){
        players = new HashMap<>();
        propertiesAvailable = new HashMap<>();
        propertiesSold = new HashMap<>();
    }

    public Map<Integer, Player> getPlayers() {
        return players;
    }

    public void setPlayers(Map<Integer, Player> players) {
        this.players = players;
    }

    public Map<Integer, Property> getPropertiesAvailable() {
        return propertiesAvailable;
    }

    public void setPropertiesAvailable(Map<Integer, Property> propertiesAvailable) {
        this.propertiesAvailable = propertiesAvailable;
    }

    public Map<Integer, Property> getPropertiesSold() {
        return propertiesSold;
    }

    public void setPropertiesSold(Map<Integer, Property> propertiesSold) {
        this.propertiesSold = propertiesSold;
    }
    
    public void loadProperties() throws Exception{
        Map<String,Integer> ret = new HashMap<String,Integer>();
        Property prop = null;
        int id = 0;
        try{
            FileInputStream in = new FileInputStream("data/cards.mpy");
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            String str = bf.readLine();
            while(!str.equals("#fin")){
                StringTokenizer tok = new StringTokenizer(str," ");
                String name = tok.nextToken();
                int price = Integer.parseInt(tok.nextToken());
                prop = new Property(name,price);
                this.propertiesAvailable.put(id++,prop);
                str = bf.readLine();
            }
            in.close();
        }catch(Exception e){
            throw e;
        }
    }
    
}
