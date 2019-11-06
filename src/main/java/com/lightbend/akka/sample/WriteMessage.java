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
public class WriteMessage {
    
    private int seq;
    private int val;
    
            public WriteMessage(int s, int v) {
                       
                        this.seq = s;
                        this.val = v;
                }
            
            public int getSeq(){
                return this.seq;
            }
            
            public int getVal(){
                return this.val;
            }
            
}
