package cn.wqk.serverwebsocket.socket;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 吴青珂
 * @Date: 2021/05/31/16:16
 * @Description:
 */
@Component
@ServerEndpoint("/websocket/{username}") //暴露的ws应用的路径
public class WebSocket {

    private static CopyOnWriteArraySet<Session> sessions =new CopyOnWriteArraySet<>();

    /**
     * 客户端与服务端连接成功
     * @param session
     * @param username
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("username") String username){
        sessions.add(session); //连接成功将该次连接添加到连接集合中
        System.out.println("与"+username+"的websocket连接成功，当前连接数"+sessions.size());
    }

    /**
     * 客户端与服务端连接关闭
     * @param session
     * @param username
     */
    @OnClose
    public void onClose(Session session,@PathParam("username") String username){
        sessions.remove(session); //连接关闭则移除该连接
        System.out.println("与"+username+"的websocket连接关闭，当前连接数"+sessions.size());
    }

    /**
     * 客户端向服务端发送消息
     * @param message
     * @param username
     * @throws IOException
     */
    @OnMessage
    public void onMsg(String message,@PathParam("username") String username) throws IOException {
        System.out.println("来自"+username+"客户端的消息："+message);
        sendAllMessage(message);
    }

    /**
     * 客户端与服务端连接异常
     * @param error
     * @param session
     * @param username
     */
    @OnError
    public void onError(Throwable error,Session session,@PathParam("username") String username) {
        System.out.println("onError......"+error.getMessage());

    }

    //向所有客户端发送消息（广播）
    private void sendAllMessage(String message){
        System.out.println("广播消息："+message);
        for (Session session : sessions) {
            try {
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //只向当前客户端发送消息
    private void sendOneMessage(String message){

    }

}
