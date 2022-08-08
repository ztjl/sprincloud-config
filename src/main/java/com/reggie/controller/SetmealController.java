package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.common.Result;
import com.reggie.dto.SetmealDto;
import com.reggie.entity.Setmeal;
import com.reggie.service.SetmealDishService;
import com.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 套餐管理
 */
@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public Result<Page> Page(int page,int pageSize){
        Page<Setmeal> setmealPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Setmeal::getUpdateTime);
        setmealService.page(setmealPage);
        return Result.success(setmealPage);
    }

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDist(setmealDto);
        return Result.success("添加成功");
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> delete(Long[] ids){
        setmealService.removeWithDist(ids);
        return Result.success("删除成功!");
    }

    /**
     * 根据id查询套餐和菜品的关联关系
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<SetmealDto> getByIdWithDish(@PathVariable Long id){
        SetmealDto setmealDto = setmealService.getByIdWithDish(id);
        return Result.success(setmealDto);
    }

    /**
     * 根据id状态更新
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> status(@PathVariable int status,Long[] ids){
        for (Long id : ids) {
            Setmeal setmeal = setmealService.getById(id);
            setmeal.setStatus(status);
            setmealService.updateById(setmeal);
        }
        return Result.success("状态更新成功");
    }

    /**
     * 修改套餐信息
     * @param setmealDto
     * @return
     */
    @PutMapping
    public Result<String> updateByIdWithDish(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());
        setmealService.updateByIdWithDish(setmealDto);
        return Result.success("修改成功");
    }







}
