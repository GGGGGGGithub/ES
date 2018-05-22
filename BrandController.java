package com.pinyougou.manager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import myutils.missage.Result;
import myutils.page.PageResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by Dreamer on 2018/3/26.
 */
@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference
    private BrandService brandService;

    @RequestMapping("/brandlist")
    public List<TbBrand> findAll(){
        return brandService.findAll();
    }

    /**
     * 分页查询品牌列表
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("findPage")
    public PageResult findPage(int page, int rows){
        return brandService.findPage(page,rows);
    }

    /**
     * 添加品牌
     * @param tbBrand
     * @return
     */
    @RequestMapping("/save")
    public Result save(@RequestBody TbBrand tbBrand){
        try {
            brandService.add(tbBrand);
            return new Result(true,"添加成功!");
        }catch (Exception e){
            return new Result(false,"添加失败!");
        }
    }

    @RequestMapping("/findOne")
    public TbBrand findOne(Long id){
        return brandService.findOne(id);
    }

    /**
     * 修改
     * @param tbBrand
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody TbBrand tbBrand){
        try {
            brandService.update(tbBrand);
            return new Result(true,"修改成功!");
        }catch (Exception e){
            return new Result(false,"修改失败!");
    }
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Long[] ids){
        try {
            brandService.delete(ids);
            return new Result(true,"删除成功!");
        }catch (Exception e){
            return new Result(false,"删除失败!");
        }

    }
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbBrand tbBrand, int page, int rows){
        return brandService.findPage(tbBrand,page,rows);
    }

    @RequestMapping("/selectBrandList")
    public List<Map> selectBrandList() {
        return brandService.selectBrandList();
    }
}
