import json
import numpy as np
import pandas as pd
import os
import glob
from sklearn.model_selection import train_test_split
# import the class
from sklearn.linear_model import LogisticRegression
from sklearn import metrics
from sklearn.preprocessing import StandardScaler
from sklearn.decomposition import PCA
from sklearn.svm import SVC
import pickle
from sklearn.multiclass import OneVsRestClassifier
from sklearn.ensemble import RandomForestClassifier
from sklearn.datasets import make_classification
from sklearn.neural_network import MLPClassifier

if __name__ == '__main__':
    data1 = pd.read_csv("./jsonFiles/mergedFiles.csv")
    label = {'book': 1, 'car': 2, 'gift': 3, 'movie':4, 'sell':5, 'total':6}
    data1.label = [label[item] for item in data1.label]

    k = data1
    y = k['label']
    x = k.iloc[:,1:-1].values
    #k.drop(['Frames#',  'score_overall', 'nose_score', 'leftEye_score', 'rightEye_score', 'leftEar_score', 'rightEar_score', 'leftShoulder_score', 'rightShoulder_score', 'leftElbow_score', 'rightElbow_score', 'leftWrist_score', 'rightWrist_score','leftHip_score', 'rightHip_score', 'leftKnee_score', 'rightKnee_score', 'rightKnee_score', 'rightKnee_score', 'leftHip_x', 'leftHip_y', 'rightHip_x', 'rightHip_y', 'leftKnee_x', 'leftKnee_y', 'rightKnee_x', 'rightKnee_y', 'leftAnkle_x', 'leftAnkle_y', 'rightAnkle_x', 'rightAnkle_y', 'leftAnkle_score', 'rightAnkle_score', 'label'], axis = 1, inplace = True)
    X_train, X_test, y_train, y_test = train_test_split(x, y, test_size=0.2, random_state=4)

    pca = PCA(.99)
    result = pca.fit_transform(X_train)
    
    from sklearn.metrics import accuracy_score  
    from sklearn.tree import DecisionTreeClassifier
    dtc = DecisionTreeClassifier( criterion = "entropy", random_state = 100, max_depth = 15, min_samples_leaf = 5)
    dtc.fit(X_train, y_train)
    pickle.dump(dtc, open("./models/DecisionTreeClassifier.pkl", 'wb'))
    y_pred = dtc.predict(X_test)
    print("DTC ") 
    print(accuracy_score(y_test,y_pred)*100)

    from sklearn.neighbors import KNeighborsClassifier
    knnModel = KNeighborsClassifier(n_neighbors = 30, metric="euclidean")
    knnModel.fit(X_train, y_train)
    pickle.dump(knnModel, open("./models/KNeighborsClassifier.pkl", 'wb'))
    y_pred = knnModel.predict(X_test)
    import statistics
    #print(statistics.mode(y_pred.tolist()))
    #print("knnModel ")
    #print(accuracy_score(y_test,y_pred)*100)

    ######################################################
    rfc = RandomForestClassifier(n_estimators=28, max_depth=8)
    rfc.fit(X_train, y_train)
    pickle.dump(rfc, open("./models/RandomForestClassifier.pkl", 'wb'))
    y_pred = rfc.predict(X_test)
    #print(rfc.score(X_test,y_test))

    #######################################################
    mlp = MLPClassifier(solver='lbfgs', alpha=1e-5,hidden_layer_sizes=(5,5), random_state=1)
    mlp.fit(X_train, y_train)
    pickle.dump(mlp, open("./models/MLPClassifier.pkl", 'wb'))
    print("Done")