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
    public ${columnMeta.javaSimpleType} get${columnMeta.methodName}() {
        return ${columnMeta.fieldName};
    }

    public void set${columnMeta.methodName}(${columnMeta.javaSimpleType} ${columnMeta.fieldName}) {
        this.${columnMeta.fieldName} = ${columnMeta.fieldName};
    }

</#list>
}
