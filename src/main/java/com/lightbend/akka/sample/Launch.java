/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lightbend.akka.sample;

/**
 * Launch message
 * @author Raphael Tran
 */
public class Launch {
    
        private int operations;
    
        public Launch(int ope){
            this.operations = ope;
        }
        
        public int getoperations(){
            return this.operations;
        }
    
}
