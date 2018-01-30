package jp.co.future.uroborosql.generator.generator;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import jp.co.future.uroborosql.UroboroSQL;
import jp.co.future.uroborosql.config.SqlConfig;
import jp.co.future.uroborosql.converter.EntityResultSetConverter;
import jp.co.future.uroborosql.generator.config.DbConfig;
import jp.co.future.uroborosql.generator.config.EntityConfig;
import jp.co.future.uroborosql.generator.exception.UroborosqlGeneratorException;
import jp.co.future.uroborosql.generator.model.ColumnMeta;
import jp.co.future.uroborosql.generator.model.TableMeta;
import jp.co.future.uroborosql.mapping.mapper.PropertyMapperManager;

/**
 * EntityGenerator
 *
 * @author Kenichi Hoshi
 */
public class EntityGenerator extends AbstractGenerator {
	private static final Logger LOG = LoggerFactory.getLogger(EntityGenerator.class);

	@Override
	public void generate() {
		EntityConfig entityConfig = new EntityConfig(loadProperties("entity-config.properties"));
		DbConfig dbConfig = new DbConfig(loadProperties("db-config.properties"));

		SqlConfig sqlConfig = UroboroSQL.builder(
				dbConfig.getUrl(),
				dbConfig.getUser(),
				dbConfig.getPassword()).build();

		final DatabaseMetaData metaData = getDatabaseMetaData(sqlConfig);

		Template template = getTemplate("entity.ftl");
		String schema = dbConfig.getSchema();
		String[] types = { "TABLE" };

		Path dirPath = Paths.get(entityConfig.getOutputDir(),
				entityConfig.getPackageName().replaceAll("\\.", "/"));
		try {
			Files.createDirectories(dirPath);
		} catch (IOException e) {
			throw new UroborosqlGeneratorException(e);
		}

		List<TableMeta> tableMetaList = getTableMetaList(metaData, schema, entityConfig);
		tableMetaList.stream()
				.peek(tableMeta -> tableMeta
						.setColumnMetaList(getColumnMetaList(metaData, tableMeta, entityConfig)))
				.forEach(tableMeta -> {
					try {
						tableMeta.setEntityConfig(entityConfig);
						Path filePath = Paths.get(dirPath.toString(),
								tableMeta.getEntityName() + ".java");
						template.process(tableMeta,
								new OutputStreamWriter(Files.newOutputStream(filePath), Charset.forName("UTF-8")));
						LOG.info("create " + tableMeta.getEntityName());
					} catch (TemplateException | IOException e) {
						throw new UroborosqlGeneratorException(e);
					}
				});
	}

	/**
	 * Get <code>DatabaseMetaData</code>
	 *
	 * @param sqlConfig <code>SqlConfig</code>
	 * @return <code>DatabaseMetaData</code>
	 */
	private DatabaseMetaData getDatabaseMetaData(final SqlConfig sqlConfig) {
		final DatabaseMetaData metaData;
		try {
			metaData = sqlConfig.getConnectionSupplier().getConnection().getMetaData();
		} catch (SQLException e) {
			throw new UroborosqlGeneratorException(e);
		}
		return metaData;
	}

	/**
	 * Get TableMeta list.
	 *
	 * @param metaData <code>DatabaseMetaData</code>
	 * @param schemaName schema name
	 * @param entityConfig <code>EntityConfig</code>
	 * @return <code>TableMeta</code> list
	 */
	private List<TableMeta> getTableMetaList(final DatabaseMetaData metaData,
			final String schemaName,
			final EntityConfig entityConfig) {
		String[] types = { "TABLE" };

		try (ResultSet rs = metaData.getTables(null, schemaName, "%", types)) {
			EntityResultSetConverter<TableMeta> converter = new EntityResultSetConverter<>(TableMeta.class,
					new PropertyMapperManager());

			List<TableMeta> tableMetaList = new ArrayList<>();

			Pattern include = null;
			if (StringUtils.isNotEmpty(entityConfig.getIncludeTablePattern())) {
				include = Pattern.compile(entityConfig.getIncludeTablePattern(), Pattern.CASE_INSENSITIVE);
			}
			Pattern exclude = null;
			if (StringUtils.isNotEmpty(entityConfig.getExcludeTablePattern())) {
				exclude = Pattern.compile(entityConfig.getExcludeTablePattern(), Pattern.CASE_INSENSITIVE);
			}
			while (rs.next()) {
				TableMeta tableMeta = converter.createRecord(rs);
				if (include == null && exclude == null) {
					tableMetaList.add(tableMeta);
					continue;
				}
				if (include != null && include.matcher(tableMeta.getTableName()).matches()) {
					tableMetaList.add(tableMeta);
					continue;
				}
				if (exclude != null && !exclude.matcher(tableMeta.getTableName()).matches()) {
					tableMetaList.add(tableMeta);
					continue;
				}
			}

			return tableMetaList;
		} catch (SQLException e) {
			throw new UroborosqlGeneratorException(e);
		}
	}

	/**
	 * Get <code>ColumnMeta</code> list.
	 *
	 * @param metaData <code>DatabaseMetaData</code>
	 * @param tableMeta <code>TableMeta</code>
	 * @param entityConfig <code>EntityConfig</code>
	 * @return <code>ColumnMeta</code> list
	 */
	private List<ColumnMeta> getColumnMetaList(final DatabaseMetaData metaData,
			final TableMeta tableMeta,
			final EntityConfig entityConfig) {
		EntityResultSetConverter<ColumnMeta> converter = new EntityResultSetConverter<>(ColumnMeta.class,
				new PropertyMapperManager());

		try (ResultSet rs = metaData.getColumns(null,
				tableMeta.getTableSchem(),
				tableMeta.getTableName(),
				"%")) {

			List<ColumnMeta> columnMetaList = new ArrayList<>();

			Pattern include = null;
			if (StringUtils.isNotEmpty(entityConfig.getIncludeColumnPattern())) {
				include = Pattern.compile(entityConfig.getIncludeColumnPattern(), Pattern.CASE_INSENSITIVE);
			}
			Pattern exclude = null;
			if (StringUtils.isNotEmpty(entityConfig.getExcludeColumnPattern())) {
				exclude = Pattern.compile(entityConfig.getExcludeColumnPattern(), Pattern.CASE_INSENSITIVE);
			}
			while (rs.next()) {
				ColumnMeta columnMeta = converter.createRecord(rs);
				if (include == null && exclude == null) {
					columnMetaList.add(columnMeta);
					continue;
				}
				if (include != null && include.matcher(columnMeta.getColumnName()).matches()) {
					columnMetaList.add(columnMeta);
					continue;
				}
				if (exclude != null && !exclude.matcher(columnMeta.getColumnName()).matches()) {
					columnMetaList.add(columnMeta);
					continue;
				}
			}

			return columnMetaList;
		} catch (SQLException e) {
			throw new UroborosqlGeneratorException(e);
		}
	}
}
