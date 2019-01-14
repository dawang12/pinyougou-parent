package com.itheima.sellergoods.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.sellergoods.service.BrandService;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.entity.PageResult;
import com.pinyougou.pojo.TbBrandExample;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private TbBrandMapper brandMapper;

    @Override
    public List<TbBrand> findAll() {
        return brandMapper.selectByExample(null);
    }

   @Override
    public PageResult findPage(TbBrand tbBrand,int pageNum, int pageSize) {

        PageHelper.startPage(pageNum,pageSize);

        TbBrandExample tbBrandExample = new TbBrandExample();

        TbBrandExample.Criteria criteria = tbBrandExample.createCriteria();

        if (tbBrand !=null){

           if (tbBrand.getName()!=null && tbBrand.getName().length()>0){

               criteria.andNameLike("%"+tbBrand.getName()+"%");
           }

           if (tbBrand.getFirstChar()!=null && tbBrand.getFirstChar().length()>0){
               criteria.andFirstCharEqualTo(tbBrand.getFirstChar());
           }

       }

      Page<TbBrand> list = (Page<TbBrand>) brandMapper.selectByExample(tbBrandExample);

       return new PageResult(list.getTotal(),list.getResult());
    }

    @Override
    public void add(TbBrand tbBrand) {
        brandMapper.insert(tbBrand);
    }

    @Override
    public void update(TbBrand tbBrand) {
        brandMapper.updateByPrimaryKey(tbBrand);
    }

    @Override
    public TbBrand findOne(Long id) {
       return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id: ids){
            brandMapper.deleteByPrimaryKey(id);
        }
    }


}
