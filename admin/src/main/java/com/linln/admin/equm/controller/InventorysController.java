package com.linln.admin.equm.controller;

import com.linln.modules.dictionary.domain.Countrylist;
import com.linln.modules.dictionary.service.CountrylistService;
import com.linln.modules.elite.elitesdemand.domain.Demandlist;
import com.linln.modules.elite.elitesdemand.service.DemandlistService;
import com.linln.modules.equm.domain.Inventorys;
import com.linln.modules.equm.repository.InventorysRepository;
import com.linln.modules.equm.service.InventorysService;
import com.linln.admin.equm.validator.InventorysValid;
import com.linln.common.enums.StatusEnum;
import com.linln.common.utils.EntityBeanUtil;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.utils.SpringContextUtil;
import com.linln.common.utils.StatusUtil;
import com.linln.common.vo.ResultVo;
import com.linln.component.excel.ExcelUtil;
import com.linln.component.shiro.ShiroUtil;
import com.linln.modules.system.domain.User;
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
import java.util.ArrayList;
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
    @Autowired
    private DemandlistService demandlistService;
    @Autowired
    private CountrylistService countrylistService;

    /**
     * 列表页面
     */
    @GetMapping(value = {"/index"})
    @RequiresPermissions("equm:inventorys:index")
    public String index(Model model, Inventorys inventorys,HttpServletRequest request) {

        User user = ShiroUtil.getSubject();

        //根据用户ID 获取国家ID
        List<Long> countryID  = inventorysService.getCountryByUserId(user.getId());
        //取国家数组
        List <String> countryname = countrylistService.getCountryByCountryID(countryID);


        // 获取数据列表
        Page<Inventorys> list = inventorysService.getBycountry(countryname,inventorys);
            // 封装数据 
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);


        return "/equm/inventorys/index";
    }


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
//         复制保留无需修改的数据
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
        model.addAttribute("templates/inventorys", inventorys);
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

    /*
     * 付款完毕对未提单的货物进行展现
     *
     * */
    @GetMapping(value = {"/A1checkingbyequmindex","/A2checkingbyequmindex",
            "/A3checkingbyequmindex","/A4checkingbyequmindex","/A5checkingbyequmindex",
            "/A6checkingbyequmindex","/checkingbyequmindex"})
    @RequiresPermissions(value = {"equm:inventorys:A1checkingbyequmindex",
            "equm:inventorys:A2checkingbyequmindex", "equm:inventorys:A3checkingbyequmindex",
            "equm:inventorys:A4checkingbyequmindex", "equm:inventorys:A5checkingbyequmindex",
            "equm:inventorys:A6checkingbyequmindex", "equm:inventorys:checkingbyequmindex"})
    public String nobillofladingindex(Model model, Demandlist demandlist, HttpServletRequest request){

        User user = ShiroUtil.getSubject();

        //根据用户ID 获取国家ID
        List<Long> countryID  = inventorysService.getCountryByUserId(user.getId());
        //取国家数组
        List <String> countryname = countrylistService.getCountryByCountryID(countryID);
        // 获取数据列表
        Example<Demandlist> example = Example.of(demandlist);
        Page<Demandlist> list = demandlistService.getPageList(example);
        model.addAttribute("user" , user);
        //Date date = new Date();
        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/equm/inventorys/checkingbyequmindex";
    }


    @PostMapping("/billoflading")
    @RequiresPermissions({"equm:inventorys:billoflading"})
    @ResponseBody
    public ResultVo billoflading(@Validated InventorysValid valid, Inventorys inventorys) {
        //首先改变库存表提货日期 和状态
        if (demandlistService.billoflading(inventorys.getId(),inventorys.getReceiveddate())){
            // 保存数据
            inventorysService.save(inventorys);
            return ResultVoUtil.SAVE_SUCCESS;
        }
        return ResultVoUtil.error("更改采购出错,联系管理员");

    }
}