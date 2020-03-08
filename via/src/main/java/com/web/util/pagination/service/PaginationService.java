package com.web.util.pagination.service;

import com.web.util.pagination.model.PageDto;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PaginationService implements Serializable {

    /**
     * Return the pagination object, which contain the pagination page number and the current page info
     * @param pageSize The max num of item for each page
     * @param maxPage The maximum length of the pagination bar
     * @param startPage The start page number of the pagination
     * @param totalItem The total number of item of the current page object
     * @return
     */
    @Transactional
    public PageDto getPageList(int pageSize, int maxPage, int startPage, int totalItem){

        PageDto pagination = new PageDto();

        Map<String, Integer> resultMap = getPageNum(pageSize, totalItem);
        int totalPage = resultMap.get("totalPage");
        int endPage = startPage + maxPage;

        if(endPage > totalPage)
            endPage = totalPage + 1; //If don't add 1 page, the below FOR LOOP will print one more page
       // if(endPage - startPage )
//        if(endPage - startPage < maxPage){
//            if(startPage - maxPage - (endPage - startPage) > 0)
//                startPage -= maxPage - (endPage - startPage);
//        }



        List<Integer> totalPageList = new ArrayList<>();
        int startCount = 0;
        int endCount = 0;

        //For first 5 elements
        if(totalPage <= maxPage || startPage <= maxPage / 2 ){
            startCount = 1;
            endCount = startCount + maxPage;
            if(endCount > totalPage)
                endCount = totalPage + 1;
        }else if(startPage > totalPage - (maxPage / 2) ){ //For the last 5 elements
            startCount = totalPage + 1 - maxPage;
            endCount = totalPage + 1;
        }
        else if(startPage > maxPage / 2){ // current page into middle of the pagination
            startCount = startPage - ( maxPage / 2);
            endCount = startCount + maxPage;
        }

        for(int count = startCount; count < endCount; count++){
            totalPageList.add(count);
        }



        //Start create pagination object with different information.
        pagination.setPageList(totalPageList);
        pagination.setTotalItem(resultMap.get("totalItem"));
        pagination.setTotalPage(resultMap.get("totalPage"));
        pagination.setItemReminder(resultMap.get("itemReminder"));
        pagination.setPerivousPage(calculatePerviousPage(startPage));
        pagination.setNextPage(calculateNextPage(startPage, totalPage ));
        pagination.setCurrentPage(startPage);
        pagination.setEndPage(endPage);

        return pagination;
    }

    /**
     * Calculate the total num of item, total page and the reminder of item
     * @Param PageSize The max num of item for each page
     * @Param totalItem The total item select count(*) from yourClass.
     * @Return Map<String, Integer> with three information -> totalItem -> totalPage -> itemReminder</>
     * */
    @Transactional
    public Map<String, Integer> getPageNum(int pageSize, int totalItem){

        //Calculate the total num of item, total page and the reminder of item
        int totalPage = totalItem / pageSize;
        int itemReminder = totalItem % 10;

        //If there is reminder, then total page should be added 1
        if(itemReminder > 0)
            totalPage += 1;

        //Add all the result to a result map
        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("totalItem", totalItem);
        resultMap.put("totalPage", totalPage);
        resultMap.put("itemReminder", itemReminder);

        return resultMap;
    }

    public String calculatePerviousPage(int startPage){
        String perviousPage = null;
        if(startPage != 1)
            perviousPage = String.valueOf(startPage - 1);
        else
            perviousPage = String.valueOf(startPage);
        return perviousPage;
    }

    public String calculateNextPage(int endPage, int totalPage){
        String nextPage = null;
        if(endPage != totalPage)
            nextPage = String.valueOf(endPage + 1);
        else
            nextPage = String.valueOf(endPage);
        return nextPage;
    }
}
