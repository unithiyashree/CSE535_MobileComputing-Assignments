#!flask/bin/python
from flask import Flask
import json
import pickle
import numpy as np
import pandas as pd
from flask import request
from flask import jsonify

app = Flask(__name__)
model1 = pickle.load(open('./models/RandomForestClassifier.pkl', 'rb'))
model2 = pickle.load(open('./models/KNeighborsClassifier.pkl', 'rb'))
model3 = pickle.load(open('./models/DecisionTreeClassifier.pkl', 'rb'))
model4 = pickle.load(open('./models/MLPClassifier.pkl', 'rb'))

patterns = {1:'book', 2:'car', 3:'gift', 4:'movie', 5:'sell',  6:'total'}
columna = ['nose_x', 'nose_y', 'leftEye_x', 'leftEye_y', 'rightEye_x', 'rightEye_y', 'leftEar_x', 'leftEar_y', 'rightEar_x', 'rightEar_y', 'leftShoulder_x', 'leftShoulder_y','rightShoulder_x', 'rightShoulder_y', 'leftElbow_x', 'rightElbow_x', 'rightElbow_y', 'leftElbow_y', 'leftWrist_x', 'leftWrist_y', 'rightWrist_x', 'rightWrist_y']

@app.route("/", methods=['GET', 'POST', 'DELETE', 'PUT'])
def predict():
    try:
        data = request.get_json()
        columns = ['score_overall', 'nose_score', 'nose_x', 'nose_y', 'leftEye_score', 'leftEye_x', 'leftEye_y',
               'rightEye_score', 'rightEye_x', 'rightEye_y', 'leftEar_score', 'leftEar_x', 'leftEar_y',
               'rightEar_score', 'rightEar_x', 'rightEar_y', 'leftShoulder_score', 'leftShoulder_x', 'leftShoulder_y',
               'rightShoulder_score', 'rightShoulder_x', 'rightShoulder_y', 'leftElbow_score', 'leftElbow_x',
               'leftElbow_y', 'rightElbow_score', 'rightElbow_x', 'rightElbow_y', 'leftWrist_score', 'leftWrist_x',
               'leftWrist_y', 'rightWrist_score', 'rightWrist_x', 'rightWrist_y', 'leftHip_score', 'leftHip_x',
               'leftHip_y', 'rightHip_score', 'rightHip_x', 'rightHip_y', 'leftKnee_score', 'leftKnee_x', 'leftKnee_y',
               'rightKnee_score', 'rightKnee_x', 'rightKnee_y', 'leftAnkle_score', 'leftAnkle_x', 'leftAnkle_y',
               'rightAnkle_score', 'rightAnkle_x', 'rightAnkle_y']
    
        csv_data = np.zeros((len(data), len(columns)))
        for i in range(csv_data.shape[0]):
            one = []
            one.append(data[i]['score'])
            for obj in data[i]['keypoints']:
                one.append(obj['score'])
                one.append(obj['position']['x'])
                one.append(obj['position']['y'])
            csv_data[i] = np.array(one)
        
        pattern1 = patterns[model1.predict(csv_data)[0]]
        pattern2 = patterns[model2.predict(csv_data)[0]]
        pattern3 = patterns[model3.predict(csv_data)[0]]
        pattern4 = patterns[model4.predict(csv_data)[0]]

        return jsonify({"1": pattern1,"2": pattern2,"3": pattern3,"4": pattern4})    
    except Exception as e:
        print(e)
        return 'MC_GRoup_16'

if __name__ == "__main__":
    app.run(host='0.0.0.0', debug=True)