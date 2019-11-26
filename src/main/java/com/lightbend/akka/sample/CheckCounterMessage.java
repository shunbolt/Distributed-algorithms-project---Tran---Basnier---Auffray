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
public class CheckCounterMessage {
    
        private int written;
    
        public CheckCounterMessage(int v){
            this.written = v;
        }
        
        public int getValue(){
            return this.written;
        }
    
}
