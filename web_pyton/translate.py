import googletrans

def checkLanguageCode(lang):
    data=googletrans.LANGCODES
    for key, value in data.items():
        if value == lang:
            global preferedLang
            preferedLang=key
            return True
    return False