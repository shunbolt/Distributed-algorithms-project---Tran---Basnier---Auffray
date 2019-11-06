/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lightbend.akka.sample;

import akka.actor.ActorRef;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Raphael Tran
 */
public class ProcessList implements Serializable {
    
                private static final long serialVersionUID = 1L;
                
		public final ArrayList<ActorRef> list;
	
		public ProcessList(ArrayList<ActorRef> l) {
                        // this.list = org.apache.commons.lang3.SerializationUtils.clone(l);
                        this.list = l;
                }
                
                public ArrayList<ActorRef> getlist(){
                    return this.list;
                }
    }
