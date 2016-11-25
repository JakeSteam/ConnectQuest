package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

public class SupportCode extends SugarRecord {
    private String supportCode;

    public SupportCode() {
    }

    public SupportCode(String supportCode) {
        this.supportCode = supportCode;
    }

    public String getSupportCode() {
        return supportCode;
    }

    public void setSupportCode(String supportCode) {
        this.supportCode = supportCode;
    }

    public static boolean alreadyApplied(String supportCode) {
        return Select.from(SupportCode.class).where(
                Condition.prop("support_code").eq(supportCode)).count() > 0;
    }
}
