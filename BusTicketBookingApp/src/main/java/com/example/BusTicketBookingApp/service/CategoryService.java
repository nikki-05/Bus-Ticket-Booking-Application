package com.example.BusTicketBookingApp.service;

import com.example.BusTicketBookingApp.entity.Category;

import java.util.List;

public interface CategoryService {
    Category getCategoryById(int id);
    List<Category> getAllCategories();
    Category updateCategory(int id, Category updatedCategory);
    boolean deleteCategory(int id);
    Category saveCategory(Category category);
}
