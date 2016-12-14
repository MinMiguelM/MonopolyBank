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
    private List<Property> properties;
    
    public Ticket(){
        players = new HashMap<>();
        properties = new ArrayList<>();
    }

    public Map<Integer, Player> getPlayers() {
        return players;
    }

    public void setPlayers(Map<Integer, Player> players) {
        this.players = players;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
    
    public void loadProperties() throws Exception{
        Map<String,Integer> ret = new HashMap<String,Integer>();
        Property prop = null;
        try{
            FileInputStream in = new FileInputStream("data/cards.mpy");
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            String str = bf.readLine();
            while(!str.equals("#fin")){
                StringTokenizer tok = new StringTokenizer(str," ");
                String name = tok.nextToken();
                int price = Integer.parseInt(tok.nextToken());
                prop = new Property(name,price);
                this.properties.add(prop);
                str = bf.readLine();
            }
            in.close();
        }catch(Exception e){
            throw e;
        }
    }
    
}
