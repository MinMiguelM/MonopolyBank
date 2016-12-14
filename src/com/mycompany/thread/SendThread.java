/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.thread;

import com.mycompany.GUI.BankWin;
import com.mycompany.data.ObjectRequest;
import com.mycompany.data.Player;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author ASUS
 */
public class SendThread extends Thread implements Runnable{
    
    private Player player;
    private ObjectRequest obj;
    private int port = 1335;
    private Map<Integer,Player> players;
    private BankWin bank;
    
    public SendThread(Map<Integer,Player> players, ObjectRequest obj,BankWin bank){
        this.players = players;
        this.obj = obj;
        this.bank = bank;
    }
    
    public SendThread(ObjectRequest obj,Player player){
        this.player = player;
        this.obj = obj;
    }
    
    public void run(){
        try{
            if(obj.getOperation() != 3){
                Socket socket = new Socket(player.getIp(), port);
                ObjectOutputStream bufferOut = new ObjectOutputStream(socket.getOutputStream());
                bufferOut.writeObject(obj);
            }else{
                Set<Integer> playersKey = players.keySet();
                for (Integer i : playersKey) {
                    Socket socket = new Socket(players.get(i).getIp(),port);
                    ObjectOutputStream bufferOut = new ObjectOutputStream(socket.getOutputStream());
                    bufferOut.writeObject(obj);
                }
                bank.deletePlayer(obj.getToPlayer());
                bank.initTable();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
