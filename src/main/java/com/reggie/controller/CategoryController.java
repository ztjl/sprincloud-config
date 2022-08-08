package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.common.Result;
import com.reggie.entity.Category;
import com.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品分类
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public Result<String> add(@RequestBody Category category){
        log.info("category:{}",category);
        categoryService.save(category);
        return Result.success("添加成功");
    }


    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public Result<Page> Page(int page,int pageSize){
        //构造分页构造器
        Page<Category> pageInfo = new Page<>(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        ////添加排序条件
        queryWrapper.orderByAsc(Category::getSort);
        //执行查询
        categoryService.page(pageInfo,queryWrapper);
        return Result.success(pageInfo);
    }

    /**
     * 根据id删除分类
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> delete(Long ids){
        //categoryService.removeById(id);
        categoryService.remove(ids);
        return Result.success("删除成功！");
    }

    /**
     * 根据id修改
     * @param category
     * @return
     */

    @PutMapping
    public Result<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return Result.success("修改分类信息成功");
    }

    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public Result<List<Category>> list(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        //调价排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return Result.success(list);

    }





}
