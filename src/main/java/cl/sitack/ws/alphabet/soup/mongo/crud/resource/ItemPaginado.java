package cl.sitack.ws.alphabet.soup.mongo.crud.resource;

import java.util.List;

public class ItemPaginado<Item> {
    private List<Item> itemList;
    private Paginado paginate;

    public ItemPaginado() {
    }

    public ItemPaginado(List<Item> itemList, Paginado paginate) {
        this.itemList = itemList;
        this.paginate = paginate;
    }

    public List<Item> getItemList() {
        return this.itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public Paginado getPaginate() {
        return this.paginate;
    }

    public void setPaginate(Paginado paginate) {
        this.paginate = paginate;
    }
}
