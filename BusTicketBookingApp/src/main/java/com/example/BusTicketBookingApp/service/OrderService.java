package com.example.BusTicketBookingApp.service;

import com.example.BusTicketBookingApp.entity.Orders;
import java.util.List;

public interface OrderService {
    Orders saveOrder(Orders order);
    List<Orders> getAllOrders();
    Orders getOrderById(int id);
    Orders updateOrder(int id, Orders order);
    boolean deleteOrder(int id);
    int countOrdersByCategoryName(String categoryName);
    int countOrdersByUserId(int userId);
    List<Orders> getOrdersByUserId(int userId);




}
