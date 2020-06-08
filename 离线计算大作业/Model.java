import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.List;

import java.sql.*;

public class Model {


//sql连接
private static Connection conn;
    public static void ShowTables(String[] args) {
        DBinit();
        try {
            //获取数据库的元数据
            DatabaseMetaData dbMetaData = conn.getMetaData();
            //从元数据中获取到所有的表名
            ResultSet rs = dbMetaData.getTables(null, null, null,new String[] { "TABLE" });
            //存放所有表名
            List<String> tableNames = new ArrayList<>();
            //存放当前表的字段
            List<String>  fields= new ArrayList<>();
            //存放当前表的字段类型
            List<String> fieldstype = new ArrayList<>();
            while(rs.next())
            {
                System.out.println("表名: "+rs.getString("TABLE_NAME"));
                System.out.println("表类型: "+rs.getString("TABLE_TYPE"));
                System.out.println("表所属数据库: "+rs.getString("TABLE_CAT"));
                System.out.println("表所属用户名: "+rs.getString("TABLE_SCHEM"));
                System.out.println("表备注: "+rs.getString("REMARKS"));
                tableNames.add(rs.getString("TABLE_NAME"));
            }
            //查询每个表的字段
            for(String record:tableNames)
            {
                String sql = "select * from " + record;
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rstable = ps.executeQuery();
                //结果集元数据
                ResultSetMetaData meta = rstable.getMetaData();
                //表列数量
                int columeCount=meta.getColumnCount();
                for (int i=1;i<=columeCount;i++)
                {
                    fields.add(meta.getColumnName(i));
                    fieldstype.add(meta.getColumnTypeName(i));

                }
                System.out.println("表"+record+"字段: "+fields);
                System.out.println("表"+record+"字段类型: "+fieldstype);
                fields.clear();
            }
        } catch (SQLException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        DBclose();
    }

    public static void DBinit(){
        //驱动程序名
        String driver = "com.mysql.jdbc.Driver";
        //MySQL配置时的用户名
        String user ="xxx";
        //MySQL配置时的密码
        String password = "xxx";
        //不同端口号
        String[] db_url_port = {"32080", "32081", "32082","3308"};
        //数据库服务器
        String dbServer="";
        for(String db_port:db_url_port)
        {
            dbServer="ip:"+db_port;
            //URL指向要访问的数据库名mydata
            String url = "jdbc:mysql://"+dbServer+"/"+"dbname";
            try {
                Class.forName("com.mysql.jdbc.Driver");
                //声明Connection对象
                conn = DriverManager.getConnection(url,user,password);
                if(!conn.isClosed()){
                    System.out.println("，数据库连接成功！");
                    break;
                }
            } catch (Exception e) {
                System.out.println("数据库连接失败！");
                continue;
            }
        }
    }

    public static void DBclose(){
        try {
            conn.close();
        } catch (SQLException e) {
            //TODO Auto-generated catch block
            System.out.println("数据关闭异常");
            e.printStackTrace();
        }
    }

public void query(){
        //执行statement的sql操作
        //调用Controller的render（）方法通知controller改变view

}



}







