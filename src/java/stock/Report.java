/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stock;

import bdd.BddObject;
import bdd.Connect;
import bdd.PrimaryKey;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;

/**
 *
 * @author ITU
 */
public class Report extends BddObject{
    String id_report;
    String id_produit;
    String id_magasin;
    int reste;
    Date date_report;

    public Report(){
        PrimaryKey primaryKey=new PrimaryKey("R",7,"Report_seq");
        this.setPrimaryKey(primaryKey);
    }

    public Report(String id_produit, String id_magasin, int reste, Date date_report) {
        PrimaryKey primaryKey=new PrimaryKey("R",7,"Report_seq");
        this.setPrimaryKey(primaryKey);
        
        this.setId_produit(id_produit);
        this.setId_magasin(id_magasin);
        this.setReste(reste);
        this.setDate_report(date_report);
    }
  
    public String getId_report() {
        return id_report;
    }

    public void setId_report(String id_report) {
        this.id_report = id_report;
    }

    public String getId_produit() {
        return id_produit;
    }

    public void setId_produit(String id_produit) {
        this.id_produit = id_produit;
    }

    public String getId_magasin() {
        return id_magasin;
    }

    public void setId_magasin(String id_magasin) {
        this.id_magasin = id_magasin;
    }

    public int getReste() {
        return reste;
    }

    public void setReste(int reste) {
        this.reste = reste;
    }

    public Date getDate_report() {
        return date_report;
    }

    public void setDate_report(Date date) {
        this.date_report = date;
    }
    
    public void setDate_report(String date) {
        this.setDate_report(Date.valueOf(date));
    }
}
