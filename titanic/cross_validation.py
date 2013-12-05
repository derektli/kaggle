"""
slice train.cvs into 5 fold and generate files for cross validation.
"""

import csv as csv
import numpy as np

tp = 1.0
tn = 1.0
fp = 1.0
fn = 1.0

for i in range(5):
    ftest = open('test' + str(i) + '.csv', 'r')
    fmodel = open('output' + str(i) + '.csv', 'r')
    ftest.readline()
    fmodel.readline()
    for line1 in ftest:
        line2 = fmodel.readline()
        t1 = line1.rstrip('\n').split(',')
        t2 = line2.rstrip('\n').split(',')
        if (t1[1] == t2[1]):
            if t1[1] == '1':
                tp += 1
            else:
                tn += 1
        else:
            if t2[1] == '1':
                fp += 1
            else:
                fn += 1

print "True Positive: ", tp
print "True Negative: ", tn
print "False Positive: ", fp
print "False Negative: ", fn


accuracy = (tp + tn)/(tp + tn + fp + fn)
error_rate = (fp + fn)/(tp + tn + fp + fn)
sensitivity = (tp)/(tp + fn)
recall = (tn)/(tn + fp)
precision = (tp)/(tp + fp)
f1 = 2 * precision * recall / (precision + recall)

print "Accuracy: ", accuracy
print "Error rate: ", error_rate
print "Sensitivity: ", sensitivity
print "Recall: ", recall
print "Precision: ", precision
print "F-1 Score: ", f1
