# Running tests
python -m pytest

# Build docker container

docker build -t robocker/tankpython .

# Running standalone by docker:

docker run -p 49155:80 -d --rm --name tank-3 robocker/tankpython:latest

# Watching logs

docker logs tank-3 --follow

# Running locally

## virtual env:
python -m venv ./env

### activating:
.\env\Scripts\activate