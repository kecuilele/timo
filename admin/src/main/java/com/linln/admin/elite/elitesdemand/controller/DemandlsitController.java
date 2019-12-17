package com.linln.admin.elite.elitesdemand.controller;
import java.io.IOException;
import java.io.InputStream;

import com.linln.modules.dictionary.service.CountrylistService;
import com.linln.modules.elite.elitesdemand.domain.Demandlist;
import com.linln.modules.elite.elitesdemand.repository.DemandlistRepository;
import com.linln.modules.elite.elitesdemand.service.DemandlistService;
import com.linln.admin.elitesdemand.validator.DemandlsitValid;
import com.linln.modules.equm.domain.Inventorys;
import com.linln.modules.equm.service.InventorysService;
import com.linln.modules.finanace.service.FinanaceorderssService;
import com.linln.common.enums.StatusEnum;
import com.linln.common.utils.EntityBeanUtil;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.utils.StatusUtil;
import com.linln.common.vo.ResultVo;
import com.linln.component.excel.ExcelUtil;
import com.linln.component.shiro.ShiroUtil;
import com.linln.modules.system.domain.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author cuike
 * @date 2019/11/18
 */
@SuppressWarnings("ALL")
@Controller
@RequestMapping("/elitesdemand/demandlsit")
public class DemandlsitController {

    @Autowired
    private DemandlistService demandlistService;

    @Autowired
    private FinanaceorderssService finanaceorderssService;

    @Autowired
    private DemandlistRepository demandlistRepository;

    @Autowired
    private InventorysService inventorysSevrice;

    @Autowired
    private CountrylistService countrylistService;

    /**
     * 列表页面
     */
    @GetMapping(value = {"/index","/paymentindex","nopaymentindex"})
    @RequiresPermissions("elitesdemand:demandlsit:index")
    public String index(Model model, Demandlist demandlist, HttpServletRequest request) {
        //订单页面只要看到 dynamics=prestart(开始录入)状态的箱子
        //demandlist.setDynamics("prestart");
        String a =request.getRequestURI();
        if (a.equals("/elitesdemand/demandlsit/paymentindex")){
            demandlist.setDynamics("preout");
        }else if (a.equals("/elitesdemand/demandlsit/nopaymentindex")){
            demandlist.setDynamics("prestart");
        }

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                //.withMatcher("unitid", match -> match.contains())
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
        Example<Demandlist> example = Example.of(demandlist, matcher);
        Page<Demandlist> list = demandlistService.getPageList(example);
//        //Invoicedate循环遍历成String类型(可优化)
//        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        User user = ShiroUtil.getSubject();
        model.addAttribute("user" , user);
        //Date date = new Date();
        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        if (a.equals("/elitesdemand/demandlsit/paymentindex")){
            return "/elitesdemand/demandlsit/paymentindex";
        }else if (a.equals("/elitesdemand/demandlsit/nopaymentindex")){
            return "/elitesdemand/demandlsit/index";
        }else {
            return "/elitesdemand/demandlsit/demandlog";
        }

    }
    /*
    * 付款完毕对未提单的货物进行展现
    *
    * */
    @GetMapping(value = {"/A1nobillofladingindex","/A2nobillofladingindex",
            "/A3nobillofladingindex","/A4nobillofladingindex","/A5nobillofladingindex",
            "/A6nobillofladingindex","/nobillofladingindex"})
    @RequiresPermissions(value = {"elitesdemand:demandlsit:A1nobillofladingindex",
            "elitesdemand:demandlsit:A2nobillofladingindex", "elitesdemand:demandlsit:A3nobillofladingindex",
            "elitesdemand:demandlsit:A4nobillofladingindex", "elitesdemand:demandlsit:A5nobillofladingindex",
            "elitesdemand:demandlsit:A6nobillofladingindex", "elitesdemand:demandlsit:nobillofladingindex"})
    public String nobillofladingindex(Model model, Demandlist demandlist, HttpServletRequest request){
        //从页面只能看到一个状态的就是 付款的prepayment 只不过有区域
        demandlist.setDynamics("prepayment");
        // 根据请求权限显示地区设置
//        String a = request.getRequestURI();
//        if (a.equals("/elitesdemand/demandlsit/A1nobillofladingindex")){
//            demandlist.setArea("A1");
//        }else if (a.equals("/elitesdemand/demandlsit/A2nobillofladingindex")){
//            demandlist.setArea("A2" );
//        }else if (a.equals("/elitesdemand/demandlsit/A3nobillofladingindex")){
//            demandlist.setArea("A3" );
//        }else if (a.equals("/elitesdemand/demandlsit/A4nobillofladingindex")){
//            demandlist.setArea("A4" );
//        }else if (a.equals("/elitesdemand/demandlsit/A5nobillofladingindex")){
//            demandlist.setArea("A5" );
//        }else if (a.equals("/elitesdemand/demandlsit/A6nobillofladingindex")){
//            demandlist.setArea("A6" );
//        }
        // 获取数据列表
        User user = ShiroUtil.getSubject();

        //根据用户ID 获取国家ID
        List<Long> countryID  = inventorysSevrice.getCountryByUserId(user.getId());

        //取国家数组
        List <String> countryname = countrylistService.getCountryByCountryID(countryID);

        Example<Demandlist> example = Example.of(demandlist);
        Page < Demandlist> list2 = demandlistService.getByCountry(countryname);
        Page<Demandlist> list = demandlistService.getPageList(example);
        model.addAttribute("user" , user);
        //Date date = new Date();
        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/elitesdemand/demandlsit/nobillofladingindex";
    }


    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("elitesdemand:demandlsit:add")
    public String toAdd(Model model) {
        //封装用户信息
        User user = ShiroUtil.getSubject();
        model.addAttribute("user" , user);
        return "/elitesdemand/demandlsit/add";
    }

    @GetMapping("/adddemand")
    @RequiresPermissions("elitesdemand:demandlsit:adddemand")
    public String toAdddemand(@RequestParam("ids") String ids, Model model) {
        model.addAttribute("ids", ids);
        //封装用户信息
        User user = ShiroUtil.getSubject();
        model.addAttribute("user" , user);
        return "/elitesdemand/demandlsit/adddemand";
    }


    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("elitesdemand:demandlsit:edit")
    public String toEdit(@PathVariable("id") Demandlist demandlist, Model model) {
        model.addAttribute("demandlsit", demandlist);
//        //封装用户信息
//        User user = ShiroUtil.getSubject();
//        model.addAttribute("user" , user);
        return "/elitesdemand/demandlsit/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"elitesdemand:demandlsit:add", "elitesdemand:demandlsit:edit"})
    @ResponseBody
    public ResultVo save(@Validated DemandlsitValid valid, Demandlist demandlsit) {

        //DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        // 复制保留无需修改的数据
        if (demandlsit.getId() != null) {
            Demandlist beDemandlsit = demandlistService.getById(demandlsit.getId());
            EntityBeanUtil.copyProperties(beDemandlsit, demandlsit);
        }
        User user = ShiroUtil.getSubject();
        demandlsit.setPurchaser(user.getUsername());
        //默认箱货状态为 pre-start 在采购页面中箱子的状态永远都是pre-start
        demandlsit.setDynamics("prestart");
        // 保存数据
        demandlistService.save(demandlsit);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("elitesdemand:demandlsit:detail")
    public String toDetail(@PathVariable("id") Demandlist demandlsit, Model model) {
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
        if (demandlistService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }


    /*
    * 提交审核 还未付款 状态为 prenopayment
    *
    *
    * */
    @RequestMapping(value={"confirmayment"})
    @RequiresPermissions("elitesdemand:demandlsit:confirmayment")
    @ResponseBody
    public ResultVo confirmayment(
            @RequestParam(value = "id") Long id){
            String  purchasestatus = "prenopayment";
            if (finanaceorderssService.selectOrderStatus(id).equals("prenopayment ")){
                return ResultVoUtil.error("订单可能已提交审核,请联系管理员查看");
            }
            Inventorys inventorys = new Inventorys();
            if (demandlistService.updatepaymentdynamics(id,purchasestatus)) {
                //还需对订单状态进行操作
                finanaceorderssService.updateOrderstatusByid(purchasestatus,id);
                //12/5 增加对equm资料的保存 在提交审核equm工作人员 就可以看见
                //找出相同订单的箱子信息
                List<Demandlist> demandlists = demandlistService.findByfinanceid(id);
                //遍历循环复制信息
//                for (int i = 0; i < demandlists.size(); i++) {
//                    BeanUtils.copyProperties(demandlists.get(i),inventorys);
//                    //对这些刚获取到的箱子
//                    inventorysSevrice.save(inventorys);
//                    System.out.println("---------同步到EQUM成功---------");
//                }
                return ResultVoUtil.success("成功操作");
            } else {
                return ResultVoUtil.error("对订单和货品状态更新操作失败");
            }
    }

    /*
    * 提交审核完毕,变为待确认付款状态
    *@param valid
    * */

    @RequestMapping(value={"submitaudit"})
    @RequiresPermissions("elitesdemand:demandlsit:submitaudit")
    @ResponseBody
    public ResultVo submitaudit(
            @RequestParam(value = "id") Long id, HttpServletRequest request){
        String requesturl = request.getRequestURI();
        String purchasestatus = "prepayment";
        if (requesturl.equals("/elitesdemand/demandlsit/submitaudit")){
            if (finanaceorderssService.selectOrderStatus(id).equals("prepayment")){
                return ResultVoUtil.error("订单可能已确认付款,请联系管理员查看");
            }
        }
        if (demandlistService.updatepaymentdynamics(id,purchasestatus)) {
            //还需对订单状态进行操作
            finanaceorderssService.updateOrderstatusByid(purchasestatus,id);

            return ResultVoUtil.success("成功操作");
        } else {
            return ResultVoUtil.error("对订单和货品状态更新操作失败");
        }
    }

    /*
    * Excel 导入
    *
    * */
    @PostMapping(value = "/upload")
    @ResponseBody
    public ResultVo upload(@RequestParam("file") MultipartFile file){
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return ResultVoUtil.error("读取文件失败");
        }
        List<Demandlist> list1 = ExcelUtil.importExcel(Demandlist.class, inputStream);
        for (Demandlist demandlist : list1) {
            demandlist.setDynamics("prestart");
            User user = ShiroUtil.getSubject();
            demandlist.setPurchaser(user.getUsername());
            //导入时发票日期有可能没有
            //发票日期只要给财务的时候有 在采购审核到财务审核那里
            /*if (demandlist.getInvoicedate()==null){
                demandlist.setInvoicedate(new Date());
            }*/
            demandlistService.save(demandlist);

        }
        return ResultVoUtil.success("成功");
    }


    @GetMapping("/export")
    @ResponseBody
    public void exportExcel() {
        ExcelUtil.exportExcel(Demandlist.class, demandlistRepository.findAll());
    }

    /**
     * 对单个箱子进行提单,跳转到编辑页面
     * @param id
     * @return
     */
    @GetMapping("/billoflading/{id}")
    @RequiresPermissions("elitesdemand:demandlsit:billoflading")
    public String billoflading(@PathVariable("id")Demandlist id,Model model){
        Inventorys inventorys = new Inventorys();
        //提单先对箱子详细信息的补充
        //将数据同步到equm储存的类
        Demandlist demandlist = demandlistService.getById(id.getId());

        BeanUtils.copyProperties(demandlist,inventorys);
        //提单自动对提货日期 发票日期进行补全
        Date date = new Date();
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        inventorys.setReceiveddate(sdf.format(date));
        DateFormat df = new SimpleDateFormat("w");
        inventorys.setWeek(df.format(date));
        model.addAttribute("inventorys",inventorys);
        return "/elitesdemand/demandlsit/addinventorys";
    }


}