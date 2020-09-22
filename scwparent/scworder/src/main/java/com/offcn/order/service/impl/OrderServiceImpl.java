package com.offcn.order.service.impl;

import com.offcn.dycommon.enums.AppResponse;
import com.offcn.dycommon.enums.OrderStatusEnumes;
import com.offcn.order.mapper.TOrderMapper;
import com.offcn.order.po.TOrder;
import com.offcn.order.service.OrderService;
import com.offcn.order.service.ProjectServiceFeign;
import com.offcn.order.vo.req.OrderInfoSubmitVo;
import com.offcn.order.vo.req.TReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private TOrderMapper tOrderMapper;

    @Autowired
    private ProjectServiceFeign projectServiceFeign;


    @Override
    public TOrder saveOrder(OrderInfoSubmitVo vo) {

        TOrder order = new TOrder();
        String accessToken = vo.getAccessToken();

        String memberId  = redisTemplate.opsForValue().get(accessToken);

        order.setMemberid(Integer.parseInt(memberId));
        order.setProjectid(vo.getProjectid());
        order.setReturnid(vo.getReturnid());

        String orderNum  = UUID.randomUUID().toString().replace("-", "");
        order.setOrdernum(orderNum);

        AppResponse<List<TReturn>> listAppResponse =
                projectServiceFeign.returnInfo(vo.getProjectid());

        List<TReturn> tReturnList  = listAppResponse.getData();
        TReturn tReturn = tReturnList.get(0);
        Integer totalMoney  = vo.getRtncount() * tReturn.getSupportmoney() + tReturn.getFreight();
        order.setMoney(totalMoney);
        order.setRtncount(vo.getRtncount());
        order.setAddress(vo.getAddress());
        order.setStatus(OrderStatusEnumes.UNPAY.getCode()+"");
        order.setInvoice(vo.getInvoice().toString());
        order.setInvoictitle(vo.getInvoictitle());

        order.setRemark(vo.getRemark());

        tOrderMapper.insertSelective(order);

        return order;


    }
}
