package org.example;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.DAO.GroupDAO;
import org.example.DAO.StudentDAO;
import org.example.model.Group;
import org.example.model.StudentView;
import org.example.sources.DbParameters;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.List;
import java.util.Objects;

@WebServlet("/hard")
public class StudentServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");

        List<StudentView> list;
        List<Group> group_list = GroupDAO.getGroupsFromDB();

        String group = req.getParameter("group");

        if(Objects.isNull(group) || group.isEmpty()){
            list = StudentDAO.getAll();
        } else {
            list = StudentDAO.getByGroup(group);
        }

        PrintWriter out = resp.getWriter();

        out.println("<html lang=\"ru\">");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\">");
        out.println("<title>module_9_hard</title>");
        out.println("<style>");
        out.println("body {\n" +
                "    font-family: Arial, sans-serif;\n" +
                "    padding: 20px;\n" +
                "    background-color: #f9f9f9;\n" +
                "}\n" +
                "h1 {\n" +
                "    text-align: center;\n" +
                "}\n" +
                "form {\n" +
                "    width: 300px;\n" +
                "    margin: 0 auto 20px auto;\n" +
                "    background-color: #fff;\n" +
                "    padding: 8px;\n" +
                "    border: 1px solid #ccc;\n" +
                "    font-size: 13px;\n" +
                "}\n" +
                "form label {\n" +
                "    display: block;\n" +
                "    margin-bottom: 3px;\n" +
                "    font-weight: bold;\n" +
                "}\n" +
                "form input, form select {\n" +
                "    width: 100%;\n" +
                "    padding: 2px 4px;\n" +
                "    margin-bottom: 8px;\n" +
                "    box-sizing: border-box;\n" +
                "}\n" +
                "form button, input[type='submit'] {\n" +
                "    padding: 4px 8px;\n" +
                "    font-size: 12px;\n" +
                "    cursor: pointer;\n" +
                "}\n" +
                "table {\n" +
                "    width: 300px;\n" +
                "    border-collapse: collapse;\n" +
                "    margin: 0 auto;\n" +
                "    background-color: #fff;\n" +
                "    font-size: 12px;\n" +
                "}\n" +
                "th, td {\n" +
                "    border: 1px solid #aaa;\n" +
                "    padding: 1px 10px;\n" +              // Меньше отступ
                "    text-align: center;\n" +
                "    line-height: 1.2;\n" +              // Меньше высота строки
                "}\n" +
                "th {\n" +
                "    background-color: #ddd;\n" +
                "}");

        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Журнал посещаемости</h1>");


        out.println("<form action='/module_9_hard/hard' method='POST'>");
        out.println("ФИО: <input type='text' name='name' required><br>");
        out.println("Группа: <input type='text' name='groupName' required><br>");
        out.println("Посетил: <select name='isAttended'>");
        out.println("    <option value='true'>Да</option>");
        out.println("    <option value='false'>Нет</option>");
        out.println("</select><br>");
        out.println("<input type='submit' value='Добавить'>");
        out.println("</form>");

        out.println("<form method='GET' action='/module_9_hard/hard'>");
        out.println("<label for='group'>Выберите группу:</label>");
        out.println("<select name='group' id='group'>");
        out.println("<option value = ''" + (group == null ? " selected" : "") + ">не выбрана</option>");

        System.out.println("проверка 01");
        System.out.println("group_list = " + group_list);

        for (Group g : group_list) {
            boolean isSelected = Objects.equals(g.getName(), group);
            out.println("<option value='"+ g.getName() +"'"+ (isSelected ? "selected": "") + ">" + g.getName() + " </option>");
        }

        System.out.println("проверка 02");
        out.println("</select>");
        out.println("<input type='submit' value='Филтровать'>");
        out.println("</form>");


        out.println("<table>");
        out.println("<thead>\n" +
                "        <tr>\n" +
                "            <th>ФИО</th>\n" +
                "            <th>Группа</th>\n" +
                "            <th>Посетил</th>\n" +
                "        </tr>\n" +
                "    </thead>\n");

        if(list.isEmpty()){
            out.println("</table>");
            out.println("<h1> Нет таблицы данных");
        } else {
            out.println("<tbody>");
            for (StudentView s: list){
                out.println("<tr>");
                out.println("<td>" + s.getName() + "</td>");
                out.println("<td>" + s.getGroup() + "</td>");
                out.println("<td>" + s.is_attended() + "</td>");
                out.println("<td>");
                out.println("<form method='POST' action='/module_9_hard/hard' onsubmit='return confirm(\"Sure?\");'>");
                out.println("<input type='hidden' name='deleteId' value='"+ s.getId() +"'/>");
                out.println("<input type='submit' value='Удалить'/>");
                out.println("</form>");
                out.println("</td>");
                out.println("</tr>");
            }
        }
        out.println("</tbody>");
        out.println("</table>");
        out.println("</body>");
        out.println("</html>");

        System.out.println(list);

    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");

        String id = req.getParameter("deleteId");


        if(id != null && !id.isEmpty()){
            System.out.println(id);
            //resp.sendRedirect("/module_9_hard/hard");
            StudentDAO.deleteStudent(Integer.parseInt(id));
            resp.sendRedirect("/module_9_hard/hard");
            return;
        }



        String sql_group = "INSERT INTO groups(group_name) values(?)  RETURNING id";
        String sql_student = "INSERT INTO student2(name, group_name_id, is_attended) values(?, ?, ?)";
        int groupId = -1;

        try(Connection conn = DriverManager.getConnection(DbParameters.DB_URL, DbParameters.DB_USER, DbParameters.DB_PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(sql_group);
            PreparedStatement stmt2 = conn.prepareStatement(sql_student);

        ){

            stmt.setString(1, req.getParameter("groupName"));

            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                groupId = rs.getInt("id");
            }

            stmt2.setString(1, req.getParameter("name"));
            stmt2.setInt(2, groupId);
            stmt2.setBoolean(3, Boolean.parseBoolean(req.getParameter("isAttended")));
            stmt2.executeUpdate();

            System.out.println("айди группы добавляемого студента: " + groupId);



        } catch (SQLException e){
            e.printStackTrace();
        }


        resp.sendRedirect("/module_9_hard/hard");

    }

}
