package jp.co.future.uroborosql.generator.model;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jp.co.future.uroborosql.generator.config.EntityConfig;
import jp.co.future.uroborosql.utils.CaseFormat;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.dna.common.text.Inflector;

/**
 * Table metadata
 *
 * @author Kenichi Hoshi
 */
public class TableMeta {
	private String tableCat;
	private String tableSchem;
	private String tableName;
	private String tableType;
	private String remarks;

	private List<ColumnMeta> columnMetaList;
	private EntityConfig entityConfig;

	public String getTableCat() {
		return tableCat;
	}

	public void setTableCat(String tableCat) {
		this.tableCat = tableCat;
	}

	public String getTableSchem() {
		return tableSchem;
	}

	public void setTableSchem(String tableSchem) {
		this.tableSchem = tableSchem;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	public String getRemarks() {
		return StringEscapeUtils.escapeHtml4(remarks);
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<ColumnMeta> getColumnMetaList() {
		return columnMetaList;
	}

	public void setColumnMetaList(List<ColumnMeta> columnMetaList) {
		this.columnMetaList = columnMetaList;
	}

	public String getEntityName() {
		String entityName = CaseFormat.PASCAL_CASE.convert(Inflector.getInstance().singularize(tableName));
		String classNameSuffix = entityConfig.getClassNameSuffix();
		if (StringUtils.isNotBlank(classNameSuffix)) {
			entityName = entityName + classNameSuffix;
		}
		return entityName;
	}

	public EntityConfig getEntityConfig() {
		return entityConfig;
	}

	public void setEntityConfig(EntityConfig entityConfig) {
		this.entityConfig = entityConfig;
		columnMetaList.forEach(columnMeta -> columnMeta.setEntityConfig(entityConfig));
	}

	/* custom methods */

	public String getPackageName() {
		return entityConfig.getPackageName();
	}

	public String getAuthorName() {
		return entityConfig.getAuthorName();
	}

	public String getBaseModelName() {
		return entityConfig.getBaseModelName();
	}

	public String getBaseModelSimpleName() {
		String[] names = entityConfig.getBaseModelName().split("\\.");
		return names[names.length - 1];
	}

	public Set<String> getImports() {
		return columnMetaList.stream()
				.map(ColumnMeta::getJavaType)
				.distinct()
				.sorted()
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	public Set<String> getValidationImports() {
		return columnMetaList.stream()
				.map(ColumnMeta::getBeanValidationDataType)
				.filter(v -> v != null)
				.distinct()
				.sorted()
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	public boolean getHasLockVersion() {
		return columnMetaList.stream()
				.anyMatch(ColumnMeta::isLockVersion);
	}

	@Override
	public String toString() {
		return String.format("catalog=%s;schema=%s;name=%s;type=%s;remarks=%s",
				tableCat, tableSchem, tableName, tableType, remarks);
	}

}
