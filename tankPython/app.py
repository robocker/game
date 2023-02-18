from flask import Flask, request
import requests
import json
import websocket

app = Flask(__name__)

@app.route('/move', methods=['POST'])
def move():
    # Placeholder for handling the move request
    pass

def retrieve_tank_info():
    response = requests.get('http://engine:8080/tank/info')
    tank_info = json.loads(response.text)
    return tank_info

def handle_websocket_message(message):
    # Placeholder for handling the websocket messages
    pass

def on_message(ws, message):
    handle_websocket_message(message)

def on_error(ws, error):
    print(error)

def on_close(ws):
    print("WebSocket closed")

def on_open(ws):
    print("WebSocket opened")

if __name__ == '__main__':
    # Retrieve tank information
    tank_info = retrieve_tank_info()
    print(f'Retrieved tank info: id={tank_info["id"]}, player_id={tank_info["playerId"]}')

    # Connect to the websocket
    ws = websocket.WebSocketApp("ws://engine:8080/state",
                                on_message=on_message,
                                on_error=on_error,
                                on_close=on_close)
    ws.on_open = on_open
    ws.run_forever()

    # Start the Flask application
    app.run(debug=True)