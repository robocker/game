import pytest
import json
from unittest.mock import MagicMock
from unittest.mock import patch
from app import handle_websocket_message, retrieve_tank_info, get_closest_enemy, move, app

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


from unittest.mock import MagicMock

def test_move():
    with app.test_client() as client:
        destination = {"x": 10.0, "y": 10.0}

        # Mock retrieve_tank_info function
        with patch('app.retrieve_tank_info') as mock_retrieve_tank_info:
            # Set current tank id and player id
            current_tank = {"id": "tank-id-123", "playerId": "player-id-123"}
            mock_retrieve_tank_info.return_value = current_tank

            # Mock handle_websocket_message function
            with patch('app.handle_websocket_message') as mock_handle_websocket_message:
                # Send a POST request to /move endpoint with destination
                response = client.post('/move', json=destination)
                assert response.status_code == 200

                # Verify websocket message is sent with correct data
                mock_handle_websocket_message.assert_called_once_with(
                    json.dumps({
                        "tankId": "tank-id-123",
                        "actions": [
                            {"angle": 45.0},
                            {"distance": 7.07},
                            {
                                "turret": {
                                    "angle": 45.0,
                                    "verticalAngle": 0.0,
                                    "shoot": "END_OF_ACTION"
                                }
                            }
                        ]
                    }),
                    current_tank
                )