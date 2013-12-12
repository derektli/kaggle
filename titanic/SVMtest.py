""" This simple code is desinged to teach a basic user to read in the files in python, simply find what proportion of males and females survived and make a predictive model based on this
Author : AstroDave
Date : 18th September, 2012

"""


import csv as csv
import numpy as np
from sklearn import svm

for id in range(5):
    sid = str(id)
    csv_file_object = csv.reader(open('train'+sid+'.csv', 'rb'))
    header = csv_file_object.next() 
    data=[] 
    for row in csv_file_object: 
        data.append(row[1:]) 
    data = np.array(data) 

    number_passengers = np.size(data[0::,0].astype(np.float))
    number_survived = np.sum(data[0::,0].astype(np.float))
    proportion_survivors = number_passengers / number_survived

    women_only_stats = data[0::,3] == "female" 
    men_only_stats = data[0::,3] != "female"

    women_onboard = data[women_only_stats,0].astype(np.float)
    men_onboard = data[men_only_stats,0].astype(np.float)

    proportion_women_survived = np.sum(women_onboard) / np.size(women_onboard)
    proportion_men_survived = np.sum(men_onboard) / np.size(men_onboard)

    print 'Proportion of women who survived is %s' % proportion_women_survived
    print 'Proportion of men who survived is %s' % proportion_men_survived

    #while 1:
    #    a = input()
    #    print data[a,0:5]

    X = []
    y = []
    for a in data:
        if a[4] != '':
            X.append([a[3] == 'male',a[4].astype(np.float),a[5].astype(np.int),a[6].astype(np.int)])
            y.append(a[0].astype(np.int))
            
    #print X[0:10]
    #print y[0:10]

    clf = svm.SVC(kernel = 'rbf', gamma = 0.1)
    clf.fit(X, y)  
    """SVC(C=1.0, cache_size=200, class_weight=None, coef0=0.0, degree=3,
    gamma=0.0, kernel='rbf', max_iter=-1, probability=False, random_state=None,
    shrinking=True, tol=0.001, verbose=False)"""

    test_file_object = csv.reader(open('test'+sid+'.csv', 'rb'))
    header = test_file_object.next()

    #Now also open the a new file so we can write to it call it something
    #descriptive

    predictions_file = csv.writer(open('output'+sid+'.csv', 'wb'))
    predictions_file.writerow(["PassengerId", "Survived"])
    x = []

    for row in test_file_object:
        """if row[3] == 'female':
            predictions_file.writerow([row[0], "1"])
        else:
            predictions_file.writerow([row[0], "0"])"""
        z = clf.predict([row[4] == 'male', row[5] == '' ? NULL : float(row[5]),int(row[6]),int(row[7])])
        predictions_file.writerow([row[0],str(z[0])])
        """if row[5] != '':
            z = clf.predict([row[4] == 'male', float(row[5]),int(row[6]),int(row[7])])
            predictions_file.writerow([row[0],str(z[0])])
        else:
            predictions_file.writerow([row[0],str(int(row[3] == 'female'))])"""
