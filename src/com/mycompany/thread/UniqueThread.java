/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.thread;

import com.mycompany.GUI.BankWin;
import com.mycompany.data.ObjectRequest;
import com.mycompany.data.Property;
import com.mycompany.data.Ticket;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
                case 4:
                    compra();
                    break;
                case 5:
                    devolucion();
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
    
    public Ticket addProperty(String name,Ticket ticket){
        Map<Integer,Property> map = new HashMap<>(ticket.getPropertiesSold());
        Set<Integer> set = map.keySet();
        for(Integer i : set) {
            if (map.get(i).getName().equals(name)) {
                ticket.getPropertiesAvailable().put(i, map.get(i));
                ticket.getPropertiesSold().remove(i);
            }
        }
        return ticket;
    }

    public Property getProperty(String name,Ticket ticket){
        Set<Integer> set = ticket.getPropertiesAvailable().keySet();
        for(Integer i : set){
            if(ticket.getPropertiesAvailable().get(i).getName().equals(name))
                return ticket.getPropertiesAvailable().get(i);
        }
        Set<Integer> setSold = ticket.getPropertiesSold().keySet();
        for(Integer i : setSold){
            if(ticket.getPropertiesSold().get(i).getName().equals(name))
                return ticket.getPropertiesSold().get(i);
        }
        return null;
    }
    
    public void devolucion() throws Exception{
        Socket socket = null;
        if(request.getObject() != null){
            int option = JOptionPane.showConfirmDialog(null,bank.getPlayers().
                            get(request.getFromPlayer()).getName()+" quiere devolver "+
                            request.getValue()+". Autoriza?", "Devolucion",JOptionPane.YES_NO_OPTION);
            if(option == JOptionPane.YES_OPTION){
                Set<Integer> set = bank.getPlayers().keySet();
                request.setOperation(6);
                request.setObject(addProperty(request.getValue(), (Ticket)request.getObject()));
                for (Integer i : set) {
                    socket = new Socket(bank.getPlayers().get(i).getIp(), port);
                    ObjectOutputStream bufferOut = new ObjectOutputStream(socket.getOutputStream());
                    bufferOut.writeObject(request);
                }
                int value = getProperty(request.getValue(), (Ticket)request.getObject()).getValue();
                value -= value*0.1;
                bank.updateTableMoney(request.getFromPlayer(), value);
                request.setObject(true);
            }else
                request.setObject(false);
            ObjectOutputStream bufferOut = new ObjectOutputStream(cliente.getOutputStream());
            bufferOut.writeObject(request);
        }else{
            int option = JOptionPane.showConfirmDialog(null,bank.getPlayers().
                            get(request.getFromPlayer()).getName()+" quiere devolver monto de: "+
                            request.getValue()+". Autoriza?", "Devolucion",JOptionPane.YES_NO_OPTION);
            if(option == JOptionPane.YES_OPTION){
                bank.updateTableMoney(request.getFromPlayer(), Integer.parseInt(request.getValue()));
                request.setObject(true);
            }else
                request.setObject(false);
            ObjectOutputStream bufferOut = new ObjectOutputStream(cliente.getOutputStream());
            bufferOut.writeObject(request);
        }
        bank.initTable();
    }
    
    public void compra() throws Exception{
        Socket socket = null;
        if(request.getObject() != null){
            JOptionPane.showMessageDialog(null,bank.getPlayers().
                            get(request.getFromPlayer()).getName()+" ha comprado "+
                            request.getValue(), "Compra",JOptionPane.INFORMATION_MESSAGE);
            Set<Integer> set = bank.getPlayers().keySet();
            request.setOperation(6);
            for (Integer i : set) {
                if(request.getFromPlayer() != i){
                    socket = new Socket(bank.getPlayers().get(i).getIp(), port);
                    ObjectOutputStream bufferOut = new ObjectOutputStream(socket.getOutputStream());
                    bufferOut.writeObject(request);
                }
            }
            bank.updateTableMoney(request.getFromPlayer(), 
                        getProperty(request.getValue(), (Ticket)request.getObject()).getValue()*-1);
        }else{
            JOptionPane.showMessageDialog(null,bank.getPlayers().
                            get(request.getFromPlayer()).getName()+" ha hecho una transaccion por: "+
                            request.getValue(), "Compra",JOptionPane.INFORMATION_MESSAGE);
            bank.updateTableMoney(request.getFromPlayer(), Integer.parseInt(request.getValue())*-1);
        }
        bank.initTable();
    }
    
    public void pago() throws Exception{
        Socket socket = null;
        int id;
        if(request.getToPlayer() == -1){
            int lottery = bank.getJTextFieldLottery();
            lottery += Integer.parseInt(request.getValue());
            bank.setJTextFieldLottery(lottery+"");
            bank.updateTableMoney(request.getFromPlayer(), Integer.parseInt(request.getValue())*(-1));
        }else if(request.getToPlayer() == -2){
            id = request.getFromPlayer();
            bank.updateTableMoney(id, Integer.parseInt(request.getValue())*-1);
            request.setValue( Integer.parseInt(request.getValue()) / (bank.getPlayers().size() - 1) +"" );
            Set<Integer> set = bank.getPlayers().keySet();
            for (Integer i : set) {
                if(i != id){
                    socket = new Socket(bank.getPlayers().get(i).getIp(),port);
                    request.setOperation(2);
                    // go to other player
                    ObjectOutputStream bufferOut = new ObjectOutputStream(socket.getOutputStream());
                    bufferOut.writeObject(request);
                    bank.updateTableMoney(i, Integer.parseInt(request.getValue()));
                }
            }
        }else{
            id = request.getToPlayer();
            bank.updateTableMoney(id, Integer.parseInt(request.getValue()));
            bank.updateTableMoney(request.getFromPlayer(), Integer.parseInt(request.getValue())*-1);
            socket = new Socket(bank.getPlayers().get(id).getIp(),port);
            request.setOperation(2);
            // go to other player
            ObjectOutputStream bufferOut = new ObjectOutputStream(socket.getOutputStream());
            bufferOut.writeObject(request);
        }
        bank.initTable();
    }
    
}
