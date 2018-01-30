<#setting number_format="0">
package ${packageName};

<#list imports as import>
import ${import};
</#list>
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;
<#if entityConfig.enabledBeanValidation>
<#list columnMetaList as columnMeta>
<#if !columnMeta.nullable>
import javax.validation.constraints.NotNull;
<#break>
</#if>
</#list>
<#list validationImports as import>
<#if import?has_content>
import javax.validation.constraints.${import};
</#if>
</#list>
</#if>

<#if baseModelName?has_content>
import ${baseModelName};
</#if>
<#if hasLockVersion>
import jp.co.future.uroborosql.mapping.annotations.Version;
</#if>
import jp.co.future.uroborosql.mapping.annotations.Table;

/**
 * ${entityName} Entity.<br>
 * Table is ${tableName}.<br>
<#if remarks?has_content>
 *
 * ${remarks}<br>
</#if>
<#if authorName?has_content>
 *
 * @author ${authorName}
</#if>
 */
@Table(name = "${tableName}")
public class ${entityName}<#if baseModelName?has_content> extends ${baseModelSimpleName}</#if> {

<#list columnMetaList as columnMeta>
	/**
	 * Column is ${columnMeta.columnName}.<br>
<#if columnMeta.remarks?has_content>
	 * ${columnMeta.remarks}<br>
</#if>
	 */
<#if columnMeta.lockVersion>
	@Version
</#if>
<#if entityConfig.enabledBeanValidation>
<#if !columnMeta.nullable>
	@NotNull
</#if>
<#if columnMeta.beanValidationDataType?has_content>
<#if columnMeta.beanValidationDataType == "Size">
	@Size(max = ${columnMeta.columnSize})
</#if>
<#if columnMeta.beanValidationDataType == "Digits">
	@Digits(integer = ${columnMeta.columnSize}, fraction = ${columnMeta.decimalDigits})
</#if>
</#if>
</#if>
<#if columnMeta.nullable>
	private Optional<${columnMeta.javaSimpleType}> ${columnMeta.fieldName} = Optional.empty();
<#else>
	private ${columnMeta.javaSimpleType} ${columnMeta.fieldName};
</#if>

</#list>

<#list columnMetaList as columnMeta>
	/**
	 * Get <#if columnMeta.remarks?has_content>${columnMeta.remarks}<#else>${columnMeta.columnName}</#if>.<br>
	 *
	 * @return <#if columnMeta.remarks?has_content>${columnMeta.remarks}<#else>${columnMeta.columnName}</#if>
	 */
<#if columnMeta.nullable>
	public Optional<${columnMeta.javaSimpleType}> get${columnMeta.methodName}() {
<#else>
	public ${columnMeta.javaSimpleType} get${columnMeta.methodName}() {
</#if>
		return ${columnMeta.fieldName};
	}

	/**
	 * Set <#if columnMeta.remarks?has_content>${columnMeta.remarks}<#else>${columnMeta.columnName}</#if>.
	 *
	 * @param ${columnMeta.fieldName} <#if columnMeta.remarks?has_content>${columnMeta.remarks}<#else>${columnMeta.columnName}</#if>
	 */
<#if columnMeta.nullable>
	public void set${columnMeta.methodName}(Optional<${columnMeta.javaSimpleType}> ${columnMeta.fieldName}) {
<#else>
	public void set${columnMeta.methodName}(${columnMeta.javaSimpleType} ${columnMeta.fieldName}) {
</#if>
		this.${columnMeta.fieldName} = ${columnMeta.fieldName};
	}

</#list>
}
