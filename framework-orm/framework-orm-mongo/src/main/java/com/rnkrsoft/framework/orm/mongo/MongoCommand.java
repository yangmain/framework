package com.rnkrsoft.framework.orm.mongo;

/**
 * Created by rnkrsoft.com on 2018/6/5.
 */
public interface MongoCommand {
    // $or或者，相当于sql  or
    //BasicDBObject queryObjectOr = new BasicDBObject().append(QueryOperators.OR, 
    //new BasicDBObject[] { new BasicDBObject("iid", 2), new BasicDBObject("iid", 8) });    
    String OR = "$or";
    // $and并且，相当于sql  and
    String AND = "$and";

    // $gt 大于
    String GT = "$gt";
    // $gte 大于等于
    String GTE = "$gte";
    // $lt 小于
    String LT = "$lt";
    // $lte 小于等于
    String LTE = "$lte";

    String NE = "$ne";
    String IN = "$in";
    String NIN = "$nin";
    String MOD = "$mod";
    String ALL = "$all";
    String SIZE = "$size";
    String EXISTS = "$exists";
    String ELEM_MATCH = "$elemMatch";
    // $regex 模糊匹配，相当于sql中like
    String REGEX = "$regex";

    // (to be implemented in QueryBuilder)  
    String WHERE = "$where";
    String NOR = "$nor";
    String TYPE = "$type";
    String NOT = "$not";

    // geo operators  
    String WITHIN = "$within";
    String NEAR = "$near";
    String NEAR_SPHERE = "$nearSphere";
    String BOX = "$box";
    String CENTER = "$center";
    String POLYGON = "$polygon";
    String CENTER_SPHERE = "$centerSphere";
    // (to be implemented in QueryBuilder)  
    String MAX_DISTANCE = "$maxDistance";
    String UNIQUE_DOCS = "$uniqueDocs";


    // meta query operators (to be implemented in QueryBuilder)  
    String RETURN_KEY = "$returnKey";
    String MAX_SCAN = "$maxScan";
    String ORDER_BY = "$orderby";
    String EXPLAIN =  "$explain";
    String SNAPSHOT = "$snapshot";
    String MIN = "$min";
    String MAX = "$max";
    String SHOW_DISK_LOC = "$showDiskLoc";
    String HINT = "$hint";
    String COMMENT = "$comment";


    // 其他关键字
    // $set 更新记录，
    // Document update = new Document();  
    // update.append("$set", new Document("nickname", "ygirl1").append("password", "ygirl1"));
    String SET = "$set";
}
