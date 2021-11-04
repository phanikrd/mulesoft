/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moviemanagementsystem;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import jdk.nashorn.internal.codegen.CompilerConstants;

/**
 *
 * @author Majey
 */
public class SQLiteConnection {
    public static Connection conn = null;
    public static String sqliteServer = "jdbc:sqlite:";
    public static String resetPath = "";
    
    public static boolean isDatabaseExist(String dbFilePath){
        File dbFile = new File(dbFilePath);
        return dbFile.exists();
    }

    
    public static void connect() {
        try {
            // db parameters
            sqliteServer = "jdbc:sqlite:";
            String getFilePath = new File("").getAbsolutePath();
            String fileAbsolutePath = getFilePath.concat("\\src\\moviemanagementsystem\\database\\majid.db");
            resetPath = fileAbsolutePath;

            System.out.println(sqliteServer);
            System.out.println(getFilePath);
            System.out.println(fileAbsolutePath);
            // create a connection to the database
            
            if(isDatabaseExist(fileAbsolutePath)){
                System.out.println("DB Selection: ");
                conn = DriverManager.getConnection(sqliteServer+fileAbsolutePath);
                System.out.println("Connection to SQLite has been established.");
            }else{
                try{
                    createNewDatabase("database", "majid");
                }catch (Exception ex){
                    System.out.println("Error: " + ex);
                }             
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        /*
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        */
    }
    
    public static void createNewDatabase(String fileSubFolder, String fileName) {
 
        String getFilePath = new File("").getAbsolutePath();
        String fileAbsolutePath = "";
        
        if(fileSubFolder.isEmpty()){
            fileAbsolutePath = getFilePath.concat("\\src\\moviemanagementsystem\\"+fileName+".db");
            resetPath = fileAbsolutePath;
        }else{
            fileAbsolutePath = getFilePath.concat("\\src\\moviemanagementsystem\\"+fileSubFolder+"\\"+fileName+".db");
            resetPath = fileAbsolutePath;
        }
        
        try (Connection conn = DriverManager.getConnection(sqliteServer+fileAbsolutePath)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                // System.out.println("The driver name is " + meta.getDriverName());
                Statement statement  = conn.createStatement();
                statement.executeQuery("CREATE TABLE words(ID INT PRIMARY KEY NOT NULL, word TEXT NOT NULL, type TEXT NOT NULL, defn TEXT NOT NULL);");
                System.out.println("Database Has Been Created!");
                // statement.executeQuery("INSERT INTO words (word, type, defn) VALUES (love, verb, Deep pondness)");
            }
            createDatabaseTable();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static void createDatabaseTable(){
        try (Connection conn = DriverManager.getConnection(sqliteServer+resetPath)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("Database Path: " + resetPath);
                
                Statement statement  = conn.createStatement();
                statement.executeUpdate("CREATE TABLE words(ID INT PRIMARY KEY NOT NULL, word TEXT NOT NULL, type TEXT NOT NULL, defn TEXT NOT NULL);");
                System.out.println("Table Has Been Created!");
                // statement.executeQuery("INSERT INTO words (word, type, defn) VALUES (love, verb, Deep pondness)");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static void insertDatabaseData(int id, String word, String type, String definition) {
        String sql = "INSERT INTO words (ID, word, type, defn) VALUES (?, ?, ?, ?)";
 
        try (Connection conn = DriverManager.getConnection(sqliteServer+resetPath)) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, word);
            pstmt.setString(3, type);
            pstmt.setString(4, definition);
            pstmt.executeUpdate();
            
            System.out.println("Data Inserted!");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
