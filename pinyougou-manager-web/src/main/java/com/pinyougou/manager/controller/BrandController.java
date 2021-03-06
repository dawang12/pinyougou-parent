package com.pinyougou.manager.controller;

import java.util.List;
import java.util.Map;

import com.itheima.sellergoods.service.BrandService;
import com.pinyougou.entity.PageResult;
import com.pinyougou.entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;


@RestController
@RequestMapping("/brand")
public class BrandController {

	@Reference
	private BrandService brandService;
	
	@RequestMapping("/findAll")
	public List<TbBrand> findAll(){
		return brandService.findAll();		
	}

	@RequestMapping("/findPage")
	public PageResult findPage(TbBrand tbBrand,Integer page, Integer size){
		return brandService.findPage(tbBrand,page, size);
	}
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbBrand tbBrand, Integer page,Integer size){

		return 	brandService.findPage(tbBrand,page,size);
	}

	@RequestMapping("/add")
	public Result add(@RequestBody TbBrand tbBrand){
		try {
			brandService.add(tbBrand);
			return new Result(true,"添加成功");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"添加失败");
		}
	}

	@RequestMapping("/update")
	public Result update(@RequestBody TbBrand tbBrand){
		try {
			brandService.update(tbBrand);
			return new Result(true,"修改成功");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"修改失败");
		}
	}

	@RequestMapping("/findOne")
	public TbBrand findOne(Long id){

		return 	brandService.findOne(id);
	}

	@RequestMapping("/delete")
	public Result delete(Long[] ids){
		try {
			brandService.delete(ids);
			return new Result(true,"删除成功");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"删除失败");
		}

	}
	@RequestMapping("/selectOptionList")
	public List<Map> selectOptionList(){
		return brandService.selectOptionList();
	}
}
