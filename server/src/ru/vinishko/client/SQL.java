package ru.vinishko.client;
import ru.vinishko.network.User;

import java.sql.*;

public class SQL {
    Connection connection=null;
    Statement statement=null;
    public SQL(){
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:mess.db");
            statement=connection.createStatement();
//            statement.executeUpdate("drop table if exists users");
//            statement.executeUpdate("CREATE TABLE users(" +
//                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
//                    "name VARCHAR(45)," + "pass VARCHAR(45)," +
//                    "email VARCHAR(64));");
            //ResultSet rs=statement.executeQuery("select * from users");
        } catch (Exception e) {
            e.printStackTrace();
        }

//        while (rs.next()){
//            System.out.print(rs.getString("id")+rs.getString("name")+"; ");
//            System.out.println(rs.getString("pass"));
//        }
    }

    public void insert(String name,String passHash, String email){
        try {
            statement.executeUpdate("insert into users(name,pass,email) values('"+name+
                    "','"+passHash+
                    "','"+email+"')");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public ResultSet find(){
        try {
            ResultSet rs=statement.executeQuery("SELECT * FROM users WHERE name="+"name");
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
    public User find(String name){
        try {
            PreparedStatement statement=connection.prepareStatement("SELECT * FROM users WHERE name = ?");
            statement.setString(1,name);
            ResultSet rs=statement.executeQuery();
            statement.clearParameters();
            return new User(rs.getString("name"),rs.getString("pass"),rs.getString("email"));
        } catch (SQLException e) {
            e.printStackTrace();
            return new User();
        }

    }
}
