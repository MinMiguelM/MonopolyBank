/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.thread;

import com.mycompany.data.Player;
import com.mycompany.data.Ticket;
import com.mycompany.GUI.MainGUI;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author ASUS
 */
public class RegisterThread extends Thread implements Runnable{
    
    private Map<Integer,Player> players;
    private Map<Integer,Socket> playersSocket;
    private boolean exit;
    private Ticket ticket;
    private int lastId;
    private MainGUI main;
    
    public RegisterThread(MainGUI main){
        exit = false;
        players = new HashMap<>();
        playersSocket = new HashMap<>();
        ticket = new Ticket();
        lastId = 0;
        this.main = main;
    }
    
    @Override
    public void run(){
        ServerSocket socket = null;
        try{
            socket = new ServerSocket(333);
            while(!exit){
                Socket cliente = socket.accept();
                if(!exit){
                    ObjectInputStream buffer = new ObjectInputStream(cliente.getInputStream());
                    Player player = (Player)buffer.readObject();
                    player.setMoney(1500);
                    players.put(lastId, player);
                    playersSocket.put(lastId++, cliente);
                    main.addToTable(player.getName());
                }
            }
            socket.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public synchronized Map<Integer,Player> getPlayers(){
        return this.players;
    }
    
    public void exit() throws Exception{
        this.exit = true;
        Socket cliente = null;
        ticket.setPlayers(players);
        ticket.loadProperties();
        Set<Integer> playersKey = playersSocket.keySet();
        for (Integer integer : playersKey) {
            // send list of players to each player and the list of properties.
            cliente = playersSocket.get(integer);
            ObjectOutputStream bufferOut = new ObjectOutputStream(cliente.getOutputStream());
            bufferOut.writeObject(ticket);
            cliente.close();
        }
    }
}
