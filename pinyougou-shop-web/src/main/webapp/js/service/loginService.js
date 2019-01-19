app.service('loginService',function($http){
    //读取登录人名称
    this.loginName=function(){
        return $http.post('../login/name.do');
    }
});