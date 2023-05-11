from flask import Flask, render_template, jsonify, request, session, redirect, url_for
from flask_socketio import SocketIO, emit, send
import requests
import datetime as dt
import googletrans
from googletrans import Translator

# app= create_app()
app = Flask(__name__)
app.config['SECRET_KEY'] = 'esunsecret1234'
socketio = SocketIO(app)


def kelvin_to_celsius_fahrenheit(kelvin):
    celsius = kelvin - 273.15
    fahrenheit = celsius * (9/5) + 32
    return celsius, fahrenheit


def get_weather_results(city, data):
    temp_celsius = data['temp_celsius']
    temp_fahrenheit = data['temp_fahrenheit']
    feels_like_celsius = data['feels_like_celsius']
    feels_like_fahrenheit = data['feels_like_fahrenheit']
    humidity = data['humidity']
    wind_speed = data['wind_speed']
    description = data['description']
    sunrise_time = data['sunrise_time']
    sunset_time = data['sunset_time']
    sunrise_time = data['sunrise_time']

    result = f"Temperature in {city}: {temp_celsius:.2f}ºC or {temp_fahrenheit:.2f}ºF \n" \
        f"Temperature in {city} feels like: {feels_like_celsius:.2f}ºC or {feels_like_fahrenheit:.2f}ºF \n" \
        f"Humidity in {city}: {humidity}% \n" \
        f"Wind Speed in {city}: {wind_speed}m/s \n" \
        f"General Weather in {city}: {description} \n" \
        f"Sun rises in {city}: {sunrise_time} local time. \n" \
        f"Sun sets in {city}: {sunset_time} local time."
    return result


def translate(text, lang):
    translator = Translator()
    detection = translator.detect(text)
    translation= translator.translate(text, src=detection.lang, dest=lang)

    return translation


@app.route('/', methods=['GET', 'POST'])
def home():
    session.clear()
    if request.method == "POST":
        name = request.form.get("username")

        if not name:
            return render_template("home.html", error="Please enter a name.", name=name)

        session["name"] = name  # Store users data temporal

        return redirect(url_for("chat"))

    return render_template("home.html", boolean=True)


@app.route("/chat")
def chat():
    # Only available to enter if you have gone through the home page first
    if chat is None or session.get("name") is None:
        return redirect(url_for("home"))

    return render_template("chat.html")


@socketio.on('get_weather')
def get_weather(city):
    BASE_URL = "http://api.openweathermap.org/data/2.5/weather?"
    API_KEY = "f1b1af4594b289ef32467e44f61e0830"
    url = BASE_URL + "appid=" + API_KEY + "&q=" + city

    response = requests.get(url).json()
    data = {
        'temp_kelvin': response['main']['temp'],
        'temp_celsius': kelvin_to_celsius_fahrenheit(response['main']['temp'])[0],
        'temp_fahrenheit': kelvin_to_celsius_fahrenheit(response['main']['temp'])[1],
        'feels_like_kelvin': response['main']['feels_like'],
        'feels_like_celsius': kelvin_to_celsius_fahrenheit(response['main']['feels_like'])[0],
        'feels_like_fahrenheit': kelvin_to_celsius_fahrenheit(response['main']['feels_like'])[1],
        'wind_speed': response['wind']['speed'],
        'humidity': response['main']['humidity'],
        'description': response['weather'][0]['description'],
        'sunrise_time': str(dt.datetime.utcfromtimestamp(response['sys']['sunrise'] + response['timezone'])),
        'sunset_time': str(dt.datetime.utcfromtimestamp(response['sys']['sunset'] + response['timezone']))
    }
    return data


@socketio.on("message")  # Aqui haurem de agafar nosaltres el text de weather
def get_data(data):
    content = {
        "name": session.get("name"),
        "message": data["data"]
    }
    send(content)
    input_string = data["data"].lower()
    print(f"{input_string}")
    words = input_string.split()
    if input_string.find("weather") != -1:
        if len(words) >= 2:
            city = ''
            for i in range(1, len(words)):
                city += words[i]+' '
            print(city)
            weather_data = get_weather(city)
            content = {
                "name": 'Server',
                "message": get_weather_results(city, weather_data)
            }
            send(content)
            print(f"Weather data: {get_weather_results(city,weather_data)}")
        pass

    elif input_string.find("translate") != -1:
        text_to_translate = ""
        for i in range(3, len(words)):
            text_to_translate += words[i]+" "
            print(f"{words[i]}")
        translation = translate(text_to_translate, words[2])
        content = {
            "name": 'Server',
            "message": translation.text
        }
        send(content)
        pass
    elif input_string.find("hola"):
        content = {
            "name": 'Server',
            "message": "Hola! En que et puc ajudar? Prova de dir: \n Weather (Ciutat) --> Per saber el temps que fa en la ciutat elegida \n Translate to EN (La frase) --> Per traduir la frase que vulguis"
        }
        send(content)

    print(f"{session.get('name')} said: {data['data']}")


if __name__ == '__main__':
    # run our flask application, and rerun if there's any changes
    socketio.run(app, debug=True)
