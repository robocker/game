
export class Websocket {
  static run() {

    var socket = new SockJS('http://localhost:8080/gs-guide-websocket');
    var stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (greeting) {
            console.log(JSON.parse(greeting.body));
        });


        stompClient.send("/app/hello", {}, JSON.stringify({'message': "Zarejestruj mnie, kurde!"}));
    });

  }
}
