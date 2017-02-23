package client.commander;

//Imports
import client.app.Client;
import client.app.obj.ScheduleEvent;
import client.app.obj.Schedule;
import client.view.UserInterface;
import client.app.obj.ScheduleGenerator;

import java.util.ArrayList;
import javax.xml.transform.TransformerException;


/*
*Class BackGround Commander acting as the Controller in the MVC Design Pattern. Responsible for changing the state of the application
* and coordinating method calls between UserInterface and the rest of the application.
*/
public class BGCommander{
    //instance variables
    private Client client;
    private ScheduleGenerator gen;
    private static BGCommander command;

    /*
    *BGCommander Constructor. DO NOT USE TO INSTANTIATE BGCommander. Instead use getBGCommander() function.
    */
    private BGCommander(){
        client = new Client();
        gen = new ScheduleGenerator();
    }

    /**
    *Static Factory method to initialize the Commander object. Use this instead of Constructor to ensure that there exists only
    *one BGCommander object.
    *@return BGCommander object
    */
    public static BGCommander getBGCommander(){
        if(command==null)
            command = new BGCommander();
        return command;
    }

    //TEMPORARY METHOD TO BE REPLACED
    public ArrayList<ScheduleEvent> demoArray(){
        ArrayList<ScheduleEvent> Events = new ArrayList<ScheduleEvent>();
        Events.add(new ScheduleEvent(2,1700, 2100, "dung", "ID1"));
        Events.add(new ScheduleEvent(12, 800, 11, "A good ol' morning at the beach","ID1"));
        Events.add(new ScheduleEvent(12, 1200, 2, "Beach clean-up","ID1"));
        Events.add(new ScheduleEvent(15, 800, 1100, "dope","ID1"));
        Events.add(new ScheduleEvent(15,1200, 1900, "superdope","ID1"));
        Events.add(new ScheduleEvent(4,1100, 1300, "Koala","ID1"));
        Events.add(new ScheduleEvent(7,500, 700, "biggie","ID1"));
        Events.add(new ScheduleEvent(27,800, 1000, "smalls","ID1"));
        return Events;

    }

    //TEMPORARY METHOD TO BE RE-IMPLEMENTED
    public ArrayList<ScheduleEvent> myEventArray(){
        return new ArrayList<ScheduleEvent>();
    }




    /**
    *Function to generate a list of Schedule objects to return to UserInterface for display.
    *Uses internal ScheduleGenerator object to perform schedule generation algorithm and uses
    *Client to get myEvents from the current User
    *@return ArrayList containing all Schedule objects obtained by Events subscribed to by the current User
    */
    public ArrayList<Schedule> genSchedule(){
        //STUB
        return new ArrayList<Schedule>();
    }

    /**
    *Function to generate a list of ScheduleEvent objects to return to UserInterface for display.
    *Uses Client to retrieve ScheduleEvent objects according to search filters, from another Organization
    *@param orgName A string corresponding to the Organization name to search through
    *@param filters A set of filters to apply when searching for a ScheduleEvent
    */
    //public ArrayList<ScheduleEvent> search(String orgName, ArrayList<String> filters){}

    /**
    *Function to set the currentUser variable in Client. Uses Client to verify that credentials are present
    *and valid on local database. Modifies Client to update currentUser
    */
    public void login(String username, String password){
        client.setCurrUser(username,password);
    }

    /**
    *Function to add a User to the application.
    *@param uname String representing the User's username
    *@param pword String representing the User's password
    */
    public void addUser(String username, String pword){
        client.addUser(username, pword);
    }

    /**
    *Function to subscribe a logged in User to a ScheduleEvent. Creates a ScheduleEvent object and passes that
    *to Client for further processing.
    *@param id A string that identifies this ScheduleEvent. Could be a id number, a name, or any other identifying information
    *@param
    */
    public void subscribeEvent(String id, String day, String starthr, String endhr, String desc){
        ScheduleEvent event = new ScheduleEvent(day, starthr, endhr, desc, id);
        client.subscribe(event);
    }

    /**
    *Function to unsubscribe a logged in User to a ScheduleEvent. Creates a null ScheduleEvent with only id
    *@param id a string identifier equal to the identifier of the Schedule to be deleted.
    */
    public void unsubscribe(String id){
       ScheduleEvent event = new ScheduleEvent(NULL, NULL, NULL, NULL, id);
       client.unsubsribe(event);
    }

    /**
    *Function to exit the application cleanly. Tells Client to exit application and write application state to file.
    */
    public void exitApp(){
        try{
            client.exitApp();
        }catch(TransformerException t){t.printStackTrace();}
    }
}
