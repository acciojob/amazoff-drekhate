package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    public void addOrder(Order order) {
        orderRepository.addOrder(order);
    }
    public void addPartner(String partnerId){
        orderRepository.addPartner(partnerId);
    }
    public void addOrderPartnerPair(String orderId, String partnerId) {
        orderRepository.addOrderPartnerPair(orderId, partnerId);
    }
    public Order getOrderById(String orderId) {
        return orderRepository.getOrderById(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return orderRepository.getPartnerById(partnerId);
    }
    public Integer getOrderCountByPartnerId(String partnerId) {
        return orderRepository.getOrderCountByPartnerId(partnerId);
    }
    public List<String> getOrdersByPartnerId(String partnerId) {
        return orderRepository.getOrdersByPartnerId(partnerId);
    }
    public List<String> getAllOrders() {
        return orderRepository.getAllOrders();
    }
    public Integer getCountOfUnassignedOrders() {
        return orderRepository.getCountOfUnassignedOrders();
    }
    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String deliveryTime, String partnerId) {
        return orderRepository.getOrdersLeftAfterGivenTimeByPartnerId(deliveryTime, partnerId);
    }
    public String getLastDeliveryTimeByPartnerId(String partnerId){
        int time = orderRepository.getLastDeliveryTimeByPartnerId(partnerId);
        int h = time / 60;
        int m = time % 60;

        String hh = "";
        String mm = "";
        if (m >= 0 && m <=9) {
            mm = "0" + String.valueOf(m);
        }
        else {
            mm = String.valueOf(m);
        }

        if (h >= 0 && h <=9) {
            hh = "0" + String.valueOf(h);
        }
        else {
            hh = String.valueOf(h);
        }
        return hh + ":" + mm;
    }
    public void deletePartnerById(String partnerId) {
        orderRepository.deletePartnerById(partnerId);
    }

    public void deleteOrderById(String orderId) {
        orderRepository.deleteOrderById(orderId);
    }
}
