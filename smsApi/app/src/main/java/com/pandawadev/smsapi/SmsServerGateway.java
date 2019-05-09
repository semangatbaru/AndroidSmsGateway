package com.pandawadev.smsapi;
import org.apache.http.*;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.*;
import org.apache.http.impl.*;
import org.apache.http.protocol.*;
import java.io.*;
import java.net.*;

public class SmsServerGateway {
    private int port;
    private HttpService httpService;
    private ServerSocket serverSocket;
    private HttpContext httpContext;
    public SmsServerGateway(int port){
        this.port = port;
        httpService = new HttpService(new BasicHttpProcessor(),
                new DefaultConnectionReuseStrategy(),
                new DefaultHttpResponseFactory());
        httpContext = new BasicHttpContext();

        //handler
        HttpRequestHandlerRegistry registry =  new HttpRequestHandlerRegistry();
        registry.register("*", new SmsGatewayHandler());
        httpService.setHandlerResolver(registry);
    }

    public void start() throws IOException, HttpException {

        //membuat server soket berdasar port
        serverSocket = new ServerSocket(port);

        while (true){

            //terima server soket client bila ada resuet masuk
            Socket socket =serverSocket.accept();

            //handle request sebagai HTTP request
            DefaultHttpServerConnection sCon = new DefaultHttpServerConnection();
            sCon.bind(socket, new BasicHttpParams());
            httpService.handleRequest(sCon,httpContext);

            //close koneksi client
            socket.close();
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
    }
}
