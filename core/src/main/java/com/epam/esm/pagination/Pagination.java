package com.epam.esm.pagination;

public class Pagination {
    public static int offset(int pageIndex, int pageSize){
        return pageSize * (pageIndex - 1);
    }
    public static int findPages(int totalRecords, int pageSize){
        return totalRecords / pageSize;
    }
    public static int findLastPage(int pages, int pageSize, int totalRecords){
        return pages * pageSize < totalRecords ? pages + 1 : pages;
    }
    public static int findPrevPage(int currentPage){
        return currentPage - 1 > 0 ? currentPage - 1 : 1;
    }
    public static int findNextPage(int currentPage, int lastPage){
        return Math.min(currentPage + 1, lastPage);
    }
}
