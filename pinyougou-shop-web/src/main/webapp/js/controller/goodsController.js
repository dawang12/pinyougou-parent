 //控制层 
app.controller('goodsController' ,function($scope,$controller,$location,goodsService,uploadService,itemCatService,typeTemplateService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(){

		var id = $location.search()['id'];

		if (id==null){

			return ;
		}

		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;

				editor.html($scope.entity.goodsDesc.introduction);

				$scope.entity.goodsDesc.itemImages=JSON.parse($scope.entity.goodsDesc.itemImages);

				$scope.entity.goodsDesc.customAttributeItems=JSON.parse($scope.entity.goodsDesc.customAttributeItems);

				$scope.entity.goodsDesc.specificationItems=JSON.parse($scope.entity.goodsDesc.specificationItems);
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	//增加商品 
	$scope.add=function(){	
		//将富文本的数据设置给entity已便提交给服务器
		$scope.entity.goodsDesc.introduction=editor.html();
		
		goodsService.add( $scope.entity  ).success(
			function(response){
				if(response.success){
					alert("保存成功");
					$scope.entity={};
					//$scope.entity={goodsDesc:{itemImages:[],specificationItems:[]} };//在图片和规格的时候需要初始化出来itemImages和specificationItems，否则再次操作时会找不到
					editor.html("");//清空富文本编辑器
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//上传图片
	$scope.uploadFile=function(){

		uploadService.uploadFile().success(
			function(response){
				alert(response.success);
				if(response.success){
					$scope.image_entity.url= response.message;
				}else{
					alert(response.message);					
				}
			}		
		).error(function () {
            alert("上传发生错误")
        });
	}
    
    $scope.entity={ goodsDesc:{itemImages:[],specificationItems:[]}  };
	
	//将当前上传的图片实体存入图片列表
	$scope.add_image_entity=function(){
		$scope.entity.goodsDesc.itemImages.push($scope.image_entity);			
	}
	
	//移除图片
	$scope.remove_image_entity=function(index){
		$scope.entity.goodsDesc.itemImages.splice(index,1);
	}
	
	//查询一级商品分类列表
	$scope.selectItemCat1List=function(){
	
		itemCatService.findByParentId(0).success(
			function(response){
				$scope.itemCat1List=response;			
			}
		);
	}
	
	//查询二级商品分类列表
	//$watch ： 监听变量的操作,参数1：监控的变量，参数2：变量变化的时候调用的方法  newValue：新值，oldValue：旧值
	$scope.$watch('entity.goods.category1Id',function(newValue,oldValue){
		
		itemCatService.findByParentId(newValue).success(
				function(response){
					$scope.itemCat2List=response;			
				}
		);
	});
	
	//查询三级商品分类列表
	$scope.$watch('entity.goods.category2Id',function(newValue,oldValue){
		
		itemCatService.findByParentId(newValue).success(
				function(response){
					$scope.itemCat3List=response;			
				}
		);
	});
	
	//读取模板ID
	$scope.$watch('entity.goods.category3Id',function(newValue,oldValue){
		
		itemCatService.findOne(newValue).success(
			function(response){
				$scope.entity.goods.typeTemplateId=response.typeId;
			}
		);		
	});
	
	//读取模板ID后，读取品牌列表 扩展属性  规格列表
	$scope.$watch('entity.goods.typeTemplateId',function(newValue,oldValue){
		//读取品牌列表、规格属性
		typeTemplateService.findOne(newValue).success(

			function(response){

				$scope.typeTemplate=response;// 模板对象 
								
				$scope.typeTemplate.brandIds= JSON.parse($scope.typeTemplate.brandIds);//品牌列表类型转换
				if ($location.search()['id']==null){

                    //扩展属性
                    $scope.entity.goodsDesc.customAttributeItems= JSON.parse($scope.typeTemplate.customAttributeItems);
				}
			}
		);
		
		//读取规格
		typeTemplateService.findSpecList(newValue).success(
			function(response){
				$scope.specList=response;
			}
		);	
	});
	
	//勾选数据获取处理
	$scope.updateSpecAttribute=function($event,name,value){
		
		//调用判断数据是否存在的方法
		var object= $scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems ,'attributeName', name);
		//判断数据是否存在
		if(object!=null){	
			//数据存在
			//判断是否勾选，勾选添加数据
			if($event.target.checked ){
				object.attributeValue.push(value);		
			}else{
				//取消勾选，去除数据
				//splice ： 删除数据  参数1：删除数据的位置，参数2：删除数据的个数
				//indexOf ：返回数据首次出现的位置
				object.attributeValue.splice( object.attributeValue.indexOf(value ) ,1);//移除选项
				//如果选项都取消了，将此条记录移除
				if(object.attributeValue.length==0){
					$scope.entity.goodsDesc.specificationItems.splice(
							$scope.entity.goodsDesc.specificationItems.indexOf(object),1);
				}
				
			}
		}else{	
			//数据不存在，往集合中添加数据
			$scope.entity.goodsDesc.specificationItems.push({"attributeName":name,"attributeValue":[value]});
		}
	}
	
	//创建SKU列表
	$scope.createItemList=function(){
		
		//初始化列表集合
		$scope.entity.itemList=[{spec:{},price:0,num:99999,status:'0',isDefault:'0'} ];//列表初始化
		
		//获取到规格信息集合
		var items= $scope.entity.goodsDesc.specificationItems;
		
		//遍历规格信息，开始生成新的规格列表
		for(var i=0;i<items.length;i++){
			$scope.entity.itemList= addColumn( $scope.entity.itemList, items[i].attributeName,items[i].attributeValue );			
		}	
	}
	
	//添加列，参数1：集合，参数2：规格的名称，参数3：规格的值
	addColumn=function(list,columnName,columnValues){
		
		var newList=[];		
		for(var i=0;i< list.length;i++){
			var oldRow=  list[i];//获取itemList集合 的数据，以便进行深度克隆		
			for(var j=0;j<columnValues.length;j++){
				var newRow=  JSON.parse( JSON.stringify(oldRow)  );//深克隆
				newRow.spec[columnName]=columnValues[j];//设置新的列表中的规格数据
				newList.push(newRow);//设置新的列表中的价格、库存等数据
			}			
		}		
		return newList;//返回最新的规格列表，可以作为下一次生成的初始集合
	}

	$scope.status=['未申请','已审核','审核通过','关闭'];//商品状态

	$scope.itemCatList=[];

	$scope.findItemCatList=function () {
		itemCatService.findAll().success(
			function (response) {
				for (var i=0;i<response.length;i++){
					$scope.itemCatList[response[i].id]=response[i].name;
				}
            }
		)
    }

    $scope.checkAttributeValue = function (specName, optionName) {
		var items=$scope.entity.goodsDesc.specificationItems;
		var object=$scope.searchObjectByKey(items,'attributeName',specName);
		if (object==null){
			return false;
		}else {
			if (object.attributeValue.indexOf(optionName)>=0){
				return true;
			}else {
				return false;
			}
		}
    }
});	
