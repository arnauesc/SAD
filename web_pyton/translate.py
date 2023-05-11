from flask import logging
import googletrans
from googletrans import Translator

def checkLanguageCode(lang):
    data=googletrans.LANGCODES
    for key, value in data.items():
        if value == lang:
            global preferedLang
            preferedLang=key
            return True
    return False

def translate_text(text, lang):
    translator = Translator()
    detection = translator.detect(text)
    print(detection.lang)
    print(lang)
    try:
        translation= translator.translate(text, src=detection.lang, dest=lang)
    except TypeError as e:
        logging.error(str(e))
        
    return translation
