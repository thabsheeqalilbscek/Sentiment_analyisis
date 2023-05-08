import pandas as pd
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score
from sklearn.model_selection import train_test_split
from sklearn.svm import LinearSVC
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



def predict(text):
    data=pd.read_csv(r"C:\Users\DELL\PycharmProjects\Sentimental\features.csv")

    #   Extract attributes and labels
    attributes = data.values[:1000, :7]
    labels = data.values[:1000, 7]


    rf = RandomForestClassifier(n_estimators=100)
    rf.fit(attributes, labels)

    # prediction
    feats1, sent1 = other_features_(text)
    print(feats1)
    arr = np.array([feats1])

    cls = rf.predict(arr)
    cls = list(cls)
    print(cls[0])
    float_array = np.array(cls)
    rounded_down_integer_array=float_array.astype(int)
    stat=""
    if rounded_down_integer_array[0]==2:
        stat="Positive"
    else:
        stat="Negative"
    return stat

