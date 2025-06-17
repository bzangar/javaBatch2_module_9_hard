package org.example.DAO;

import org.example.model.Group;
import org.example.sources.DbParameters;

import java.sql.*;
import java.util.*;

public class GroupDAO {
    public static List<Group> getGroupsFromDB(){
        List<Group> result = new ArrayList<>();

        String sql = "select * from groups";
        try(Connection conn = DriverManager.getConnection(DbParameters.DB_URL, DbParameters.DB_USER, DbParameters.DB_PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while(rs.next()){
                result.add(Group.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("group_name"))
                        .build());
            }

        } catch (SQLException e){
            e.printStackTrace();
        }

        return result;
    }
}
