package cn.wqk.serverwebsocket.socket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 吴青珂
 * @Date: 2021/05/31/16:16
 * @Description:
 */
@Component
@Slf4j
@ServerEndpoint("/websocket/{username}") //暴露的ws应用的路径
public class WebSocket {

    /** 当前在线客户端数量（线程安全的） */
    private static AtomicInteger onlineClientNumber = new AtomicInteger(0);

    /** 当前在线客户端集合（线程安全的）：以键值对方式存储，key是连接的编号，value是连接的对象 */
    private static Map<String ,Session> onlineClientMap = new ConcurrentHashMap<>();

    /**
     * 客户端与服务端连接成功
     * @param session
     * @param username
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("username") String username){
        /*
            do something for onOpen
            与当前客户端连接成功时
         */
        onlineClientNumber.incrementAndGet();//在线数+1
        onlineClientMap.put(session.getId(),session);//添加当前连接的session
        log.info("时间[{}]：与用户[{}]的连接成功，当前连接编号[{}]，当前连接总数[{}]",
                new Date().toLocaleString(),
                username,
                session.getId(),
                onlineClientNumber);
    }

    /**
     * 客户端与服务端连接关闭
     * @param session
     * @param username
     */
    @OnClose
    public void onClose(Session session,@PathParam("username") String username){
        /*
            do something for onClose
            与当前客户端连接关闭时
         */
        onlineClientNumber.decrementAndGet();//在线数-1
        onlineClientMap.remove(session.getId());//移除当前连接的session
        log.info("时间[{}]：与用户[{}]的连接关闭，当前连接编号[{}]，当前连接总数[{}]",
                new Date().toLocaleString(),
                username,
                session.getId(),
                onlineClientNumber);
    }

    /**
     * 客户端与服务端连接异常
     * @param error
     * @param session
     * @param username
     */
    @OnError
    public void onError(Throwable error,Session session,@PathParam("username") String username) {
        /*
            do something for onError
            与当前客户端连接异常时
         */
        error.printStackTrace();
    }

    /**
     * 客户端向服务端发送消息
     * @param message
     * @param username
     * @throws IOException
     */
    @OnMessage
    public void onMsg(Session session,String message,@PathParam("username") String username) throws IOException {
        /*
            do something for onMessage
            收到来自当前客户端的消息时
         */
        log.info("时间[{}]：来自连接编号为[{}]的消息：[{}]",
                new Date().toLocaleString(),
                session.getId(),
                message);
        sendAllMessage(message);
    }

    //向所有客户端发送消息（广播）
    private void sendAllMessage(String message){
        Set<String> sessionIdSet = onlineClientMap.keySet(); //获得Map的Key的集合
        for (String sessionId : sessionIdSet) { //迭代Key集合
            Session session = onlineClientMap.get(sessionId); //根据Key得到value
            session.getAsyncRemote().sendText(message); //发送消息给客户端
        }
    }

    //只向当前客户端发送消息
    private void sendOneMessage(String message){

    }

}
