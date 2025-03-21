package com.sky.controller.user;


import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "Client end API")

public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    @ApiOperation("add shopping cart")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO){

        log.info("add shopping cart, and the shop message is:{}" , shoppingCartDTO);
        shoppingCartService.addShoppingCart(shoppingCartDTO);

        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("search shoppingcart")
    public  Result<List<ShoppingCart>> list(){

    List<ShoppingCart> list = shoppingCartService.showShoppingcart();
        return Result.success(list);

    }


    @DeleteMapping("/clean")
    @ApiOperation("delete shoppingcart")
    public Result clean(){
        shoppingCartService.cleanShoppingCart();
        return  Result.success();

    }
}
