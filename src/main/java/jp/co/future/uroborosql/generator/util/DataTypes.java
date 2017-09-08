package jp.co.future.uroborosql.generator.util;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

/**
 * Map java.sql.Types to Java's class.
 *
 * @author Kenichi Hoshi
 */
public enum DataTypes {
    BIGINT(Types.BIGINT, Long.class.getName()),
    BINARY(Types.BINARY, "byte[]"),
    BIT(Types.BIT, Boolean.class.getName()),
    BLOB(Types.BLOB, Blob.class.getName()),
    BOOLEAN(Types.BOOLEAN, Boolean.class.getName()),
    CHAR(Types.CHAR, String.class.getName()),
    CLOB(Types.CLOB, Clob.class.getName()),
    DATE(Types.DATE, LocalDate.class.getName()),
    DECIMAL(Types.DECIMAL, BigDecimal.class.getName()),
    DOUBLE(Types.DOUBLE, Double.class.getName()),
    FLOAT(Types.FLOAT, Float.class.getName()),
    INTEGER(Types.INTEGER, Integer.class.getName()),
    LONGNVARCHAR(Types.LONGNVARCHAR, String.class.getName()),
    LONGVARBINARY(Types.LONGVARBINARY, "byte[]"),
    LONGVARCHAR(Types.LONGVARCHAR, String.class.getName()),
    NCHAR(Types.NCHAR, String.class.getName()),
    NLOB(Types.NCLOB, NClob.class.getName()),
    NUMERIC(Types.NUMERIC, BigDecimal.class.getName()),
    REAL(Types.REAL, Float.class.getName()),
    SMALLINT(Types.SMALLINT, Short.class.getName()),
    SQLXML(Types.SQLXML, SQLXML.class.getName()),
    TIME(Types.TIME, LocalTime.class.getName()),
    TIMESTAMP(Types.TIMESTAMP, LocalDateTime.class.getName()),
    TINYINT(Types.TINYINT, Short.class.getName()),
    VARBINARY(Types.VARBINARY, "byte[]"),
    VARCHAR(Types.VARCHAR, String.class.getName()),
    NVARCHAR(Types.NVARCHAR, String.class.getName());

    private int dbType;
    private String javaType;

    /**
     * Constractor
     *
     * @param dbType   <code>java.sql.Types</code>
     * @param javaType Java's class
     */
    private DataTypes(int dbType, String javaType) {
        this.dbType = dbType;
        this.javaType = javaType;
    }

    /**
     * Get the Java's class name
     *
     * @return Java's class name(FQDN)
     */
    public String getJavaType() {
        return javaType;
    }

    /**
     * Create DataTypes.
     *
     * @param dbType <code>java.sql.Types</code>
     * @return <code>DataTypes</code>
     */
    public static DataTypes of(int dbType) {
        return Arrays.stream(DataTypes.values())
                .filter(dataTypes -> dataTypes.dbType == dbType)
                .findFirst()
                .orElse(null);
    }

}
