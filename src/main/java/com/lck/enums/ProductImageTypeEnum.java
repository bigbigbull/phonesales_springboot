package com.lck.enums;
/**
 * describe:
 *
 * @author lichangkai
 * @date 2018/12/19
 */
public enum ProductImageTypeEnum {
    /**
     * 单个产品图片
     * */
    TYPE_SINGLE("single"),
    /**
     * 详情产品图片
     * */
    TYPE_DETAIL("detail");

    private final String text;
    
   ProductImageTypeEnum(final String text){
        this.text=text;
    }
    
    @Override
    public String toString() {
        return text;
    }
}
