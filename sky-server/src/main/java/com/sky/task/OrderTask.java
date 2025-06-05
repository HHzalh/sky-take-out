package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 定时任务类，定时处理订单状态
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时的订单
     */
    @Scheduled(cron = "0 * * * * ?")//每分钟触发一次
    public void processTimeoutOrder() {
        log.info("定时处理超时订单:{}", LocalDateTime.now());

        //当前时间-15分钟
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);

        //下单后超过15分钟仍处于未支付则判定为超时支付订单，如果存在则修改订单状态为"已取消"
        if (ordersList != null && ordersList.size() > 0) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时,自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }

    /**
     * 处理一直处于派送中转态的订单
     */
    @Scheduled(cron = "0 0 1 * * ?")//每天凌晨1点触发一次
    public void processDeliveryOrder() {
        log.info("定时处理处于派送中的订单:{}", LocalTime.now());

        //当前时间-60分钟(上一个工作日)
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);
        //通过定时任务每天凌晨1点检查一次是否存在"派送中"的订单，如果存在则修改状态为"已完成"
        if (ordersList != null && ordersList.size() > 0) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }

}
