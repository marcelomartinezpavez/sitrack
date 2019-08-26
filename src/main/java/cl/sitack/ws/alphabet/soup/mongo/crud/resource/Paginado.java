package cl.sitack.ws.alphabet.soup.mongo.crud.resource;

public class Paginado {
    private String firstPageUrl;
    private String lastPageUrl;
    private String nextPageUrl;
    private String prevPageUrl;
    private Integer limit;
    private Long page;
    private Long total;

    public Paginado() {
    }

    public Paginado(String firstPageUrl, String lastPageUrl, String nextPageUrl, String prevPageUrl, Integer limit, Long page, Long total) {
        this.firstPageUrl = firstPageUrl;
        this.lastPageUrl = lastPageUrl;
        this.nextPageUrl = nextPageUrl;
        this.prevPageUrl = prevPageUrl;
        this.limit = limit;
        this.page = page;
        this.total = total;
    }

    public String getFirstPageUrl() {
        return this.firstPageUrl;
    }

    public void setFirstPageUrl(String firstPageUrl) {
        this.firstPageUrl = firstPageUrl;
    }

    public String getLastPageUrl() {
        return this.lastPageUrl;
    }

    public void setLastPageUrl(String lastPageUrl) {
        this.lastPageUrl = lastPageUrl;
    }

    public String getNextPageUrl() {
        return this.nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public String getPrevPageUrl() {
        return this.prevPageUrl;
    }

    public void setPrevPageUrl(String prevPageUrl) {
        this.prevPageUrl = prevPageUrl;
    }

    public Integer getLimit() {
        return this.limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Long getPage() {
        return this.page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Long getTotal() {
        return this.total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
