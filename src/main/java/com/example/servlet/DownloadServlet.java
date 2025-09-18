package com.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

@WebServlet("/download")
public class DownloadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String raw = req.getParameter("path");
        if (raw == null || raw.isBlank()) {
            resp.sendError(400, "Missing 'path' parameter");
            return;
        }

        File file = new File(raw);
        if (!file.exists() || !file.isFile()) {
            resp.sendError(404, "File not found: " + raw);
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
