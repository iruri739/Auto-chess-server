package com.accenture.huaweigroup.controller;


import com.accenture.huaweigroup.module.entity.Chess;
import com.accenture.huaweigroup.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/Admin")
@Api(value = "管理员逻辑", tags = "管理员逻辑接口")
public class AdminController {

    @Autowired
    AdminService adminService;
    @ApiOperation(value = "获取所有卡牌", notes = "获取所有卡牌", httpMethod = "GET")
    @GetMapping("/getAll")
    public List<Chess> getAll()
    {
        return adminService.allChess();
    }

    @ApiOperation(value = "插入卡牌", notes = "插入卡牌", httpMethod = "POST")
    @PostMapping("/insertChess")
    public void insertChess(@RequestBody Chess chess)
    {
         adminService.insertChess(chess);
    }

    @ApiOperation(value = "删除卡牌", notes = "删除卡牌", httpMethod = "Get")
    @GetMapping("/deleteCard")
    public void deleteCard(@RequestParam("chess_id") int chess_id)
    {
        adminService.deleteChess(chess_id);    }







}
