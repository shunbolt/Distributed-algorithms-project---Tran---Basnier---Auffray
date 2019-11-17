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
     
    // private bool safe;
    
    // Sequence value that tells if the actor is up to date
    
    private int sequence;
    
    
    // Value stored 
    
    private int value;
    
    // Counter and array used for the number of answers received
    
    private int counter_WriteAnswer;
    private ArrayList<ReadAnswer> ReadAnswerList;
    
    // Welcome class message
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
                // Behavior for a write message
                if(message instanceof WriteMessage){
                    // Write out value and sequence integers of the process
                    if(this.sequence > ((WriteMessage) message).getSeq()){
                        this.sequence = ((WriteMessage) message).getSeq();
                        this.value = ((WriteMessage) message).getVal();
                        log.info( "["+getSelf().path().name()+"] received write message from ["+ getSender().path().name());
                        // Send ACK message as WriteAnswer to the sender
                        WriteAnswer ans = new WriteAnswer(this.sequence);
                        getSender().tell(ans,getSelf());
                    }
                }
                if(message instanceof ReadMessage){
                    log.info( "["+getSelf().path().name()+"] received read message from ["+ getSender().path().name());
                    // Send seq, value and readid to the sender as ReadAnswer
                    ReadAnswer ans = new ReadAnswer(this.sequence, this.value);
                    getSender().tell(ans,getSelf());
                }
                if(message instanceof WriteAnswer){
                    this.counter_WriteAnswer++;
                    log.info("["+getSelf().path().name()+"] received WriteAnswer message from ["+ getSender().path().name());
                }
                if(message instanceof ReadAnswer){
                    ReadAnswer ans = (ReadAnswer) message;
                    this.ReadAnswerList.add(ans);
                    log.info("["+getSelf().path().name()+"] received ReadAnswer message from ["+ getSender().path().name());
                }
	}
    
}
