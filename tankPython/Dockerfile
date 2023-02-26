# Use Python 3.8 slim image as the base image
FROM python:3.8-slim

# Set the working directory
WORKDIR /app

# Copy the requirements file
COPY requirements.txt .

# Install required packages
RUN pip install -r requirements.txt && pip install Jinja2==3.0

# Copy the application code
COPY app.py .

# Start the application
CMD [ "python", "app.py" ]