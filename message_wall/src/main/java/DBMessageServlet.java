import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



@WebServlet("/messageDB")
public class DBMessageServlet extends HttpServlet {
    //解析json
    public ObjectMapper objectMapper = new ObjectMapper();
    //保存表白墙留言
    //List<Message> messages = new ArrayList<>();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.解析json格式的请求
        Message message = objectMapper.readValue(req.getInputStream(), Message.class);

        //2.保存留言信息
        save(message);

        //3.告知网页响应为json格式和返回响应内容
        resp.setContentType("application/json; charset=utf8");
        resp.getWriter().write("{\"ok\":true}");
    }

    private void save(Message message) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            //建立连接
            connection = DBUtil.getConnection();
            //构造sql语句
            String sql = "insert into message values(?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, message.from);
            statement.setString(2, message.to);
            statement.setString(3, message.message);
            statement.setString(4, message.romAdv);
            //执行sql语句
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(null, statement, connection);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Message> messages = load();
        //1.将保存的数据转换成json格式的字符串
        String jsonStr = objectMapper.writeValueAsString(messages);

        //test
        System.out.println(jsonStr);

        //2.告知网页放回的响应是json格式的
        resp.setContentType("application/json; charset=utf8");

        //3.返回全部的留言信息
        resp.getWriter().write(jsonStr);
    }

    private List<Message> load() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Message> messages = new ArrayList<>();
        try {
            connection = DBUtil.getConnection();

            String sql = "select * from message";
            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Message message = new Message();
                message.from = resultSet.getString("from");
                message.to = resultSet.getString("to");
                message.message = resultSet.getString("message");
                message.romAdv = resultSet.getString("romadv");
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(resultSet, statement, connection);
        }
        return messages;
    }
}
