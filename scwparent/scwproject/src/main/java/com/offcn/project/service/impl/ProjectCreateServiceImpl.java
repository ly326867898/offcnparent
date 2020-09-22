package com.offcn.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.offcn.dycommon.enums.ProjectStatusEnume;
import com.offcn.project.constant.ProjectConstant;
import com.offcn.project.enums.ProjectImageTypeEnume;
import com.offcn.project.mapper.*;
import com.offcn.project.po.*;
import com.offcn.project.service.ProjectCreateService;
import com.offcn.project.vo.req.ProjectRedisReqVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectCreateServiceImpl implements ProjectCreateService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private TProjectMapper projectMapper;

    @Autowired
    private TProjectImagesMapper imagesMapper;

    @Autowired
    private TProjectTagMapper tProjectTagMapper;

    @Autowired
    private TProjectTypeMapper tProjectTypeMapper;

    @Autowired
    private TReturnMapper returnMapper;

    @Override
    public String initCreateProject(Integer memberId) {
        //1、发放项目票据
        String projectToken = UUID.randomUUID().toString().replace("-","");

        //2、项目的空对象
        ProjectRedisReqVo reqVo = new ProjectRedisReqVo();
        reqVo.setProjectToken(projectToken);

        //3、对象转换为String
        String jsonVo = JSON.toJSONString(reqVo);
        stringRedisTemplate.opsForValue().set(ProjectConstant.TEMP_PROJECT_PREFIX+projectToken,jsonVo);

        return projectToken;
    }

    @Override
    public void saveProjectInfo(ProjectStatusEnume statusEnume, ProjectRedisReqVo redisReqVo) {
        //1、获取实体对象
        TProject project = new TProject();
        BeanUtils.copyProperties(redisReqVo,project);
        project.setStatus(statusEnume.getCode()+"");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        project.setCreatedate(sdf.format(new Date()));

        projectMapper.insertSelective(project);
        //2、添加图片
        int projectId = project.getId();

        //2.1 取头部图片
        String headerImage = redisReqVo.getHeaderImage();
        TProjectImages headImage = new TProjectImages(null,projectId,headerImage, ProjectImageTypeEnume.HEADER.getCode());
        imagesMapper.insertSelective(headImage);

        //2.2 存详细图片
        List<String> imagesList = redisReqVo.getDetailsImage();
        for (String imgUrl : imagesList) {
            TProjectImages detailsImage = new TProjectImages(null,projectId,imgUrl, ProjectImageTypeEnume.DETAILS.getCode());
            imagesMapper.insertSelective(detailsImage);
        }

        //3、存项目标签中间关系表
        List<Integer> tagList =  redisReqVo.getTagids();
        for (Integer tid : tagList) {
            TProjectTag pt = new TProjectTag(null,projectId,tid);
            tProjectTagMapper.insertSelective(pt);
        }

        //4、存项目 分类中间关系表
        List<Integer> typeIds = redisReqVo.getTypeids();
        for (Integer typeId : typeIds) {
            TProjectType tProjectType = new TProjectType(null,projectId,typeId);
            tProjectTypeMapper.insertSelective(tProjectType);
        }

        //5、存回报对象
        List<TReturn> returnList = redisReqVo.getProjectReturns();
        for (TReturn tReturn : returnList) {
            tReturn.setProjectid(projectId);
            returnMapper.insertSelective(tReturn);
        }
        //6、录入到mysql数据库中以后 删除缓存中的临时变量

        stringRedisTemplate.delete(ProjectConstant.TEMP_PROJECT_PREFIX+redisReqVo.getProjectToken());

    }
}
