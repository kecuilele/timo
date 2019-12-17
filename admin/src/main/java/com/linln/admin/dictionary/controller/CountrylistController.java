package com.linln.admin.dictionary.controller;

import com.linln.modules.dictionary.domain.Countrylist;
import com.linln.modules.dictionary.service.CountrylistService;
import com.linln.admin.dictionary.validator.CountrylistValid;
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
 * @date 2019/12/10
 */
@Controller
@RequestMapping("/dictionary/countrylist")
public class CountrylistController {

    @Autowired
    private CountrylistService countrylistService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("dictionary:countrylist:index")
    public String index(Model model, Countrylist countrylist) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching();

        // 获取数据列表
        Example<Countrylist> example = Example.of(countrylist, matcher);
        Page<Countrylist> list = countrylistService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/dictionary/countrylist/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("dictionary:countrylist:add")
    public String toAdd() {
        return "/dictionary/countrylist/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("dictionary:countrylist:edit")
    public String toEdit(@PathVariable("id") Countrylist countrylist, Model model) {
        model.addAttribute("countrylist", countrylist);
        return "/dictionary/countrylist/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"dictionary:countrylist:add", "dictionary:countrylist:edit"})
    @ResponseBody
    public ResultVo save(@Validated CountrylistValid valid, Countrylist countrylist) {
        // 复制保留无需修改的数据
        if (countrylist.getId() != null) {
            Countrylist beCountrylist = countrylistService.getById(countrylist.getId());
            EntityBeanUtil.copyProperties(beCountrylist, countrylist);
        }

        // 保存数据
        countrylistService.save(countrylist);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("dictionary:countrylist:detail")
    public String toDetail(@PathVariable("id") Countrylist countrylist, Model model) {
        model.addAttribute("countrylist",countrylist);
        return "/dictionary/countrylist/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("dictionary:countrylist:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (countrylistService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}