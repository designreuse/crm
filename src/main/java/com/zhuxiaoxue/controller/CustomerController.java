package com.zhuxiaoxue.controller;

import com.google.common.collect.Maps;
import com.zhuxiaoxue.dto.DataTableReasult;
import com.zhuxiaoxue.pojo.Customer;
import com.zhuxiaoxue.service.CustomerService;
import com.zhuxiaoxue.util.ShiroUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Inject
    private CustomerService customerService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model){
        List<Customer> companyList = customerService.findAllCompany();
        System.out.println(companyList);
        model.addAttribute("companyList",companyList);
        return "/customer/list";
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    public DataTableReasult<Customer> showAll(HttpServletRequest request){
        String draw = request.getParameter("draw");
        String start = request.getParameter("start");
        String length = request.getParameter("length");
        String keyword = request.getParameter("search[value]");

        Map<String,Object> params = Maps.newHashMap();
        params.put("start",start);
        params.put("length",length);
        params.put("keyword",keyword);

        List<Customer> customerList = customerService.findAllByParams(params);
        Long count = customerService.count();
        Long filterCount = customerService.countByParams(params);

        return new DataTableReasult<>(draw,customerList,count,filterCount);
    }

    /**
     * 保存新客户
     * @param customer
     * @return
     */
    @RequestMapping(value = "/new",method = RequestMethod.POST)
    @ResponseBody
    public String addCustomer(Customer customer){
        customerService.addCustomer(customer);
        return "success";
    }


    /**
     * 删除客户
     * @param id
     * @return
     */
    @RequestMapping(value = "/del/{id:\\d+}",method = RequestMethod.GET)
    @ResponseBody
    public String delCustomer(@PathVariable Integer id){
        customerService.delCustomerById(id);
        return "success";
    }

}
