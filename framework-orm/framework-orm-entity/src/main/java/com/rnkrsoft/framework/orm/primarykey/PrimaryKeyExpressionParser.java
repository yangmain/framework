package com.rnkrsoft.framework.orm.primarykey;

import com.devops4j.utils.DateStyle;
import com.devops4j.utils.DateUtils;
import com.devops4j.utils.StringUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rnkrsoft.com on 2018/4/11.
 */
public class PrimaryKeyExpressionParser {
    /**
     * @param expression
     */
    public List<Value> parse(String expression) {
        String expression0 = expression;
        List<Value> list = new ArrayList<Value>();
        while (!expression0.isEmpty()) {
            int beginPos0 = expression0.indexOf("${");
            int endPos0 = expression0.indexOf("}");
            if (beginPos0 > 0) {
                String val = expression0.substring(0, beginPos0);
                Value staticValue = new Value(val);
                list.add(staticValue);
            }
            if (beginPos0 < 0 && endPos0 < 0) {
                Value staticValue = new Value(expression0);
                list.add(staticValue);
                return list;
            }
            if (beginPos0 >= 0 && endPos0 > 0) {
                String exp = expression0.substring(beginPos0 + 2, endPos0);
                Value value = value(exp);
                list.add(value);
            } else {

                throw new IllegalArgumentException("无效表达式");
            }
            expression0 = expression0.substring(endPos0 + 1);
        }
        return list;
    }


    static class Value {
        String expression;
        String value = "";
        int length;

        public Value(String value) {
            this.value = value;
        }

        public Value(String expression, int length) {
            this.expression = expression;
            this.length = length;
        }

        public String getValue() {
            if("yyyyMMdd".equals(expression)){
                this.value = DateUtils.formatJavaDate2String(new Date(), DateStyle.FILE_FORMAT6);
            }else if ("yyyyMMddHHmmss".equals(expression)){
                this.value = DateUtils.formatJavaDate2String(new Date(), DateStyle.FILE_FORMAT3);
            }else if ("yyyyMMddHHmmssSSS".equals(expression)) {
                this.value = DateUtils.formatJavaDate2String(new Date(), DateStyle.FILE_FORMAT2);
            }
            return this.value;
        }

        @Override
        public String toString() {
            if(expression == null){
                return value;
            }else{
                return expression + ":" + length + "(" + value + ")";
            }
        }
    }

    static Value value(String value) {
        String[] ss = value.split(":");
        if (ss.length == 1) {
            return new Value(ss[0], ss[0].length());
        } else {
            return new Value(ss[0], Integer.valueOf(ss[1]));
        }

    }
}
