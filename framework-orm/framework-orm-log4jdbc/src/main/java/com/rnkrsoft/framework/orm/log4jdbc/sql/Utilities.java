package com.rnkrsoft.framework.orm.log4jdbc.sql;

public class Utilities {
    /**
     * 在左侧进行空格填充
     * @param fieldSize 填充到的目标长度
     * @param field 内容
     * @return 填充结果
     */
    public static String rightJustify(int fieldSize, String field) {
        if (field == null) {
            field = "";
        }
        StringBuffer output = new StringBuffer();
        for (int i = 0, j = fieldSize - field.length(); i < j; i++) {
            output.append(' ');
        }
        output.append(field);
        return output.toString();
    }

}
