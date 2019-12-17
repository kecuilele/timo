package com.linln.admin.dictionary.controller;

import com.linln.modules.dictionary.domain.Boxtype;
import com.linln.modules.dictionary.service.BoxtypeService;
import com.linln.admin.dictionary.validator.BoxtypeValid;
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
 * @date 2019/11/29
 */
@Controller
@RequestMapping("/dictionary/boxtype")
public class BoxtypeController {

    @Autowired
    private BoxtypeService boxtypeService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("dictionary:boxtype:index")
    public String index(Model model, Boxtype boxtype) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching();

        // 获取数据列表
        Example<Boxtype> example = Example.of(boxtype, matcher);
        Page<Boxtype> list = boxtypeService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/dictionary/boxtype/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("dictionary:boxtype:add")
    public String toAdd() {
        return "/dictionary/boxtype/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("dictionary:boxtype:edit")
    public String toEdit(@PathVariable("id") Boxtype boxtype, Model model) {
        model.addAttribute("boxtype", boxtype);
        return "/dictionary/boxtype/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"dictionary:boxtype:add", "dictionary:boxtype:edit"})
    @ResponseBody
    public ResultVo save(@Validated BoxtypeValid valid, Boxtype boxtype) {
        // 复制保留无需修改的数据
        if (boxtype.getId() != null) {
            Boxtype beBoxtype = boxtypeService.getById(boxtype.getId());
            EntityBeanUtil.copyProperties(beBoxtype, boxtype);
        }

        // 保存数据
        boxtypeService.save(boxtype);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("dictionary:boxtype:detail")
    public String toDetail(@PathVariable("id") Boxtype boxtype, Model model) {
        model.addAttribute("boxtype",boxtype);
        return "/dictionary/boxtype/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("dictionary:boxtype:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (boxtypeService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}