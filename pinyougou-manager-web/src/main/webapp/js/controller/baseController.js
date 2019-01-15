app.controller('baseController',function ($scope) {

    //重新加载列表 数据
    $scope.reloadList=function(){
        //切换页码
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
        // $scope.findPage( $scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    };
    //分页控件配置
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function(){
            $scope.reloadList();//重新加载
        }
    };
    $scope.seleteIds=[];

    $scope.updateSelection=function ($event, id) {

        if ($event.target.checked){

            $scope.seleteIds.push(id);
        }else {

            var ix = $scope.seleteIds.indexOf(id);
            $scope.seleteIds.splice(ix,1);
        }
    };
});