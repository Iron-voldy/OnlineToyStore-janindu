package com.toystore.servlet;

import com.toystore.controller.UserController;
import com.toystore.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserController userController;

    @Override
    public void init() throws ServletException {
        String contextPath = getServletContext().getRealPath("/");
        userController = new UserController(contextPath);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Validate parameters
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Username and password are required");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }

        // Authenticate the user
        User user = userController.authenticate(username, password);

        if (user != null) {
            // Create a new session and store the user
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            // Redirect to the home page
            response.sendRedirect("home");
        } else {
            // Authentication failed
            request.setAttribute("error", "Invalid username or password");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
}