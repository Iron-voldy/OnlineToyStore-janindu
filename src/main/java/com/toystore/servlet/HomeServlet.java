package com.toystore.servlet;

import com.toystore.controller.ToyController;
import com.toystore.model.Toy;
import com.toystore.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Check if user is logged in
        if (user == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String contextPath = getServletContext().getRealPath("/");
        ToyController toyController = new ToyController(contextPath);

        // Get sort parameter
        String sortBy = request.getParameter("sortBy");
        String category = request.getParameter("category");
        String ageFilter = request.getParameter("age");
        List<Toy> toys;

        // Apply filters and sorting
        if (category != null && !category.isEmpty()) {
            toys = toyController.getToysByCategory(category);
        } else if (ageFilter != null && !ageFilter.isEmpty()) {
            try {
                int age = Integer.parseInt(ageFilter);
                toys = toyController.getToysByAgeRange(age);
            } catch (NumberFormatException e) {
                toys = toyController.getAllToys();
            }
        } else {
            toys = toyController.getAllToys();
        }

        // Apply sorting
        if ("age".equals(sortBy)) {
            toys = toyController.getAllToysSortedByAgeRange();
        } else if ("price".equals(sortBy)) {
            toys = toyController.getAllToysSortedByPrice();
        } else if ("ageAndPrice".equals(sortBy)) {
            toys = toyController.getAllToysSortedByAgeAndPrice();
        }

        // Set attributes
        request.setAttribute("toys", toys);
        request.setAttribute("categories", getUniqueCategories(toys));

        // Forward to the home page
        request.getRequestDispatcher("home.jsp").forward(request, response);
    }

    private List<String> getUniqueCategories(List<Toy> toys) {
        return toys.stream()
                .map(Toy::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());  // Use this instead of toList()
    }
}