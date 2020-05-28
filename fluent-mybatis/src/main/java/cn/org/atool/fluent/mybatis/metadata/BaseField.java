package cn.org.atool.fluent.mybatis.metadata;

import lombok.Getter;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.Field;

import static cn.org.atool.fluent.mybatis.util.Constants.COMMA;

/**
 * BaseFieldInfo
 *
 * @author darui.wu
 * @create 2020/5/27 6:46 下午
 */
@Getter
public abstract class BaseField implements Comparable<FieldInfo> {
    /**
     * 字段名
     */
    protected final String column;
    /**
     * 属性名
     */
    protected final String property;
    /**
     * 属性类型
     */
    protected final Class<?> propertyType;
    /**
     * JDBC类型
     */
    private JdbcType jdbcType;

    public BaseField(String column, Field field) {
        this.column = column;
        this.property = field.getName();
        this.propertyType = field.getType();
    }

    public void setJdbcType(JdbcType jdbcType) {
        if (JdbcType.UNDEFINED == jdbcType) {
            this.jdbcType = null;
        } else {
            this.jdbcType = jdbcType;
        }
    }

    protected String el() {
        String el = this.property;
        if (this.jdbcType != null) {
            el += (COMMA + "jdbcType=" + jdbcType.name());
        }
        return el;
    }

    public boolean isSelected() {
        return true;
    }

    @Override
    public int compareTo(FieldInfo info) {
        return this.column.compareTo(info == null ? null : info.getColumn());
    }
}