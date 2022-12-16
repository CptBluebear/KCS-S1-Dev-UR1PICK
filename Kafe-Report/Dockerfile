FROM python:3.9.15-slim-bullseye
WORKDIR /app
ADD ./requirements.txt ./
RUN ["pip3", "install", "-r", "requirements.txt"]
ADD ./main.py ./
CMD ["python3", "main.py"]
