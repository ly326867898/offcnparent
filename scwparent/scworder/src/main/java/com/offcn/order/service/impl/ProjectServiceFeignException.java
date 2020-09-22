package com.offcn.order.service.impl;

import com.offcn.dycommon.enums.AppResponse;
import com.offcn.order.service.ProjectServiceFeign;
import com.offcn.order.vo.req.TReturn;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceFeignException implements ProjectServiceFeign {

    @Override
    public AppResponse<List<TReturn>> returnInfo(Integer projectId) {
        AppResponse<List<TReturn>> fail = AppResponse.fail(null);
        fail.setMeg("调用远程服务器失败【订单】");
        return fail;
    }

}
