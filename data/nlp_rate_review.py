import csv
import pandas as pd
import numpy as np

# Imports the Google Cloud client library
from google.cloud import language
from google.cloud.language import enums
from google.cloud.language import types

def analyze_sentiment(client, text):
    '''
      Analyze one sentence.
    '''
    document = types.Document(
        content=text.decode('utf-8','ignore').encode("utf-8"),
        type=enums.Document.Type.PLAIN_TEXT)

    # Detects the sentiment of the text
    try:
        sentiment = client.analyze_sentiment(document=document).document_sentiment
        return sentiment.score, sentiment.magnitude
    except:
        print(text)
        
def analyze_paragraph(client, paragraph):
    '''
      Analyze a paragraph by breaking into sentences and calling analyze_sentiment.
    '''
    print(paragraph)
    sentences = paragraph.split("Workload")[0].split(".")
    scores = []
    magnitudes = []
    for sentence in sentences:
        s, m = analyze_sentiment(client, sentence)
        scores.append(s)
        magnitudes.append(m)

    return np.sum(scores), np.sum(magnitudes)    

def main():
    df = pd.read_csv("review.csv")
    
    client = language.LanguageServiceClient()

    df[["score", "magnitude"]] = df["review"].apply(lambda row : analyze_paragraph(client, row)).apply(pd.Series)

    df.to_csv("sentiments.csv", index = False)

    print("Most positive sentiment: \n", df.ix[df["score"].idxmax()]["review"])
    print("Most negative sentiment: \n", df.ix[df["score"].idxmin()]["review"])
    
if __name__ == "__main__":
    main()
