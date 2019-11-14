/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lightbend.akka.sample;

/**
 * Answer after a WriteMessage where the process acknowledge that it's value has been written
 * @author Raphael Tran
 */
public class WriteAnswer {
    
    private int seq;
    
            public WriteAnswer(int s) {
                       
                        this.seq = s;
                }
            
            public int getSeq(){
                return this.seq;
            }  
}
