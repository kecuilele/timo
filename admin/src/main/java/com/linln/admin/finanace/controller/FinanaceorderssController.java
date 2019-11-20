package com.linln.admin.finanace.controller;

import com.linln.admin.elite.elitesdemand.service.DemandlistService;
import com.linln.admin.finanace.domain.Finanaceorderss;
import com.linln.admin.finanace.service.FinanaceorderssService;
import com.linln.admin.finanace.validator.FinanaceorderssValid;
import com.linln.common.enums.StatusEnum;
import com.linln.common.utils.EntityBeanUtil;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.utils.StatusUtil;
import com.linln.common.vo.ResultVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.linln.admin.elitesdemand.domain.Demandlist;

import java.util.List;

/**
 * @author 崔珂
 * @date 2019/11/19
 */
@Controller
@RequestMapping("/finanace/finanaceorderss")
public class FinanaceorderssController {

    @Autowired
    private FinanaceorderssService finanaceorderssService;
    @Autowired
    private DemandlistService demandlistService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("finanace:finanaceorderss:index")
    public String index(Model model, Finanaceorderss finanaceorderss) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("id", match -> match.contains());

        // 获取数据列表
        Example<Finanaceorderss> example = Example.of(finanaceorderss, matcher);
        Page<Finanaceorderss> list = finanaceorderssService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/finanace/finanaceorderss/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("finanace:finanaceorderss:add")
    public String toAdd() {
        return "/finanace/finanaceorderss/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("finanace:finanaceorderss:edit")
    public String toEdit(@PathVariable("id") Finanaceorderss finanaceorderss, Model model) {
        model.addAttribute("finanaceorderss", finanaceorderss);
        return "/finanace/finanaceorderss/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"finanace:finanaceorderss:add", "finanace:finanaceorderss:edit",
            "finanace:finanaceorderss:adddemand"})
    @ResponseBody
    public ResultVo save(@Validated FinanaceorderssValid valid, Finanaceorderss finanaceorderss,
                         @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 复制保留无需修改的数据
        if (finanaceorderss.getId() != null) {
            Finanaceorderss beFinanaceorderss = finanaceorderssService.getById(finanaceorderss.getId());
            EntityBeanUtil.copyProperties(beFinanaceorderss, finanaceorderss);
        }

        // 保存数据
        Finanaceorderss f = finanaceorderssService.save(finanaceorderss);
        Demandlist demandlist = null;
        demandlist.setDynamics("precheck");
        demandlist.setFinanaceid(f.getId());

        //获取刚刚添加的这个数据的ID
        if (ids != null){
            //这些箱子需要关联订单ID 改变状态 发票日期
            for (int i = 0; i <ids.size(); i++) {
                //获取现在箱子的详细信息
                Demandlist de = demandlistService.getById(ids.get(i));
                //复制当前数据
                EntityBeanUtil.copyProperties(demandlist,de);
                demandlistService.save(de);
            }
        }
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("finanace:finanaceorderss:detail")
    public String toDetail(@PathVariable("id") Finanaceorderss finanaceorderss, Model model) {
        model.addAttribute("finanaceorderss",finanaceorderss);
        return "/finanace/finanaceorderss/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("finanace:finanaceorderss:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (finanaceorderssService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}