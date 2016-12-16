package uk.co.jakelee.cityflow.model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import uk.co.jakelee.cityflow.helper.EncryptHelper;

public class SupportCode extends SugarRecord {
    private String supportCode;

    public SupportCode() {
    }

    public SupportCode(String supportCode) {
        this.supportCode = EncryptHelper.encode(supportCode);
    }

    public String getSupportCode() {
        return EncryptHelper.decode(supportCode);
    }

    public void setSupportCode(String supportCode) {
        this.supportCode = EncryptHelper.encode(supportCode);
    }

    public static boolean alreadyApplied(String supportCode) {
        return Select.from(SupportCode.class).where(
                Condition.prop("support_code").eq(EncryptHelper.encode(supportCode))).count() > 0;
    }
}
