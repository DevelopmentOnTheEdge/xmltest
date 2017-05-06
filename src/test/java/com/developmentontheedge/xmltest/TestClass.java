package com.developmentontheedge.xmltest;

public class TestClass
{
    public static final String ATTR_DBMS_mysql  = "mysql";
    public static final String ATTR_DBMS_postgres  = "postgres";

    public String format(String sql, String dbmsName){
        if( ATTR_DBMS_mysql.equals(dbmsName) )
        {
            return sql;
        }
        else if( ATTR_DBMS_postgres.equals(dbmsName) )
        {
            return sql.replace("`", "\"");
        }
        throw new RuntimeException("Unknown DBMS \"" + dbmsName+ "\". DBMS should be: db2, mysql, oracle, postgresql or sqlserver.");
    }
}
