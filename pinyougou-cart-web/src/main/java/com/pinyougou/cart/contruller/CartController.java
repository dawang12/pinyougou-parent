package com.pinyougou.cart.contruller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.entity.Result;
import com.pinyougou.pojogroup.Cart;
import com.pinyougou.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference(timeout = 6000)
    private CartService cartService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @RequestMapping("/findCartList")
    public List<Cart> findCartList(){

        //得到登陆人账号,判断当前是否有人登陆
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (username.equals("anonymousUser")){
            String cartListString= CookieUtil.getCookieValue(request,"cartList","UTF-8");
            if (cartListString==null||cartListString.equals("")){
                cartListString="[]";
            }
            List<Cart> cartList_cookie = JSON.parseArray(cartListString, Cart.class);
            return cartList_cookie;
        }else {
            List<Cart> cartList_redis = cartService.findCartListFromRedis(username);
            return cartList_redis;
        }

    }

    @RequestMapping("/addGoodsToCartList")
    public Result addGoodsToCartList(Long itemId,Integer num){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("当前登录用户"+username);
        try {
            List<Cart> cartList = findCartList();
            cartList = cartService.addGoodsToCartList(cartList, itemId, num);
            if (username.equals("anonymousUser")){
                CookieUtil.setCookie(request, response, "cartList", JSON.toJSONString(cartList), 3600 * 24, "UTF-8");
            }else {
                cartService.saveCartListToRedis(username,cartList);
            }
            return new Result(true, "添加成功");
        }catch (RuntimeException e){
            e.printStackTrace();
            return new Result(false,e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"添加失败");
        }
    }
}
