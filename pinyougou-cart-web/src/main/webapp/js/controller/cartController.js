//购物车控制层
app.controller('cartController',function($scope,cartService){
    //查询购物车列表
    $scope.findCartList=function(){
        cartService.findCartList().success(
            function(response){
                $scope.cartList=response;
                $scope.totalValue=cartService.sum($scope.cartList);//求合计数
            }
        );
    }

    //添加商品到购物车
    $scope.addGoodsToCartList=function (itemId, num) {
        cartService.addGoodsToCartList(itemId,num).success(
            function (response) {
                if (response.success()){
                    $scope.findCartList();

                }else {
                    alert(response.message);
                }
            }
        )
    }
});