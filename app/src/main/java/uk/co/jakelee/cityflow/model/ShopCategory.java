package uk.co.jakelee.cityflow.model;


import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

public class ShopCategory extends SugarRecord{
    private int categoryId;

    public ShopCategory() {
    }

    public ShopCategory(int categoryId) {
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

    public static List<ShopItem> getItems(int categoryId) {
        return Select.from(ShopItem.class).where(
                Condition.prop("category_id").eq(categoryId)).list();
    }
}
