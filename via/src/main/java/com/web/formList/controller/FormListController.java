package com.web.formList.controller;

import com.via.entity.Product;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.web.formList.model.FormList;
import com.web.formList.service.FormListService;
import com.web.util.pagination.model.PageDto;
import com.web.util.pagination.service.PaginationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class FormListController {

    private final static Logger logger = LoggerFactory.getLogger(FormListController.class);

    @Autowired
    private FormListService service;

    @Autowired
    private PaginationService paginationService;


    @RequestMapping(value = {"/form-list/form-list"}, method = RequestMethod.GET)
    public String formList(Model model,
                           @RequestParam("page") Optional<Integer> page,
                           @RequestParam("size") Optional<Integer> size,
                           @RequestParam(value = "text", required = false) String text,
                           @RequestParam(value = "id", required = false) Integer searchId,
                           @RequestParam(value = "name", required = false) String staffName,
                           @RequestParam(value = "mail", required = false) String mail,
                           @RequestParam(value = "time", required = false) String submitTimeRange,
                           @RequestParam(value = "status", required = false) String status
                   ){

        /**Default page size and current page*/
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);
        Map<String, Object> searchCriteria = new HashMap<>();


        //Set the searchCriteria set.
        searchCriteria.put("id", searchId);
        searchCriteria.put("borrowerName", staffName);
        searchCriteria.put("email", mail);
        searchCriteria.put("createTime", submitTimeRange);
        searchCriteria.put("status", status);

        String searchText = "";
        if(text != null && !text.isEmpty())
            searchText += text;

        Page<Product> itemList = service.findPaginated(PageRequest.of(currentPage - 1, pageSize), searchCriteria);

        FormList formList = new FormList();
        formList.setProductList(service.findAll());
        PageDto pagination = paginationService.getPageList(pageSize, 5, currentPage, (int)itemList.getTotalElements());

        model.addAttribute("formList", itemList);
        model.addAttribute("pagination", pagination);
        model.addAttribute("searchText", searchText);
        model.addAttribute("id", searchId == null ? "" : searchId);
        model.addAttribute("name", staffName == null ? "" : staffName);
        model.addAttribute("mail", mail == null ? "" : mail);
        model.addAttribute("time", submitTimeRange == null ? "" : submitTimeRange);
        model.addAttribute("status", status == null ? "" : status);

        return "/form-list/form-list";
    }

    @RequestMapping(value = {"/form-list/csv"}, method = RequestMethod.GET)
    public void downloadCSV(Model model, @RequestParam(value = "datetimes", required = false) String dateRange, HttpServletResponse response){

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        try{
            String filename = new Date().toString();
            response.setHeader("Content-Disposition", "attachment; filename=\"form-list_+" + filename + "+.csv\"");

            dateRange = dateRange.trim();
            String[] rangeSplit = null;
            if(dateRange.indexOf('-') > 0)
                rangeSplit = dateRange.split("-");

            if(rangeSplit != null){
                Date startDate = formatter.parse(rangeSplit[0].trim());
                Date endDate = formatter.parse(rangeSplit[1].trim());

                Writer writer = response.getWriter();

                CSVWriter csvWriter = new CSVWriter(writer,
                        CSVWriter.DEFAULT_SEPARATOR,
                        CSVWriter.NO_QUOTE_CHARACTER,
                        CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                        CSVWriter.DEFAULT_LINE_END);

                String[] headerRecord = {"id", "Staff Name", "Email", "Phone Num", "Create Date", "Status"};
                csvWriter.writeNext(headerRecord);


                List<Product> dataList = service.getFormList(startDate, endDate);


                StatefulBeanToCsv<Product> beanToCsv = new StatefulBeanToCsvBuilder(writer)
                        .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                        .build();

                beanToCsv.write(dataList);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
