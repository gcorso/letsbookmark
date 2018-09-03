package com.gcappslab.gcorso.letsbookmark;

import java.io.Serializable;


public class Site implements Serializable {

    private String name;
    private String URL;
    private String photoRes;
    private String folderName;


    /**
     * @param name
     * @param URL
     */
    public Site(String name, String URL, String photoRes) {
        super();
        this.name = name;
        this.URL = URL;
        this.photoRes = photoRes;

    }

    /**
     * @param name
     * @param URL
     */
    public Site(String name, String URL, String photoRes, String folderName) {
        super();
        this.name = name;
        this.URL = URL;
        this.photoRes = photoRes;
        this.folderName = folderName;
    }



    /**
     * @return the name
     */
    public String getName() {
        return name;
    }


    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * @return the URL
     */
    public String getURL() {
        return URL;
    }


    /**
     * @param URL the URL to set
     */
    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getPhotoRes() {
        return photoRes;
    }

    public void setPhotoRes(String photoRes) {
        this.photoRes = photoRes;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }





    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Site [name=" + name + ", URL=" + URL + "]";
    }



    @Override
    public boolean equals(Object o) {
        Site siteToBeCompared;
        boolean result = false;

        try {
            siteToBeCompared = (Site) o;
        }catch (Exception e ) {
            return false;
        }

        if(!this.getName().equalsIgnoreCase(siteToBeCompared.getName())){
            result = false;
        } else if(!this.getURL().equalsIgnoreCase(siteToBeCompared.getURL())){
            result = false;
        } else {
            result = true;
        }

        return result;
    }



}


