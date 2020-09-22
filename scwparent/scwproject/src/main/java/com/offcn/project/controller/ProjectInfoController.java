package com.offcn.project.controller;

import com.offcn.dycommon.enums.AppResponse;
import com.offcn.project.po.*;
import com.offcn.project.service.ProjectInfoService;
import com.offcn.project.vo.req.ProjectDetailVo;
import com.offcn.project.vo.req.ProjectVo;
import com.offcn.util.OssTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags="项目基本功能模块（文件上传、项目信息获取等）")
@Slf4j
@RestController
@RequestMapping("/project")
public class ProjectInfoController {
    @Autowired
    private OssTemplate ossTemplate;

    @Autowired
    private ProjectInfoService projectInfoService;

    @ApiOperation("文件上传功能")
    @PostMapping("/upload")
    public AppResponse<Map<String,Object>> upload(@RequestParam("file") MultipartFile[] files)throws IOException {
        Map<String, Object> map = new HashMap<>();
        ArrayList<String> list = new ArrayList<>();

        if(files!=null&&files.length>0){
            for (MultipartFile item : files) {
                if (!item.isEmpty()){
                    String upload = ossTemplate.upload(item.getInputStream(), item.getOriginalFilename());
                    list.add(upload);
                }
            }
        }
        map.put("urls",list);
        log.debug("ossTemplate信息：{},文件上传成功访问路径{}",ossTemplate,list);
        return AppResponse.ok(map);

    }

    @ApiOperation("获取项目汇报列表")
    @GetMapping("/details/returns/{projectId}")
    public AppResponse<List<TReturn>> detailsReturn(@PathVariable("projectId") Integer projectId){
        List<TReturn> projectReturns = projectInfoService.getProjectReturns(projectId);
        return AppResponse.ok(projectReturns);
    }

    @ApiOperation("获取系统所有的项目")
    @GetMapping("/all")
    public AppResponse<List<ProjectVo>> all() {
        ArrayList<ProjectVo> prosVo  = new ArrayList<>();

        List<TProject> pros = projectInfoService.getAllProjects();
        for (TProject tProject  : pros) {
            Integer id = tProject.getId();
            List<TProjectImages> projectImages =
                    projectInfoService.getProjectImages(id);
            ProjectVo projectVo = new ProjectVo();

            BeanUtils.copyProperties(tProject,projectVo);

            for (TProjectImages projectImage : projectImages) {

                if (projectImage.getImgtype() == 0){
                    projectVo.setHeaderImage(projectImage.getImgurl());
                }
            }
            prosVo.add(projectVo);
        }
        return AppResponse.ok(prosVo);
    }

    @ApiOperation("获取项目信息详情")
    @GetMapping("/details/info/{projectId}")
    public AppResponse<ProjectDetailVo> detailsInfo(@PathVariable("projectId") Integer projectId) {
        TProject projectInfo = projectInfoService.getProjectInfo(projectId);

        ProjectDetailVo projectDetailVo = new ProjectDetailVo();

        List<TProjectImages> projectImages = projectInfoService.getProjectImages(projectInfo.getId());

        List<String> detailsImage = projectDetailVo.getDetailsImage();
        if(detailsImage==null){
            detailsImage=new ArrayList<>();
        }

        for (TProjectImages tProjectImages : projectImages) {
            if (tProjectImages.getImgtype() == 0) {
                projectDetailVo.setHeaderImage(tProjectImages.getImgurl());
            } else {
                detailsImage.add(tProjectImages.getImgurl());
            }
        }
        projectDetailVo.setDetailsImage(detailsImage);


        List<TReturn> projectReturns = projectInfoService.getProjectReturns(projectInfo.getId());
        projectDetailVo.setProjectReturns(projectReturns);

        BeanUtils.copyProperties(projectInfo,projectDetailVo);
        return AppResponse.ok(projectDetailVo);

    }

    @ApiOperation("获取系统所有的项目标签")
    @GetMapping("/tags")
    public AppResponse<List<TTag>> tags() {
        List<TTag> tags = projectInfoService.getAllProjectTags();
        return AppResponse.ok(tags);
    }

    @ApiOperation("获取系统所有的项目分类")
    @GetMapping("/types")
    public AppResponse<List<TType>> types() {
        List<TType> types = projectInfoService.getProjectTypes();
        return AppResponse.ok(types);
    }

    @ApiOperation("获取回报信息")
    @GetMapping("/returns/info/{returnId}")
    public AppResponse<TReturn> getTReturn(@PathVariable("returnId") Integer returnId){
        TReturn tReturn = projectInfoService.getReturnInfo(returnId);
        return AppResponse.ok(tReturn);
    }
}
