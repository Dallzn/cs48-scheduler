package client.commander;
import client.app.obj.ScheduleEvent;
import client.app.obj.User;

import java.util.ArrayList;
import java.util.Iterator;


public class ScheduleGenerator{
  private ArrayList<ScheduleEvent> events;
  private int number_of_events;
  
  public ScheduleGenerator(ArrayList<ScheduleEvent> e){
    events = e;
    ArrayList<ScheduleEvent> copy = new ArrayList<ScheduleEvent>();
    copy = e;
    int number = 0;
    do{
        ScheduleEvent hold = copy.get(0);
        copy.remove(0);
        Iterator<ScheduleEvent> iter = copy.iterator();
        while (iter.hasNext()){
          ScheduleEvent ss = iter.next();
          if(hold.getID() == ss.getID()){
            copy.remove(ss);
          }
        }
        number++;
      }while(copy.size()!=0)
      number_of_events = number;
      return true;
  }
  
  
  public ArrayList<ScheduleEvent> getSchedules(){
    do{
        ArrayList<ScheduleEvent> output = new ArrayList<ScheduleEvent>();
        for (int i=0; i<this.number_of_events; i++){
            int a = events.size();
            Random random = new Random();
            int index = random.nextInt(a);
            ScheduleEvent randomEvent = events.get(index);
            ouput.add(randomEvent);
        }
    }while(check_ID(output==false) || check_timeblock(output==false)
           return output;
  }
            
  public boolean check_ID(ArrayList<ScheduleEvent> e){
    ArrayList<ScheduleEvent> copy = new ArrayList<ScheduleEvent>();
    copy = e;
    do{
        ScheduleEvent hold = copy.get(0);
        copy.remove(0);
        Iterator<ScheduleEvent> iter = copy.iterator();
        while (iter.hasNext()){
          ScheduleEvent ss = iter.next();
          if(hold.getID() == ss.getID()){
            return false;
          }
        }
      }while(copy.size()!=0)
      return true;
  }
           
  public boolean check_timeblock(ArrayList<ScheduleEvent> e){
    ArrayList<ScheduleEvent> copy = new ArrayList<ScheduleEvent>();
    copy = e;
    copy = e;
    do{
        ScheduleEvent hold = copy.get(0);
        copy.remove(0);
        Iterator<ScheduleEvent> iter = copy.iterator();
        while (iter.hasNext()){
          ScheduleEvent ss = iter.next();
          if(hold.what_day() == ss.what_day()){
            if (hold.when_to_start()> ss.when_to_start() && hold.when_to_start()<ss.when_to_end()){return false;}
            if (ss.when_to_start()> hold.when_to_start() && ss.when_to_start()<hold.when_to_end()){return false;}
          }
        }
      }while(copy.size()!=0)
      return true;
  }       
}
