/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lightbend.akka.sample;

/**
 * Class ReadMessage which isntructs a process to read it's value
 * 
 * @author Raphael Tran
 */
public class ReadMessage {
    
    private int seq;
    
            public ReadMessage(int s) {
                       
                        this.seq = s;
            }
            
            public int getSeq() {
                return this.seq;
            }
    
}
