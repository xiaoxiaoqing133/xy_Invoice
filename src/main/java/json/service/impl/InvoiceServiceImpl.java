package json.service.impl;


import com.github.pagehelper.PageHelper;
import json.mapper.InvoiceMapper;
import json.model.Invoice;
import json.service.InvoiceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;



@Service(value = "InvoiceService")
public class InvoiceServiceImpl implements InvoiceService {
    //这个注解是用来自动帮我省略set/get
    @Autowired
    //这里会报错，但是并不会影响，将接口方法私有化
    private InvoiceMapper invoiceMapper;

    /*
     * 这个方法中用到了我们开头配置依赖的分页插件pagehelper
     * 很简单，只需要在service层传入参数，然后将参数传递给一个插件的一个静态方法即可；
     * pageNum 开始页数
     * pageSize 每页显示的数据条数
     * */
    //物理翻页
    @Override
    public List<Invoice> findAllInvoice(int pageNum, int pageSize) {
        //将参数传给这个方法就可以实现物理分页了，非常简单。
        PageHelper.startPage(pageNum, pageSize);
        return invoiceMapper.selectAllInvoice();
    }

    //成组检查重复并插入
    @Override//继承父类的方法，对其覆盖
    public List<Invoice> checkRepeat(List<Invoice> receptInvoiceList) {
        //定义一个新的实体对象
        Invoice tempInvoice = new Invoice();
        //定义一个新的表单对象
        List<Invoice> res = new ArrayList();
        //定义一个可变长的字符串，里面带个符号
        StringBuffer code = new StringBuffer("(");

        //整个循环过程是为了形成（xx,xxx,xxxx）,(xx,xxx,xxxx)...
        if (receptInvoiceList != null) {
            for (int i = 0; i < receptInvoiceList.size(); i++) {
                //根据表单的长度决定循环次数
                tempInvoice = receptInvoiceList.get(i);
                code.append(tempInvoice.getXyInvoiceCode());
                code.append(",");
                code.append(tempInvoice.getXyInvoiceNum());
                //执行到这里添加需要保证下次循环开头也有符号
                code.append("),(");
            }
            //出来的结果需要减去后面2位，原因在上面
            code.delete(code.length() - 2, code.length());
            //打印看一下，结果格式对不对，数据有没正确传进来
            System.out.print(code.toString());
            //执行mapper接口方法将参数送入mapping层，执行SQL查询重复，返回形成一个list对象
            List<Invoice> result = invoiceMapper.selectForCheckRepeat(code.toString());

            try {
                //如果返回的list对象数目大于0
                if (result.size() > 0){
                    //返回这个list对象
                    res = result;
                } else {
                    //否则执行mapper接口方法将表单参数传入mappeing层，执行SQL插入list对象，返回插入个数
                    int res1 = invoiceMapper.saveArray(receptInvoiceList);
                    //如果插入个数大于0
                    if (res1 > 0){
                        //定义一个动态数组
                        ArrayList al = new ArrayList();
                        //添加一个当前插入返回的行数；
                        al.add(res1);
                        //返回这个数组
                        res = al;
                    }else {
                        //定义一个动态数组
                        ArrayList al = new ArrayList();
                        //添加0进入数组来界定空插
                        al.add(0);
                        //返回这个数组
                        res = al;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    //查找总表
    @Override
    public List<Invoice> Query() {
        return invoiceMapper.Query();
    }

    //单条检查重复并插入
    @Override
    public Invoice addData(Invoice temp) {
        String code = temp.getXyInvoiceCode();
        String num = temp.getXyInvoiceNum();
        Invoice res = new Invoice();
        try {

            Invoice tempInoice = invoiceMapper.Aldis(code, num);
            if (tempInoice != null) {
                 res = tempInoice;
            } else {
                invoiceMapper.insert(temp);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

}

