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
import java.util.Random;

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
     
    private boolean safe = Boolean.TRUE;
    
    // Sequence value that tells if the actor is up to date
    
    private int sequence;
    
    
    // Value stored 
    
    private int value;
    
    // Counter and array used for the number of answers received
    
    private int counter_WriteAnswer;
    private ArrayList<ReadAnswer> ReadAnswerList;
    
    /*
        Classes
    */
    
    // Welcome class message
    static public class Welcome{
        public final String msg;
        
        public Welcome(String m){
            this.msg = m;
        }
    }
    
    // Launch message to trigger processes operations put and get
    
    // Empty constructor
    public Process() {};
    
    /*
        Functions
    */
    
    // Function put that writes a value
    /***
    public boolean put(int v){
        // Sends a Writemessage to all processes 
        WriteMessage msg = new WriteMessage(this.sequence + 1,v);
        this.counter_WriteAnswer = 0;
        for(ActorRef process : this.ActorList){
            process.tell(msg, getSelf());
        }
        // Wait that at least half the processes have answered
         while(this.counter_WriteAnswer < this.ActorList.size()/2){
            if(this.counter_WriteAnswer >= this.ActorList.size()/2){
                break;
            }
        }
        
        return false;
    }
    
    // Function get that returns value of the system
    
    public int get(){
        
        int val = -1;
        int current_seq = 0;
        ReadMessage msg = new ReadMessage(this.sequence + 1);
        this.ReadAnswerList = new ArrayList<>();
        // Sends a ReadMessage to all processes
        for(ActorRef process : this.ActorList){
            process.tell(msg, getSelf());
        }
        // Wait that at least half the processes have answered
        
        
        while(this.ReadAnswerList.size() < this.ActorList.size()/2){
            log.info("Process waiting for at least half answers");
        }
        // Select among the responses the most up to date one
        for(ReadAnswer ans : this.ReadAnswerList){
            if(current_seq < ans.getSeq() ){
                current_seq = ans.getSeq();
                val = ans.getVal();
            }
        }
        
        return val;
    }
    ***/
    // Setter for sequence
    
    public void setSeq(int s){
        this.sequence = s;
    }
    
    public void crash(){
        this.safe = Boolean.FALSE;
    }
    
    // Static function that creates the actor
    public static Props createActor() {
		return Props.create(Process.class, () -> {
			return new Process();
		});
	}
    
    
    // createReceive function which describes the behavior of the process when it receives a message
    @Override
	public void onReceive(Object message) throws Throwable {
                if(this.safe == Boolean.FALSE){
                    return;
                }
                if(message instanceof KillMessage){
                    crash();
                    log.info("[" + getSelf().path().name() + "] has received crash message");
                }
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
                    this.setSeq(0);
                    // log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with content: ["+w.msg+"]");
                }
                // Behavior for a launch message
                if(message instanceof Launch){
                    
                    for(int i = 0; i < ((Launch) message).getoperations() ; i++){
                        Random rand = new Random();
                        
                        UpdateSequenceMessage putmsg = new UpdateSequenceMessage(rand.nextInt(10));
                        getSelf().tell(putmsg, getSelf());
                    }
                }
                // Behavior for a UpdateSequence message
                if(message instanceof UpdateSequenceMessage){
                    log.info("********** [" + getSelf().path().name() + "] is updating it's sequence number to put a value" );
                    
                    ReadMessage msg = new ReadMessage(this.sequence + 1);
                    this.ReadAnswerList = new ArrayList<>();
                    // Sends a ReadMessage to all processes
                    for(ActorRef process : this.ActorList){
                        process.tell(msg, getSelf());
                    }
                    // Sends a CheckRAnswerSequence Message
                    
                    CheckRAnswerSequenceMessage chkmsg = new CheckRAnswerSequenceMessage(((UpdateSequenceMessage) message).getValue());
                    getSelf().tell(chkmsg, getSelf());
                }
                // Behavior for a CheckRAnswerSequence Message
                if(message instanceof CheckRAnswerSequenceMessage){
                    if(this.ReadAnswerList.size() < this.ActorList.size()/2){
                        getSelf().tell(message, getSelf());
                    }
                    else{
                        int current_seq = 0;
                        
                        // Select among the responses the most up to date one
                        for(ReadAnswer ans : this.ReadAnswerList){
                            // If sequence value is higher than current or has the same, we load new value
                            if(current_seq < ans.getSeq() ){
                                current_seq = ans.getSeq();
                            }
                        }
                        PutMessage putmsg = new PutMessage( ((CheckRAnswerSequenceMessage) message).getValue());
                        getSelf().tell(putmsg,getSelf());
                        GetMessage getmsg = new GetMessage();
                        getSelf().tell(getmsg, getSelf());
                    }
                }
                // Behavior for a put message
                if(message instanceof PutMessage){
                    log.info("******** Process [" + getSelf().path().name() + "] is launching a Put operation with value " + ((PutMessage) message).getValue() );
                    // Sends a Writemessage to all processes 
                    WriteMessage msg = new WriteMessage(this.sequence + 1,((PutMessage) message).getValue());
                    this.counter_WriteAnswer = 0;
                    for(ActorRef process : this.ActorList){
                        process.tell(msg, getSelf());
                    }
                    // Sends CheckCounterMessage to self
                    CheckCounterMessage chk = new CheckCounterMessage(((PutMessage) message).getValue());
                    getSelf().tell(chk, getSelf());
                }
                // Behavior for a CheckCounter message 
                if(message instanceof CheckCounterMessage){
                    if(this.counter_WriteAnswer < this.ActorList.size()/2) {
                        getSelf().tell(message, getSelf());
                    }
                    else{
                        log.info("******** Value " + ((CheckCounterMessage) message).getValue() + " successfully written in the system from [" + getSelf().path().name() + "]");
                        // GetMessage gmsg = new GetMessage();
                        // getSelf().tell(gmsg,getSelf());
                    }
                }
                // Behavior for a get message
                if(message instanceof GetMessage){
                    log.info("********* Process [" + getSelf().path().name() + "] is launching a Get operation ");
                    ReadMessage msg = new ReadMessage(this.sequence + 1);
                    this.ReadAnswerList = new ArrayList<>();
                    // Sends a ReadMessage to all processes
                    for(ActorRef process : this.ActorList){
                        process.tell(msg, getSelf());
                    }
                    // Sends CheckRAnswersMessage to self
                    CheckRAnswerMessage chkmsg = new CheckRAnswerMessage();
                    getSelf().tell(chkmsg,getSelf());                  
                }
                // Behavior for a CheckRAnswerMessage
                if(message instanceof CheckRAnswerMessage){
                    if(this.ReadAnswerList.size() < this.ActorList.size()/2){
                        getSelf().tell(message, getSelf());
                    }
                    else{
                        int current_seq = 0;
                        int val = -1;
                        // Select among the responses the most up to date one
                        for(ReadAnswer ans : this.ReadAnswerList){
                            // If sequence value is higher than current or has the same, we load new value
                            if(current_seq < ans.getSeq() || (current_seq == ans.getSeq() && val < ans.getVal()) ){
                                current_seq = ans.getSeq();
                                val = ans.getVal();
                            }
                        } 
                        log.info("******** Get value is " + val + " from [" + getSelf().path().name() + "]" );
                    }
                }
                // Behavior for a write message
                if(message instanceof WriteMessage){
                    // Write out value and sequence integers of the process
                    if(this.sequence < ((WriteMessage) message).getSeq()){
                        this.sequence = ((WriteMessage) message).getSeq();
                        this.value = ((WriteMessage) message).getVal();
                        log.info( "["+getSelf().path().name()+"] received write message from ["+ getSender().path().name() + "] with value written " + ((WriteMessage) message).getVal() );
                        // Send ACK message as WriteAnswer to the sender
                        WriteAnswer ans = new WriteAnswer();
                        // log.info("Write answer initialized");
                        
                        getSender().tell(ans, getSelf());
                        // this.ActorList.get(0).tell(ans, getSelf());
                    }
                    
                }
                if(message instanceof ReadMessage){
                    log.info( "["+getSelf().path().name()+"] received read query from ["+ getSender().path().name() + ']');
                    // Send seq, value and readid to the sender as ReadAnswer
                    ReadAnswer ans = new ReadAnswer(this.sequence, this.value);
                    getSender().tell(ans,getSelf());
                }
                if(message instanceof WriteAnswer){
                    // log.info("["+getSelf().path().name()+"] received WriteAnswer message from ["+ getSender().path().name() + "]");
                    this.counter_WriteAnswer++;    
                }
                if(message instanceof ReadAnswer){
                    ReadAnswer ans = (ReadAnswer) message;
                    this.ReadAnswerList.add(ans);
                    // log.info("["+getSelf().path().name()+"] received ReadAnswer message from ["+ getSender().path().name() + ']');
                }
	}
    
}
