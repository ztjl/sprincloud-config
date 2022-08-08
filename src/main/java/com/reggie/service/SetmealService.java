package com.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.dto.SetmealDto;
import com.reggie.entity.Setmeal;
import com.reggie.entity.SetmealDish;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐同时保存套餐和菜品的关联关系
     * @param setmealDto
     */
    void saveWithDist(SetmealDto setmealDto);

    /**
     * 删除套餐和菜品的关联关系
     * @param ids
     */
    void removeWithDist(Long[] ids);

    /**
     * 根据id查询套餐和菜品的关联关系
     * @param id
     */
    SetmealDto getByIdWithDish(Long id);

    /**
     *修改套餐同时保存套餐和菜品的关联关系
     */
    void updateByIdWithDish(SetmealDto setmealDto);
}
