/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sample.jmx.mx;

import java.beans.ConstructorProperties;

/**
 *
 * @author ricky
 */
public class Statistics {
    
    private long hellos = 0;
    private long namedHellos = 0;
    private long worldHellos = 0;    
    
    @ConstructorProperties({"hellos", "namedHellos", "worldHellos"})
    public Statistics(long hellos, long namedHellos, long worldHellos) {
        this.namedHellos = namedHellos;
        this.worldHellos = worldHellos;
        this.hellos = hellos;
    }

    public long getHellos() {
        return hellos;
    }

    public void setHellos(long hellos) {
        this.hellos = hellos;
    }

    public long getNamedHellos() {
        return namedHellos;
    }

    public void setNamedHellos(long namedHellos) {
        this.namedHellos = namedHellos;
    }

    public long getWorldHellos() {
        return worldHellos;
    }

    public void setWorldHellos(long worldHellos) {
        this.worldHellos = worldHellos;
    }
    
}
