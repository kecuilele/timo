package com.linln.admin.elitesdemand.controller;
import	java.lang.annotation.Retention;

import com.linln.admin.elitesdemand.domain.Demandlsit;
import com.linln.admin.elitesdemand.service.DemandlsitService;
import com.linln.admin.elitesdemand.validator.DemandlsitValid;
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

import java.util.List;

/**
 * @author cuike
 * @date 2019/11/18
 */
@Controller
@RequestMapping("/elitesdemand/demandlsit")
public class DemandlsitController {

    @Autowired
    private DemandlsitService demandlsitService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("elitesdemand:demandlsit:index")
    public String index(Model model, Demandlsit demandlsit) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("unitid", match -> match.contains())
                .withMatcher("type", match -> match.contains())
                .withMatcher("owenrs", match -> match.contains())
                .withMatcher("builtdate", match -> match.contains())
                .withMatcher("machinery", match -> match.contains())
                .withMatcher("loccode", match -> match.contains())
                .withMatcher("depotcode", match -> match.contains())
                .withMatcher("area", match -> match.contains())
                .withMatcher("country", match -> match.contains())
                .withMatcher("location", match -> match.contains())
                .withMatcher("condition", match -> match.contains())
                .withMatcher("costprice", match -> match.contains())
                .withMatcher("sellingprice", match -> match.contains())
                .withMatcher("purchaser", match -> match.contains())
                .withMatcher("Invoicedate", match -> match.contains())
                .withMatcher("remark", match -> match.contains());

        // 获取数据列表
        Example<Demandlsit> example = Example.of(demandlsit, matcher);
        Page<Demandlsit> list = demandlsitService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/elitesdemand/demandlsit/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("elitesdemand:demandlsit:add")
    public String toAdd() {
        return "/elitesdemand/demandlsit/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("elitesdemand:demandlsit:edit")
    public String toEdit(@PathVariable("id") Demandlsit demandlsit, Model model) {
        model.addAttribute("demandlsit", demandlsit);
        return "/elitesdemand/demandlsit/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"elitesdemand:demandlsit:add", "elitesdemand:demandlsit:edit"})
    @ResponseBody
    public ResultVo save(@Validated DemandlsitValid valid, Demandlsit demandlsit) {
        //默认箱货状态为 pre-start 在采购页面中箱子的状态永远都是pre-start
        demandlsit.setDynamics("prestart");
        // 复制保留无需修改的数据
        if (demandlsit.getId() != null) {
            Demandlsit beDemandlsit = demandlsitService.getById(demandlsit.getId());
            EntityBeanUtil.copyProperties(beDemandlsit, demandlsit);
        }

        // 保存数据
        demandlsitService.save(demandlsit);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("elitesdemand:demandlsit:detail")
    public String toDetail(@PathVariable("id") Demandlsit demandlsit, Model model) {
        model.addAttribute("demandlsit",demandlsit);
        return "/elitesdemand/demandlsit/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("elitesdemand:demandlsit:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (demandlsitService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }

    /*
    * cuike
    * 向财务订单转存
    *
    * */
    @RequestMapping("/submit")
    @RequiresPermissions("elitesdemand:demandlsit:submit")
    @ResponseBody
    public ResultVo submit(
            @RequestParam(value = "ids", required = false) List<Long> ids){
        Demandlsit demandlsit;
        // 向财务表储存 1.拿到信息 elite_demandlsit
        for (int i = 0; i <ids.size(); i++) {
            //2.拿到信息向财务审核表储存
            demandlsit = demandlsitService.getById(ids.get(i));
        }
        return ResultVoUtil.success();
    }
}