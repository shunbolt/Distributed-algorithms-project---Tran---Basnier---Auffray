/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lightbend.akka.sample;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.ArrayList;

/**
 *
 * @author Raphael Tran
 */
public class Process extends UntypedAbstractActor {
    
    // Logger attached to the process
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    
    // Actor reference that points to neighbor actor
    
    private ArrayList<ActorRef> ActorList;
    
    // Boolean status : faulty or safe
    
    private boolean status; 
    
    static public class Welcome{
        public final String msg;
        
        public Welcome(String m){
            this.msg = m;
        }
    }
    
    
    // Empty constructor
    public Process() {};
    
    
    
    // Static function that creates the actor
    public static Props createActor() {
		return Props.create(Process.class, () -> {
			return new Process();
		});
	}
    
    
    // createReceive function which describes the behavior of the process when it receives a message
    @Override
	public void onReceive(Object message) throws Throwable {
                // Store the list of processes into the ProcessList class
                if(message instanceof ProcessList){
                    this.ActorList = ((ProcessList) message).getlist();
                    log.info("[" + getSelf().path().name()+ "] has received the list of all actors in the system " ); 
                    // Loop through all the processes to give a welcome message
                    for(ActorRef d : this.ActorList ){
                        Welcome w = new Welcome("Welcome process !");
                        d.tell(w,getSelf());
                    }
                } 
                // Behavior for a welcome message
                if(message instanceof Welcome){
                    Welcome w = (Welcome) message;
                    log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with content: ["+w.msg+"]");
                }
	}
    
}
