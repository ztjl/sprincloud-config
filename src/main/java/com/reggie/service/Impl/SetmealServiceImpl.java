package com.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.common.CustomException;
import com.reggie.dto.SetmealDto;
import com.reggie.entity.Setmeal;
import com.reggie.entity.SetmealDish;
import com.reggie.mapper.SetmealMapper;
import com.reggie.service.SetmealDishService;
import com.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;
    /**
     * 新增套餐同时保存套餐和菜品的关联关系
     * @param setmealDto
     */
    @Transactional
    public void saveWithDist(SetmealDto setmealDto) {
        //保存套餐的基本信息
        this.save(setmealDto);
        //保存套餐he菜品的关联信息操作setmeal_dish
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);

    }

    /**
     * 删除套餐和菜品的关联关系
     * @param ids
     */
    public void removeWithDist(Long[] ids) {
        //删除套餐和菜品的关联信息操作setmeal_dish
        //查询套餐状态，确定是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(queryWrapper);
        if (count>0){
            throw new CustomException("商品正在售卖，不能删除!");
        }
        //可以删除
        //删除套餐的基本信息
        this.removeByIds(Arrays.asList(ids));
        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,0);
        setmealDishService.remove(queryWrapper1);
    }

    /**
     * 根据id查询套餐和菜品的关联关系
     * @param id
     */
    @Transactional
    public SetmealDto getByIdWithDish(Long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(true,SetmealDish::getSetmealId,id.toString());
        List<SetmealDish> setmealDishList = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(setmealDishList);
        return setmealDto;
    }

    /**
     * 新增套餐同时保存套餐和菜品的关联关系
     * @param setmealDto
     */
    @Transactional
    public void updateByIdWithDish(SetmealDto setmealDto) {
        System.out.println(setmealDto.getId());
        //修改套餐的基本信息
        this.updateById(setmealDto);
        //修改套餐和菜品的关联信息操作setmeal_dish
        //删除setmeal_dish数据
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(true,SetmealDish::getSetmealId,setmealDto.getId().toString());
        setmealDishService.remove(queryWrapper);
        //重新添加setmeal_dish表数据
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }
}
