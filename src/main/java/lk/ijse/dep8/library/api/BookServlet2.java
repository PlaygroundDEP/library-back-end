package lk.ijse.dep8.library.api;

import lk.ijse.dep8.library.dto.BookDTO;
import lk.ijse.dep8.library.exception.ValidationException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Path;

@MultipartConfig(location = "/tmp", maxFileSize = 15 * 1024 * 1024)
@WebServlet(name = "BookServlet2", value = "/v2/books/")
public class BookServlet2 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doSaveOrUpdate(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doSaveOrUpdate(req, resp);
    }

    private void doSaveOrUpdate(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        if (req.getContentType() == null || !req.getContentType().toLowerCase().startsWith("multipart/form-data")) {
            res.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            return;
        }
        String method = req.getMethod();
        String pathInfo = req.getPathInfo();

        if (method.equals("POST") && (pathInfo != null && !pathInfo.equals("/"))) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } else if (method.equals("PUT") && !(pathInfo != null &&
                pathInfo.substring(1).matches("\\d{10,13}[/]?"))){
            res.sendError(HttpServletResponse.SC_NOT_FOUND, "Book does not exist");
            return;
        }

        try {
            String isbn = req.getParameter("isbn");
            String name = req.getParameter("name");
            String author = req.getParameter("author");
            Part preview = req.getPart("preview");

            BookDTO book;

            if (preview!=null && !preview.getSubmittedFileName().isEmpty()){
                if (!preview.getContentType().toLowerCase().startsWith("image/")) {
                    throw new ValidationException("Invalid preview");
                }

                byte[] buffer = new byte[(int) preview.getSize()];
                preview.getInputStream().read(buffer);

                book = new BookDTO(isbn, name, author, buffer);
            } else {
                book = new BookDTO(isbn, name, author);
            }


            if (method.equals("POST") && (book.getIsbn() == null || !book.getIsbn().matches("\\d{10,13}"))) {
                throw new ValidationException("Invalid ISBN");
            } else if (book.getName() == null || !book.getName().matches(".+")) {
                throw new ValidationException("Invalid Name");
            } else if (book.getAuthor() == null || !book.getAuthor().matches("[A-Za-z0-9 ]+")) {
                throw new ValidationException("Invalid Author");
            }

            if (method.equals("PUT")) {
                book.setIsbn(pathInfo.replaceAll("[/]", ""));
            }
        } catch (ValidationException e) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } catch (Throwable t) {
            t.printStackTrace();
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
    }
}
