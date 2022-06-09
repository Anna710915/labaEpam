package com.epam.esm.pagination;

/**
 * The type Pagination.
 * @author Anna Merkul
 */
public class Pagination {
    /**
     * Offset int.
     *
     * @param pageIndex the page index
     * @param pageSize  the page size
     * @return the int
     */
    public static int offset(int pageIndex, int pageSize){
        return pageSize * (pageIndex - 1);
    }

    /**
     * Find pages int.
     *
     * @param totalRecords the total records
     * @param pageSize     the page size
     * @return the int
     */
    public static int findPages(int totalRecords, int pageSize){
        return totalRecords / pageSize;
    }

    /**
     * Find last page int.
     *
     * @param pages        the pages
     * @param pageSize     the page size
     * @param totalRecords the total records
     * @return the int
     */
    public static int findLastPage(int pages, int pageSize, int totalRecords){
        return pages * pageSize < totalRecords ? pages + 1 : pages;
    }

    /**
     * Find prev page int.
     *
     * @param currentPage the current page
     * @return the int
     */
    public static int findPrevPage(int currentPage){
        return currentPage - 1 > 0 ? currentPage - 1 : 1;
    }

    /**
     * Find next page int.
     *
     * @param currentPage the current page
     * @param lastPage    the last page
     * @return the int
     */
    public static int findNextPage(int currentPage, int lastPage){
        return Math.min(currentPage + 1, lastPage);
    }
}
