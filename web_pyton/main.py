from flask import Flask, render_template, jsonify, request, session, redirect, url_for
from flask_socketio import SocketIO, send
import requests
import datetime as dt
import googletrans 
from weather import get_weather_results, kelvin_to_celsius_fahrenheit 

from translate import checkLanguageCode, translate_text

app = Flask(__name__) 
app.config['SECRET_KEY'] = 'esunsecret1234' #Para proporcionar seguridad en la aplicación.
socketio = SocketIO(app) #Ofrece una solución de comunicación bidireccional confiable y escalable

#Controladores de ruta que definen la lógica que se ejecutará cuando se acceda a una determinada URL
@app.route('/', methods=['GET', 'POST'])
def home():
    session.clear() #remove all the items stored in the session object
    if request.method == "POST":
        name = request.form.get("username")

        if not name:
            return render_template("home.html", error="Please enter a name.", name=name)
        session["name"] = name  # Store users data temporal

        return redirect(url_for("chat"))

    return render_template("home.html", boolean=True)


@app.route("/chat", methods=["GET", "POST"])
def chat():
    if request.method == "POST":
        return redirect(url_for("home"))

    # Only available to enter if you have gone through the home page first
    if chat is None or session.get("name") is None:
        return redirect(url_for("home"))

    return render_template("chat.html")


#Sockeio.on– used to decorate a function that will handle a particular event that is expected to be sent from a WebSocket client 
# to the server
@socketio.on('get_weather') 
def get_weather(city):
    BASE_URL = "http://api.openweathermap.org/data/2.5/weather?"
    API_KEY = "f1b1af4594b289ef32467e44f61e0830"
    url = BASE_URL + "appid=" + API_KEY + "&q=" + city
    error=False

    weather_info = requests.get(url) #HTTP requests to external APIs or services, and then process the responses to render a dynamic web page
    print(weather_info)
    if weather_info.status_code == 404: #If the information is not found
        error= True
        data={}
    else:
        response= weather_info.json()
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
    return error, data


@socketio.on("message")  # Aqui haurem de agafar nosaltres el text de weather o translate
def get_data(data):
    #We get the information from the field in the form 
    content = {
        "name": session.get("name"),
        "message": data["data"]
    }
    send(content) #We send our message to be printed on the chat screen
    input_string = data["data"].lower()
    print(f"{input_string}")
    words = input_string.split()
    print(words)

    # We check the first word of the sentence if matches any of our options
    if words[0]=='weather': #Pattern: weather [city]
        if len(words) >= 2:
            city = ''
            for i in range(1, len(words)):
                city += words[i]+' '
            print(city)
            error, weather_data= get_weather(city)
            msg= get_weather_results(city, weather_data, error)
            print(f"Weather data: {get_weather_results(city,weather_data, error)}")
        else:
            msg = "Incorrect input --> Weather [city]"

    elif words[0]=='translate': #Pattern: Translate to [language] [text]
        text_to_translate = ''
        #We verify we are using the right words
        if(len(words)>3):
            for i in range(3, len(words)):
                text_to_translate += words[i]+' '
            lang= words[2]
            #print(text_to_translate)
            #print(lang)
            if checkLanguageCode(lang): #Verify if the language exists
                translation = translate_text(text_to_translate, lang)
                msg= translation.text
            else:
                link = "https://py-googletrans.readthedocs.io/en/latest/#googletrans-languages "
                link_text = "Language not found. You can find the available languages by clicking here."
                msg = f'<a href="{link}">{link_text}</a>'
        else:
            msg = "Incorrect input --> Translate to [language code] [text to translate]"
        pass
    #If not, the message will be a list of the possible options
    else: 
        msg="Hi! How can I help you out? You can say: \n Weather [city] --> To know the weather in the selected city\n Translate to [language code] [text to translate] --> To translate the text you want\n Some common language codes are:\n EN (English)\n ES (Spanish)\n DE (German)\n FR (French)" 
    
    #We now send the proper server message depending on the situation
    content = {
    "name": 'Server',
    "message": msg
    }
    send(content)

    print(f"{session.get('name')} said: {data['data']}")


if __name__ == '__main__':
    # run our flask application, and rerun if there's any changes
    socketio.run(app, debug=True) #the server will reload itself whenever the code changes and it also provides more detailed error messages.
