package com.linln.admin.finanace.controller;

import com.linln.modules.elite.elitesdemand.domain.Demandlist;
import com.linln.modules.elite.elitesdemand.service.DemandlistService;
import com.linln.modules.finanace.domain.Finanaceorderss;
import com.linln.modules.finanace.service.FinanaceorderssService;
import com.linln.admin.finanace.validator.FinanaceorderssValid;
import com.linln.common.enums.StatusEnum;
import com.linln.common.utils.EntityBeanUtil;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.utils.StatusUtil;
import com.linln.common.vo.ResultVo;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
    @GetMapping(value ={"/index","/nopaymentindex","/paymentindex","/purchaseOrderReview"})
    @RequiresPermissions(value={"finanace:finanaceorderss:index"})
    public String index(Model model, Finanaceorderss finanaceorderss, HttpServletRequest request) {
        String a = request.getRequestURI();
        //付款中 prepayment
        if (a.equals("/finanace/finanaceorderss/paymentindex")){
            finanaceorderss.setOrderstatus("prepayment");
        }else if (a.equals("/finanace/finanaceorderss/nopaymentindex")){
            finanaceorderss.setOrderstatus("prenopayment" );
        }else if (a.equals("/finanace/finanaceorderss/purchaseOrderReview")){
            finanaceorderss.setOrderstatus("prechecking");
        }
        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("id", match -> match.contains());

        // 获取数据列表
        Example<Finanaceorderss> example = Example.of(finanaceorderss, matcher);
        Page<Finanaceorderss> list = finanaceorderssService.getPageList(example);
        User user = ShiroUtil.getSubject();
        //2019/11/27新加需求 计算采购总价
        //Long id = list.getContent().get(1).getId();
        for (int i = 0; i <list.getContent().size() ; i++) {
            Long id = list.getContent().get(i).getId();
            //取出相同订单ID的价格
            Double allprice = demandlistService.getAllprice(id);
            list.getContent().get(i).setAllprice(allprice);
        }

        // 封装数据
        model.addAttribute("user",user);
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        if (a.equals("/finanace/finanaceorderss/nopaymentindex")){
            return "/finanace/finanaceorderss/index";
        }else if (a.equals("/finanace/finanaceorderss/purchaseOrderReview")){
            return "/finanace/finanaceorderss/purchaseOrderReview";
        }else{
            return "/finanace/finanaceorderss/paymentindex";
        }

    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("finanace:finanaceorderss:add")
    public String toAdd(Model model) {
        User user = ShiroUtil.getSubject();
        model.addAttribute("user" , user);
        return "/finanace/finanaceorderss/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("finanace:finanaceorderss:edit")
    public String toEdit(@PathVariable("id") Finanaceorderss finanaceorderss, Model model) {
        model.addAttribute("finanaceorderss", finanaceorderss);
        User user = ShiroUtil.getSubject();
        model.addAttribute("user" , user);
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
                         @RequestParam(name = "ids", required = false) List<Long> ids) {
        // 复制保留无需修改的数据
        if (finanaceorderss.getId() != null) {
            Finanaceorderss beFinanaceorderss = finanaceorderssService.getById(finanaceorderss.getId());
            EntityBeanUtil.copyProperties(beFinanaceorderss, finanaceorderss);
        }
        //开始的创建都是 checking
        finanaceorderss.setOrderstatus("prechecking");
        User user = ShiroUtil.getSubject();
        //demandlist.setDynamics(user.getUsername());
        finanaceorderss.setInitiators(user.getUsername());
        //保存发票日期 即为今天的日期
        finanaceorderss.setCheckdate(new Date());
        // 保存数据
        Finanaceorderss f = finanaceorderssService.save(finanaceorderss);

        //获取刚刚添加的这个数据的ID
        if (ids != null){
            //这些箱子需要关联订单ID 改变状态 发票日期
            for (int i = 0; i <ids.size(); i++) {
                //获取现在箱子的详细信息
                Demandlist de = demandlistService.getById(ids.get(i));
                //复制当前数据
                de.setDynamics("prechecking");
                de.setFinanaceid(f.getId());
                //改变箱子的信息,改变状态和关联订单信息(service加入了更新发票日期)
                demandlistService.updatedemanddynamics("prechecking",f.getId(),ids.get(i));
                //12/8 在提交审核 就要EQUM看见
            }
        }
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/demanddetails/{id}")
    @RequiresPermissions("finanace:finanaceorderss:demanddetails")
    public String toDetail(@PathVariable("id") Finanaceorderss finanaceorderss, Model model) {

        Demandlist demandlist = new Demandlist();
        demandlist.setFinanaceid(finanaceorderss.getId());

        Example<Demandlist> example = Example.of(demandlist);
        //Page<Demandlist> demandlist =finanaceorderssService.getPageList(finanaceorderss.getId());
        Page<Demandlist> demandlist1 = demandlistService.getPageList(example);
        // 封装数据
        model.addAttribute("list", demandlist1.getContent());
        model.addAttribute("page", demandlist1);
        return "/finanace/finanaceorderss/demanddetails";
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