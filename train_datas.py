import string
import pandas as pd
import numpy as np
import nltk
# nltk.download('stopwords')
from nltk.stem import PorterStemmer
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score
from sklearn.model_selection import train_test_split
from vaderSentiment.vaderSentiment import SentimentIntensityAnalyzer as VS
from textstat.textstat import *
sentiment_analyzer = VS()
stemmer = PorterStemmer()

stopwords=nltk.corpus.stopwords.words("english")
print("Stopwords   :   ", stopwords)

other_exclusions = ["#ff", "ff", "rt"]
stopwords.extend(other_exclusions)
punct = string.punctuation
print("Punctuation : ", punct)


def preprocess(text_string):
    """
    Accepts a text string and replaces:
    1) urls with URLHERE
    2) lots of whitespace with one instance
    3) mentions with MENTIONHERE
    This allows us to get standardized counts of urls and mentions
    Without caring about specific people mentioned
    """
    space_pattern = '\s+'
    giant_url_regex = ('http[s]?://(?:[a-zA-Z]|[0-9]|[$-_@.&+]|'
        '[!*\(\),]|(?:%[0-9a-fA-F][0-9a-fA-F]))+')
    mention_regex = '@[\w\-]+'
    parsed_text = re.sub(space_pattern, ' ', text_string)
    parsed_text = re.sub(giant_url_regex, 'URLHERE', parsed_text)
    parsed_text = re.sub(mention_regex, 'MENTIONHERE', parsed_text)
    #parsed_text = parsed_text.code("utf-8", errors='ignore')
    return parsed_text


def tokenize(tweet):
    """Removes punctuation & excess whitespace, sets to lowercase,
    and stems tweets. Returns a list of stemmed tokens."""
    tweet_list = tweet.split(" ")
    print("With punctuation ", tweet_list)
    for j in range (len(tweet_list)):
        for i in punct:
            if i in tweet_list[j]:
                word = tweet_list[j].replace(i, "")
                tweet_list[j]=word
    print("Removed punctuation ",tweet_list)
    tokens=[]
    for w in tweet_list:
        # print(w, " : ", stemmer.stem(w))
        tokens.append(stemmer.stem(w))
    # tokens = [stemmer.stem(t) for t in ]
    return tokens

def other_features_(tweet):
    """This function takes a string and returns a list of features.
    These include Sentiment scores, Text and Readability scores,
    as well as Twitter specific features.
    This is modified to only include those features in the final
    model."""

    sentiment = sentiment_analyzer.polarity_scores(tweet)
    # print("SSS   ",sentiment)
    words = preprocess(tweet) #Get text only

    syllables = textstat.syllable_count(words)  # count syllables in words

    num_chars = sum(len(w) for w in words) #num chars in words
    num_words = len(words.split())
    num_unique_terms = len(set(words.split()))
    avg_syl = round(float((syllables + 0.001)) / float(num_words + 0.001), 4)

    ###Modified FK grade, where avg words per sentence is just num words/1
    FKRA = round(float(0.39 * float(num_words) / 1.0) + float(11.8 * avg_syl) - 15.59, 1)
    ##Modified FRE score, where sentence fixed to 1
    FRE = round(206.835 - 1.015 * (float(num_words) / 1.0) - (84.6 * float(avg_syl)), 2)


    pol=0
    if float(sentiment['neg'])> float(sentiment['neu']) and float(sentiment['neg'])> float(sentiment['pos']):
        pol=0
    elif float(sentiment['neu'])> float(sentiment['neg']) and float(sentiment['neu'])> float(sentiment['pos']):
        pol=1

    elif float(sentiment['pos'])> float(sentiment['neg']) and float(sentiment['pos'])> float(sentiment['neu']):
        pol=2

    features = [ FKRA, FRE, syllables, num_chars, num_words,
                num_unique_terms, pol]
    #features = pandas.DataFrame(features)
    return features, sentiment



def get_oth_features(tweets):
    """Takes a list of tweets, generates features for
    each tweet, and returns a numpy array of tweet x features"""
    feats=[]
    for t in tweets:
        feat, sent=other_features_(t)
        # print(feat, "  ", sent)
        feats.append(feat)
    return np.array(feats)



#====================================================================================

df = pd.read_csv(r"C:\HP backup\project\untitled3\static\data_train.csv")
print(df)
tweets=df.Text
classes=df.Emotion
tweets = [x for x in tweets if type(x) == str]
feats = get_oth_features(tweets)




# writing features and label to csv
headings=["FKRA", "FRE", "syllables", "num_chars", "num_words",
                "num_unique_terms", "pol", "label"]
import csv
with open('features.csv', 'w', newline='') as f:
    csv_writer=csv.writer(f)
    csv_writer.writerow(headings)

for i in range(len(tweets)):
    row=[]
    row.append(feats[i][0])     #       FKRA
    row.append(feats[i][1])     #       FRE
    row.append(feats[i][2])     #       syllables
    row.append(feats[i][3])     #       num_chars
    row.append(feats[i][4])     #       num_words
    row.append(feats[i][5])     #       num_unique_terms
    row.append(feats[i][6])     #       polarity
    row.append(classes[i])      #       label

    with open('features.csv', 'a', newline='') as f:
        csv_writer = csv.writer(f)
        csv_writer.writerow(row)



#   performance
data=pd.read_csv('features.csv')
X=data.values[:3000,:7]
Y=data.values[:3000,7]

X_train, X_test, Y_train, Y_test= train_test_split(X, Y, test_size=0.2)

rf =RandomForestClassifier(n_estimators=100)
rf.fit(X, Y)
pred=rf.predict(X_test)
acc=accuracy_score(Y_test, pred)*100
#print("Accuracy Random Forest: ", acc)







#prediction
text="absolutely good product"
feats1, sent1=other_features_(text)
print(feats1)
arr=np.array([feats1])
rf.fit(X, Y)
print(arr)
cls = rf.predict(arr)
cls=list(cls)
print(cls)
