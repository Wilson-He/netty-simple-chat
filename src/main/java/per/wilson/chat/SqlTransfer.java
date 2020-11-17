package per.wilson.chat;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Wilson
 */
public class SqlTransfer {
    public static void main(String[] args) throws JSQLParserException, IOException {
        String mysqlDDLPath = "D:\\github-repository\\netty-chatroom\\simple_chat-postgresql.sql";
        String dDLs = FileUtils.readFileToString(new File(mysqlDDLPath));

        System.out.println(dDLs);
        System.out.println("++++++++++开始转换SQL语句+++++++++++++");

        Statements statements = CCJSqlParserUtil.parseStatements(dDLs);

        statements.getStatements()
                .stream()
                .map(statement -> (CreateTable) statement).forEach(ct -> {
            Table table = ct.getTable();
            List<ColumnDefinition> columnDefinitions = ct.getColumnDefinitions();
            List<String> comments = new ArrayList<>();
            List<ColumnDefinition> collect = columnDefinitions.stream()
                    .peek(columnDefinition -> {
                        List<String> columnSpecStrings = columnDefinition.getColumnSpecStrings();                        int commentIndex = getCommentIndex(columnSpecStrings);                        if (commentIndex != -1) {                            int commentStringIndex = commentIndex + 1;
                            String commentString = columnSpecStrings.get(commentStringIndex);

                            String commentSql = genCommentSql(table.toString(), columnDefinition.getColumnName(), commentString);
                            comments.add(commentSql);
                            columnSpecStrings.remove(commentStringIndex);
                            columnSpecStrings.remove(commentIndex);
                        }
                        columnDefinition.setColumnSpecStrings(columnSpecStrings);
                    }).collect(Collectors.toList());
            ct.setColumnDefinitions(collect);
            String createSQL = ct.toString()
                    .replaceAll("`", "\"")
                    .replaceAll("BIGINT UNIQUE NOT NULL AUTO_INCREMENT", "BIGSERIAL PRIMARY KEY")
                    .replaceAll("BIGINT NULL AUTO_INCREMENT", "BIGSERIAL PRIMARY KEY")
                    .replaceAll("BIGINT NOT NULL AUTO_INCREMENT", "BIGSERIAL PRIMARY KEY")
                    .replaceAll("INT NOT NULL AUTO_INCREMENT", "BIGSERIAL PRIMARY KEY")
                    .replaceAll("INT NULL AUTO_INCREMENT", "BIGSERIAL PRIMARY KEY")
                    .replaceAll("IF NOT EXISTS", "")
                    .replaceAll("TINYINT", "SMALLINT")
                    .replaceAll("DATETIME", "TIMESTAMP")
                    .replaceAll(", PRIMARY KEY \\(\"id\"\\)", "");            // 如果存在表注释
            if (createSQL.contains("COMMENT")) {
                createSQL = createSQL.substring(0, createSQL.indexOf("COMMENT"));
            }
            System.out.println(createSQL + ";");

            comments.forEach(t -> System.out.println(t.replaceAll("`", "\"") + ";"));
        });
    }    /**
     * 获得注释的下标
     *
     * @param columnSpecStrings columnSpecStrings
     * @return 下标
     */
    private static int getCommentIndex(List<String> columnSpecStrings) {        for (int i = 0; i < columnSpecStrings.size(); i++) {            if ("COMMENT".equalsIgnoreCase(columnSpecStrings.get(i))) {                return i;
    }
    }        return -1;
    }    /**
     * 生成COMMENT语句
     *
     * @param table        表名
     * @param column       字段名
     * @param commentValue 描述文字
     * @return COMMENT语句
     */
    private static String genCommentSql(String table, String column, String commentValue) {        return String.format("COMMENT ON COLUMN %s.%s IS %s", table, column, commentValue);
    }
}
