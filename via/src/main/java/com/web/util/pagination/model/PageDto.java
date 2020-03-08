package com.web.util.pagination.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The pagination Dto for storing pagination info.
 */

public class PageDto implements Serializable {

    private int currentPage;

    private int endPage;

    private int totalItem;

    private int totalPage;

    private String perivousPage = "";

    private String nextPage = "";

    private int itemReminder;

    private List<Integer> pageList = new ArrayList<>();

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public String getPerivousPage() {
        return perivousPage;
    }

    public void setPerivousPage(String perivousPage) {
        this.perivousPage = perivousPage;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public int getItemReminder() {
        return itemReminder;
    }

    public void setItemReminder(int itemReminder) {
        this.itemReminder = itemReminder;
    }

    public List<Integer> getPageList() {
        return pageList;
    }

    public void setPageList(List<Integer> pageList) {
        this.pageList = pageList;
    }
}
