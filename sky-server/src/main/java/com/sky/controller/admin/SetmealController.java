package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 套餐管理
 */
@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "套餐相关接口")
public class SetmealController {

    @Autowired
    SetmealService setmealService;
    @Autowired
    SetmealMapper setmealMapper;


    /**
     * 套餐分页查询
     *
     * @param setmealPageQueryDTO
     * @return
     */
    @ApiOperation("套餐分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        //log.info("套餐分页查询:{}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 新增套餐
     *
     * @param setmealDTO
     * @return
     */
    @ApiOperation("新增套餐")
    @PostMapping()
    public Result save(@RequestBody SetmealDTO setmealDTO) {
        //log.info("新增套餐:{}", setmealDTO);
        setmealService.saveWithSetmealDishes(setmealDTO);
        return Result.success();
    }

    /**
     * 根据id查询套餐及关联菜品
     *
     * @param id
     * @return
     */
    @ApiOperation("根据id查询套餐")
    @GetMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        //log.info("根据id查询套餐:{}",id);
        SetmealVO setmealVO = setmealService.getByIdWithDishes(id);
        return Result.success(setmealVO);
    }

    /**
     * 修改套餐
     *
     * @param setmealDTO
     * @return
     */
    @ApiOperation("修改套餐")
    @PutMapping()
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        //log.info("修改套餐:{}", setmealDTO);
        setmealService.updateWithSetmealDishes(setmealDTO);
        return Result.success();
    }

    /**
     * 套餐起售、停售
     *
     * @param status
     * @param id
     * @return
     */
    @ApiOperation("套餐起售、停售")
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        //log.info("套餐起售、停售:{},{}", status, id);
        setmealService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * 套餐批量删除
     *
     * @param ids
     * @return
     */
    @ApiOperation("套餐批量删除")
    @DeleteMapping()
    public Result delete(@RequestParam List<Long> ids) {
        setmealService.deleteBatch(ids);
        return Result.success();
    }
}
