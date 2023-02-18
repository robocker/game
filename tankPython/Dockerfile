# Używamy obrazu bazowego z Pythona
FROM python:3.8-slim

# Ustawiamy katalog roboczy
WORKDIR /app

# Kopiujemy plik requirements.txt
COPY requirements.txt .

# Instalujemy potrzebne pakiety
RUN pip install -r requirements.txt

# Kopiujemy kod aplikacji
COPY app.py .

# Uruchamiamy aplikację
CMD [ "python", "app.py" ]