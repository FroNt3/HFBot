package org.patrick;

public class Person {
    
    private String name;
    private String key;
    private String posi;
    private String filename;
    private Boolean status;
    
    public Person(String name, String key, String filename) {
        this.name = name;
        this.key = key;
        this.filename = filename;
        this.status = false;
    }
    
    public Person(String[] args) {
        this.name = args[0];
        this.key = args[1];
        this.filename = args[2];
        this.status = false;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @return the posi
     */
    public String getPosi() {
        return posi;
    }

    /**
     * @return the status
     */
    public Boolean getStatus() {
        return status;
    }
    
    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }
    
    public void setPosi(String posi) {
        this.posi = posi;
    }
    
    public void setStatus(Boolean bool) {
        this.status = bool;
    }
    
}