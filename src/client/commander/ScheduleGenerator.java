package client.commander;
import client.app.obj.ScheduleEvent;
import client.app.obj.User;

import java.util.Random;
import java.util.ArrayList;
import java.util.Iterator;


public class ScheduleGenerator{
	private ArrayList<ScheduleEvent> events; //input array
	private int number_of_events; //number of different events (same event with different duration count as one event)
	  
	  public ScheduleGenerator(ArrayList<ScheduleEvent> e){
	    events = e;
	    
	    //count number of different events on the array
	    ArrayList<ScheduleEvent> copy = new ArrayList<ScheduleEvent>();
	    copy = new ArrayList<ScheduleEvent>(e);
	    int number = 0;
	    do{
	        ScheduleEvent hold = copy.get(0);
	        copy.remove(0);
	    	for(int i=0; i<copy.size(); i++)
	    	{
	    		ScheduleEvent s = copy.get(i);
	    		if (s.get_ID() == hold.get_ID()){
	    			copy.remove(i);
	    		}
	    	}
	    	number++;
	      }while(copy.size()!=0);
	      //store it in number_of_events
	      number_of_events = number;
	  }
	  
	  
	  public ArrayList<ScheduleEvent> getSchedules(){
		ArrayList<ScheduleEvent> output = new ArrayList<ScheduleEvent>();
	    do{
	    	if (output.size()!=0){
	    		output.clear();
	    	}
	        //random select event add to array
	        for (int i=0; i<number_of_events; i++){
	            int a = events.size();
	            Random random = new Random();
	            int index = random.nextInt(a);
	            System.out.println(index);
	            ScheduleEvent randomEvent = events.get(index);
	            output.add(randomEvent);}
	    }while(check_timeblock(output)==false || check_ID(output)==false);
	    return output;
	  }
	            
	  public boolean check_ID(ArrayList<ScheduleEvent> e){
	    ArrayList<ScheduleEvent> copy = new ArrayList<ScheduleEvent>();
	    copy = new ArrayList<ScheduleEvent>(e);
	    do{
	        ScheduleEvent hold = copy.get(0);
	        copy.remove(0);
	        Iterator<ScheduleEvent> iter = copy.iterator();
	        while (iter.hasNext()){
	          ScheduleEvent ss = iter.next();
	          if(hold.get_ID() == ss.get_ID()){
	            return false;
	          }
	        }
	      }while(copy.size()!=0);
	      return true;
	  }
	           
	  public boolean check_timeblock(ArrayList<ScheduleEvent> e){
	    ArrayList<ScheduleEvent> copy = new ArrayList<ScheduleEvent>();
	    copy = new ArrayList<ScheduleEvent>(e);
	    do{
	        ScheduleEvent hold = copy.get(0);
	        copy.remove(0);
	        Iterator<ScheduleEvent> iter = copy.iterator();
	        while (iter.hasNext()){
	          ScheduleEvent ss = iter.next();
	          if(hold.what_day() == ss.what_day()){
	            if (hold.when_to_start()>= ss.when_to_start() && hold.when_to_start()<=ss.when_to_end()){
	            	return false; 
	            	}
	            if (ss.when_to_start()>= hold.when_to_start() && ss.when_to_start()<=hold.when_to_end()){
	            	return false; 
	            	}
	          }
	        }
	      }while(copy.size()!=0);
	      return true;
	  }
}
