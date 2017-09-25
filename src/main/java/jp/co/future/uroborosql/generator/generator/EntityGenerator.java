package jp.co.future.uroborosql.generator.generator;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import jp.co.future.uroborosql.config.DefaultSqlConfig;
import jp.co.future.uroborosql.config.SqlConfig;
import jp.co.future.uroborosql.converter.EntityResultSetConverter;
import jp.co.future.uroborosql.generator.config.DbConfig;
import jp.co.future.uroborosql.generator.config.EntityConfig;
import jp.co.future.uroborosql.generator.exception.UroborosqlGeneratorException;
import jp.co.future.uroborosql.generator.model.ColumnMeta;
import jp.co.future.uroborosql.generator.model.TableMeta;
import jp.co.future.uroborosql.mapping.mapper.PropertyMapperManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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

        SqlConfig sqlConfig = DefaultSqlConfig.getConfig(
                dbConfig.getUrl(),
                dbConfig.getUser(),
                dbConfig.getPassword());

        final DatabaseMetaData metaData = getDatabaseMetaData(sqlConfig);

        Template template = getTemplate("entity.ftl");
        String schemaName = "";
        String tableNamePattern = "%";
        String columnNamePattern = "%";
        String[] types = {"TABLE"};

        Path dirPath = Paths.get(entityConfig.getOutputDir(),
                entityConfig.getPackageName().replaceAll("\\.", "/"));
        try {
            Files.createDirectories(dirPath);
        } catch (IOException e) {
            throw new UroborosqlGeneratorException(e);
        }

        List<TableMeta> tableMetaList = getTableMetaList(metaData, schemaName, tableNamePattern, types);
        tableMetaList.stream()
                .peek(tableMeta -> tableMeta.setColumnMetaList(getColumnMetaList(metaData, tableMeta, columnNamePattern)))
                .forEach(tableMeta -> {
                    try {
                        Path filePath = Paths.get(dirPath.toString(),
                                tableMeta.getEntityName() + ".java");

                        tableMeta.setEntityConfig(entityConfig);
                        template.process(tableMeta, new OutputStreamWriter(Files.newOutputStream(filePath)));
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
    private DatabaseMetaData getDatabaseMetaData(SqlConfig sqlConfig) {
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
     * @param metaData         <code>DatabaseMetaData</code>
     * @param schemaName       schema name
     * @param tableNamePattern include table name pattern
     * @param types            include table types
     * @return <code>TableMeta</code> list
     */
    private List<TableMeta> getTableMetaList(DatabaseMetaData metaData,
                                             String schemaName,
                                             String tableNamePattern,
                                             String[] types) {
        try {
            ResultSet rs = metaData.getTables(null, schemaName, tableNamePattern, types);
            EntityResultSetConverter<TableMeta> converter =
                    new EntityResultSetConverter<>(TableMeta.class, new PropertyMapperManager());

            List<TableMeta> tableMetaList = new ArrayList<>();

            while (rs.next()) {
                tableMetaList.add(converter.createRecord(rs));
            }

            return tableMetaList;
        } catch (SQLException e) {
            throw new UroborosqlGeneratorException(e);
        }
    }

    /**
     * Get <code>ColumnMeta</code> list.
     *
     * @param metaData          <code>DatabaseMetaData</code>
     * @param tableMeta         <code>TableMeta</code>
     * @param columnNamePattern include column name pattern
     * @return <code>ColumnMeta</code> list
     */
    private List<ColumnMeta> getColumnMetaList(DatabaseMetaData metaData,
                                               TableMeta tableMeta,
                                               String columnNamePattern) {
        EntityResultSetConverter<ColumnMeta> converter =
                new EntityResultSetConverter<>(ColumnMeta.class, new PropertyMapperManager());

        try {
            ResultSet rs = metaData.getColumns(null,
                    tableMeta.getTableSchem(),
                    tableMeta.getTableName(),
                    columnNamePattern);

            List<ColumnMeta> columnMetaList = new ArrayList<>();

            while (rs.next()) {
                columnMetaList.add(converter.createRecord(rs));
            }

            return columnMetaList;
        } catch (SQLException e) {
            throw new UroborosqlGeneratorException(e);
        }
    }
}
