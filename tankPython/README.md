# Running tests
python -m pytest

# Build docker container

sudo docker build -t robocker/tankpython .

# Running standalone by docker:

sudo docker run -p 49155:80 -d --rm --name tank-1 robocker/tankpython:latest