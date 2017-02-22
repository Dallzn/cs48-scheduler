package client.commander;
import client.app.obj.ScheduleEvent;
import client.app.obj.User;

import java.util.ArrayList;


public class ScheduleGenerator{
  private ArrayList<ScheduleEvent> events;
  private int number_of_events;
  
  public ScheduleGenerator(ArrayList<ScheduleEvent> e, int number){
    events = e;
    number_of_events = number;
  }
  
  public void sperate_events(){
    for(int i=1; i<=number;i++){
      String strI = Integer.toString(i);
      "eventlist" + strI = new ArrayList<ScheduleEvent>();
    }
    
    for(int i=1; i<=number;i++){
      ScheduleEvent s = events.get(0);
      String strI = Integer.toString(i);
      ("eventlist" + strI).add(s);
      this.events.remove(s);
      Iterator<ScheduleEvent> iter = events.iterator();
    	while (iter.hasNext()){
    		ScheduleEvent ss = iter.next();
     		if (s.getID() == ss.getID())
    		{
            ("eventlist" + strI).add(ss);
    	    	this.events.remove(ss);
    		}
    	}
     }
  }
  
  public void 
 
}
