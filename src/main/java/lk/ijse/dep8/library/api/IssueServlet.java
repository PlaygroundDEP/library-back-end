package lk.ijse.dep8.library.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import lk.ijse.dep8.library.dto.IssueDTO;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "IssueServlet", value = {"/issues", "/issues/"})
public class IssueServlet extends HttpServlet {

    @Resource(name = "java:comp/env/jdbc/pool4library")
    private DataSource pool;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getContentType() == null || !req.getContentType().startsWith("application/json")) {
            resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            return;
        }

        try {
            Jsonb jsonb = JsonbBuilder.create();
            IssueDTO issue = jsonb.fromJson(req.getReader(), IssueDTO.class);

            if (issue.getNic() == null || !issue.getNic().matches("\\d{9}[Vv]")) {
                throw new ValidationException("Invalid NIC");
            } else if (issue.getIsbn() == null || !issue.getIsbn().matches("\\d+")) {
                throw new ValidationException("Invalid ISBN");
            }

            try (Connection connection = pool.getConnection()) {
                /*PreparedStatement stm = connection.prepareStatement("SELECT * FROM member WHERE nic=?");
                stm.setString(1, issue.getNic());
                if (!stm.executeQuery().next()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid NIC");
                    return;
                }

                stm = connection.prepareStatement("SELECT * FROM book WHERE isbn=?");
                stm.setString(1, issue.getIsbn());
                if (!stm.executeQuery().next()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ISBN");
                    return;
                }*/

                PreparedStatement stm = connection.prepareStatement("SELECT * FROM member INNER JOIN book WHERE nic=? AND isbn=?");
                stm.setString(1, issue.getNic());
                stm.setString(2, issue.getIsbn());
                if (!stm.executeQuery().next()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid NIC or Invalid ISBN");
                    return;
                }

                stm = connection.prepareStatement("SELECT * FROM issue WHERE isbn=?");
                stm.setString(1, issue.getIsbn());
                ResultSet rst = stm.executeQuery();
                if (rst.next()) {
                    resp.sendError(HttpServletResponse.SC_GONE, "The book has been already issued");
                    return;
                }

                /*DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                LocalDateTime now = LocalDateTime.now();*/

                issue.setDate(Date.valueOf(LocalDate.now()));

                stm = connection.prepareStatement("INSERT INTO issue (nic, isbn, date) VALUES (?,?,?)");
                stm.setString(1, issue.getNic());
                stm.setString(2, issue.getIsbn());
                stm.setDate(3, issue.getDate());
                if (stm.executeUpdate() != 1) {
                    throw new RuntimeException("Failed to record the issue details");
                }
                resp.setStatus(HttpServletResponse.SC_OK);
            } catch (SQLException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (JsonbException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON");
        } catch (ValidationException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
