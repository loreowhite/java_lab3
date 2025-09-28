package com.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String email = req.getParameter("email");

        if (login == null || login.isBlank()
                || password == null || password.isBlank()
                || email == null || email.isBlank()) {
            req.setAttribute("error", "Все поля обязательны");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        try {
            if (UserDao.find(login) != null) {
                req.setAttribute("error", "Такой логин уже используется");
                req.getRequestDispatcher("/register.jsp").forward(req, resp);
                return;
            }

            User user = new User(login, password, email);
            UserDao.save(user);

            Path userHome = AppConfig.HOMES_ROOT.resolve(login);
            Files.createDirectories(userHome);

            HttpSession session = req.getSession(true);
            session.setAttribute("user", login);
            resp.sendRedirect(req.getContextPath() + "/files");

        } catch (SQLException e) {
            req.setAttribute("error", "Ошибка БД: " + e.getMessage());
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }
    }
}