import pandas as pd
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score
from sklearn.model_selection import train_test_split
from sklearn.svm import LinearSVC


def find_acc_rf():
    data=pd.read_csv(r"C:\HP backup\project\untitled3\features.csv")

    #   Extract attributes and labels
    attributes = data.values[:3000, :7]
    labels = data.values[:3000, 7]

    #   Split attributes and labels
    X_train, X_test, Y_train, Y_test = train_test_split(attributes, labels, test_size=0.2)
    rf = RandomForestClassifier(n_estimators=100)
    rf.fit(attributes, labels)

    #   Predict result
    pred = rf.predict(X_test)
    print("Original Result\t\tPredicted Result")
    for i in range(len(pred)):
        print(Y_test[i], "\t\t", pred[i])



    #   Find accuracy score
    acc=accuracy_score(Y_test, pred)
    print(acc)
    print("\nAccuracy : ", round(acc*100,2), "%")

    #f1-score
    from sklearn.metrics import f1_score
    rf_f1=f1_score(Y_test, pred, average="weighted")
    print("F1 score Random Forest", rf_f1)

    # recall
    from sklearn.metrics import recall_score
    rf_recall = recall_score(Y_test, pred, average="weighted")
    print("Recall Random Forest", rf_recall)

find_acc_rf()