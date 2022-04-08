
export class Websocket {
  static run(gameManager) {

    var socket = new SockJS('http://localhost:8080/websocket');
    var stompClient = Stomp.over(socket);
    stompClient.debug = function(str){
        // console.log('from my file:', str);
    }
    stompClient.connect({}, function (frame) {
        // console.log('Connected: ' + frame);
        stompClient.subscribe('/state/tanks', function (response) {
            gameManager.updateTanks(JSON.parse(response.body));

        });


        stompClient.send("/game/tanks", {}, JSON.stringify({'message': "Zarejestruj mnie, kurde!"}));
    });

  }
}
