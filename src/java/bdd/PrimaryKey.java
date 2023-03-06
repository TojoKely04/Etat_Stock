package bdd;

import java.sql.*;

public class PrimaryKey 
{
    String pref;
    int longeur;
    String fonction;

    public PrimaryKey(String pref, int longeur, String seq) {
        this.pref = pref;
        this.longeur = longeur;
        this.fonction = "select "+seq+".nextVal from dual";
    }

    public String getPrimaryKeyValue(Connection connection) throws Exception 
    {
        boolean createNewConnection=false;
        if (connection==null){
            connection=Connect.seConnecterOracle("stock", "stock");
            createNewConnection=true;
        }
        Statement stat = connection.createStatement();
        ResultSet idNumber = stat.executeQuery(this.getFonction());
        idNumber.next();
        String idN = idNumber.getString(1);
        // System.out.println(idN);
        String cle = this.getPref();
        for (int i = 0; i < this.getLongeur() - (idN.length() + this.getPref().length()); i++) {
            cle = cle.concat("0");
        }
        cle = cle + idN;
        if(createNewConnection) connection.close();
        return cle;

    } 
    public boolean hasSeq(Connection con,String TableName) throws Exception{
        
        boolean createNewConnection=false;
        if (con==null){
            con=Connect.seConnecterOracle("stock", "stock");
            createNewConnection=true;
        }
        
        boolean exists=false;
        try {
            Statement state=con.createStatement();
            String table=TableName+"_SEQ";
            ResultSet res=state.executeQuery("Select * from user_sequences where sequence_name='"+table.toUpperCase()+"'");
            int count=0;
            if (res.next()) {
                count=1;
            }
            if (count==1) {
                exists=true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(createNewConnection) con.close();

        return exists;
    }



    public String getPref() {
        return pref;
    }



    public void setPref(String pref) {
        this.pref = pref;
    }



    public int getLongeur() {
        return longeur;
    }



    public void setLongeur(int longeur) {
        this.longeur = longeur;
    }



    public String getFonction() {
        return fonction;
    }



    public void setFonction(String fonction) {
        this.fonction = fonction;
    }
}
