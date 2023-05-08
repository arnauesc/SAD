from flask import Flask, render_template, jsonify, request, session, redirect, url_for
from flask_socketio import SocketIO, emit, send
import requests

#app= create_app()
app = Flask(__name__)
app.config['SECRET_KEY'] = 'esunsecret1234'
socketio = SocketIO(app)

@app.route('/', methods=['GET', 'POST'])
def home():
    session.clear()
    if request.method == "POST":
        name = request.form.get("name")

        if not name:
            return render_template("home.html", error="Please enter a name.", name=name)
        
        session["name"]=name #Store users data temporal
        
        return redirect(url_for("chat"))
        
    return render_template("home.html", boolean=True)

@app.route("/chat")
def chat():
    if chat is None or session.get("name") is None: #Only available to enter if you have gone through the home page first
        return redirect(url_for("home"))

    return render_template("chat.html")

# @socketio.on("message") #Aqui haurem de agafar nosaltres el text de weather
# def message(data):
#   

# @socketio.on("connect")
# def connect(auth):
#     
# @socketio.on("disconnect")
# def disconnect():
#    
          
if __name__== '__main__':
    socketio.run(app, debug=True) #run our flask application, and rerun if there's any changes

