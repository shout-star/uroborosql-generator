package ${packageName};

<#list imports as import>
import ${import};
</#list>
<#if baseModelName?has_content>
import ${baseModelName};
</#if>
<#if hasLockVersion>
import jp.co.future.uroborosql.mapping.annotations.Version;
</#if>
import jp.co.future.uroborosql.mapping.annotations.Table;

/**
 * ${entityName} Entity.
 * Table is ${tableName}.
<#if remarks?has_content>
 *
 * ${remarks}
</#if>
<#if authorName?has_content>
 *
 * @author ${authorName}
</#if>
 */
@Table(name = "${tableName}")
public class ${entityName}<#if baseModelName?has_content> extends ${baseModelSimpleName}</#if> {
<#list columnMetaList as columnMeta>
    /* Column is ${columnMeta.columnName}.<#if columnMeta.remarks?has_content>${columnMeta.remarks}</#if> */
<#if columnMeta.lockVersion>
    @Version
</#if>
    private ${columnMeta.javaSimpleType} ${columnMeta.fieldName};

</#list>

<#list columnMetaList as columnMeta>
    /**
     * Get <#if columnMeta.remarks?has_content>${columnMeta.remarks}<#else>${columnMeta.columnName}</#if>.
     *
     * @return <#if columnMeta.remarks?has_content>${columnMeta.remarks}<#else>${columnMeta.columnName}</#if>
     */
    public ${columnMeta.javaSimpleType} get${columnMeta.methodName}() {
        return ${columnMeta.fieldName};
    }

    /**
     * Set <#if columnMeta.remarks?has_content>${columnMeta.remarks}<#else>${columnMeta.columnName}</#if>.
     *
     * @param ${columnMeta.fieldName} <#if columnMeta.remarks?has_content>${columnMeta.remarks}<#else>${columnMeta.columnName}</#if>
     */
    public void set${columnMeta.methodName}(${columnMeta.javaSimpleType} ${columnMeta.fieldName}) {
        this.${columnMeta.fieldName} = ${columnMeta.fieldName};
    }

</#list>
}
