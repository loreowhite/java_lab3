package com.example.servlet; // ← подгони под свой пакет

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@WebServlet("/files")
public class FilesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = Optional.ofNullable(req.getParameter("path")).orElse(System.getProperty("user.home"));
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));

        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            resp.sendError(404, "Path not found or not a directory: " + path);
            return;
        }

        File[] files = dir.listFiles();
        if (files == null) files = new File[0];

        req.setAttribute("now", time);
        req.setAttribute("currentPath", path);
        req.setAttribute("parent", dir.getParent());
        req.setAttribute("files", files);
        req.getRequestDispatcher("/mypage.jsp").forward(req, resp);
    }
}

