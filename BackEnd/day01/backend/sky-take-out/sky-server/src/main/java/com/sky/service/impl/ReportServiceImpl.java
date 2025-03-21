package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
    // list for storing begin to end date
        List<LocalDate> dateList = new ArrayList<>();

        dateList.add(begin);

        while(!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }


        List<Double> turnoverList = new ArrayList<>();

        for (LocalDate date : dateList) {
            //get turnover from date
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            // GET sum amount from order where order_time < ? and order_time< ? and status = 5
            Map map =  new HashMap<>();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);
            Double turnover = orderMapper.sumByMap(map);
            turnover = turnover == null? 0.0: turnover;
            turnoverList.add(turnover);
        }

        return TurnoverReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }


    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end){

        List<LocalDate> dateList = new ArrayList<>();

        dateList.add(begin);

        while(!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        List<Integer> newUserList =  new ArrayList<>();
        List<Integer> totalUserList =  new ArrayList<>();

        for (LocalDate date : dateList) {
            //get turnover from date
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            // GET sum amount from order where order_time < ? and order_time< ? and status = 5
            Map map =  new HashMap<>();

            map.put("begin", beginTime);
            Integer newUser = userMapper.countByMap(map);

            map.put("end", endTime);
            Integer totalUser = userMapper.countByMap(map);

            newUserList.add(newUser);
            totalUserList.add(totalUser);

        }


        return UserReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .newUserList(StringUtils.join(newUserList,","))
                .build();
    }



    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end){

        // date
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while(!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        List<Integer> orderCountList =  new ArrayList<>();
        List<Integer> validOrderCountList  =  new ArrayList<>();

        for (LocalDate date : dateList) {
            //get turnover from date
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            // GET total order !!per day!! select count(id) from order where order_time < ? and order_time< ?
            Integer orderCount = getOrderCount(beginTime, endTime, null);
            orderCountList.add(orderCount);

            // GET total order per day  select count(id) from order where order_time < ? and order_time< ?  and status = 5
            Integer validOrderCount = getOrderCount(beginTime, endTime,Orders.COMPLETED);
            validOrderCountList.add(validOrderCount);
        }
        //caculate total order
        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
        // caculate valid order
        Integer totalValidOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();


        Double orderCompletionRate = 0.0;
        if(totalOrderCount != 0){
          orderCompletionRate =  totalValidOrderCount.doubleValue() / totalOrderCount;
        }


        return OrderReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(validOrderCountList,","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(totalValidOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();

    };

    private Integer getOrderCount(LocalDateTime begin,LocalDateTime end, Integer status ){
        Map map =  new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        map.put("status", status);

        return orderMapper.countByMap(map);

    }

    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end){

        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        List<GoodsSalesDTO> salesTop10 = orderMapper.getSalesTop10(beginTime, endTime);
        List<String> names = salesTop10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        String nameList = StringUtils.join(names, ",");

        List<Integer> numbers = salesTop10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());
        String numberList = StringUtils.join(numbers, ",");

        return SalesTop10ReportVO
                .builder()
                .nameList(nameList)
                .numberList(numberList)
                .build();

    }

    
}
