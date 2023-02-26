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

    # Find the closest enemy tank
    closest_enemy = get_closest_enemy(current_tank, enemies)

    # Compute the angle and distance required to reach the destination
    dx = destination['x'] - current_tank['x']
    dy = destination['y'] - current_tank['y']
    distance = math.sqrt(dx**2 + dy**2)
    angle = math.degrees(math.atan2(dy, dx))

    # Compute the angle and vertical angle required to aim at the closest enemy
    dt = distance / 10  # time required for bullet to reach destination
    ex = closest_enemy['x'] + closest_enemy['speedX'] * dt  # estimated x position of enemy at time of bullet impact
    ey = closest_enemy['y'] + closest_enemy['speedY'] * dt  # estimated y position of enemy at time of bullet impact
    etx = ex - current_tank['x']  # x distance from tank to estimated enemy position
    ety = ey - current_tank['y']  # y distance from tank to estimated enemy position
    angle_to_enemy = math.degrees(math.atan2(ety, etx))  # angle required to aim at estimated enemy position
    vertical_angle_to_enemy = math.degrees(math.atan2(closest_enemy['z'] - current_tank['z'], distance))  # vertical angle required to aim at estimated enemy position

    # Create the commands
    commands = [
        {
            'angle': angle,
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

    # Send the message over the websocket
    message = {
        'tankId': current_tank['id'],
        'actions': commands
    }
    ws.send(json.dumps(message))

    return '', 204

def get_closest_enemy(current_tank, enemies):
    # Initialize the closest enemy and its distance to be None and infinity, respectively
    closest_enemy = None
    closest_distance = float('inf')

    # Iterate over all enemies to find the closest one
    for enemy in enemies:
        # Calculate the distance between the current tank and the enemy
        dx = enemy['x'] - current_tank['x']
        dy = enemy['y'] - current_tank['y']
        distance = math.sqrt(dx**2 + dy**2)

        # If the current enemy is closer than the closest enemy, update the closest enemy and its distance
        if distance < closest_distance:
            closest_enemy = enemy
            closest_distance = distance

    # Return the closest enemy
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