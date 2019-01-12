package com.itheima.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import entity.PageResult;

import java.util.List;

//品牌接口
public interface BrandService {

    public List<TbBrand> findAll();

    public PageResult findPage(int page, int size);
}
