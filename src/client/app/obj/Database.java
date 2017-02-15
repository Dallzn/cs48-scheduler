package client.app.obj;

//Imports
//Filesystem imports
import java.io.File;
//DOM XML imports
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;

//ArrayList imports
import java.util.ArrayList;
import java.util.ListIterator;
//local imports
import client.app.exceptions.*;
import client.app.obj.Filter;

/*
*Class representing a database store. Keeps all information about User objects and their corresponding Events.
*Allows for program persistence after termination. Contains functions to modify an XML document as a record.
* Used by Client to manipulate program state.
*/
public class Database{
    private static File record;
    private static Document doc;
    private static Element users;
    private static Transformer transformer;
    private int usernum;

    /*
    *Helper functions to locate Elements in the NodeList provided.
    *@return int index of the User; -1 if not found
    */
    private int getIndexOf(String idnum, NodeList list){
        for(int i=0; i<list.getLength(); i++){
            String query = ((Element)list.item(i)).getAttribute("id");
            if(query.equals(idnum)) return i;
        }
        return -1;
    }
    private Element findUserElement(Element userField, String tag, String idnum) throws ElementNotFoundException{
        NodeList list = userField.getElementsByTagName(tag);
        int index = getIndexOf(idnum, list);
        if(index == -1) throw new ElementNotFoundException();
        Element e = (Element) list.item(index);
        return e;
    }

    //Constructor. Checks for existence of appliation record. Loads it if available, makes new one if not.
    public Database(){
        try{
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformer = transformerFactory.newTransformer();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            record = new File("record.xml");
            if(record.createNewFile()){ //create structure for XML document
                System.out.println("New record created.");
                doc = docBuilder.newDocument();
                users = doc.createElement("users");
                doc.appendChild(users);
                usernum=0;
            }
            else{ //read in existing record
                doc = docBuilder.parse(record);
                System.out.println("Record loaded.");
                users = doc.getDocumentElement();
                usernum = users.getElementsByTagName("user").getLength();
            }
        } catch (Exception e){e.printStackTrace();}
    }

    /*
    *Function to add a new User to the data store. Creates XML structure for a new User with no subcscriptions.
    *DOES NOT check for User existence
    *@param username String of the User's username
    *@param password String of the User's password
    */
    public void addUser(String username, String password){
        //create user with all fields needed.
        Element user = doc.createElement("user");
        Attr uname = doc.createAttribute("id");
        uname.setValue(username);
        user.setAttributeNode(uname);
        Element pw = doc.createElement("pw");
            pw.appendChild(doc.createTextNode(password));
            user.appendChild(pw);
        Element myEvents = doc.createElement("myEvents");
        user.appendChild(myEvents);
        Element myHostedEvents = doc.createElement("myHostedEvents");
        user.appendChild(myHostedEvents);
        Element mySchedules = doc.createElement("mySchedules");
        user.appendChild(mySchedules);
        Element myOrgs = doc.createElement("myOrgs");
        user.appendChild(myOrgs);

        users.appendChild(user); //add user to total list
    }

    /*
    *Function to add a ScheduleEvent to a User.
    *@param uname the username of the User to add the Event to.
    *@id an integer id of the ScheduleEvent to be added.
    *@param eventDetails an ArrayList containing Strings containing {start;end;desc;block;dep} and data.
    */
    public void subscribe(String uname, String id, ArrayList<String> eventDetails) throws ElementNotFoundException {
        Element user = findUserElement(users, "user", uname);
        Element userEvents = (Element)user.getElementsByTagName("myEvents").item(0);
        //create the event Element to be added based on the information provided in the eventDetails
        Element newev = doc.createElement("event");
        Attr idnum = doc.createAttribute("id");
        Element timeblocks = doc.createElement("blocks");
        Element dependencies = doc.createElement("deps");
        ListIterator iter=eventDetails.listIterator();
        String d=(String)iter.next();
        while(iter.hasNext()){
            switch(d){
                case "start": Element start = doc.createElement("start");
                    d=(String)iter.next(); start.appendChild(doc.createTextNode(d));
                    newev.appendChild(start);
                    break;
                case "end": Element end = doc.createElement("end");
                    d=(String)iter.next(); end.appendChild(doc.createTextNode(d));
                    newev.appendChild(end);
                    break;
                case "desc": Element desc = doc.createElement("desc");
                    d=(String)iter.next(); desc.appendChild(doc.createTextNode(d));
                    newev.appendChild(desc);
                    break;
                case "block": Element tb = doc.createElement("tb");
                    d=(String)iter.next(); tb.appendChild(doc.createTextNode(d));
                    timeblocks.appendChild(tb);
                    break;
                case "dep": Element dep = doc.createElement("dep");
                    d=(String)iter.next(); dep.appendChild(doc.createTextNode(d));
                    dependencies.appendChild(dep);
                    break;
                case "name": Element name = doc.createElement("name");
                    d=(String)iter.next(); name.appendChild(doc.createTextNode(d));
                    newev.appendChild(name);
                default: break;
            }
            if(iter.hasNext()) d=(String)iter.next();
        }
        idnum.setValue(id);
        newev.appendChild(timeblocks);
        newev.appendChild(dependencies);
        newev.setAttributeNode(idnum);
        userEvents.appendChild(newev);
    }
    /*
    *Function to remove a ScheduleEvent from a User.
    *@param uname the username of the User to remove the Event from.
    *@param id the integer id of the ScheduleEvent
    */
    public void unsubscribe(String uname, String id) throws ScheduleEventNotFoundException, ElementNotFoundException{
        Element user = findUserElement(users, "user", uname);
        Element userEvents = (Element)user.getElementsByTagName("myEvents").item(0);
        XPathFactory xPathfactory = XPathFactory.newInstance();
        try{
            XPath xpath = xPathfactory.newXPath();
            XPathExpression ex = xpath.compile("event[@id=\""+id+"\"]");
            NodeList result = (NodeList) ex.evaluate(userEvents, XPathConstants.NODESET);
            if(result.getLength()==0) throw new ScheduleEventNotFoundException();
            else{
                Element deleted = (Element)result.item(0);
                deleted.getParentNode().removeChild(deleted);
            }
        } catch(XPathExpressionException e){}
    }

    /*
    *Function to modify the XML record of a specified User by either adding to or deleting from the main XML fields. (e.g. myEvents, myOrgs
    *mySchedules, myHostedEvents)
    *@param uname the username of the User to modify
    *@param add a boolean value either true meaning add, false meaning delete.
    *@param f the main XMl field to alter {myEvents,myHostedEvents,mySchedules,myOrgs}
    *@param newelem a DOM Element Node containing all runtime information about the object to be stored in the main XML file.
    */
    public void modifyUser(String userid, boolean add, String f, Element newelem) throws UserNotFoundException, ElementNotFoundException{
        Element user = findUserElement(users, "user", userid);
        Element field = doc.createElement("null");
        String tagname = "null";
        switch(f){
            case "myEvents": field = (Element)user.getElementsByTagName("myEvents").item(0);
                tagname="event";
                break;
            case "myHostedEvents": field = (Element)user.getElementsByTagName("myHostedEvents").item(0);
                tagname="event";
                break;
            case "mySchedules": field = (Element)user.getElementsByTagName("mySchedules").item(0);
                tagname="schedule";
                break;
            case "myOrgs": field = (Element)user.getElementsByTagName("myOrgs").item(0);
                tagname="org";
                break;
            default: break;
        }
        if(add) field.appendChild(newelem);
        else{
            //find element in doc and delete it.
            String newelemid = newelem.getAttribute("id");
            Element deleted = findUserElement(field, tagname, newelemid);
            deleted.getParentNode().removeChild(deleted);
        }
    }

    public String outputSearchResultString(String uname, ArrayList<Filter>filters) throws UserNotFoundException{
            //get element corresponding to tagname and val.
            //get all elements from myHostedEvents and apply filters.
            return ""; //STUB
    }

    public static void writeToFile() throws TransformerException{
        transformer.transform(new DOMSource(doc), new StreamResult(record));
    }

    public static void main(String argv[]){
        Database tester = new Database();
        ArrayList<String> list = new ArrayList<String>();
        Element newev = doc.createElement("event");
        Attr idnum = doc.createAttribute("id");
        idnum.setValue("1");
        Element timeblocks = doc.createElement("blocks");
        Element dependencies = doc.createElement("deps");
        Element start = doc.createElement("start");
        Element end = doc.createElement("end");
        newev.setAttributeNode(idnum);
        newev.appendChild(start);
        newev.appendChild(end);
        newev.appendChild(dependencies);
        newev.appendChild(timeblocks);
        try{
            tester.addUser("jared","aowruigh");
            tester.modifyUser("jared", true, "myHostedEvents", newev);
            tester.modifyUser("jared", true, "myHostedEvents", newev);
            writeToFile();
        } catch(Exception e){}
        /*
        list.add("start");
        list.add("Apr 7 1995");
        list.add("end");
        list.add("Feb 2 2017");
        list.add("desc");
        list.add("STUB");
        list.add("block");
        list.add("STUB");
        list.add("dep");
        list.add("STUB");

        tester.addUser("Jared","asdf");
        System.out.println("added user");
        try{
            tester.subscribe("Jared", "2", list);
        //System.out.println("added event");
        writeToFile();
        }
        catch(Exception e){e.printStackTrace();}
        /*try{
            tester.unsubscribe("Jared", 1);
        //System.out.println("added event");
        writeToFile();
        }
        catch(Exception e){e.printStackTrace();}
    */}
}
