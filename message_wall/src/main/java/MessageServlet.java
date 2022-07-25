import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



@WebServlet("/message")
public class MessageServlet extends HttpServlet {
    //解析json
    public ObjectMapper objectMapper = new ObjectMapper();
    //保存表白墙留言
    List<Message> messages = new ArrayList<>();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.解析json格式的请求
        Message message = objectMapper.readValue(req.getInputStream(), Message.class);

        //2.保存留言信息
        messages.add(message);

        //3.告知网页响应为json格式和返回响应内容
        resp.setContentType("application/json; charset=utf8");
        resp.getWriter().write("{\"ok\":true}");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.将保存的字符串转换成json格式的字符串
        String jsonStr = objectMapper.writeValueAsString(messages);

        //test
        System.out.println(jsonStr);

        //2.告知网页放回的响应是json格式的
        resp.setContentType("application/json; charset=utf8");

        //3.返回全部的留言信息
        resp.getWriter().write(jsonStr);
    }
}
