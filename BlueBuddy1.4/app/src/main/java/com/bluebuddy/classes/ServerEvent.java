package com.bluebuddy.classes;


public class ServerEvent {
    private ServerResponse serverResponse;
    private ServerResponseLogin serverResponseLogin;


    public ServerResponseLogin getServerResponseLogin() {
        return serverResponseLogin;
    }

    public void setServerResponseLogin(ServerResponseLogin serverResponseLogin) {
        this.serverResponseLogin = serverResponseLogin;
    }

    public ServerEvent(ServerResponseLogin serverResponseLogin) {
        this.serverResponseLogin = serverResponseLogin;
    }




    public ServerResponse getServerResponse() {
        return serverResponse;
    }

    public void setServerResponse(ServerResponse serverResponse) {
        this.serverResponse = serverResponse;
    }
    public ServerEvent(ServerResponse serverResponse) {
        this.serverResponse = serverResponse;
    }
}
