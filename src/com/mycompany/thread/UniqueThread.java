/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.thread;

import com.mycompany.GUI.BankWin;
import com.mycompany.data.ObjectRequest;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ASUS
 */
public class UniqueThread extends Thread implements Runnable{
    
    private ObjectRequest request;
    private Socket cliente;
    private BankWin bank;
    private int port = 1335;
    
    public UniqueThread(ObjectRequest request,Socket cliente,BankWin bank){
        this.request = request;
        this.cliente = cliente;
        this.bank = bank;
    }
    
    @Override
    public void run(){
        try{
            switch(request.getOperation()){
                case 1:
                    pago();
                    break;
                default:
                    break;
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                cliente.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void pago() throws Exception{
        Socket socket = null;
        int id;
        if(request.getToPlayer() == -1){
            int lottery = bank.getJTextFieldLottery();
            lottery += request.getValue();
            bank.setJTextFieldLottery(lottery+"");
            return;
        }else if(request.getToPlayer() == -2 || request.getToPlayer() >= 98){
            // broadcast
            // operation = id*100 -2 it's helpful to identify the source player
            // when the type of payment is broadcast.
            // playerTo + 2 / 100 == id source player
            request.setValue(request.getValue() / (bank.getPlayers().size() - 1));
            id = (request.getToPlayer() + 2) / 100;
            Set<Integer> set = bank.getPlayers().keySet();
            for (Integer i : set) {
                if(i != id){
                    socket = new Socket(bank.getPlayers().get(i).getIp(),port);
                    request.setOperation(2);
                    // go to other player
                    ObjectOutputStream bufferOut = new ObjectOutputStream(socket.getOutputStream());
                    bufferOut.writeObject(request);
                }
            }
        }else{
            id = request.getToPlayer();
            socket = new Socket(bank.getPlayers().get(id).getIp(),port);
            request.setOperation(2);
            // go to other player
            ObjectOutputStream bufferOut = new ObjectOutputStream(socket.getOutputStream());
            bufferOut.writeObject(request);
        }
    }
    
}
