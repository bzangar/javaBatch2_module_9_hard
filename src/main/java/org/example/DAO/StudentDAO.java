package org.example.DAO;

import org.example.model.StudentView;
import org.example.sources.DbParameters;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    public static List<StudentView> getAll() {
        List<StudentView> result = new ArrayList<>();

        String sql = "select s.id, s.name, g.group_name, s.is_attended\n" +
                "from student2 as s\n" +
                "join groups as g on s.group_name_id = g.id;";

       // String sql = "select * from students";

        try(Connection conn = DriverManager.getConnection(DbParameters.DB_URL, DbParameters.DB_USER, DbParameters.DB_PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while(rs.next()){
                StudentView st = StudentView.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .group(rs.getString("group_name"))
                        .is_attended(rs.getBoolean("is_attended"))
                        .build();
                result.add(st);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Количество студентов: " + result.size());
        return result;
    }

    public static List<StudentView> getByGroup(String group){
        List<StudentView> result = new ArrayList<>();

        String sql = "select s.id, s.name, g.group_name, s.is_attended from student2 as s " +
                "join groups as g " +
                "on s.group_name_id = g.id " +
                "where g.group_name = ?;";

        //String sql = "select * from students where group_name=?";

        try(Connection conn = DriverManager.getConnection(DbParameters.DB_URL, DbParameters.DB_USER, DbParameters.DB_PASSWORD);
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, group);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                result.add(StudentView.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .group(rs.getString("group_name"))
                        .is_attended(rs.getBoolean("is_attended")).build());
            }


        } catch(SQLException e){
            e.printStackTrace();
        }

        return result;
    }

    public static void deleteStudent(int id){
        String sql = "delete from student2 where id = ?";

        try(Connection conn = DriverManager.getConnection(DbParameters.DB_URL, DbParameters.DB_USER, DbParameters.DB_PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);

            stmt.executeUpdate();
            System.out.println("Был удален студент с индексом: " + id);


        } catch(SQLException e){
            e.printStackTrace();
        }


    }
}
