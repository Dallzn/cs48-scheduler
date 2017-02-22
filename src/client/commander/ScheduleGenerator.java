package client.commander;
import client.app.obj.ScheduleEvent;
import client.app.obj.User;

import java.util.ArrayList;


public class ScheduleGenerator{
  private ArrayList<ScheduleEvent> events;
  private int number_of_events;
  private ArrayList<ArrayList<ScheduleEvent>> nodes;
  private ArrayList<ScheduleEvent> output;
  
  public ScheduleGenerator(ArrayList<ScheduleEvent> e, int number){
    events = e;
    number_of_events = number;
    nodes = new ArrayList<ArrayList<ScheduleEvent>>();
    for (int i=0; i<number; i++){
      ArrayList<ScheduleEvent> nodeList = new ArrayList<ScheduleEvent>();
      nodes.add(nodeList);
    }
    output = new ArrayList<ScheduleEvent>();
  }
  
  public void sperate_events(){
    
    for(int i=0; i<this.number_of_events;i++){
      ScheduleEvent s = events.get(0);
      nodes.get(i).add(s);
      this.events.remove(s);
      Iterator<ScheduleEvent> iter = events.iterator();
    	while (iter.hasNext()){
    		ScheduleEvent ss = iter.next();
     		if (s.getID() == ss.getID())
    		{
            nodes.get(i).add(ss);
    	    	this.events.remove(ss);
    		}
    	}
    }
  }
  
  public void creat_schedule(){
    do{
        for (int i=0; i<this.number_of_events; i++){
            int a = this.nodes.get(i).size();
            Random random = new Random();
            int index = random.nextInt(a);
            ScheduleEvent randomEvent = this.nodes.get(i).get(index);
            this.ouput.add(randomEvent);
        }
        int count = 0;

      }while
  } 
 
}
