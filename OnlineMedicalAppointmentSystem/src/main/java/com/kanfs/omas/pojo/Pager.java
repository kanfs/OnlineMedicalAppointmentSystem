package com.kanfs.omas.pojo;


public class Pager {
    private int curPage;           // 当前显示页
    private int perPageRows;       // 每页显示行数
    private int rowCount;          // 总行数
    private int pageCount;         // 总页数

    public int getPageCount(){
        return (rowCount + perPageRows - 1) / perPageRows;
    }
    public int getFirstLimitParam(){
        return (curPage - 1) * perPageRows;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getPerPageRows() {
        return perPageRows;
    }

    public void setPerPageRows(int perPageRows) {
        this.perPageRows = perPageRows;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
}
