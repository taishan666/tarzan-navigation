package com.tarzan.nav.utils;

import java.sql.*;

public class DButil {
    private static final String driver = "org.postgresql.Driver";
    private static final String user = "postgres";
    private static final String pwd = "#5Rd!TC2CBA";
    private static final String dbName = "shandong_energy_lgyz";
    private static final String url = "jdbc:postgresql://119.167.159.214:5432/"+dbName+ "??currentSchema=public";
    private static Connection getConnection = null;
    /**
     * 链接数据库
     */
    private static Connection getConnections() {
        try {
            Class.forName(driver);
            getConnection = DriverManager.getConnection(url,user,pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getConnection;
    }


    public static void main(String[] args) throws SQLException {
        tableNames();
    }



    /**
     * 方法描述: 一次生产所有表
     */
    public static void tableNames() {
        getConnection = getConnections();
        try {
            DatabaseMetaData metaData = getConnection.getMetaData();
            ResultSet rs = metaData.getTables(getConnection.getCatalog(), getConnection.getSchema(), null, new String[] { "TABLE" });
            while (rs.next()) {
                String tableName=rs.getString("TABLE_NAME");
                getTableData(tableName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getTableData(String tableName) {
        try {
            String sql = "select count(1) from "+tableName;
            PreparedStatement st=getConnection.prepareStatement(sql);
            ResultSet rs =st.executeQuery();
            while (rs.next()) {
                int count=rs.getInt(1);
                if(count>0){
                    System.out.println(tableName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
