package com.linln.admin.equm.controller;

import com.linln.admin.equm.domain.Inventorys;
import com.linln.admin.equm.repository.InventorysRepository;
import com.linln.admin.equm.service.InventorysService;
import com.linln.admin.equm.validator.InventorysValid;
import com.linln.common.enums.StatusEnum;
import com.linln.common.utils.EntityBeanUtil;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.utils.SpringContextUtil;
import com.linln.common.utils.StatusUtil;
import com.linln.common.vo.ResultVo;
import com.linln.component.excel.ExcelUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author cuike
 * @date 2019/11/07
 */
@Controller
@RequestMapping("/equm/inventorys")
public class InventorysController {

    @Autowired
    private InventorysService inventorysService;

    /**
     * 列表页面
     */
    @GetMapping(value = {"/index","/A1index","/A2index","/A3index","/A4index","/A5index","/A6index"})
    @RequiresPermissions("equm:inventorys:index")
    public String index(Model model, Inventorys inventorys,HttpServletRequest request) {
        String a  = request.getRequestURI();
        //可优化
        if (a.equals("/equm/inventorys/A2index")){
            inventorys.setAREA("A2");
        } else if (a.equals("/equm/inventorys/A3index")){
            inventorys.setAREA("A3");
        }else if (a.equals("/equm/inventorys/A4index")){
            inventorys.setAREA("A4");
        }else if (a.equals("/equm/inventorys/A5index")){
            inventorys.setAREA("A5");
        }else if (a.equals("/equm/inventorys/A6index")){
            inventorys.setAREA("A6");
        }else if (a.equals("/equm/inventorys/A1index")){
            inventorys.setAREA("A1");
        }

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching();

        // 获取数据列表
        Example<Inventorys> example = Example.of(inventorys, matcher);
        Page<Inventorys> list = inventorysService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/equm/inventorys/index";
    }

//    /**
//     * A2列表页面
//     */
//    @GetMapping("/A2index")
//    @RequiresPermissions("equm:inventorys:index")
//    public String A1index(Model model, Inventorys inventorys) {
//
//        // 创建匹配器，进行动态查询匹配
//        ExampleMatcher matcher = ExampleMatcher.matching();
//        inventorys.setAREA("A2");
//
//        // 获取数据列表
//        Example<Inventorys> example = Example.of(inventorys, matcher);
//        Page<Inventorys> list = inventorysService.getPageList(example);
//
//        // 封装数据
//        model.addAttribute("list", list.getContent());
//        model.addAttribute("page", list);
//        return "/equm/inventorys/index";
//    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("equm:inventorys:add")
    public String toAdd() {
        return "/equm/inventorys/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("equm:inventorys:edit")
    public String toEdit(@PathVariable("id") Inventorys inventorys, Model model) {
        model.addAttribute("inventorys", inventorys);
        return "/equm/inventorys/add";
    }

    /**
     * 保存添加/修改的数据
     *
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"equm:inventorys:add", "equm:inventorys:edit"})
    @ResponseBody
    public ResultVo save(@Validated InventorysValid valid, Inventorys inventorys) {
        // 复制保留无需修改的数据
        if (inventorys.getId() != null) {
            Inventorys beInventorys = inventorysService.getById(inventorys.getId());
            EntityBeanUtil.copyProperties(beInventorys, inventorys);
        }

        // 保存数据
        inventorysService.save(inventorys);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("equm:inventorys:detail")
    public String toDetail(@PathVariable("id") Inventorys inventorys, Model model) {
        model.addAttribute("inventorys", inventorys);
        return "/equm/inventorys/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("equm:inventorys:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (inventorysService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }

    /*
     * 导出Excel
     *
     * ba*/

    @GetMapping("/export")
    @ResponseBody
    public void exportExcel() {
        InventorysRepository inventorysRepository = SpringContextUtil.getBean(InventorysRepository.class);
        ExcelUtil.exportExcel(Inventorys.class, inventorysRepository.findAll());
    }

    /*
     * 导入
     * */
    @PostMapping(value = "/upload")
    @ResponseBody
    public ResultVo uploadExcel(@RequestParam("file") MultipartFile file) {
        //String fileName = file.getOriginalFilename();
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Inventorys> list1 = ExcelUtil.importExcel(Inventorys.class, inputStream);
        for (Inventorys inventorys : list1) {
            inventorysService.save(inventorys);
        }
         return ResultVoUtil.success("成功");
    }

    //}

}