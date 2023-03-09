
r="good boy"
from nltk.sentiment import SentimentIntensityAnalyzer
sia = SentimentIntensityAnalyzer()
scr = sia.polarity_scores(r)
print(scr)
r_stat = ""
if scr['neg'] > scr['pos'] and scr['neg'] > scr['neu']:
    r_stat = "neg"
elif scr['pos'] > scr['neg'] and scr['pos'] > scr['neu']:
    r_stat = "pos"
else:
    r_stat = "neu"

print(r_stat)
