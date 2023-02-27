# Running tests
python -m pytest

# Build docker container

sudo docker build -t robocker/tankpython .

# Running standalone by docker:

sudo docker run -p 49155:80 -d --rm --name tank-3 robocker/tankpython:latest

# Entering into container

sudo docker exec -it tank-3 'python app.py'