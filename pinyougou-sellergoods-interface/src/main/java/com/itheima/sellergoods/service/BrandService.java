package com.itheima.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import com.pinyougou.entity.PageResult;

import java.util.List;
import java.util.Map;

//品牌接口
public interface BrandService {

    public List<TbBrand> findAll();

    public PageResult findPage(TbBrand tbBrand,int pageNum, int pageSize);

    public void add(TbBrand tbBrand);

    public void update(TbBrand tbBrand);

    public TbBrand findOne(Long id);

    public void delete(Long[] ids);

    public List<Map> selectOptionList();
}
