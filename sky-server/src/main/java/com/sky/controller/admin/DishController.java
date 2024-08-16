package com.sky.controller.admin;



import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin/dish")
@Api(tags = "dish API")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @PostMapping
    @ApiOperation("add dish")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("add dish:{}", dishDTO);
        dishService.saveWithFlavour(dishDTO);
        return Result.success();
    }


    @GetMapping("/page")
    @ApiOperation("dish page query")
    public  Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("Dish page query:{}", dishPageQueryDTO);
        PageResult pageResult =  dishService.pageQuery(dishPageQueryDTO);
        return Result.success( pageResult);

    }

    @DeleteMapping
    @ApiOperation("Delete one or more dish")
    public Result delete(@RequestParam List<Long> ids){
        log.info("Delete dishs:{}", ids);
        //deleteBatch是
        dishService.deleteBatch(ids);
        return Result.success();

    }

    //因为dishVo里面有flavor这个数据返回，而Dish里面没有，所以这里面Result我们希望返回的类型螫DishVo

    @GetMapping("/{id}")
    @ApiOperation("get dish by id")
    public Result<DishVO> getById(@PathVariable Long id){
        log.info("get dish by id",id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);

    }

    @PutMapping
    @ApiOperation("update dish data")
    public Result update(@RequestBody DishDTO dishDTO){

        log.info("update dish data:{}", dishDTO);
        dishService.updateWithFlavor(dishDTO);
        return Result.success();
    }

}
