package com.offcn.project.service;

import com.offcn.dycommon.enums.ProjectStatusEnume;
import com.offcn.project.vo.req.ProjectRedisReqVo;

public interface ProjectCreateService {
    /**
     * 初始化项目
     * @param memberId
     * @return
     */
    public String initCreateProject(Integer memberId);

    /**
     * 保存项目信息
     * @param auth  项目状态信息
     * @param project  项目全部信息
     */
    public void saveProjectInfo(ProjectStatusEnume auth, ProjectRedisReqVo project);
}
