package com.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            resp.sendRedirect(req.getContextPath() + "/files");
            return;
        }
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String login = req.getParameter("login");
        String password = req.getParameter("password");

        if (login == null || login.isBlank() || password == null || password.isBlank()) {
            req.setAttribute("error", "Введите логин и пароль");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        try {
            if (UserRepository.checkCredentials(login, password)) {
                HttpSession session = req.getSession(true);
                session.setAttribute("user", login);
                resp.sendRedirect(req.getContextPath() + "/files");
            } else {
                req.setAttribute("error", "Неверный логин или пароль");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            }
        } catch (RuntimeException e) {
            req.setAttribute("error", "Ошибка авторизации: " + e.getMessage());
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}