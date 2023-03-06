/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stock;

import bdd.Connect;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import objet.Magasin;
import objet.Produit;

/**
 *
 * @author ITU
 */
public class EtatStock {
    Produit produit;
    int entree;
    int sortie;
    int reste;
    double valeur=0;

    public static void main(String[] args)throws Exception{
        Connection connection= Connect.seConnecterOracle("stock","stock");
        ArrayList listById = (new Magasin()).selectById(connection, "magasin", "M000001");
            Magasin magasin = (Magasin)listById.get(0);
            magasin.setProduits(connection);
            magasin.setEtatStockOptimise(connection,Date.valueOf(LocalDate.now()));
            
//            System.out.println(magasin.getId_magasin());
//            System.out.println(magasin.getProduits().length);
//                        System.out.println(magasin.getProduits()[0].getNom());
//                        System.out.println(magasin.getProduits()[1].getNom());

//            System.out.println(magasin.getEtatStock().length);
            for(int i=0 ; i<magasin.getEtatStock().length ; i++){
//                System.out.println(magasin.getEtatStock()[i].getEntree());
//                System.out.println(magasin.getEtatStock()[i].getSortie());
            }
        ArrayList<MouvementStock> mouvementStock = magasin.getMouvementStockBetweenTwoDate(connection,null,Date.valueOf(LocalDate.now()));
        System.out.println("l : "+ mouvementStock.size());

        connection.close();
    }
    public EtatStock getEtatStockOfGivenMouvement(Produit produit , ArrayList<MouvementStock> listMouvement)
    {
        EtatStock etatStock = new EtatStock();
        etatStock.setProduit(produit);
        
        int entree=0 , sortie=0 , reste=0;
        double valeur=0;
        
        for(int i=0 ; i<listMouvement.size() ; i++){
            MouvementStock temp = (MouvementStock)listMouvement.get(i);
            if(temp.getId_produit().equals(produit.getId_produit())){
                entree+=temp.getEntree();
                sortie+=temp.getSortie();
            }
        }
        reste=entree-sortie;
        
        etatStock.setEntree(entree);
        etatStock.setSortie(sortie);
        etatStock.setReste(reste);

        return etatStock;
    }
    
    public EtatStock getEtatStockOfGivenMouvementDate(Produit produit , ArrayList<MouvementStock> listMouvement , Date d)
    {
        EtatStock etatStock = new EtatStock();
        etatStock.setProduit(produit);
        
        int entree=0 , sortie=0 , reste=0;
        double valeur=0;
        
        for(int i=0 ; i<listMouvement.size() ; i++){
            MouvementStock temp = (MouvementStock)listMouvement.get(i);
            if(temp.getId_produit().equals(produit.getId_produit()) && temp.getDate_mouvement().compareTo(d)>=0){
                entree+=temp.getEntree();
                sortie+=temp.getSortie();
            }
        }
        reste=entree-sortie;
        
        etatStock.setEntree(entree);
        etatStock.setSortie(sortie);
        etatStock.setReste(reste);

        return etatStock;
    }
    public EtatStock[] getEtatStockOfManyProduct(Produit[] produit , ArrayList<MouvementStock> listMouvement){
        EtatStock[] result = new EtatStock[produit.length];
        for(int i=0 ; i<produit.length ; i++){
            Produit temp = produit[i];
            result[i]=this.getEtatStockOfGivenMouvement(temp, listMouvement);
        }

        return result;
    }
    
    public EtatStock[] getEtatStockAfterDate(Produit[] produit , ArrayList<MouvementStock> listMouvement , Date d){
        EtatStock[] result = new EtatStock[produit.length];
        for(int i=0 ; i<produit.length ; i++){
            Produit temp = produit[i];
            result[i]=this.getEtatStockOfGivenMouvementDate(temp, listMouvement,d);
        }
        return result;
    }
    public EtatStock[] getEtatStockAfterReport(Report[] report , Produit[] produit , ArrayList<MouvementStock> listMouvement){
        EtatStock[] result = this.getEtatStockAfterDate(produit, listMouvement , report[0].getDate_report());

        for(int j=0 ; j<result.length ; j++){
            System.out.println("rs"+result[j].getProduit().getId_produit());
            for(int i=0 ; i<report.length ; i++ ){           
                System.out.println("rp"+report[i].getId_produit());
                if(report[i].getId_produit().equals(result[j].getProduit().getId_produit())){
                    result[j].setEntree(result[j].getEntree()+report[i].getReste());
                    result[j].setReste(result[j].getEntree()-result[j].getSortie());
                }
            }
        }
        return result;
    }
    
    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
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

    public int getReste() {
        return reste;
    }

    public void setReste(int reste) {
        this.reste = reste;
    }

    public double getValeur() {
        return valeur;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
    }
    
}
