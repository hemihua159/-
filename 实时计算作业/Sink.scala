import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.DriverManager
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction
import org.apache.flink.configuration.Configuration


class Sink extends RichSinkFunction[(String,String)]{

  var conn:Connection=_;
  var pres:PreparedStatement = _;
  var username = "user20";
  var password = "pass@bingo20";
  var dburl = "jdbc:mysql://10.202.92.28:3306/{db_name}";
  var sql = "insert into words(word,count) values(?,?)";
  override def invoke(value:(String, String) ) {

    pres.setString(1, value._1);
    pres.setString(2,value._2);
    pres.executeUpdate();
    System.out.println("values ：" +value._1+"--"+value._2);
  }

  override def open( parameters:Configuration) {
    Class.forName("com.mysql.jdbc.Driver");
    conn = DriverManager.getConnection(dburl, username, password);
    pres = conn.prepareStatement(sql);
    super.close()
  }

  override def close() {
    pres.close();
    conn.close();
  }
}