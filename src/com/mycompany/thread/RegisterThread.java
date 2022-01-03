/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.thread;

import com.mycompany.data.Player;
import com.mycompany.data.Ticket;
import com.mycompany.GUI.MainGUI;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ASUS
 */
public class RegisterThread extends Thread implements Runnable{
    
    private Map<Integer,Player> players;
    private Map<Integer,Socket> playersSocket;
    private Ticket ticket;
    private Ticket lastTicket;
    private int lastId;
    private MainGUI main;
    private int option;
    private ServerSocket serverSocket;
    
    public RegisterThread(MainGUI main,int option,Ticket ticket){
        players = new HashMap<>();
        playersSocket = new HashMap<>();
        this.ticket = new Ticket();
        lastId = 0;
        this.main = main;
        this.option = option;
        if(option == 1)
            lastTicket = ticket;
    }
    
    @Override
    public void run(){
        try{
            serverSocket = new ServerSocket(333);
            serverSocket.setSoTimeout(300000);
            while(true){
                Socket cliente = serverSocket.accept();
                ObjectInputStream buffer = new ObjectInputStream(cliente.getInputStream());
                Player player = (Player)buffer.readObject();
                System.out.println("New player registered: " + player.getName());
                player.setPrison(0);
                players.put(lastId, player);
                playersSocket.put(lastId++, cliente);
                main.addToTable(player.getName());
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    System.out.println("Closing socket in RegisterThread");
                    serverSocket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    public synchronized Ticket getTicket(){
        return this.ticket;
    }
    
    public boolean exit(int money) throws Exception {
        if (!this.serverSocket.isClosed()) {
            this.serverSocket.close();
        }
        Socket cliente = null;
        if (option == 0){
            this.setMoney(money);
            ticket.setPlayers(players);
            ticket.loadProperties();
        }
        if (option == 1){
            Set<Integer> set = players.keySet();
            for (Integer i : set) {
                if(!setPlayer(i)){
                    JOptionPane.showMessageDialog(null, 
                            "Uno de los participantes no estaba en la sesion anterior.","Error",JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            ticket.setPlayers(players);
            ticket.setPropertiesAvailable(lastTicket.getPropertiesAvailable());
            ticket.setPropertiesSold(lastTicket.getPropertiesSold());
        }
        Set<Integer> playersKey = playersSocket.keySet();
        for (Integer integer : playersKey) {
            // send list of players to each player and the list of properties.
            cliente = playersSocket.get(integer);
            ObjectOutputStream bufferOut = new ObjectOutputStream(cliente.getOutputStream());
            bufferOut.writeObject(ticket);
            cliente.close();
        }
        return true;
    }
    
    public boolean setPlayer(int id){
        Set<Integer> playersSet = lastTicket.getPlayers().keySet();
        for (Integer i : playersSet) {
            if(lastTicket.getPlayers().get(i)
                    .getName().equalsIgnoreCase(players.get(id).getName())){
                players.get(id).setMoney(lastTicket.getPlayers().get(i).getMoney());
                players.get(id).setPrison(lastTicket.getPlayers().get(i).getPrison());
                return true;
            }
        }
        return false;
    }
    
    public void setMoney(int money){
        Set<Integer> set = players.keySet();
        for (Integer i : set) {
            players.get(i).setMoney(money);
        }
    }
}
