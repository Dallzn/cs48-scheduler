package client.app.obj;

//Imports
import client.app.obj.ScheduleEvent;
import client.app.obj.TimeBlock;
import java.util.ArrayList;
import client.app.interfaces.ScheduleObject;
import org.w3c.dom.Element;


public class Schedule extends ScheduleObject{
	private ArrayList<TimeBlock> tb;
	private ArrayList<ScheduleEvent> se;
	private String id;

	public Schedule(ArrayList<ScheduleEvent> events, String idnum)
	{
		id = idnum;
		//create timeblocks for a week
		for (int day=1; day<=7; day++)
			for (int hour=0; hour<24; hour++)
			{
				TimeBlock t=new TimeBlock(day,hour);
				tb.add(t);
			}
		se=events;
		//insert events into their corresponding timeblock(s)
		for (int i=0; i<se.size(); i++){
			for (int j=0;j<=(se.get(i).duration()-1)/100;j++){
				tb.get((se.get(i).what_day()-1)*24+se.get(i).when_to_start()+j).addEvent(se.get(i));
			}
		}
	}

	public int size_of_TimeBlock() {return tb.size();}
	public TimeBlock get_TimeBlock(int index) {return tb.get(index);}
	public String getID(){return id;}
	//to simplify event modification, I suggest using remove and add function of class
	//timeblock itself. remove the event requiring modification and add the modified one
	//back.

	//ScheduleObject methods
	@Override public Element record(){
		return super.record(this);
    }
	public void load(Element e){}
}
