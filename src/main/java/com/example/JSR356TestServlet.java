package com.example;

import org.eclipse.jetty.demo.EchoEndpoint;
import org.eclipse.jetty.demo.OriginServerConfigurator;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.ClientEndpointConfig;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;

@WebServlet(name= "test", urlPatterns={"/test"})
public class JSR356TestServlet extends HttpServlet {

    private WebSocketContainer client;

    private WebSocketContainer getClientContainer()
    {
        if (client == null)
            client = ContainerProvider.getWebSocketContainer();
        return client;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            URI echoUri = URI.create("wss://echo.websocket.org");

            ClientEndpointConfig clientEndpointConfig = ClientEndpointConfig.Builder.create()
                    .configurator(new OriginServerConfigurator(echoUri))
                    .build();

            EchoEndpoint echoEndpoint = new EchoEndpoint();
            getClientContainer().connectToServer(echoEndpoint, clientEndpointConfig, echoUri);
            System.out.printf("Connected to : %s%n", echoUri);

        } catch (DeploymentException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
