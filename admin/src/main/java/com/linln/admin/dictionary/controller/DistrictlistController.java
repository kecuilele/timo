package com.linln.admin.dictionary.controller;

import com.linln.modules.dictionary.domain.Countrylist;
import com.linln.modules.dictionary.domain.Districtlist;
import com.linln.modules.dictionary.service.CountrylistService;
import com.linln.modules.dictionary.service.DistrictlistService;
import com.linln.admin.dictionary.validator.DistrictlistValid;
import com.linln.common.enums.StatusEnum;
import com.linln.common.utils.EntityBeanUtil;
import com.linln.common.utils.ResultVoUtil;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author cuike
 * @date 2019/12/02
 */
@Controller
@RequestMapping("/dictionary/districtlist")
public class DistrictlistController {

    @Autowired
    private DistrictlistService districtlistService;

    @Autowired
    private CountrylistService countrylistService;
    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("dictionary:districtlist:index")
    public String index(Model model, Districtlist districtlist) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching();

        // 获取数据列表
        Example<Districtlist> example = Example.of(districtlist, matcher);
        Page<Districtlist> list = districtlistService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/dictionary/districtlist/index";
    }

    /**

     *@描述 显示城市管理

     *@创建人  cuike

     *@创建时间  2019/12/9

     *@修改人和其它信息

     */

    /**
     * 列表页面
     */
    @GetMapping("/cityindex")
    @RequiresPermissions("dictionary:districtlist:cityindex")
    public String cityindex(Model model, Districtlist districtlist){
        //List<Districtlist> list = districtlistService.findCountry(districtlist);

        // 封装数据
//        model.addAttribute("list", list);
//        model.addAttribute("page", list);
        return "/dictionary/districtlist/cityindex";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("dictionary:districtlist:add")
    public String toAdd() {
        return "/dictionary/districtlist/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("dictionary:districtlist:edit")
    public String toEdit(@PathVariable("id") Districtlist districtlist, Model model) {
        model.addAttribute("districtlist", districtlist);
        return "/dictionary/districtlist/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"dictionary:districtlist:add", "dictionary:districtlist:edit"})
    @ResponseBody
    public ResultVo save(@Validated DistrictlistValid valid, Districtlist districtlist) {
        // 复制保留无需修改的数据
        if (districtlist.getId() != null) {
            Districtlist beDistrictlist = districtlistService.getById(districtlist.getId());
            EntityBeanUtil.copyProperties(beDistrictlist, districtlist);
        }

        // 保存数据
        districtlistService.save(districtlist);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("dictionary:districtlist:detail")
    public String toDetail(@PathVariable("id") Districtlist districtlist, Model model) {
        model.addAttribute("districtlist",districtlist);
        return "/dictionary/districtlist/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("dictionary:districtlist:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (districtlistService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
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
        List<Districtlist> list1 = ExcelUtil.importExcel(Districtlist.class, inputStream);
        for (Districtlist inventorys : list1) {
            districtlistService.save(inventorys);
        }
        return ResultVoUtil.success("成功");
    }

    /**

     *@描述  将国家导入表中

     *@创建人  cuike

     *@创建时间  2019/12/10

     *@修改人和其它信息

     */
    @GetMapping("/uploadCountry")
    @RequiresPermissions("dictionary:districtlist:uploadCountry")
    @ResponseBody
    public ResultVo uploadCountry(){
        Countrylist countrylist = new Countrylist();
        //查询所有城市
        List<Districtlist> list = districtlistService.findCountry();
        //循环遍历
        for (int i = 0; i <list.size() ; i++) {
            //保存area 和 country
//            countrylist.setArea(list.get(i).getArea());
//            countrylist.setCountry(list.get(i).getCountry());
//            countrylistService.save(countrylist);
            String area =list.get(i).getArea();
            String country = list.get(i).getCountry();
            countrylistService.savecity(area,country);
        }
        return ResultVoUtil.success("成功");
    }


}