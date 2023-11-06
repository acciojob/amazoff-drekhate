package com.driver;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class OrderRepository {
    private final Map<String, Order> orderDb = new HashMap<>();
    private final Map<String, DeliveryPartner> deliveryPartnerDb = new HashMap<>();
    private final Map<String, List<Order>> partnerOrderDb = new HashMap<>();
    private final Map<String, String> orderPartnerDb = new HashMap<>();
    public void addOrder(Order order) {
        try {
            orderDb.put(order.getId(), order);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void addPartner(String partnerId) {
        DeliveryPartner dp = new DeliveryPartner(partnerId);
        deliveryPartnerDb.put(partnerId, dp);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        Order order = orderDb.get(orderId);
        DeliveryPartner deliveryPartner = deliveryPartnerDb.get(partnerId);
        if(order == null || deliveryPartner == null) {
            return;
        }
        if(partnerOrderDb.containsKey(deliveryPartner.getId())) {
            partnerOrderDb.get(deliveryPartner.getId()).add(order);
        } else {
            List<Order> list = new ArrayList<>();
            list.add(order);
            partnerOrderDb.put(deliveryPartner.getId(), list);
        }
        orderPartnerDb.put(orderId, partnerId);
        deliveryPartner.setNumberOfOrders(deliveryPartner.getNumberOfOrders() + 1);
    }

    public Order getOrderById(String orderId) {
        return orderDb.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return deliveryPartnerDb.get(partnerId);
    }
    public Integer getOrderCountByPartnerId(String partnerId) {
        if(!partnerOrderDb.containsKey(partnerId)) {
            return 0;
        }
        List<Order> list = partnerOrderDb.get(partnerId);
        return list.size();
    }
    public List<String> getOrdersByPartnerId(String partnerId) {
        List<String> orders = new ArrayList<>();
        List<Order> list = partnerOrderDb.get(partnerId);
        for (Order curr: list) {
            orders.add(curr.getId());
        }
        return orders;
    }
    public List<String> getAllOrders() {
        List<String> list = new ArrayList<>();
        for (String key: orderDb.keySet()) {
            list.add(key);
        }
        return list;
    }
    public Integer getCountOfUnassignedOrders() {
        Integer total = orderDb.size();
        Integer countOrder = 0;
        for (String key: partnerOrderDb.keySet()) {
            countOrder += getOrderCountByPartnerId(key);
        }
        return total - countOrder;
    }
    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String deliveryTime, String partnerId) {
        String[] str = deliveryTime.split(":");
        int hour = Integer.parseInt(str[0]);
        int min = Integer.parseInt(str[1]);
        int givenTime = (hour * 60) + min;
        List<Order> list = partnerOrderDb.get(partnerId);
        Integer count = 0;
        for(Order ele: list) {
            if(givenTime < ele.getDeliveryTime()) {
                count ++;
            }
        }
        return count;
    }
    public int getLastDeliveryTimeByPartnerId(String partnerId){
        int lastTime = 0;
        List<Order> list = partnerOrderDb.get(partnerId);
        for(Order ele: list) {
            if(ele.getDeliveryTime() > lastTime) {
                lastTime = ele.getDeliveryTime();
            }
        }
        return lastTime;
    }
    public void deletePartnerById(String partnerId){
        DeliveryPartner dp = deliveryPartnerDb.get(partnerId);
        dp.setNumberOfOrders(0);

        List<Order> list = partnerOrderDb.get(partnerId);
        for (Order o: list) {
            orderPartnerDb.remove(o.getId());
        }
        deliveryPartnerDb.remove(partnerId);
        partnerOrderDb.remove(partnerId);
    }
    public void deleteOrderById(String orderId) {
        String partnerId = orderPartnerDb.get(orderId);
        Order order = orderDb.get(orderId);
        partnerOrderDb.get(partnerId).remove(order);
        deliveryPartnerDb.get(partnerId).setNumberOfOrders(deliveryPartnerDb.get(partnerId).getNumberOfOrders()-1);
        orderPartnerDb.remove(orderId);
        orderDb.remove(orderId);
    }
}
