app.controller('brandController',function($scope,$http,$controller,brandService){

    $controller('baseController',{$scope:$scope});
    //查询品牌列表
    $scope.findAll=function(){
        brandService.findAll().success(
            function(response){
                $scope.list=response;
            }
        );
    };

    //分页
    $scope.findPage=function(page,size){
        brandService.findPage(page,size).success(
            function(response){
                $scope.list=response.rows;//后台传过来的数据 跟pojo类里的一样
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    };

    //查询实体
    $scope.findOne=function (id) {
        brandService.findOne(id).success(
            function (response) {
                $scope.entity=response;
            }
        )
    };
    //添加
    $scope.save=function () {

        var object=null;

        if($scope.entity.id!=null){

            object=brandService.update($scope.entity)
        }else {
            object=brandService.add($scope.entity)
        }

        object.success(
            function (response) {
                if (response.success){
                    $scope.reloadList()
                }else {
                    alert(response.message)
                }
            }
        )
    };



    $scope.dele=function () {

        brandService.dele($scope.seleteIds).success(
            function (response) {

                if (response.success){

                    $scope.reloadList()
                }
            }
        )
    };

    $scope.searchEntity={};//定义搜索对象

    $scope.search=function (page, size) {
        brandService.search(page,size,$scope.searchEntity).success(
            function (reponse) {
                $scope.paginationConf.totalItems=reponse.total;//总记录数
                $scope.list=reponse.rows;//记录数
            }
        )
    }

});
