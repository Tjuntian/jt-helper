package com.asphyxia.controller;


import com.asphyxia.pojo.Brain;
import com.asphyxia.pojo.Code;
import com.asphyxia.pojo.Result;
import com.asphyxia.service.IBrainService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author JunTian
 * @since 2023-03-22
 */
@RestController
@RequestMapping("/brain")
public class BrainController {

    @Autowired
    private IBrainService brainService;


    //增
    @PostMapping
    public Result save(@RequestBody Brain brain) {
        System.out.println("------------------"+brain);
        boolean flag = brainService.save(brain);
        return new Result(flag ? Code.SAVE_OK : Code.SAVE_ERR, null, "添加成功");
    }


    //删
    @DeleteMapping("{id}")
    public Result delete(@PathVariable Integer id) {
        boolean flag = brainService.removeById(id);
        return new Result(flag ? Code.DELETE_OK : Code.DELETE_ERR, null, "删除成功");
    }

    //查单个
    @GetMapping("/one/{id}")
    public Result getById(@PathVariable("id") Integer id) {
        Brain brain = brainService.getById(id);
        Integer code = brain != null ? Code.GET_OK : Code.GET_ERR;
        return  new Result(code,brain,"查询成功");
    }

    //条件查询
    @GetMapping("/like/{name}")
    public Result findLike(@PathVariable("name") String name) {
        LambdaQueryWrapper<Brain> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(Brain::getName, name);
        BaseMapper<Brain> baseMapper = brainService.getBaseMapper();
        List<Brain> brains = baseMapper.selectList(lambdaQueryWrapper);
        Integer code = brains != null ? Code.GET_OK : Code.GET_ERR;
        return new Result(code, brains, "查询成功");
    }

    //改
    @PutMapping
    public Result update(@RequestBody Brain brain){
        boolean flag = brainService.updateById(brain);
        return new Result(flag?Code.UPDATE_OK:Code.UPDATE_ERR,null,"修改成功");
    }
}

