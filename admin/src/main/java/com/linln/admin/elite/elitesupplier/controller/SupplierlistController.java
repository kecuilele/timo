package com.linln.admin.elite.admin.elitesupplier.controller;

import com.linln.modules.elite.elitesupplier.domain.Supplierlist;
import com.linln.modules.elite.elitesupplier.service.SupplierlistService;
import com.linln.admin.elite.admin.elitesupplier.validator.SupplierlistValid;
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
 * @author 崔珂
 * @date 2019/11/18
 */
@Controller
@RequestMapping("/elitesupplier/supplierlist")
public class SupplierlistController {

    @Autowired
    private SupplierlistService supplierlistService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("elitesupplier:supplierlist:index")
    public String index(Model model, Supplierlist supplierlist) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("suppliersname", match -> match.contains())
                .withMatcher("supplieraddress", match -> match.contains())
                .withMatcher("supplierwebsite", match -> match.contains())
                .withMatcher("contactname", match -> match.contains())
                .withMatcher("contacttitle", match -> match.contains())
                .withMatcher("contactphone", match -> match.contains())
                .withMatcher("contactemail", match -> match.contains());

        // 获取数据列表
        Example<Supplierlist> example = Example.of(supplierlist, matcher);
        Page<Supplierlist> list = supplierlistService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/elitesupplier/supplierlist/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("elitesupplier:supplierlist:add")
    public String toAdd() {
        return "/elitesupplier/supplierlist/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("elitesupplier:supplierlist:edit")
    public String toEdit(@PathVariable("id") Supplierlist supplierlist, Model model) {
        model.addAttribute("templates/supplierlist", supplierlist);
        return "/elitesupplier/supplierlist/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"elitesupplier:supplierlist:add", "elitesupplier:supplierlist:edit"})
    @ResponseBody
    public ResultVo save(@Validated SupplierlistValid valid, Supplierlist supplierlist) {
        // 复制保留无需修改的数据
        if (supplierlist.getId() != null) {
            Supplierlist beSupplierlist = supplierlistService.getById(supplierlist.getId());
            EntityBeanUtil.copyProperties(beSupplierlist, supplierlist);
        }

        // 保存数据
        supplierlistService.save(supplierlist);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("elitesupplier:supplierlist:detail")
    public String toDetail(@PathVariable("id") Supplierlist supplierlist, Model model) {
        model.addAttribute("templates/supplierlist",supplierlist);
        return "/elitesupplier/supplierlist/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("elitesupplier:supplierlist:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (supplierlistService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}