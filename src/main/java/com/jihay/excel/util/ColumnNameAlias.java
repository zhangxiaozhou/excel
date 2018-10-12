package com.jihay.excel.util;

import java.util.*;

public class ColumnNameAlias {

    public static Map<String, List<String>> columnNameAlias = new HashMap<String, List<String>>();

    static {
        columnNameAlias.put("品名", Arrays.asList(new String[]{"品名", "名称", "描述", "品种", "产地名称", "品种板/卷"}));

        columnNameAlias.put("规格", Arrays.asList(new String[]{"规格", "详细说明", "厚度"}));

        columnNameAlias.put("材质", Arrays.asList(new String[]{"材质", "牌号"}));

        columnNameAlias.put("产地", Arrays.asList(new String[]{"产地", "钢厂"}));

        columnNameAlias.put("价格", Arrays.asList(new String[]{"价格", "单价", "挂牌价"}));

        columnNameAlias.put("仓库", Arrays.asList(new String[]{ "仓库*", "存放地*", "所在地*", "存放仓库"}));
    }

}
