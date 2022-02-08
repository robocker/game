
export class Websocket {
  static run() {

    var socket = new SockJS('http://localhost:8080/websocket');
    var stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/state/tanks', function (greeting) {
            console.log(JSON.parse(greeting.body));
        });


        stompClient.send("/game/tanks", {}, JSON.stringify({'message': "Zarejestruj mnie, kurde!"}));
    });

  }
}
