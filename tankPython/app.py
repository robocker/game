from flask import Flask, request
import requests
import json
import websocket
import math

app = Flask(__name__)

current_tank = None
allies = []
enemies = []

@app.route('/move', methods=['POST'])
def move():
    global current_tank, enemies
    destination = request.get_json()

    closest_enemy = get_closest_enemy(current_tank, enemies)
    angle, distance = compute_angle_and_distance(current_tank, destination)
    angle_to_enemy, vertical_angle_to_enemy = compute_aim(current_tank, closest_enemy, distance)

    commands = create_commands(angle, distance, angle_to_enemy, vertical_angle_to_enemy)
    send_message(ws, current_tank, commands)

    return '', 204


def compute_angle_and_distance(current_tank, destination):
    dx = destination['x'] - current_tank['x']
    dy = destination['y'] - current_tank['y']
    distance = math.sqrt(dx**2 + dy**2)
    angle = math.degrees(math.atan2(dy, dx))

    # Adjust angle based on quadrant of destination point
    if dx >= 0 and dy >= 0:
        angle -= 90
    elif dx >= 0 and dy < 0:
        angle = 90 - angle
    elif dx < 0 and dy < 0:
        angle += 90
    elif dx < 0 and dy >= 0:
        angle += 90

    return angle, distance


def compute_aim(current_tank, closest_enemy, distance):
    bullet_speed = 10
    bullet_gravity = 1
    required_angle = math.degrees(math.atan2(closest_enemy['y'] - current_tank['y'], closest_enemy['x'] - current_tank['x']))
    horizontal_distance = distance * math.cos(math.radians(current_tank['angle'] - required_angle))
    time_to_target = horizontal_distance / bullet_speed
    vertical_distance = (distance * math.sin(math.radians(current_tank['angle'] - required_angle))
                         - 0.5 * bullet_gravity * time_to_target**2)
    vertical_angle_to_enemy = math.degrees(math.atan2(vertical_distance, horizontal_distance))
    angle_to_enemy = required_angle - current_tank['angle']
    return angle_to_enemy, vertical_angle_to_enemy


def create_commands(angle, distance, angle_to_enemy, vertical_angle_to_enemy):
    commands = [
        {
            'angle': angle
        },
        {
            'distance': distance
        },
        {
            'turret': {
                'angle': angle_to_enemy,
                'verticalAngle': vertical_angle_to_enemy,
                'shoot': 'END_OF_ACTION'
            }
        }
    ]
    return commands


def send_message(ws, current_tank, commands):
    message = {
        'tankId': current_tank['id'],
        'actions': commands
    }
    ws.send(json.dumps(message))


def get_closest_enemy(current_tank, enemies):
    closest_enemy = None
    closest_distance = float('inf')
    for enemy in enemies:
        dx = enemy['x'] - current_tank['x']
        dy = enemy['y'] - current_tank['y']
        distance = math.sqrt(dx ** 2 + dy ** 2)
        if distance < closest_distance:
            closest_enemy = enemy
            closest_distance = distance
    return closest_enemy

def retrieve_tank_info():
    response = requests.get('http://engine:8080/tank/info')
    tank_info = json.loads(response.text)
    return tank_info

def handle_websocket_message(message, current_tank):
    global allies, enemies

    data = json.loads(message)
    tanks = data.get('tanks', [])

    # Update information about current tank and other tanks
    for tank in tanks:
        if tank['id'] == current_tank['id']:
            current_tank.update(tank)
        elif tank['playerId'] == current_tank['playerId']:
            allies.append(tank)
        else:
            enemies.append(tank)

def on_message(ws, message):
    global current_tank
    handle_websocket_message(message, current_tank)

def on_error(ws, error):
    print(error)

def on_close(ws):
    print("WebSocket closed")

def on_open(ws):
    global current_tank
    # Retrieve tank information
    current_tank = retrieve_tank_info()
    print(f'Retrieved tank info: id={current_tank["id"]}, player_id={current_tank["playerId"]}')
    # Connect to the websocket
    ws.send(json.dumps({'id': current_tank['id']}))
    print("WebSocket opened")

if __name__ == '__main__':
    # Connect to the websocket
    ws = websocket.WebSocketApp("ws://engine:8080/state",
                                on_message=on_message,
                                on_error=on_error,
                                on_close=on_close)
    ws.on_open = on_open
    ws.run_forever()

    # Start the Flask application
    app.run(debug=True)