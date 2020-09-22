package com.offcn.order.service;

import com.offcn.dycommon.enums.AppResponse;
import com.offcn.order.service.impl.ProjectServiceFeignException;
import com.offcn.order.vo.req.TReturn;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "SCW-PROJECT",fallback = ProjectServiceFeignException.class)
public interface ProjectServiceFeign {

    @GetMapping("/project/details/returns/{projectId}")
    public AppResponse<List<TReturn>> returnInfo(@PathVariable(name="projectId") Integer projectId);

}
