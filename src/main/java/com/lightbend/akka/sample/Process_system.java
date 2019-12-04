/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lightbend.akka.sample;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Random;

/**
 *
 * @author Raphael Tran
 */
public class Process_system {
    public static void main(String[] args) {
        // Instantiate system actor
            final ActorSystem system = ActorSystem.create("Process_Tran");
        try {
            Scanner reader = new Scanner(System.in);
            System.out.println("How many processes do you want in the system ?");
            int n = reader.nextInt();
            
            System.out.println("How many operations per process do you want in the system ?");
            int ope = reader.nextInt();
            
            // Choose random crashed processes 
                // Random r = new Random();
                // int fails = r.nextInt(n/2);
            
            
            ArrayList <ActorRef> listProcesses = new ArrayList<>();
            
            
            // Instantiate n processes in a ArrayList
            for(int i = 0; i < n; i++){
                String pname = "process" + i;
                listProcesses.add(system.actorOf(Process.createActor(), pname));
            }
            
            russianRoulette(listProcesses);
            
            // Loop and message the n processes with the list of processes
            
            for(ActorRef process : listProcesses ){
                ProcessList pl;
                pl = new ProcessList(listProcesses);
                process.tell(pl, ActorRef.noSender());  
            }
            
            // Launch first process (assuming no fails)
            
            Launch l = new Launch(ope);
            for(ActorRef process : listProcesses){
                process.tell(l, ActorRef.noSender());
            }
            
            
            System.out.println(">>> Press ENTER to exit <<<");
            System.in.read();
            } catch (IOException ioe) {
            } finally {
                system.terminate();
            }
        
    }
    
    static void russianRoulette(ArrayList<ActorRef> players){
        Random rand = new Random();
        int size = players.size();
        int bullets = rand.nextInt(size/2);

        System.out.println("There are " + bullets + "/" + size + " processes down.");

        Collections.shuffle(players);

        int cpt = 0;
        
        KillMessage kill = new KillMessage();
        
        for(ActorRef i : players){
            if(cpt < bullets){
                i.tell(kill,ActorRef.noSender());
            }
            cpt++;
        }

    }
}

