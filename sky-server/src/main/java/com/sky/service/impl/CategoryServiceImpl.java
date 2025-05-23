package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 新增分类
     *
     * @param categoryDTO
     * @return
     */
    public void addCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        //对象属性拷贝
        BeanUtils.copyProperties(categoryDTO, category);
        //默认禁用状态
        category.setStatus(StatusConstant.DISABLE);
        //已通过AOP切面统一对公共字段进行赋值，以提高代码复用性和可维护性

        /*设置当前记录的创建时间和修改时间
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        设置创建人id和修改人id
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateUser(BaseContext.getCurrentId());*/
        categoryMapper.insert(category);
    }

    /**
     * 分类分页查询
     *
     * @param categoryPageQueryDTO
     * @return
     */
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        //开始分页查询
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        Page<Category> page = categoryMapper.pageQuery(categoryPageQueryDTO);
        long total = page.getTotal();
        List<Category> records = page.getResult();
        return new PageResult(total, records);
    }

    /**
     * 启用、禁用分类
     *
     * @param status
     * @param id
     * @return
     */
    public void startOrStop(Integer status, Long id) {
        Category category = Category.builder().
                status(status).
                id(id).
                /*updateUser(BaseContext.getCurrentId()).
                updateTime(LocalDateTime.now()).*/
                        build();
        //已通过AOP切面统一对公共字段进行赋值，以提高代码复用性和可维护性
        categoryMapper.update(category);
    }

    /**
     * 根据id删除分类
     *
     * @param id
     * @return
     */
    public void deleteById(Long id) {
        //查询当前分类是否关联了菜品，如果关联了就抛出业务异常
        Integer count = dishMapper.countByCategoryId(id);
        if (count > 0) {
            //当前分类下有菜品，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }
        //查询当前分类是否关联了套餐,如果关联了就抛出业务异常
        count = setmealMapper.countByCategoryId(id);
        if (count > 0) {
            //当前分类下有菜品，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }
        categoryMapper.delete(id);
    }

    /**
     * 修改分类
     *
     * @param categoryDTO
     * @return
     */
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        //对象属性拷贝
        BeanUtils.copyProperties(categoryDTO, category);

        //已通过AOP切面统一对公共字段进行赋值，以提高代码复用性和可维护性

        /*category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.getCurrentId());*/
        categoryMapper.update(category);
    }

    /**
     * 根据类型查询分类
     *
     * @param type
     * @return
     */
    public List<Category> list(Integer type) {
        return categoryMapper.list(type);
    }


}
