/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lightbend.akka.sample;

/**
 *
 * @author Raphael Tran
 */
public class UpdateSequenceMessage {
 
    private int value;
    
    public UpdateSequenceMessage(int v){
        this.value = v;
    }
    
    public int getValue(){
        return this.value;
    }
}
