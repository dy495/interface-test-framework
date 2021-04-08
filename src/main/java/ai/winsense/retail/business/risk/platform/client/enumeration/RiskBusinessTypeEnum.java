package ai.winsense.retail.business.risk.platform.client.enumeration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ：Created by yanghang on 2021/3/31.  15:44
 */
public enum  RiskBusinessTypeEnum {

    /**
     *
     */
    FIRST_INSPECTION("首次检查"),
    INSURANCE_CONTRACT_CUSTOMERS("保险合同客户"),
    PDI_FACTORY("PDI-厂家"),
    INSURANCE_DIRECT_INDEMNITY("保险直赔"),
    ROUTINE_MAINTENANCE("常规维修"),
    RESCUE("救援"),
    INSURANCE_MARKETING("保险营销"),
    GROUP_INTERNAL_MAINTENANCE("集团内部维修"),
    KEY_ACCOUNT_CONTRACT("大客户合同"),
    ZERO_FEE("零费"),
    WARRANTY_CLAIM("保修索赔"),
    INSURANCE_OWN_EXPENSE("保险自费"),
    REGULAR_MAINTENANCE("定期保养"),
    FIRST_MAINTENANCE("首保"),
    SHOP_MAINTENANCE("本店内修"),
    PDI_INNER("PDI-内部"),
    INTERNAL_FRICTION("内耗"),
    SHEET_METAL_SPRAY_OWN_EXPENSE("自费钣喷"),
    FREE_INSPECTION("免费检测"),
    REWORK_MAINTENANCE("返修"),
    DECORATE("装饰"),

    ;

    private String name;

    public String getName() {
        return name;
    }

    RiskBusinessTypeEnum(String name) {
        this.name = name;
    }
}