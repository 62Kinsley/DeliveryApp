package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
//@Api(tags=)
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }


  @PostMapping
  @ApiOperation("add new staff")
    public Result save(@RequestBody EmployeeDTO employeeDTO){

        log.info("new staff:{}", employeeDTO);
        System.out.println("Current thread ID:" + Thread.currentThread().getId());
        employeeService.save(employeeDTO);
        return Result.success();
    }


    @GetMapping("/page")
    @ApiOperation("Employee Paginated Query")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("Employee Paginated Query, parameter is: {}",employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }


    @PostMapping("/status/{status}")
    @ApiOperation("startOrStop staff acount")
    public Result startOrStop(@PathVariable Integer status, Long id){
        log.info("startOrStop staff acount:{},{}", status, id);
        employeeService.startOrStop(status,id);
        return Result.success();

    }

   //Query Employee Information by ID
    @GetMapping("/{id}")
    @ApiOperation("Query Employee Information by ID")
    public Result<Employee> getById(@PathVariable Long id){
         Employee employee = employeeService.getById(id);
         return Result.success(employee);
    }


    //Edit employee information
    @PutMapping
    @ApiOperation("Edit employee information")
    public Result update(@RequestBody EmployeeDTO employeeDTO){
        log.info("Edit employee information:{}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }




}
