package com.itheima.sellergoods.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itheima.sellergoods.service.GoodsService;
import com.pinyougou.entity.PageResult;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import com.pinyougou.pojogroup.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.pojo.TbGoodsExample.Criteria;
import org.springframework.transaction.annotation.Transactional;


/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbBrandMapper brandMapper;

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Autowired
    private TbSellerMapper sellerMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbGoods> findAll() {
        return goodsMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(Goods goods) {
        goods.getGoods().setAuditStatus("0");        //设置未申请状态
        goodsMapper.insert(goods.getGoods());
        goods.getGoodsDesc().setGoodsId(goods.getGoods().getId());//设置id
        goodsDescMapper.insert(goods.getGoodsDesc());

        saveItemList(goods);
    }

    private void setItemValus(Goods goods,TbItem item){

        item.setGoodsId(goods.getGoods().getId());//商品spu编号
        item.setSellerId(goods.getGoods().getSellerId());//商家编号
        item.setCategoryid(goods.getGoods().getCategory3Id());//商品分类编号
        item.setCreateTime(new Date());//创建日期
        item.setUpdateTime(new Date());//修改日期

        TbBrand brand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
        item.setBrand(brand.getName());

        TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(goods.getGoods().getCategory3Id());
        item.setCategory(itemCat.getName());

        TbSeller seller = sellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId());
        item.setSellerId(seller.getNickName());

        List<Map> mapList = JSON.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);
        if (mapList.size() > 0) {
            item.setImage((String) mapList.get(0).get("url"));
        }

    }


    /**
     * 修改
     */
    @Override
    public void update(Goods goods) {

        goods.getGoods().setAuditStatus("0");

        goodsMapper.updateByPrimaryKey(goods.getGoods());

        goodsDescMapper.updateByPrimaryKey(goods.getGoodsDesc());

        //删除原有的sku列表数据
        TbItemExample example = new TbItemExample();

        TbItemExample.Criteria criteria = example.createCriteria();

        criteria.andGoodsIdEqualTo(goods.getGoods().getId());

        itemMapper.deleteByExample(example);


        saveItemList(goods);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public Goods findOne(Long id) {

        Goods goods = new Goods();

        TbItemExample example = new TbItemExample();

        TbItemExample.Criteria criteria = example.createCriteria();

        criteria.andGoodsIdEqualTo(id);

        List<TbItem> itemList = itemMapper.selectByExample(example);

        goods.setItemList(itemList);

        return goods;
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {

            TbGoods goods = goodsMapper.selectByPrimaryKey(id);

            goods.setIsDelete("1");
            goodsMapper.updateByPrimaryKey(goods);
        }
    }


    @Override
    public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbGoodsExample example = new TbGoodsExample();
        Criteria criteria = example.createCriteria();

        criteria.andIsDeleteIsNull();//非删除状态
        if (goods != null) {
            if (goods.getSellerId() != null && goods.getSellerId().length() > 0) {
                //criteria.andSellerIdLike("%" + goods.getSellerId() + "%");
                criteria.andSellerIdEqualTo(goods.getSellerId());
            }
            if (goods.getGoodsName() != null && goods.getGoodsName().length() > 0) {
                criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");
            }
            if (goods.getAuditStatus() != null && goods.getAuditStatus().length() > 0) {
                criteria.andAuditStatusLike("%" + goods.getAuditStatus() + "%");
            }
            if (goods.getIsMarketable() != null && goods.getIsMarketable().length() > 0) {
                criteria.andIsMarketableLike("%" + goods.getIsMarketable() + "%");
            }
            if (goods.getCaption() != null && goods.getCaption().length() > 0) {
                criteria.andCaptionLike("%" + goods.getCaption() + "%");
            }
            if (goods.getSmallPic() != null && goods.getSmallPic().length() > 0) {
                criteria.andSmallPicLike("%" + goods.getSmallPic() + "%");
            }
            if (goods.getIsEnableSpec() != null && goods.getIsEnableSpec().length() > 0) {
                criteria.andIsEnableSpecLike("%" + goods.getIsEnableSpec() + "%");
            }
            if (goods.getIsDelete() != null && goods.getIsDelete().length() > 0) {
                criteria.andIsDeleteLike("%" + goods.getIsDelete() + "%");
            }

        }

        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void updateStatus(Long[] ids, String status) {

        for (Long id:ids){

            TbGoods goods = goodsMapper.selectByPrimaryKey(id);

            goods.setAuditStatus(status);

            goodsMapper.updateByPrimaryKey(goods);
        }
    }

    private void saveItemList(Goods goods){
        if ("1".equals(goods.getGoods().getIsEnableSpec())) {
            for (TbItem item : goods.getItemList()) {
                String title = goods.getGoods().getGoodsName();
                Map<String, Object> map = JSON.parseObject(item.getSpec());

                for (String key : map.keySet()) {
                    title = " " + map.get(key);
                }
                item.setTitle(title);
                setItemValus(goods,item);
                itemMapper.insert(item);
            }
        }else {
            TbItem item = new TbItem();
            item.setTitle(goods.getGoods().getGoodsName());//商品kpu+规格描述串作为sku名称
            item.setPrice(goods.getGoods().getPrice());
            item.setStatus("1");
            item.setIsDefault("1");
            item.setNum(99999);
            item.setSpec("{}");
            setItemValus(goods,item);
            itemMapper.insert(item);
        }
    }


    @Override
    public List<TbItem> findItemListByGoodsIdandStatus(Long[] goodsIds, String status) {
        TbItemExample example=new TbItemExample();
        com.pinyougou.pojo.TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdIn(Arrays.asList(goodsIds));
        criteria.andStatusEqualTo(status);
        return itemMapper.selectByExample(example);
    }

}
