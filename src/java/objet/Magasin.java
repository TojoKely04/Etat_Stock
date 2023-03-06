/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objet;

import bdd.BddObject;
import bdd.Connect;
import bdd.PrimaryKey;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import stock.EtatStock;
import stock.MouvementStock;
import stock.Report;

/**
 *
 * @author ITU
 */
public class Magasin extends BddObject{
    String id_magasin;
    String nom;
    Produit[] produits;
    EtatStock[] etatStock;
    
    public void setEtatStockNonDate(Connection connection)throws Exception{
        if(this.getProduits()==null) this.setProduits(connection);
        ArrayList<MouvementStock> allMouvementStock = this.getAllMouvementStockOfMagasin(connection);
        System.out.println("a"+allMouvementStock.size());
        EtatStock[] listEtat = (new EtatStock()).getEtatStockOfManyProduct(this.getProduits(), allMouvementStock);
        this.setEtatStock(listEtat);
    }
    
    public void setEtatStockDate(Connection connection , Date date)throws Exception{
        if(this.getProduits()==null) this.setProduits(connection);
        if(date==null) date = Date.valueOf(LocalDate.now());
        ArrayList<MouvementStock> mouvementStock = this.getMouvementStockBetweenTwoDate(connection, null, date);  
        System.out.println("p"+mouvementStock.size());
        EtatStock[] listEtat = (new EtatStock()).getEtatStockOfManyProduct(this.getProduits(), mouvementStock);

        this.setEtatStock(listEtat);
    }
    
    public void setEtatStockOptimise(Connection connection , Date d)throws Exception{
        if(d==null) d=Date.valueOf(LocalDate.now());
        if(this.getProduits()==null) this.setProduits(connection);
        
        Date lastDateReport = this.lastDateReportOfMagasin(connection, d);
        Report[] allProductReport = this.reportAllProduct(connection, d);

        ArrayList<MouvementStock> mouvementStock = this.getMouvementStockBetweenTwoDate(connection, lastDateReport, d);
        
        EtatStock[] listEtat = (new EtatStock()).getEtatStockAfterReport(allProductReport, this.getProduits(), mouvementStock);
        
        System.out.println("ok"+listEtat.length);
        System.out.println("ok"+listEtat[0].getEntree());
        this.setEtatStock(listEtat);
    }
    
    public ArrayList<MouvementStock> getAllMouvementStockOfMagasin(Connection connection) throws Exception{
        ArrayList listMouvementStock = (new MouvementStock()).select(connection, "mouvementstock");
        ArrayList<MouvementStock> listMvtSckMagasin = new ArrayList<MouvementStock>();
        System.out.println(this.getId_magasin());
        for(int i=0 ; i<listMouvementStock.size() ; i++){
            MouvementStock temp = (MouvementStock)listMouvementStock.get(i);
            if(temp.getId_magasin().equals(this.getId_magasin())){
                listMvtSckMagasin.add(temp);
            }
        }
        return listMvtSckMagasin;
    }
    
    public ArrayList<MouvementStock> getMouvementStockBetweenTwoDate(Connection connection , Date d1 , Date d2) throws Exception
    {
        if(d2==null) d2=Date.valueOf(LocalDate.now());
        
        ArrayList allMouvement = this.getAllMouvementStockOfMagasin(connection);
        ArrayList<MouvementStock> result = new ArrayList<MouvementStock>();
        for(int i=0 ; i<allMouvement.size() ;i++){
            MouvementStock temp = (MouvementStock)allMouvement.get(i);
            if(d1!=null && d2!=null && temp.getDate_mouvement().compareTo(d1)>0 && temp.getDate_mouvement().compareTo(d2)<=0){
                result.add(temp);
            }
            if(d1==null && d2!=null && temp.getDate_mouvement().compareTo(d2)<=0){
                result.add(temp);
            }
        }
        return result;
    }
    
    public Report lastReportOfProduct(Connection connection , Date d , Produit produit) throws Exception{
        Report r = null;
        ArrayList<Report> listReport = (new Report()).select(connection, "REPORT");
        Date lastReport = this.lastDateReportOfMagasin(connection, d);
        for(int i=0 ; i<listReport.size() ; i++){
            Report temp = (Report)listReport.get(i);
            if(temp.getDate_report().compareTo(lastReport)==0 && temp.getId_produit().equals(produit.getId_produit())) {
                r=temp;
            }
        }
        if(r==null){
            r=new Report();
            r.setId_magasin(this.getId_magasin());
            r.setId_produit(produit.getId_produit());
            r.setDate_report(this.lastDateReportOfMagasin(connection, d));
        }
        return r;
    }
    
    public Report[] reportAllProduct(Connection connection , Date d)throws Exception{
        Report[] result = new Report[this.getProduits().length];
        for(int i=0 ; i<this.getProduits().length ; i++){
            result[i]=this.lastReportOfProduct(connection, d, this.getProduits()[i]);
        }
        return result;
    }
        
    
    public Date lastDateReportOfMagasin(Connection connection , Date d)throws Exception{
        ArrayList<Report> listReport = (new Report()).select(connection, "REPORT");
        Date lastDateReport = Date.valueOf("0000-01-01");
        for(int i=0 ; i<listReport.size() ; i++){
            Report temp = (Report)listReport.get(i);
            if(temp.getId_magasin().equals(this.getId_magasin())){
                if(d==null && temp.getDate_report().compareTo(lastDateReport)>=0){
                    lastDateReport=temp.getDate_report();
                }
                if(d!=null && temp.getDate_report().compareTo(lastDateReport)>=0 && temp.getDate_report().compareTo(d)<=0){
                    lastDateReport=temp.getDate_report();
                }
            }
        }
        return lastDateReport;
    }
        
    public static Magasin[] allMagasin(){
        Magasin[] magasin = new Magasin[0];
        Connection c;
        try {
            c = Connect.seConnecterOracle("stock", "stock");
            ArrayList listMagasin = (new Magasin()).select(c, "magasin");
            magasin=new Magasin[listMagasin.size()];
            for(int i=0 ; i<listMagasin.size() ; i++){
                Magasin temp = (Magasin)listMagasin.get(i);
                magasin[i]=temp;
            }
            c.close();
        } catch (Exception ex) {
            Logger.getLogger(Magasin.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return magasin;
    }
    public Magasin(){
        PrimaryKey primaryKey=new PrimaryKey("M",7,"Magasin_seq");
        this.setPrimaryKey(primaryKey);
    }
    
    public Produit[] getProduits() {
        return produits;
    }

    public void setProduits(Produit[] produits) {
        this.produits = produits;
    }

    public EtatStock[] getEtatStock() {
        return etatStock;
    }

    public void setEtatStock(EtatStock[] etatStock) {
        this.etatStock = etatStock;
    }

    public void setProduits(Connection connection) throws Exception{
        boolean createNewConnection = false;
        if(connection==null) {
            connection=Connect.seConnecterOracle("stock", "stock");
            createNewConnection=true;
        }
        
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT count(*) FROM PRODUITMAGASIN WHERE ID_MAGASIN='"+this.getId_magasin()+"' ORDER BY ID_PRODUIT");
        resultSet.next();
        Produit[] listProduit = new Produit[resultSet.getInt("count(*)")];
        
        resultSet = statement.executeQuery("SELECT * FROM PRODUITMAGASIN WHERE ID_MAGASIN='"+this.getId_magasin()+"' ORDER BY ID_PRODUIT");
        int i=0;
        while(resultSet.next()){
            String id_produit=resultSet.getString("id_produit");
            ArrayList listProduitByID = (new Produit()).selectById(connection, "PRODUIT", id_produit);
            listProduit[i]=(Produit)listProduitByID.get(0);
            i++;
        }
        this.setProduits(listProduit);
        if(createNewConnection) connection.close();
    }
    /*public EtatStock[] getEtatStockNonDate(Connection connection) throws Exception{
        boolean createNewConnection = false;
        if(connection==null) {
            connection=Connect.seConnecterOracle("stock", "stock");
            createNewConnection=true;
        }
        
        ArrayList listMouvementStock = (new MouvementStock()).select(connection, "mouvementstock");
        EtatStock[] listEtatStock = new EtatStock[listMouvementStock.size()];
        for(int i=0 ; i<listEtatStock.length ; i++){
            listEtatStock[i]=new EtatStock();
            listEtatStock[i].set
        }
        
        if(createNewConnection) connection.close();
    }*/
    
    public String getId_magasin() {
        return id_magasin;
    }

    public void setId_magasin(String id_magasin) {
        this.id_magasin = id_magasin;
    }



    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    
    
}
