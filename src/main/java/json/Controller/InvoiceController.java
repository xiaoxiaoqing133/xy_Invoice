package json.Controller;

import com.alibaba.fastjson.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import json.model.Invoice;
import json.service.InvoiceService;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping(value = "/xytax")

public class InvoiceController {


    @Autowired
    private InvoiceService invoiceService;

/*    //增加用户
    @ResponseBody
    @RequestMapping(value = "/add", produces = {"application/json;charset=UTF-8;"})
    public int addInvoice(Invoice Invoice){
        return InvoiceService.addInvoice(Invoice);
    }*/

    //查询总表
    @ResponseBody
    @RequestMapping(value = "/all", produces = {"application/json;charset=UTF-8"})
    public List<Invoice> Query() {

        return invoiceService.Query();
    }

    //物理分页
    @ResponseBody
    @RequestMapping(value = "/all/{pageNum}/{pageSize}", produces = {"application/json;charset=UTF-8"})
    public Object findAllInvoice(@PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize) {

        return invoiceService.findAllInvoice(pageNum, pageSize);
    }

    //查找重复
    @ResponseBody
    @RequestMapping(value = "/distinct", produces = {"application/json;charset=UTF-8"})
    public Invoice distinct(@RequestBody Map<String, Object> body1, @Param("xyInvoiceCode") String code, @Param("xyInvoiceNum") String num, HttpServletResponse response) throws Exception {
        System.out.println("有人想要单条插入JSON");
/*        for (Map.Entry<String, Object> entry : body1.entrySet()) {
            System.out.println(entry.getKey() + "的value类型为:" + entry.getValue().getClass());

        }*/
        //封装变量
        Invoice temp1Invoice = new Invoice();


        temp1Invoice.setXyInvoiceCode(body1.get("xyInvoiceCode").toString());

        temp1Invoice.setXyInvoiceNum(body1.get("xyInvoiceNum").toString());

        temp1Invoice.setXyInvoiceFlownum(body1.get("xyInvoiceFlownum").toString());

        String cash = body1.get("xyInvoiceCash").toString();
        float invoice_cash = Float.valueOf(cash);
        temp1Invoice.setXyInvoiceCash(invoice_cash);

        String tax = body1.get("xyInvoiceTax").toString();
        float invoice_tax = Float.valueOf(tax);
        temp1Invoice.setXyInvoiceTax(invoice_tax);

        String total = body1.get("xyInvoiceTotal").toString();
        float invoice_total = Float.valueOf(total);
        temp1Invoice.setXyInvoiceTotal(invoice_total);

        temp1Invoice.setXyInvoiceType(body1.get("xyInvoiceType").toString());
        temp1Invoice.setXyNote(body1.get("xyNote").toString());
        temp1Invoice.setXyBuyername(body1.get("xyBuyername").toString());
        temp1Invoice.setXyBuyertaxcode(body1.get("xyBuyertaxcode").toString());
        temp1Invoice.setXyBuyerbankAccount(body1.get("xyBuyerbankAccount").toString());
        temp1Invoice.setXyBuyertel(body1.get("xyBuyertel").toString());
        temp1Invoice.setXySalertaxcode(body1.get("xySalertaxcode").toString());
        temp1Invoice.setXySalername(body1.get("xySalername").toString());
        temp1Invoice.setXySalertel(body1.get("xySalertel").toString());
        temp1Invoice.setXySalerbankaccount(body1.get("xySalerbankaccount").toString());

/*        DateFormat od = new SimpleDateFormat("yyyy-MM-dd");//日期格式化
        Date odate = od.parse(body1.get("xyOdate").toString());
        temp1Invoice.setXyOdate(odate);*/
        temp1Invoice.setXyOdate(body1.get("xyOdate").toString());

        temp1Invoice.setXyV(body1.get("xyV").toString());
        temp1Invoice.setXyR(body1.get("xyR").toString());
        temp1Invoice.setXyPeople(body1.get("xyPeople").toString());


/*        DateFormat rd = new SimpleDateFormat("yyyy-MM-dd");//日期格式化
        Date rdate = rd.parse(body1.get("xyRdate").toString());
        temp1Invoice.setXyRdate(rdate);*/

        temp1Invoice.setXyRdate(body1.get("xyRdate").toString());

/*        if (temp1Invoice != null) {//判断集合为空
            response.getWriter().write("ok");
        } else {
            response.getWriter().write("no");
        }*/

/*        List<Invoice> ff = invoiceService.dis(temp1Invoice);

        System.out.print(ff);*/
        Invoice result = invoiceService.addData(temp1Invoice);
        return result;

    }

    //成组解析
    @ResponseBody
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @RequestMapping(value = "/json", produces = "application/json;charset=UTF-8")
    public List<Invoice> json(@RequestBody JSONArray params) {
        System.out.println("有人成组插入JSON");
        //定义一个list对象来存放实体对象
        List<Invoice> receptInvoiceList = new ArrayList();

        for (int i = 0; i < params.size(); i++) {
            try {
                //定义一个实体对象来存入JsonArray，放这里主要很吃内存，但是可以实现数组存入
                Invoice tempInvoice = new Invoice();
                //用来决定Json对象要循环几次解析
                JSONObject jsonObj = params.getJSONObject(i);

                //把json的数据放进实体对象中
                tempInvoice.setXyId(jsonObj.getInteger("xyId"));
                tempInvoice.setXyInvoiceCode(jsonObj.getString("xyInvoiceCode"));
                tempInvoice.setXyInvoiceNum(jsonObj.getString("xyInvoiceNum"));
                tempInvoice.setXyInvoiceFlownum(jsonObj.getString("xyInvoiceFlownum"));

                tempInvoice.setXyInvoiceCash(jsonObj.getFloat("xyInvoiceCash"));
                tempInvoice.setXyInvoiceTax(jsonObj.getFloat("xyInvoiceTax"));
                tempInvoice.setXyInvoiceTotal(jsonObj.getFloat("xyInvoiceTotal"));

                tempInvoice.setXyInvoiceType(jsonObj.getString("xyInvoiceType"));
                tempInvoice.setXyNote(jsonObj.getString("xyNote"));
                tempInvoice.setXyBuyername(jsonObj.getString("xyBuyername"));
                tempInvoice.setXyBuyertaxcode(jsonObj.getString("xyBuyertaxcode"));
                tempInvoice.setXyBuyerbankAccount(jsonObj.getString("xyBuyerbankAccount"));
                tempInvoice.setXyBuyertel(jsonObj.getString("xyBuyertel"));
                tempInvoice.setXySalertaxcode(jsonObj.getString("xySalertaxcode"));
                tempInvoice.setXySalername(jsonObj.getString("xySalername"));
                tempInvoice.setXySalertel(jsonObj.getString("xySalertel"));
                tempInvoice.setXySalerbankaccount(jsonObj.getString("xySalerbankaccount"));

                tempInvoice.setXyOdate(jsonObj.getString("xyOdate"));

                tempInvoice.setXyV(jsonObj.getString("xyV"));
                tempInvoice.setXyR(jsonObj.getString("xyR"));
                tempInvoice.setXyPeople(jsonObj.getString("xyPeople"));

                tempInvoice.setXyRdate(jsonObj.getString("xyRdate"));




                //依次把实体对象的数据，添加进list对象，形成表单
                receptInvoiceList.add(tempInvoice);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        //执行将参数表单通过接口方法传入service层，实现方法后，返回list对象出来
        return invoiceService.checkRepeat(receptInvoiceList);

    }
}