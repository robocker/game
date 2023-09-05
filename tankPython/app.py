from flask import Flask, request
import requests
import json
import websocket
import math
import threading
import logging

print('Tank python- very first line v.1')
app = Flask(__name__)

current_tank = None
allies = []
enemies = []
lock = threading.Lock()

@app.route("/")
def hello():
    return "Tank Python"

@app.route('/move', methods=['POST'])
def move():
    global current_tank, enemies, lock

    # acquire lock before entering critical section
    lock.acquire()

    try:
        destination = request.get_json()

        closest_enemy = get_closest_enemy(current_tank, enemies)
        angle, distance = compute_angle_and_distance(current_tank, destination)
        angle_to_enemy, vertical_angle_to_enemy = compute_aim(current_tank, closest_enemy, distance)

        print(f"Received move request to destination {destination}")
        print(f"Closest enemy is {closest_enemy} at a distance of {distance}")
        print(f"Aiming at enemy with angle {angle_to_enemy} and vertical angle {vertical_angle_to_enemy}")

        commands = create_commands(angle, distance, angle_to_enemy, vertical_angle_to_enemy)
        send_message(ws, current_tank, commands)
    finally:
        # release lock when done with critical section
        lock.release()

    return '', 204


def compute_angle_and_distance(current_tank, destination):
    dx = destination['x'] - current_tank['x']
    dz = destination['z'] - current_tank['z']
    distance = math.sqrt(dx**2 + dz**2)
    angle = math.degrees(math.atan2(dz, dx))

    # Adjust angle based on quadrant of destination point
    if dx >= 0 and dz >= 0:
        angle -= 90
    elif dx >= 0 and dz < 0:
        angle = 90 - angle
    elif dx < 0 and dz < 0:
        angle += 90
    elif dx < 0 and dz >= 0:
        angle += 90

    return angle, distance


def compute_aim(current_tank, closest_enemy, distance):
    bullet_speed = 10
    bullet_gravity = 1
    required_angle = math.degrees(math.atan2(closest_enemy['z'] - current_tank['z'], closest_enemy['x'] - current_tank['x']))
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
        dz = enemy['z'] - current_tank['z']
        distance = math.sqrt(dx ** 2 + dz ** 2)
        if distance < closest_distance:
            closest_enemy = enemy
            closest_distance = distance
    return closest_enemy

def retrieve_tank_info():
    print(f"Request for tank info")
    response = requests.get('http://engine:8080/tank/info')
    print(f"Received tank info: {response.text}")
    tank_info = json.loads(response.text)
    return tank_info

def handle_websocket_message(message, current_tank):
    global allies, enemies

    data = json.loads(message)
    tanks = data.get('tanks', [])

    # Update information about current tank and other tanks
    for tank in tanks:
        tank_id = tank['id']
        player_id = tank['playerId']

        if tank_id == current_tank['id']:
            if tank != current_tank:
                #print(f"Updated information about current tank: {current_tank} -> {tank}")
                current_tank.update(tank)
        elif any(tank_id == t['id'] for t in allies):
            index = next((i for i, t in enumerate(allies) if t['id'] == tank_id), None)
            if tank != allies[index]:
                #print(f"Updated information about ally tank {tank_id}: {allies[index]} -> {tank}")
                allies[index].update(tank)
        elif any(tank_id == t['id'] for t in enemies):
            index = next((i for i, t in enumerate(enemies) if t['id'] == tank_id), None)
            if tank != enemies[index]:
                #print(f"Updated information about enemy tank {tank_id}: {enemies[index]} -> {tank}")
                enemies[index].update(tank)
        else:
            if player_id == current_tank['playerId']:
                allies.append(tank)
                print(f"Found ally tank: {tank}")
            else:
                enemies.append(tank)
                print(f"Found enemy tank: {tank}")

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

def set_current_tank(tank):
    global current_tank
    current_tank = tank

def get_allies():
    global allies
    return allies

def get_enemies():
    global enemies
    return enemies

def get_current_tank():
    global current_tank
    return current_tank

if __name__ == '__main__':

    # Retrieve tank information
    try:
        current_tank = retrieve_tank_info()
    except Exception:
        logging.exception("info error")

    # Connect to the websocket
    ws = websocket.WebSocketApp("ws://engine:8080/state",
                                on_message=on_message,
                                on_error=on_error,
                                on_open = on_open,
                                on_close=on_close)

    # Start the WebSocket connection in a separate thread
    websocket_thread = threading.Thread(target=ws.run_forever)
    websocket_thread.start()

    # Start the Flask application
    app.run(debug=True, host='0.0.0.0', port=80)