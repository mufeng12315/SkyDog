package com.mufeng.skydog.repo;

import com.mufeng.skydog.core.DataSourceFactory;
import com.mufeng.skydog.bean.SdColumn;
import com.mufeng.skydog.bean.SdRow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SdSqlExecutor {

    @Resource
    private DataSourceFactory dataSourceFactory;

    /**
     * 查询数据
     * @param sql
     * @return
     */
    public List<SdRow> executeSql(String sql,String dataSourceCode){
        List<SdRow> sdRowList = new ArrayList<SdRow>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            if(dataSourceCode==null || dataSourceCode.isEmpty()){
                throw new RuntimeException("dataSourceCode is not null");
            }
            DataSource dataSource = dataSourceFactory.getDataSource(dataSourceCode);
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            ResultSetMetaData metaData = stmt.getMetaData();
            while(rs.next()){
                SdRow row = new SdRow();
                Map<String,SdColumn> columnMap = new HashMap<String,SdColumn>();
                row.setColumnMap(columnMap);
                for(int i=0;i<metaData.getColumnCount();i++){
                    int colIndex = i+1;
                    SdColumn column = new SdColumn();
                    column.setColIndex(colIndex);
                    column.setColName(metaData.getColumnName(colIndex));
                    column.setColType(metaData.getColumnTypeName(colIndex));
                    column.setColValue(rs.getObject(colIndex));
//                    column.setColValue(rs.getObject(colIndex, SdSQLTypeEnum.getEnumByCode(column.getColType()).getCls()));
                    columnMap.put(column.getColName(),column);
                }
                sdRowList.add(row);
            }
        } catch (SQLException e) {
            log.error("executeSql error",e);
        } finally {
            closeResultSet(rs);
            rs = null;
            closeStatement(stmt);
            stmt = null;
            closeConnection(conn);
            conn = null;
        }
        return sdRowList;
    }

    private static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException var2) {
                log.debug("Could not close JDBC ResultSet", var2);
            } catch (Exception var3) {
                log.debug("Unexpected exception on closing JDBC ResultSet", var3);
            }
        }

    }

    private static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException var2) {
                log.debug("Could not close JDBC Statement", var2);
            } catch (Exception var3) {
                log.debug("Unexpected exception on closing JDBC Statement", var3);
            }
        }

    }

    private static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException var2) {
                log.debug("Could not close JDBC Connection", var2);
            } catch (Exception var3) {
                log.debug("Unexpected exception on closing JDBC Connection", var3);
            }
        }

    }

}
