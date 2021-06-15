package com.ziperp.dms.common.paging

class PagingParam {
    var currentPage = 1
    var loadingPage = 1
    var totalPage = 1
    var totalItems = 0

    fun clear() {
        currentPage = 1
        loadingPage = 1
        totalPage = 1
    }

    fun hasNext(): Boolean {
        return currentPage < totalPage
    }

    fun nextPage(isLoadMore: Boolean): Int {
        loadingPage = if (isLoadMore) currentPage + 1 else currentPage
        return loadingPage
    }

    fun updateTotalPage(totalPage: Int) {
        this.totalPage = totalPage
    }

    fun updateTotalItems(totalItems: Int) {
        this.totalItems = totalItems
    }

    fun loadedPage(){
        currentPage = loadingPage
    }
}