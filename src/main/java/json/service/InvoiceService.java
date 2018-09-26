package json.service;


import json.model.Invoice;
import java.util.List;

public interface InvoiceService {


    List<Invoice> Query();

    Invoice addData(Invoice temp);
    

    Object findAllInvoice(int pageNum, int pageSize);

    //出参                    //入参
    List<Invoice> checkRepeat(List<Invoice> receptInvoiceList);
}


