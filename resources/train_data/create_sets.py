import random
import csv
import numpy as np

def to_array(json_matrix):
    tmp = []
    for sub in json_matrix:
        vals = np.asarray(sub)
        tmp_1 = []
        for val in vals:
            tmp_1.append(float(val))

        tmp.append(tmp_1)

    return tmp

def interpolate(json_matrix):
    matrix = to_array(json_matrix)
    print len(matrix)
    for i in range(0, len(matrix)*2 - 4, 2):
        new_item = (np.asarray(matrix[i]) + np.asarray(matrix[i+1]))/2
        print i
        new_item[0] = matrix[i][0]
        new_item[1] = matrix[i][1]
        matrix.insert(i+1, new_item)
    return matrix




def main():
    train_size = .70
    test_size = .15
    valid_size = .15

    # Load and combine all csv files
    with open("aalborg.csv") as f:
        r = csv.reader(f)
        header, l = next(r), list(r)

    with open("alpine-1.csv") as f:
        r = csv.reader(f)
        _, l_1 = next(r), list(r)

    with open("f-speedway.csv") as f:
        r = csv.reader(f)
        _, l_2 = next(r), list(r)


    # print l
    l = interpolate(l)
    l_1 = interpolate(l_1)
    l_2 = interpolate(l_2)
    l = interpolate(l)
    l_1 = interpolate(l_1)
    l_2 = interpolate(l_2)

    # write a train, test and valid set
    with open("aalborg-extended.csv", "wb") as f:
        csv.writer(f).writerows([header] + l)

            # write a train, test and valid set
    with open("alphine-1-extended.csv", "wb") as f:
        csv.writer(f).writerows([header] + l_1)   

    # write a train, test and valid set
    with open("f-speedway-extended.csv", "wb") as f:
        csv.writer(f).writerows([header] + l_2)


    combined = l + l_1 + l_2

    random.shuffle(combined)

    n_items = len(combined)

    start = 0
    train_end = int(n_items * train_size)
    test_end = int(train_end +  n_items * test_size)
    valid_end = n_items - 1
    
    # write a train, test and valid set
    with open("train-extended.csv", "wb") as f:
        csv.writer(f).writerows([header] + combined[0: train_end])

    with open("test-extended.csv", "wb") as f:
        csv.writer(f).writerows([header] + combined[train_end: test_end])

    with open("valid-extended.csv", "wb") as f:
        csv.writer(f).writerows([header] + combined[test_end: valid_end])


if __name__ == '__main__':
    main()