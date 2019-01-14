package com.itheima.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import com.pinyougou.entity.PageResult;

import java.util.List;

//品牌接口
public interface BrandService {

    public List<TbBrand> findAll();

    public PageResult findPage(int pageNum, int pageSize);
}
