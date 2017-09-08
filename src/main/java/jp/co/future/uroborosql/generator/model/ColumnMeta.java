package jp.co.future.uroborosql.generator.model;

import jp.co.future.uroborosql.generator.config.EntityConfig;
import jp.co.future.uroborosql.generator.util.DataTypes;
import jp.co.future.uroborosql.utils.CaseFormat;

/**
 * Column metadata
 *
 * @author Kenichi Hoshi
 */
public class ColumnMeta {
    private String columnName;
    private int dataType;
    private String typeName;
    private Integer columnSize;
    private Integer decimalDigits;
    private Boolean nullable;
    private String columnDef;
    private String remarks;
    private EntityConfig entityConfig;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(Integer columnSize) {
        this.columnSize = columnSize;
    }

    public Integer getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(Integer decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    public Boolean getNullable() {
        return nullable;
    }

    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }

    public String getColumnDef() {
        return columnDef;
    }

    public void setColumnDef(String columnDef) {
        this.columnDef = columnDef;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public EntityConfig getEntityConfig() {
        return entityConfig;
    }

    public void setEntityConfig(EntityConfig entityConfig) {
        this.entityConfig = entityConfig;
    }


    /* custom methods */

    /**
     * Get Java type name.
     *
     * @return Java type name
     */
    public String getJavaType() {
        return DataTypes.of(dataType).getJavaType();
    }

    /**
     * Get Java type simple name.
     *
     * @return Java type simple name
     */
    public String getJavaSimpleType() {
        String javaType = DataTypes.of(dataType).getJavaType();
        String[] names = javaType.split("\\.");
        return names.length == 0 ? javaType : names[names.length - 1];
    }

    /**
     * Get field name.
     *
     * @return field name
     */
    public String getFieldName() {
        return CaseFormat.CAMEL_CASE.convert(columnName);
    }

    /**
     * Get method name.
     *
     * @return method name
     */
    public String getMethodName() {
        return CaseFormat.PASCAL_CASE.convert(columnName);
    }

    /**
     * Returns whether this column is a lock version or not..
     *
     * @return If this is a lock vesion, return <code>true</code>,
     */
    public boolean isLockVersion() {
        return columnName.equalsIgnoreCase(entityConfig.getLockVersionName());
    }

    @Override
    public String toString() {
        return String.format("columnName=%s;dataType=%s;typeName=%s;columnSize=%s;decimalDigits=%s;nullable=%s;columnDef=%s;remarks=%s",
                columnName, dataType, typeName, columnSize, decimalDigits, nullable, columnDef, remarks);
    }

}
