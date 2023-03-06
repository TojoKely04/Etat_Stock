
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
public class MouvementStock extends BddObject{
    String id_mouvement_stock;
    String id_magasin;
    Date date_mouvement;
    String id_produit;
    int entree;
    int sortie;

    public MouvementStock(){
        PrimaryKey primaryKey=new PrimaryKey("MS",7,"MouvementStock_SEQ");
        this.setPrimaryKey(primaryKey);
    }

    public MouvementStock(String id_magasin, Date date_mouvement, String id_produit, int entree, int sortie) {
        PrimaryKey primaryKey=new PrimaryKey("MS",7,"MouvementStock_SEQ");
        this.setPrimaryKey(primaryKey);
        
        this.setId_magasin(id_magasin);
        this.setDate_mouvement(date_mouvement);
        this.setId_produit(id_produit);
        this.setEntree(entree);
        this.setSortie(sortie);
    }
    
    public String getId_magasin() {
        return id_magasin;
    }

    public void setId_magasin(String id_magasin) {
        this.id_magasin = id_magasin;
    }

    public String getId_mouvement_stock() {
        return id_mouvement_stock;
    }

    public void setId_mouvement_stock(String id_mouvement_stock) {
        this.id_mouvement_stock = id_mouvement_stock;
    }

    public Date getDate_mouvement() {
        return date_mouvement;
    }

    public void setDate_mouvement(Date date_mouvement) {
        this.date_mouvement = date_mouvement;
    }
    
    public void setDate_mouvement(String date_mouvement) {
        this.setDate_mouvement(Date.valueOf(date_mouvement));
    }

    public String getId_produit() {
        return id_produit;
    }

    public void setId_produit(String id_produit) {
        this.id_produit = id_produit;
    }

    public int getEntree() {
        return entree;
    }

    public void setEntree(int entree) {
        this.entree = entree;
    }

    public int getSortie() {
        return sortie;
    }

    public void setSortie(int sortie) {
        this.sortie = sortie;
    }   
}
