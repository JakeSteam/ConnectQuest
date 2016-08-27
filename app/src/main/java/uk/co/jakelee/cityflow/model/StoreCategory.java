package uk.co.jakelee.cityflow.model;


import com.orm.SugarRecord;

public class StoreCategory extends SugarRecord{
    private int categoryId;

    public StoreCategory() {
    }

    public StoreCategory(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return Text.get("SHOP_CATEGORY_", getCategoryId(), "_NAME");
    }
}
