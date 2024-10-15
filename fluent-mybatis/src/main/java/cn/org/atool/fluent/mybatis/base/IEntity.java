package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.base.entity.AMapping;
import cn.org.atool.fluent.mybatis.base.intf.IDataByColumn;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.functions.TableSupplier;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import cn.org.atool.fluent.mybatis.utility.RefKit;

import java.io.Serializable;
import java.util.Map;

/**
 * IEntity 实体基类
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "rawtypes", "unused", "UnusedReturnValue"})
public interface IEntity extends IDataByColumn, Serializable {
    /**
     * 返回实体主键
     *
     * @return 主键
     */
    default Object findPk() {
        FieldMapping f = RefKit.byEntity(this.entityClass()).primaryMapping();
        return f == null ? null : f.getter.get(this);
    }

    /**
     * 数据库实体对应的Entity类名称
     * 在具体的XyzEntity类中定义为final
     *
     * @return 实例类
     */
    default Class<? extends IEntity> entityClass() {
        return MybatisUtil.entityClass(this.getClass());
    }

    /**
     * 将实体对象转换为map对象, 不包括空字段
     *
     * @return map对象
     */
    default Map<String, Object> toEntityMap(FieldMapping... fields) {
        return this.toEntityMap(false, fields);
    }


    /**
     * 将实体对象转换为map对象
     *
     * @param allowedNull true:所有字段; false: 仅仅非空字段
     * @return map对象
     */
    default Map<String, Object> toEntityMap(boolean allowedNull, FieldMapping... fields) {
        return RefKit.entityKit(this.entityClass()).toEntityMap(this, allowedNull, fields);
    }

    /**
     * 将实体对象转换为数据库字段为key的map对象, 不包括空字段
     *
     * @return map对象
     */
    default Map<String, Object> toColumnMap(FieldMapping... fields) {
        return this.toColumnMap(false, fields);
    }

    /**
     * 将实体对象转换为数据库字段为key的map对象
     *
     * @param allowNull true:仅仅非空字段; false: 所有字段
     * @return map对象
     */
    default Map<String, Object> toColumnMap(boolean allowNull, FieldMapping... fields) {
        return RefKit.entityKit(this.entityClass()).toColumnMap(this, allowNull, fields);
    }

    /**
     * 拷贝对象
     *
     * @param <E> 实例类型
     * @return 实例对象
     */
    default <E extends IEntity> E copy() {
        return RefKit.entityKit(this.entityClass()).copy(this);
    }

    /**
     * 从 entity 拷贝同名属性值
     *
     * @param entity 拷贝对象
     * @param <E>    类型
     * @return self
     */
    default <E extends IEntity> E copy(IEntity entity) {
        Map<String, Object> values = entity.toEntityMap();
        return this.valueByFields(values);
    }

    /**
     * 设置字段值
     *
     * @param columnName 数据库字段名称
     * @param value      值
     * @return ignore
     */
    default <E extends IEntity> E valueByColumn(String columnName, Object value) {
        RefKit.entityKit(this.entityClass()).valueByColumn(this, columnName, value);
        return (E) this;
    }

    /**
     * 设置字段值
     *
     * @param fieldName 字段名称
     * @param value     值
     * @return ignore
     */
    default <E extends IEntity> E valueByField(String fieldName, Object value) {
        RefKit.entityKit(this.entityClass()).valueByField(this, fieldName, value);
        return (E) this;
    }

    /**
     * 从 values 拷贝设置同名属性值
     *
     * @param values 属性值
     * @return ignore
     */
    default <E extends IEntity> E valueByFields(Map<String, ?> values) {
        AMapping kit = RefKit.byEntity(this.entityClass());
        for (Map.Entry<String, ?> entry : values.entrySet()) {
            if (kit.fieldsMap.containsKey(entry.getKey())) {
                kit.valueByField(this, entry.getKey(), entry.getValue());
            }
        }
        return (E) this;
    }

    /**
     * 获取entity的属性field值
     *
     * @param field 属性名称
     * @param <T>   属性值类型
     * @return 属性值
     */
    default <T> T valueByField(String field) {
        return RefKit.entityKit(this.entityClass()).valueByField(this, field);
    }

    /**
     * 获取entity的对应数据库字段的属性值
     *
     * @param column 数据库字段名称
     * @param <T>    属性值类型
     * @return 属性值
     */
    default <T> T valueByColumn(String column) {
        return RefKit.entityKit(this.entityClass()).valueByColumn(this, column);
    }

    /**
     * 设置entity属性值
     *
     * @param values 属性值
     * @return ignore
     */
    default <E extends IEntity> E valueByColumns(Map<String, ?> values) {
        values.forEach(this::valueByColumn);
        return (E) this;
    }

    /**
     * 获取entity的对应字段的属性值
     *
     * @param fieldMapping 字段映射
     * @param <T>          属性值类型
     * @return 属性值
     */
    default <T> T valueBy(FieldMapping fieldMapping) {
        return (T) fieldMapping.getter.get(this);
    }

    /**
     * 动态修改归属表, 默认无需设置
     * 只有在插入数据时, 不想使用默认对应的数据库表, 想动态调整时才需要
     *
     * @param supplier 动态归属表
     * @return ignore
     */
    default <E extends IEntity> E tableSupplier(TableSupplier supplier) {
        return (E) this;
    }

    /**
     * 动态修改归属表, 默认无需设置
     * 只有在插入数据时, 不想使用默认对应的数据库表, 想动态调整时才需要
     *
     * @param supplier 动态归属表
     * @return ignore
     */
    default <E extends IEntity> E tableSupplier(String supplier) {
        return (E) this;
    }

    /**
     * 返回动态归属表
     *
     * @return 动态归属表
     */
    default String tableSupplier() {
        return null;
    }
}