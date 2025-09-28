package com.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


@WebServlet("/files")
public class FilesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        String login = (String) session.getAttribute("user");
        Path userHome = AppConfig.HOMES_ROOT.resolve(login).normalize();
        Files.createDirectories(userHome);

        String raw = Optional.ofNullable(req.getParameter("path")).orElse(userHome.toString());
        Path requested = Path.of(raw).normalize();

        if (!requested.startsWith(userHome)) {
            requested = userHome;
        }

        File dir = requested.toFile();
        if (!dir.exists() || !dir.isDirectory()) {
            resp.sendError(404, "Path not found or not a directory: " + requested);
            return;
        }
        File[] files = dir.listFiles();
        if (files == null) files = new File[0];

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        req.setAttribute("now", time);
        req.setAttribute("currentPath", dir.getAbsolutePath());
        req.setAttribute("parent", userHome.equals(dir.toPath()) ? null : dir.getParent());
        req.setAttribute("files", files);
        req.setAttribute("username", login);
        req.getRequestDispatcher("/mypage.jsp").forward(req, resp);
    }
}