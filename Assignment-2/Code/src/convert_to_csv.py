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


path_to_videos = "./jsonFiles/"


def convert_to_csv(path_to_video, fileName, path_to_videos):
    columns = ['score_overall', 'nose_score', 'nose_x', 'nose_y', 'leftEye_score', 'leftEye_x', 'leftEye_y',
               'rightEye_score', 'rightEye_x', 'rightEye_y', 'leftEar_score', 'leftEar_x', 'leftEar_y',
               'rightEar_score', 'rightEar_x', 'rightEar_y', 'leftShoulder_score', 'leftShoulder_x', 'leftShoulder_y',
               'rightShoulder_score', 'rightShoulder_x', 'rightShoulder_y', 'leftElbow_score', 'leftElbow_x',
               'leftElbow_y', 'rightElbow_score', 'rightElbow_x', 'rightElbow_y', 'leftWrist_score', 'leftWrist_x',
               'leftWrist_y', 'rightWrist_score', 'rightWrist_x', 'rightWrist_y', 'leftHip_score', 'leftHip_x',
               'leftHip_y', 'rightHip_score', 'rightHip_x', 'rightHip_y', 'leftKnee_score', 'leftKnee_x', 'leftKnee_y',
               'rightKnee_score', 'rightKnee_x', 'rightKnee_y', 'leftAnkle_score', 'leftAnkle_x', 'leftAnkle_y',
               'rightAnkle_score', 'rightAnkle_x', 'rightAnkle_y']
    filesr = os.listdir(path_to_video)
    frame = pd.DataFrame()
    for files in filesr:
        data = json.loads(open(path_to_video + files, 'r').read())
        csv_data = np.zeros((len(data), len(columns)))
        for i in range(csv_data.shape[0]):
           one = []
           one.append(data[i]['score'])
           for obj in data[i]['keypoints']:
              one.append(obj['score'])
              one.append(obj['position']['x'])
              one.append(obj['position']['y'])
           csv_data[i] = np.array(one)
           tmp_frame = pd.DataFrame(csv_data, columns=columns)
           tmp_frame['label'] = fileName
        frame = frame.append(tmp_frame, ignore_index=True)
    frame.to_csv(path_to_videos + fileName+'.csv', index_label='Frames#')
        


if __name__ == '__main__':

    files = os.listdir(path_to_videos)
    for file in files:
        if  os.path.isdir(path_to_videos + file + "/"):
            new_path = path_to_videos + os.path.splitext(file)[0] + "/"
            convert_to_csv(new_path, os.path.splitext(file)[0], path_to_videos)
    results = pd.DataFrame([])
    os.chdir(path_to_videos)
    fileNames = [i for i in glob.glob('*.{}'.format('csv'))]
    mergedFile = pd.concat([pd.read_csv(f) for f in fileNames ])
    mergedFile.to_csv( "mergedFiles.csv", index=False, encoding='utf-8-sig')
    #for counter, file in enumerate(glob.glob(".csv")):
    #   namedf = pd.read_csv(file, skiprows=0)
    #   results = results.append(namedf)
    #results.to_csv(path_to_videos+'combinedfile.csv')
    print("Merged File Generation is done")
    
    
    
    




    

    
 



