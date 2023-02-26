import pytest
import json
from app import handle_websocket_message, retrieve_tank_info

def test_handle_websocket_message():
    message = '{"tanks": [{"id": 1, "x": 10, "y": 20, "angle": 0, "playerId": 1, "lifeLevel": 100, "turret": {"angle": 0, "verticalAngle": 0}}]}'
    current_tank = {'id': 1, 'playerId': 1}
    allies = []
    enemies = []

    handle_websocket_message(message, current_tank)

    assert current_tank['x'] == 10
    assert current_tank['y'] == 20
    assert allies == []
    assert enemies == []


def test_retrieve_tank_info(requests_mock):
    # Mock the response from the server
    tank_info = {"id": 1, "playerId": 1}
    requests_mock.get("http://engine:8080/tank/info", json=tank_info)

    # Call the function
    result = retrieve_tank_info()

    # Assert the result
    assert tank_info == result