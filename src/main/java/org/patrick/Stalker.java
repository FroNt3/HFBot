package org.patrick;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

public class Stalker {

    private ArrayList<Person> personList;
    
    /**
     * Stalker objects default to all info available
     */
    public Stalker() {
        ArrayList<String[]> infoList = PrivateInfo.getAllInfo();
        ArrayList<Person> personList = new ArrayList<Person>();        
        for (String[] info : infoList) {
            personList.add(new Person(info));
        }
        this.personList = personList;
    }
    
    /**
     * If you only want a set of persons you can provide a list of them yourself
     * 
     * @param personList List of persons to stalk
     */
    public Stalker(ArrayList<Person> personList) {
        this.personList = personList;
    }
    
    public void updateStatus() throws CustomException {
        ArrayList<String> playerList;
        try {
            playerList = ApiReader.getOnlinePlayers();
        } catch (JSONException | IOException e) {            
            e.printStackTrace();
            throw new CustomException("Error while trying to update online players.");
        }
        
        for (Person person : getPersonList()) {
            person.setStatus(false);
            for (String player : playerList) {
                if (player.contains(person.getName())) {
                    person.setStatus(true);
                }
            }
        }
    }
    
    public void updatePosi() throws CustomException {
        for (Person person : getPersonList()) {
            String tmpPosi;
            try {
                tmpPosi = ApiReader.getPosition(person.getKey());
            } catch (JSONException | IOException e) {
                e.printStackTrace();
                throw new CustomException("Error while trying to update position of " + person.getName());
            }
            person.setPosi(tmpPosi);
        }
    }
    
    public void update() throws CustomException {
        this.updateStatus();
        this.updatePosi();
    }
    
    public Person getPerson(String name) {
        for (Person person : getPersonList()) {
            if (person.getName().toLowerCase().contains(name.toLowerCase())) {
                return person;
            }
        }
        
        return null;
    }
    
    public ArrayList<Person> getPersonList() {
        return this.personList;
    }
    
}
