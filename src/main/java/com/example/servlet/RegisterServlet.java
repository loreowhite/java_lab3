package com.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
            if (UserRepository.findByLogin(login) != null) {
                req.setAttribute("error", "Такой логин уже используется");
                req.getRequestDispatcher("/register.jsp").forward(req, resp);
                return;
            }

            UserRepository.save(new User(login, password, email));

            Path userHome = AppConfig.HOMES_ROOT.resolve(login);
            Files.createDirectories(userHome);

            HttpSession session = req.getSession(true);
            session.setAttribute("user", login);
            resp.sendRedirect(req.getContextPath() + "/files");

        } catch (RuntimeException e) {
            req.setAttribute("error", "Ошибка при сохранении: " + e.getMessage());
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }
    }
}