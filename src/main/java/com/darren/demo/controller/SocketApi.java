package com.darren.demo.controller;

import com.darren.demo.model.Order;
import com.darren.demo.response.MessageCode;
import com.darren.demo.response.WsResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SocketApi {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;//spring websockt通过messagingTemplate可以主动向客户端推送消息


    /**
     * 向客户端推送所有订单数据
     * Scheduled 定义推送频率
     */
    @Scheduled(fixedRate = 3000,initialDelay = 3000)
    private void pushDeviceInfo(){
        WsResponse serverResponse=WsResponse.failure(MessageCode.COMMON_FAILURE);
        List<Order> orderList=new ArrayList<Order>();
        for(int i=0;i<10;i++){
            Order order=new Order();
            order.setOrderId(RandomUtils.nextLong());
            order.setUserId(RandomStringUtils.randomAlphanumeric(5));
            order.setPrice(RandomUtils.nextDouble());
            orderList.add(order);
        }
        if(!CollectionUtils.isEmpty(orderList)){
            serverResponse=WsResponse.success(orderList);
        }
        messagingTemplate.convertAndSend("/socket/topic/order_list",serverResponse);
    }

    /**
    *  客户端个性化订阅自己的实时订单,send路径(/socket/user/order/+{userId}),根据配置类，subscribe路径(/socket/user/+${userId}+/order/)
    * @param userId
    * @return void
    * @author darren
    * @date 2019-07-14 18:17
    */
    @MessageMapping("/order/{userId}")
    private void getLatestOrder(@DestinationVariable(value="userId") String  userId){
        WsResponse serverResponse=WsResponse.failure(MessageCode.COMMON_FAILURE);
        List<Order> orderList=new ArrayList<Order>();
        for(int i=0;i<10;i++){
            Order order=new Order();
            order.setOrderId(RandomUtils.nextLong());
            order.setUserId(userId);
            order.setPrice(RandomUtils.nextDouble());
            orderList.add(order);
        }
        if(!CollectionUtils.isEmpty(orderList)){
            serverResponse=WsResponse.success(orderList);
        }
        messagingTemplate.convertAndSendToUser(userId,"order",serverResponse);
    }
    
}
