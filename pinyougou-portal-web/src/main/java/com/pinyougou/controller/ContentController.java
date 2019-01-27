package com.pinyougou.controller;
import java.util.List;

import com.pinyougou.entity.PageResult;
import com.pinyougou.entity.Result;
import com.pinyougou.service.ContentService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbContent;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/content")
public class ContentController {

	@Reference
	private ContentService contentService;

	@RequestMapping("/findByCategoryId")
	public List<TbContent> findByCategoryId(Long categoryId){

		return contentService.findByCategoryId(categoryId);
	}
}
