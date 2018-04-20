package com.rnkrsoft.framework.orm.expression;

import com.devops4j.utils.DateStyle;
import com.devops4j.utils.DateUtils;
import com.devops4j.utils.StringUtils;
import com.rnkrsoft.framework.sequence.SequenceService;
import com.rnkrsoft.framework.sequence.SequenceServiceFactory;

import java.util.*;

/**
 * Created by rnkrsoft.com on 2018/4/11.
 * 物理主键表达式解析器
 */
public class ExpressionHelper {
    /**
     * 缓存已解析的表达式
     */
    static Map<String, Expression> CACHE = new HashMap();

    static Expression getExpression(String exp, boolean useCache) {
        Expression expression = null;
        //是否使用缓存
        if (useCache) {
            expression = CACHE.get(exp);
        }else{
            expression = parse0(exp);
        }
        if (expression == null) {
            synchronized (CACHE) {
                Expression expressionSecond = CACHE.get(exp);
                //进行第二次校验
                if (expressionSecond == null) {
                    expression = parse0(exp);
                    CACHE.put(exp, expression);
                }
            }
        }
        return expression;
    }

    /**
     * 将字符串表达式解析为表达式对象
     * @param exp 字符串表达式
     * @return 表达式对象
     */
    static Expression parse0(String exp){
        Expression expression = new Expression(exp);
        String expTemp = exp;
        List<Token> list = new ArrayList<Token>();
        while (!expTemp.isEmpty()) {
            int beginPos0 = expTemp.indexOf("${");
            int endPos0 = expTemp.indexOf("}");
            if (beginPos0 > 0) {
                String val = expTemp.substring(0, beginPos0);
                Token staticExpression = new Token(val, val.length(), ExpressionType.TEXT);
                list.add(staticExpression);
            }
            if (beginPos0 < 0 && endPos0 < 0) {
                Token staticExpression = new Token(expTemp, expTemp.length(), ExpressionType.TEXT);
                list.add(staticExpression);
                expression.setTokens(list);
                return expression;
            }
            if (beginPos0 >= 0 && endPos0 > 0) {
                String exp1 = expTemp.substring(beginPos0 + 2, endPos0);
                String value = getValue(exp1);
                Token token = new Token(value, getLength(exp1), getType(value));
                list.add(token);
            } else {
                throw new IllegalArgumentException("无效表达式");
            }
            expTemp = expTemp.substring(endPos0 + 1);
        }
        expression.setTokens(list);
        return expression;
    }

    /**
     * 解析表达式字符为表达式数组
     *
     * @param exp 表达式字符串
     * @return 表达式列表
     */
    public static Expression parse(String exp, boolean useCache) {
        Expression expression = getExpression(exp, useCache);
        return expression;
    }

    /**
     * 获取单个表达式的值
     *
     * @param token 表达式对象
     * @param ctx   上下文
     * @return 值
     */
    public static String value(Token token, ExpressionContext ctx) {
        if (token.type == ExpressionType.SEQ) {
            SequenceService sequenceService = SequenceServiceFactory.instance(ctx.getSequenceClassName());
            int val = sequenceService.nextval(ctx.getSchema(), ctx.getPrefix(), ctx.getSequenceName(), ctx.getFeature());
            return StringUtils.fill(String.valueOf(val), true, '0', token.length);
        } else if (token.type == ExpressionType.RANDOM) {
            int bit = new Random(System.nanoTime()).nextInt(token.length);
            int seed = (int) Math.pow(10, bit);
            int val = new Random(System.nanoTime()).nextInt(seed);
            return StringUtils.fill(String.valueOf(val), true, '0', token.length);
        } else if (token.type == ExpressionType.DATE) {
            return DateUtils.formatJavaDate2String(new Date(), DateStyle.FILE_FORMAT2);
        } else {
            return token.expression;
        }
    }

    /**
     * 将表达式计算为值字符串
     *
     * @param ctx 上下文
     * @return 值字符串
     */
    public static String eval(ExpressionContext ctx) {
        String exp = ctx.getExpression();
        StringBuilder expBuilder = new StringBuilder();
        for (Token token : parse(exp, ctx.isUseCache()).tokens) {
            expBuilder.append(value(token, ctx));
        }
        return expBuilder.toString();
    }

    static ExpressionType getType(String value) {
        if (DateStyle.FILE_FORMAT2.getFormat().equals(value)) {
            return ExpressionType.DATE;
        } else if ("SEQ".equals(value)) {
            return ExpressionType.SEQ;
        } else if ("RANDOM".equals(value)) {
            return ExpressionType.RANDOM;
        } else {
            throw new IllegalArgumentException("illegal expression '" + value + "'");
        }
    }

    static String getValue(String value) {
        String[] vals = value.split(":");
        if (vals.length >= 1) {
            return vals[0];
        } else {
            throw new NullPointerException("'" + value + "' is null!");
        }
    }

    static int getLength(String value) {
        String[] vals = value.split(":");
        if (vals.length == 2) {
            return Integer.valueOf(vals[1]);
        } else {
            return -1;
        }
    }


}
