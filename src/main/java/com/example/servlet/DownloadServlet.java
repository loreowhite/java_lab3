package com.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;


@WebServlet("/download")
public class DownloadServlet extends HttpServlet {

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

        String raw = req.getParameter("path");
        if (raw == null || raw.isBlank()) {
            resp.sendError(400, "Missing 'path' parameter");
            return;
        }
        Path requested = Path.of(raw).normalize();

        if (!requested.startsWith(userHome)) {
            resp.sendError(403, "Access denied");
            return;
        }

        File file = requested.toFile();
        if (!file.exists() || !file.isFile()) {
            resp.sendError(404, "File not found: " + requested);
            return;
        }

        String fileName = file.getName();
        String mime = getServletContext().getMimeType(fileName);
        if (mime == null) mime = "application/octet-stream";

        resp.setContentType(mime);
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        resp.setContentLengthLong(file.length());

        try (OutputStream out = resp.getOutputStream()) {
            Files.copy(file.toPath(), out);
        }
    }
}