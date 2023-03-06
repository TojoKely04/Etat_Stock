package bdd;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class BddObject 
{
    PrimaryKey primaryKey;
    
    public static String changeFirstLetter(String mot)
    {
        char[] motChar=mot.toCharArray();
        char first=Character.toUpperCase(motChar[0]);
        motChar[0]=first;
        return String.valueOf(motChar);
    }
    public ArrayList select(Connection connection , String base) throws Exception
    {
        base=base.toUpperCase();
        Statement stat=connection.createStatement();
        ArrayList listObjet=new ArrayList();
        ArrayList listField=BddObject.getFields(connection, this , base);

        ResultSet resultSet=stat.executeQuery("SELECT * FROM "+base);
        while (resultSet.next()) 
        {
            Object objet=this.getClass().getConstructor().newInstance();
            for (int i = 0; i < listField.size(); i++) 
            {
                Field temp=(Field)listField.get(i);
                String getterBase="get"+BddObject.changeFirstLetter(temp.getType().getSimpleName());
                if(BddObject.changeFirstLetter(temp.getType().getSimpleName()).equals("Date"))
                    getterBase="getString";
                String setterObject="set"+BddObject.changeFirstLetter(temp.getName());
                Method get=ResultSet.class.getMethod(getterBase,getterBase.getClass());
                Method set=objet.getClass().getMethod(setterObject,temp.getType());
                if(temp.getType()==java.sql.Date.class)
                    set=objet.getClass().getMethod(setterObject,String.class);
                set.invoke(objet,get.invoke(resultSet,temp.getName()));
            }
            listObjet.add(objet);
        }
        stat.close(); 
        return listObjet;
    }       
        
    public ArrayList find(Connection connection , String base , String requete)throws Exception
    {
        base=base.toUpperCase();
        Statement stat=connection.createStatement();
        ArrayList listObjet=new ArrayList();
        ArrayList listField=BddObject.getFields(connection, this , base);

        ResultSet resultSet=stat.executeQuery(requete);
        while (resultSet.next()) 
        {
            Object objet=this.getClass().getConstructor().newInstance();
            for (int i = 0; i < listField.size(); i++) 
            {
                Field temp=(Field)listField.get(i);
                String getterBase="get"+BddObject.changeFirstLetter(temp.getType().getSimpleName());
                if(BddObject.changeFirstLetter(temp.getType().getSimpleName()).equals("Date"))
                    getterBase="getString";
                String setterObject="set"+BddObject.changeFirstLetter(temp.getName());
                Method get=ResultSet.class.getMethod(getterBase,getterBase.getClass());
                Method set=objet.getClass().getMethod(setterObject,temp.getType());
                if(temp.getType()==java.sql.Date.class)
                    set=objet.getClass().getMethod(setterObject,String.class);
                set.invoke(objet,get.invoke(resultSet,temp.getName()));
            }
            listObjet.add(objet);
        }
        stat.close(); 
        return listObjet;
    }
    public ArrayList selectById(Connection connection , String base , String id) throws Exception
    {
        base=base.toUpperCase();
        Statement stat=connection.createStatement();
        ArrayList listObjet=new ArrayList();
        ArrayList listField=BddObject.getFields(connection, this , base);
        String sql = "SELECT * FROM "+base;
        String primaryKey = null;
        try {
            primaryKey=BddObject.getPrimaryKeyName(connection, base);
        } catch (Exception e) { }
        
        if(primaryKey!=null){
            sql = sql +" WHERE "+primaryKey+"='"+id+"'";
        }
//        System.out.println(sql);
        ResultSet resultSet=stat.executeQuery(sql);
        while (resultSet.next()) 
        {
            Object objet=this.getClass().getConstructor().newInstance();
            for (int i = 0; i < listField.size(); i++) 
            {
                Field temp=(Field)listField.get(i);
                String getterBase="get"+BddObject.changeFirstLetter(temp.getType().getSimpleName());
                if(BddObject.changeFirstLetter(temp.getType().getSimpleName()).equals("Date"))
                    getterBase="getString";
                String setterObject="set"+BddObject.changeFirstLetter(temp.getName());
                Method get=ResultSet.class.getMethod(getterBase,getterBase.getClass());
                Method set=objet.getClass().getMethod(setterObject,temp.getType());
                if(temp.getType()==java.sql.Date.class)
                    set=objet.getClass().getMethod(setterObject,String.class);
                set.invoke(objet,get.invoke(resultSet,temp.getName()));
            }
            listObjet.add(objet);
        }
        stat.close(); 
        return listObjet;
    }

    public void insert(Connection connection , String base) throws Exception
    {
        base=base.toUpperCase();
        Statement statement=connection.createStatement();
        ArrayList listField=BddObject.getFields(connection, this, base);
        String colonne="";
        String donnees="";
        String primaryKey=null;
        try {
            primaryKey=BddObject.getPrimaryKeyName(connection, base);
        } catch (Exception e) { }
        for (int j = 0; j < listField.size() ; j++) 
        {
            Field temp=(Field)listField.get(j);
            String getter="get"+BddObject.changeFirstLetter(temp.getName());
            String setter="set"+BddObject.changeFirstLetter(temp.getName());

            Method method = this.getClass().getMethod(getter);
            String d=null;
            if (method.invoke(this) instanceof Number) 
            {
                d=String.valueOf(method.invoke(this));
            }else{
                d="'"+method.invoke(this)+"'";
            }

            if (BddObject.hasSeq(connection, base) && primaryKey.compareTo(temp.getName())==0)
            {
                String pkey=this.getPrimaryKey().getPrimaryKeyValue(connection);
                d="'"+pkey+"'";
                Method setPkey=this.getClass().getMethod(setter,temp.getType());
                setPkey.invoke(this,pkey);
            }
            
            if (j!=listField.size()-1) 
            {
                colonne=colonne+String.valueOf(temp.getName())+",";
                donnees=donnees+d+",";
            }else{
                colonne=colonne+String.valueOf(temp.getName());
                donnees=donnees+d;
            }
        }
        String sql="INSERT INTO "+base+"("+colonne+")"+ " values ("+donnees+")";
        System.out.println(sql);
        statement.executeQuery(sql);
        statement.close();
    }
    public void delete(Connection connection,String base_name) throws Exception
    {
        base_name=base_name.toUpperCase();
        Statement stat = connection.createStatement();
        String primaryKey = BddObject.getPrimaryKeyName(connection, base_name);

        Method method = this.getClass().getMethod("get"+BddObject.changeFirstLetter(primaryKey));
        String primarykeyvalue = String.valueOf(method.invoke(this));

        // this.historiser(connection, base_name, "delete");

        String final_request = "DELETE FROM "+base_name+" WHERE "+primaryKey+" ="+primarykeyvalue;
        ResultSet answer = stat.executeQuery(final_request);
        stat.close();
    }

    public void update(Connection connection,String base_name) throws Exception
    {
        base_name=base_name.toUpperCase();
        Statement stat = connection.createStatement();
        String primaryKey = BddObject.getPrimaryKeyName(connection, base_name);
        Field objectField = this.getClass().getDeclaredField(primaryKey);

        Method method = this.getClass().getMethod("get"+BddObject.changeFirstLetter(primaryKey));
        String primarykeyvalue = String.valueOf(method.invoke(this));

        ArrayList allObjectFields = BddObject.getFields(connection,this,base_name);
        String modification="";
        for (int i = 0; i < allObjectFields.size(); i++) 
        {
            Field temporaire = (Field)allObjectFields.get(i);
            String getter = "get"+BddObject.changeFirstLetter(temporaire.getName());
            Method m = this.getClass().getMethod(getter);
            String donnee_temp = "'"+m.invoke(this)+"'";
            if (i != allObjectFields.size()-1) 
            {
                modification = modification+String.valueOf(temporaire.getName())+"="+donnee_temp+",";
            }
            else{
                modification = modification+String.valueOf(temporaire.getName())+"="+donnee_temp;
            }
        }
        // this.historiser(connection, base_name, "update");
        String final_request = "UPDATE "+base_name+" SET "+modification+" WHERE "+primaryKey+" = "+"'"+primarykeyvalue+"'";
        System.out.println(final_request);
        ResultSet resultSet = stat.executeQuery(final_request);
        stat.close();       
    }
    // public void historiser(Connection connection , String table , String action) throws Exception
    // {
    //     String dateModification=BddObject.getDateTimeSys(connection);
    //     String valeur=this.getLastValue(connection, table);
    //     PrimaryKey primaryKey=new PrimaryKey("HIS",7,"historique_seq");
    //     String id=primaryKey.getPrimaryKeyValue(connection);
    //     Historique historique=new Historique(id,table,action,dateModification,valeur);
    //     historique.insert(connection,"HISTORIQUE");
    // }
    public static ArrayList getColumnName(Connection connection , String base) throws Exception
    {
        base=base.toUpperCase();
        String nomBase=base.toUpperCase();
        Statement stat=connection.createStatement();
        ResultSet resultSet=stat.executeQuery("Select column_name from USER_TAB_COLUMNS where table_name='"+nomBase+"'");
       
        ArrayList colonne=new ArrayList<String>();
        while(resultSet.next())
        {
            colonne.add(resultSet.getString("COLUMN_NAME").toLowerCase());
        }
        stat.close();
        return colonne;
    }
    public static ArrayList getFields(Connection connection ,Object object, String nomBase)throws Exception
    {
        Statement statement=connection.createStatement();
        ArrayList listColumnBase=BddObject.getColumnName(connection, nomBase);
        Field[] attributObject=object.getClass().getDeclaredFields();
        ArrayList listAttribut=new ArrayList<Field>();
        for (int i = 0; i < attributObject.length; i++) 
        {
            if (listColumnBase.contains(attributObject[i].getName().toLowerCase())) 
            {
                listAttribut.add(attributObject[i]);
            }
        }
        statement.close();
        return listAttribut;
    }
    public static String getPrimaryKeyName(Connection connection , String baseName)throws Exception
    {
        String nomBase=baseName.toUpperCase();
        Statement stat=connection.createStatement();
        String sql = "SELECT cols.column_name FROM all_constraints cons, all_cons_columns cols WHERE cols.table_name = '"+baseName+"' AND cons.constraint_type = 'P' AND cons.constraint_name = cols.constraint_name AND cons.owner = cols.owner ORDER BY cols.table_name, cols.position";
        ResultSet resultSet = stat.executeQuery(sql);
        resultSet.next();
        return resultSet.getString("column_name").toLowerCase();
    }

    public String getLastValue(Connection connection,String table) throws Exception
    {
        String valiny="";
        Statement statement=connection.createStatement();

        String primaryKey = BddObject.getPrimaryKeyName(connection, table);
        Method method = this.getClass().getMethod("get"+BddObject.changeFirstLetter(primaryKey));
        String primarykeyvalue = String.valueOf(method.invoke(this));

        String sql = "SELECT * FROM "+table+" WHERE "+primaryKey +" = "+primarykeyvalue;

        ResultSet resultSet=statement.executeQuery(sql);
        resultSet.next();

        ArrayList listField=BddObject.getFields(connection, this , table);
        Object objet=this.getClass().getDeclaredConstructors()[0].newInstance();
        for (int i = 0; i < listField.size(); i++) 
        {
            Field temp=(Field)listField.get(i);
            String getterBase="get"+BddObject.changeFirstLetter(temp.getType().getSimpleName());
            Method get=ResultSet.class.getMethod(getterBase,getterBase.getClass());

            if (i<listField.size()-1) 
            {
                valiny+=temp.getName()+":"+get.invoke(resultSet,temp.getName())+",";
            }else{
                valiny+=temp.getName()+":"+get.invoke(resultSet,temp.getName());
            }
        }
        // System.out.println(valiny);
        return valiny;
        
    }
    public static String getDateTimeSys(Connection connection) throws Exception
    {
        Statement statement=connection.createStatement();
        ResultSet resultSet=statement.executeQuery("SELECT CURRENT_TIMESTAMP FROM DUAL");
        resultSet.next();
        String date=resultSet.getString("CURRENT_TIMESTAMP");
        statement.close();
        return date;
    }
    public static boolean hasSeq(Connection con,String TableName) throws Exception
    {
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
        return exists;
    }
    // public static PrimaryKey getExemplePrimary(Connection connection ,String nomClasse ,String table , String colonnePrimaryKey) throws Exception
    // {
    //     int taille=7;
    //     Statement statement=connection.createStatement();
    //     ResultSet resultSet=statement.executeQuery("SELECT "+ colonnePrimaryKey +" FROM "+table);
    //     try {
    //         resultSet.next();
    //         String primaryKey=resultSet.getString(colonnePrimaryKey);
    //         taille=primaryKey.length();
    //     } catch (Exception e) {}
    //     statement.close();
    //     char[] prefChar=new char[3];
    //     for (int i = 0; i < 3; i++) 
    //     {
    //         prefChar[i]=nomClasse.toCharArray()[i];
    //     }
    //     PrimaryKey pk=new PrimaryKey(String.valueOf(prefChar),taille, table+"_seq");
    //     return pk;
    // }
    public PrimaryKey getPrimaryKey() {
        return primaryKey;
    }
    public void setPrimaryKey(PrimaryKey primaryKey) {
        this.primaryKey = primaryKey;
    }
}
