# Use Python 3.8 slim image as the base image
FROM python:3.8-slim

# Set the working directory
WORKDIR /app

# Copy the requirements file
COPY requirements.txt .

# Install required packages
RUN pip install -r requirements.txt

# Copy the application code
COPY app.py .

# Start the application
EXPOSE 80
CMD [ "python", "app.py" ]