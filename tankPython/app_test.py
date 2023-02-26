from unittest.mock import patch
from app import handle_websocket_message, retrieve_tank_info, get_closest_enemy, app,compute_angle_and_distance
import math

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


def test_get_closest_enemy():
    # Test case 1: Single enemy
    current_tank = {
        "x": 0,
        "y": 0,
        "turret": {
            "angle": 0
        }
    }
    enemies = [
        {
            "x": 10,
            "y": 10,
            "turret": {
                "angle": 0
            }
        }
    ]
    assert get_closest_enemy(current_tank, enemies) == enemies[0]

    # Test case 2: Multiple enemies, one closest
    current_tank = {
        "x": 0,
        "y": 0,
        "turret": {
            "angle": 0
        }
    }
    enemies = [
        {
            "x": 10,
            "y": 10,
            "turret": {
                "angle": 0
            }
        },
        {
            "x": 20,
            "y": 20,
            "turret": {
                "angle": 0
            }
        }
    ]
    assert get_closest_enemy(current_tank, enemies) == enemies[0]

    # Test case 3: Multiple enemies, all equidistant
    current_tank = {
        "x": 0,
        "y": 0,
        "turret": {
            "angle": 0
        }
    }
    enemies = [
        {
            "x": 10,
            "y": 10,
            "turret": {
                "angle": 0
            }
        },
        {
            "x": -10,
            "y": -10,
            "turret": {
                "angle": 0
            }
        }
    ]
    assert get_closest_enemy(current_tank, enemies) == enemies[0]

    # Test case 4: No enemies
    current_tank = {
        "x": 0,
        "y": 0,
        "turret": {
            "angle": 0
        }
    }
    enemies = []
    assert get_closest_enemy(current_tank, enemies) is None


def test_compute_angle_and_distance():
    # Test with destination at (0, 0)
    current_tank = {'x': 1, 'y': 1}
    destination = {'x': 0, 'y': 0}
    angle, distance = compute_angle_and_distance(current_tank, destination)
    assert math.isclose(angle, 315, rel_tol=1e-9) or math.isclose(angle, -45, rel_tol=1e-9)
    assert math.isclose(distance, math.sqrt(2), rel_tol=1e-9)

    # Test with destination at (1, 2)
    current_tank = {'x': 0, 'y': 0}
    destination = {'x': 1, 'y': 2}
    angle, distance = compute_angle_and_distance(current_tank, destination)
    assert math.isclose(angle, 63.4349488, rel_tol=1e-9)
    assert math.isclose(distance, math.sqrt(5), rel_tol=1e-9)