/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.thread;

import com.mycompany.GUI.BankWin;
import com.mycompany.data.ObjectRequest;
import com.mycompany.data.Player;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import javax.swing.JFrame;

/**
 *
 * @author ASUS
 */
public class CoordinatorThread extends Thread implements Runnable{
    
    private BankWin bank;
    private int port = 334;
    
    public CoordinatorThread(BankWin bank){
        this.bank = bank;
    }
    
    @Override
    public void run(){
        ServerSocket server = null;
        try{
            server = new ServerSocket(port);
            while(true){
                Socket cliente = server.accept();
                ObjectInputStream bufferIn = new ObjectInputStream(cliente.getInputStream());
                ObjectRequest data = (ObjectRequest)bufferIn.readObject();
                UniqueThread ut = new UniqueThread(data,cliente,bank);
                ut.start();
                // create a new thread to distribute the messages
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
