from flask import logging
import googletrans
from googletrans import Translator

# Functions designes to help with the translation process

#Check if a language code is correct
def checkLanguageCode(lang):
    data=googletrans.LANGCODES

    for key, value in data.items():
         # Sets the value of a global variable preferedLang to the corresponding key of the code in the 
         # googletrans.LANGCODES dictionary and returns True
        if value == lang:
            global preferedLang #variable can be accessed and modified outside of the function, in the global scope.
            preferedLang=key
            return True
    return False

def translate_text(text, lang):
    translator = Translator()
    #Detect the language of the input text
    detection = translator.detect(text)
    print(detection.lang)
    print(lang)

    # use the translate() method of the Translator class to translate the input text from the detected language to the target language
    # if a TypeError is raised, log the error message using the logging module
    try:
        translation= translator.translate(text, src=detection.lang, dest=lang)
    except TypeError as e:
        logging.error(str(e))
        
    return translation
